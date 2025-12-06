package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.openftc.apriltag.AprilTagPose;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

public class GPad
{
    Map<String, Consumer<Boolean>> input = new HashMap<>();
    Gamepad gamepad;
    ControlHub hub;
    Vision vision;
    AprilTagDetector aprilTagDetector;

    public final int MAX_GEAR = 4;
    public final int MIN_GEAR = 1;
    private int gear = MIN_GEAR;
    private boolean leftBumperAlreadyPressed = false;
    private boolean rightBumperAlreadyPressed = false;
    private double leftFrontPower = 0;
    private double leftBackPower = 0;
    private double rightFrontPower = 0;
    private double rightBackPower = 0;
    DriveMode driveMode = DriveMode.RobotCentric;

    enum
    DriveMode
    {
        RobotCentric,
        FieldCentric,
    }


    public GPad(ControlHub hb, Gamepad gmp, Vision v)
    {
        input.put("x", this::ButtonX);
        input.put("a", this::ButtonA);
        input.put("b", this::ButtonB);
        input.put("y", this::ButtonY);
        input.put("left_bumper", this::ButtonLeftBumper);
        input.put("right_bumper", this::ButtonRightBumper);
        input.put("back", this::ButtonBack);
        input.put("start", this::ButtonStart);
        input.put("dpad_up", this::DpadUp);
        input.put("dpad_down", this::DpadDown);
        input.put("dpad_left", this::DpadLeft);
        input.put("dpad_right", this::DpadRight);

        gamepad = gmp;
        hub = hb;
        vision = v;
    }

    public double scaleInput(double input) 
    {    return input * input * input * 0.8 + input * 0.2;
    }
    
    public void Joystick(float l_xAxis, float l_yAxis, float r_xAxis, float r_yAxis)
    {
        double y = scaleInput(-l_yAxis); // Remember, Y stick value is reversed
        double x = scaleInput(l_xAxis) * 1.1; // Counteract imperfect strafing
        double rx = scaleInput(r_xAxis * 1.3);

        double lf = 0;
        double lb = 0;
        double rf = 0;
        double rb = 0;

        switch(this.driveMode)
        {
            case FieldCentric:
                lf = (y + x + rx);
                lb = (y - x + rx);
                rf = (y - x - rx);
                rb = (y + x - rx);

                break;
            case RobotCentric:
                double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                double rotationX = x * Math.cos(-botAngle) - y * Math.sin(-botAngle);
                double rotationY = x * Math.sin(-botAngle) + y * Math.cos(-botAngle);

                lf = (rotationY + rotationX + rx);
                lb = (rotationY - rotationX + rx);
                rf = (rotationY - rotationX - rx);
                rb = (rotationY + rotationX - rx);

                break;
        }

        double gearMultiplier = .15;
        double wheelPowerMultiplier = 0.6 * (1 + (gear - 1) * gearMultiplier);

        lf *= wheelPowerMultiplier;
        lb *= wheelPowerMultiplier;
        rf *= wheelPowerMultiplier;
        rb *= wheelPowerMultiplier;

        this.leftFrontPower += lf;
        this.leftBackPower += lb;
        this.rightFrontPower += rf;
        this.rightBackPower += rb;
    }

    public void ButtonX(boolean pressed)
    {
    }

    public void ButtonA(boolean pressed)
    {

        /*

        hub.conveyorMotor.setPower(.25);

         */
    }

    public void ButtonB(boolean pressed)
    {
    }

    public static double normalizeRadians(double angle) {
        angle = angle % (2 * Math.PI);
        if (angle < 0) angle += 2 * Math.PI;
        return angle;
    }


    public void ButtonY(boolean pressed) {
        if(!pressed)
        {
            driveMode = DriveMode.RobotCentric;
            return;
        }

        // vision not installed...
        if(vision == null)
        {   return;
        }

        // Color hasn't initialized yet.
        if(aprilTagDetector.GetTeamColor() == null)
        {   return;
        }

        driveMode = DriveMode.FieldCentric;

        // test maintain this angle.
        final double ONE_RADIAN = 2 * Math.PI;
        final double rotationSpeedMult = .6;
        double targetAngle;
        double currentAngle;
        double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        AprilTagDetection tag = aprilTagDetector.GetTeam();
        AprilTagPoseFtc pose = null;
        AprilTagDetector.TeamColor teamColor = aprilTagDetector.GetTeamColor();
        double lf = 0;
        double lb = 0;
        double rf = 0;
        double rb = 0;

        // Are we in the line of sight?
        if(tag == null)
        {
            AprilTagDetection lastSeenTeam = aprilTagDetector.GetLastSeenTeam();
            Pose3D lastSeenRobotPose;
            double lastSeenRotation = 0;
            double rotationSpeed = 0;

            if(lastSeenTeam != null)
            {
                lastSeenRobotPose = lastSeenTeam.robotPose;

                lastSeenRotation = lastSeenRobotPose.getOrientation().getYaw(AngleUnit.RADIANS);

                double error = AngleUnit.normalizeRadians(lastSeenRotation - botAngle);

                rotationSpeed = (error / Math.PI) * rotationSpeedMult;
            }
            else
            {
                AprilTagDetection opp = (teamColor == AprilTagDetector.TeamColor.Red) ?
                        aprilTagDetector.GetLastSeenBlue() :
                        aprilTagDetector.GetLastSeenRed();

                if(opp != null)
                {
                    double target = opp.robotPose.getOrientation().getYaw(AngleUnit.RADIANS);
                    double error = AngleUnit.normalizeRadians(target - botAngle);

                    rotationSpeed = (error / Math.PI)  * rotationSpeedMult;
                }
            }


            lf = rotationSpeed;
            lb = rotationSpeed;
            rf = -rotationSpeed;
            rb = -rotationSpeed;
        }
        else
        {
            pose = tag.ftcPose;
            targetAngle = Math.atan2(pose.y, pose.x);

            double error = AngleUnit.normalizeRadians(targetAngle - botAngle);

            double rotationSpeed = (error / Math.PI) * rotationSpeedMult;

            // if within 1% ignore.
            if(Math.abs(error) < Math.toRadians(2))
            {   rotationSpeed = 0;
            }

            // turn Left
            lf = rotationSpeed;
            lb = rotationSpeed;
            rf = -rotationSpeed;
            rb = -rotationSpeed;
        }



        this.leftFrontPower += lf;
        this.leftBackPower += lb;
        this.rightFrontPower += rf;
        this.rightBackPower += rb;
    }

