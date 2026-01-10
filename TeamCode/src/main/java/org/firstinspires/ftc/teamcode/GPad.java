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

    boolean buttonYWasPressed = false;
    boolean debugModeOn = false;
    boolean dpadDownWasPressed = false;

    enum
    DriveMode
    {
        RobotCentric,
        FieldCentric,
    }


    public GPad(ControlHub hb, Gamepad gmp)
    {
        gamepad = gmp;
        hub = hb;
        vision = hb.vision;

        aprilTagDetector = hub.detector;

        // make sure keeps field orientation.
        hub.drive.lazyImu.get().resetYaw();
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
                double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                double rotationX = x * Math.cos(-botAngle) - y * Math.sin(-botAngle);
                double rotationY = x * Math.sin(-botAngle) + y * Math.cos(-botAngle);

                lf = (rotationY + rotationX + rx);
                lb = (rotationY - rotationX + rx);
                rf = (rotationY - rotationX - rx);
                rb = (rotationY + rotationX - rx);

                break;
            case RobotCentric:
                // boost rx if moving + turning.
                final double MIN_BUMP_UP = .45;
                if(Math.abs(y) > MIN_BUMP_UP && Math.abs(rx) > MIN_BUMP_UP)
                {   rx += Math.abs(y) * .20;
                }
                lf = (y + x + rx);
                lb = (y - x + rx);
                rf = (y - x - rx);
                rb = (y + x - rx);


                break;
        }

        double gearMultiplier = .25;
        double wheelPowerMultiplier = .70; //0.40 * (1 + (gear - 1) * gearMultiplier);

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
        if(pressed){
            hub.conveyorMotor.setPower(.4);
        }else{
            hub.conveyorMotor.setPower(0);
        }
    }

    public void ButtonA(boolean pressed)
    {

        if(pressed){
            hub.bottomOutputMotor.setPower(.3);
            hub.topOutputMotor.setPower(1);
        }else{
            hub.bottomOutputMotor.setPower(0);
            hub.topOutputMotor.setPower(0);
        }
    }

    public void ButtonB(boolean pressed)
    {
        if(pressed){
            hub.conveyorMotor.setPower(-.4);
        }
    }

    public static double normalizeRadians(double angle) {
        angle = angle % (2 * Math.PI);
        if (angle < 0) angle += 2 * Math.PI;
        return angle;
    }


    public void ButtonY(boolean pressed) {
        if(this.buttonYWasPressed && pressed)
        {   return;
        }

        this.buttonYWasPressed = pressed;

        if(!pressed)
        {   return;
        }

        switch(this.driveMode) {
            case FieldCentric:
                this.driveMode = DriveMode.RobotCentric;
                hub.drive.lazyImu.get().resetYaw();
                return;
            case RobotCentric:
                this.driveMode = DriveMode.FieldCentric;
                return;
        }

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
        // reset yaw calculations should account for most inacurracies
        hub.drive.lazyImu.get().resetYaw();

        // test maintain this angle.
        final double rotationSpeedMult = 0.6;
        double targetAngle;
        double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        AprilTagDetection teamTag = aprilTagDetector.GetTeam();
        AprilTagPoseFtc pose = null;
        AprilTagDetector.TeamColor teamColor = aprilTagDetector.GetTeamColor();

        double lf = 0;
        double lb = 0;
        double rf = 0;
        double rb = 0;

        // Are we in the line of sight?
        if(teamTag == null)
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
            pose = teamTag.ftcPose;
            targetAngle = Math.atan2(pose.y, pose.x);

            double error = AngleUnit.normalizeRadians(targetAngle - botAngle);

            double rotationSpeed = (error / Math.PI) * rotationSpeedMult;

            // if within 2% ignore.
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
        /*
        final float DEADZONE_THRESHOLD = 0.075f;
        double conveyorPower = 0f;
        float multiplier = .25f;
        
        if(pressAmount >= DEADZONE_THRESHOLD)
        {   conveyorPower = pressAmount;
        }

         */
        if(pressAmount>.5){
            hub.inputServo.setPower(scaleInput(1));
        }else{
            hub.inputServo.setPower(0);
        }
        //hub.conveyorMotor.setPower(conveyorPower * multiplier);
    }

    public void ButtonRightTrigger(float pressAmount)
    {
        /*
        final float DEADZONE_THRESHOLD = 0.075f;
        double conveyorPower = 0f;
        float multiplier = .6f;

        if(pressAmount >= DEADZONE_THRESHOLD)
        {   conveyorPower = pressAmount;
        }

         */
        if(pressAmount>.5){
            hub.inputServo.setPower(scaleInput(-1));
        }

        //hub.conveyorMotor.setPower(-conveyorPower * multiplier);
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
        if(this.dpadDownWasPressed && pressed)
        {   return;
        }

        this.dpadDownWasPressed = pressed;

        if(!pressed)
        {   return;
        }

        this.debugModeOn = !this.debugModeOn;
    }

    public void DpadLeft(boolean pressed)
    {
    }

    public void DpadRight(boolean pressed)
    {
    }

    public void HandleInput()
    {
        ButtonX(gamepad.x);
        ButtonA(gamepad.a);
        ButtonB(gamepad.b);
        ButtonY(gamepad.y);
        ButtonLeftBumper(gamepad.left_bumper);
        ButtonRightBumper(gamepad.right_bumper);
        ButtonBack(gamepad.back);
        ButtonStart(gamepad.start);
        DpadUp(gamepad.dpad_up);
        DpadDown(gamepad.dpad_down);
        DpadLeft(gamepad.dpad_left);
        DpadRight(gamepad.dpad_right);
        ButtonLeftTrigger(gamepad.left_trigger);
        ButtonRightTrigger(gamepad.right_trigger);
        Joystick(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x, gamepad.right_stick_y);

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
        if(!this.debugModeOn)
        {
            hub.drive.leftFront.setPower(this.leftFrontPower);
            hub.drive.leftBack.setPower(this.leftBackPower);
            hub.drive.rightFront.setPower(this.rightFrontPower);
            hub.drive.rightBack.setPower(this.rightBackPower);
        }

        hub.telemetry.addData("Team", this.aprilTagDetector.GetTeam());
        hub.telemetry.addData("Camera State", this.vision.GetPortal() != null ?  this.vision.GetPortal().getCameraState() : "DEAD");
        hub.telemetry.addData("Movement Vector", String.format("(lf: %f, lb: %f, rf: %f, rb: %f)", this.leftFrontPower, this.leftBackPower, this.rightFrontPower, this.rightBackPower));
        hub.telemetry.addData("Heading", FTCDebug.GetRobotMovementDirection(this.leftFrontPower, this.rightFrontPower, this.leftBackPower, this.rightBackPower));
        hub.telemetry.addData("Heading Degree", FTCDebug.GetRobotMovementAngle(this.leftFrontPower, this.rightFrontPower, this.leftBackPower, this.rightBackPower));
        hub.telemetry.addData("Detections", this.vision.GetPortal() != null ?  this.hub.vision.GetProcessor().getDetections() : "DEAD");
        hub.telemetry.addData("Mode", this.driveMode);
        hub.telemetry.addData("DebugMode", this.debugModeOn);

        this.leftFrontPower = 0;
        this.leftBackPower = 0;
        this.rightFrontPower = 0;
        this.rightBackPower = 0;
    }
}