    public void ButtonLeftBumper(boolean pressed)
    {
        /* only handle first input */
        if(leftBumperAlreadyPressed && pressed)
        {   return;
        }

        leftBumperAlreadyPressed = pressed;

        if(pressed)
        {
            // lower  gear.
            gear = Math.max(gear - 1, MIN_GEAR);

            gamepad.rumble(.1, .1, 150);
        }
    }

    public void ButtonRightBumper(boolean pressed)
    {
        /* only handle first input */
        if(rightBumperAlreadyPressed && pressed)
        {   return;
        }

        rightBumperAlreadyPressed = pressed;

        if(pressed)
        {
            gear = Math.min(gear + 1, MAX_GEAR);

            gamepad.rumble(.05, .25, 75);
        }
    }

    public void ButtonLeftTrigger(float pressAmount)
    {
        final float DEADZONE_THRESHOLD = 0.075f;
        double conveyorPower = 0f;
        float multiplier = .6f;
        
        if(pressAmount >= DEADZONE_THRESHOLD)
        {   conveyorPower = pressAmount;
        }
        
        hub.conveyorMotor.setPower(conveyorPower * multiplier);
    }

    public void ButtonRightTrigger(float pressAmount)
    {
        final float DEADZONE_THRESHOLD = 0.075f;
        double conveyorPower = 0f;
        float multiplier = .6f;

        if(pressAmount >= DEADZONE_THRESHOLD)
        {   conveyorPower = pressAmount;
        }

        hub.conveyorMotor.setPower(-conveyorPower * multiplier);
    }

    public void ButtonBack(boolean pressed)
    {
    }

    public void ButtonStart(boolean pressed)
    {
    }

    public void ButtonLeftJoystick(boolean pressed)
    {
    }

    public void ButtonRightJoystick(boolean pressed)
    {
    }

    public void DpadUp(boolean pressed)
    {
    }

    public void DpadDown(boolean pressed)
    {
    }

    public void DpadLeft(boolean pressed)
    {
    }

    public void DpadRight(boolean pressed)
    {
    }

    public void HandleInput()
    {
        Class<?> _class = gamepad.getClass();
        Consumer<Boolean> func;
        String key;
        Field field;
        Boolean value;

        for (Map.Entry<String, Consumer<Boolean>> i : input.entrySet())
        {
            try
            {
                key = i.getKey();
                func = i.getValue();

                field = _class.getDeclaredField(key);
                value = (Boolean)field.get(gamepad);

                func.accept(value);
            }
            catch (Exception e)
            {   // If its not there??? Then we don't need to do anything.
            }
        }

        ButtonLeftTrigger(gamepad.left_trigger);
        ButtonRightTrigger(gamepad.right_trigger);
        Joystick(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x, gamepad.right_stick_y);


        // clamp
        double max = Math.max(
                Math.max(Math.abs(leftFrontPower), Math.abs(leftBackPower)),
                Math.max(Math.abs(rightFrontPower), Math.abs(rightBackPower)));

        if(max > 1.0)
        {
            this.leftBackPower /= max;
            this.leftFrontPower /= max;
            this.rightFrontPower /= max;
            this.rightBackPower /= max;
        }

        // Apply motor timings
        //hub.drive.leftFront.setPower(this.leftFrontPower);
        //hub.drive.leftBack.setPower(this.leftBackPower);
        //hub.drive.rightFront.setPower(this.rightFrontPower);
        //hub.drive.rightBack.setPower(this.rightBackPower);

        hub.telemetry.addData("LF", this.leftFrontPower);
        hub.telemetry.addData("LB", this.leftBackPower);
        hub.telemetry.addData("RF", this.rightFrontPower);
        hub.telemetry.addData("RB", this.rightBackPower);

        this.leftFrontPower = 0;
        this.leftBackPower = 0;
        this.rightFrontPower = 0;
        this.rightBackPower = 0;


    }
}
