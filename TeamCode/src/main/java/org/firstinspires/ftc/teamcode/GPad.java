package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

public class GPad
{
    Map<String, Consumer<Boolean>> input = new HashMap<>();
    Gamepad gamepad;
    ControlHub hub;

    public final int MAX_GEAR = 4;
    public final int MIN_GEAR = 1;
    private int gear = MIN_GEAR;

    private boolean leftBumperAlreadyPressed = false;
    private boolean rightBumperAlreadyPressed = false;
    DriveMode driveMode = DriveMode.RobotCentric;

    enum
    DriveMode
    {
        RobotCentric,
        FieldCentric,
    }


    public GPad(ControlHub hb, Gamepad gmp)
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
    }

    public double scaleInput(double input) 
    {    return input * input * input * 0.8 + input * 0.2;
    }
    
    public void Joystick(float l_xAxis, float l_yAxis, float r_xAxis, float r_yAxis)
    {
        double y = scaleInput(-l_yAxis); // Remember, Y stick value is reversed
        double x = scaleInput(l_xAxis) * 1.1; // Counteract imperfect strafing
        double rx = scaleInput(r_xAxis * 1.3);

        double denominator;
        double leftFrontPower = .0;
        double leftBackPower = .0;
        double rightFrontPower = .0;
        double rightBackPower = .0;

        switch(this.driveMode)
        {
            case FieldCentric:
                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio,
                // but only if at least one is out of the range [-1, 1]
                denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

                leftFrontPower = (y + x + rx) / denominator;
                leftBackPower = (y - x + rx) / denominator;
                rightFrontPower = (y - x - rx) / denominator;
                rightBackPower = (y + x - rx) / denominator;

                break;
            case RobotCentric:
                double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                double rotationX = x * Math.cos(-botAngle) - y * Math.sin(-botAngle);
                double rotationY = x * Math.sin(-botAngle) + y * Math.cos(-botAngle);

                denominator = Math.max(Math.abs(rotationY) + Math.abs(rotationX) + Math.abs(rx), 1);

                leftFrontPower = (rotationY + rotationX + rx) / denominator;
                leftBackPower = (rotationY - rotationX + rx) / denominator;
                rightFrontPower = (rotationY - rotationX - rx) / denominator;
                rightBackPower = (rotationY + rotationX - rx) / denominator;

                break;
        }

        double gearMultiplier = .25;
        double wheelPowerMultiplier = 0.5 * (1 + (gear - 1) * gearMultiplier);

        hub.drive.leftFront.setPower(leftFrontPower * wheelPowerMultiplier);
        hub.drive.rightFront.setPower(rightFrontPower * wheelPowerMultiplier);
        hub.drive.leftBack.setPower(leftBackPower * wheelPowerMultiplier);
        hub.drive.rightBack.setPower(rightBackPower * wheelPowerMultiplier);
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


    public void ButtonY(boolean pressed)
    {
        /*
        if(!pressed)
        {
            driveMode = DriveMode.RobotCentric;
            return;
        }

        driveMode = DriveMode.FieldCentric;

        // test maintain this angle.
        final double ONE_RADIAN = 2 * Math.PI;

        double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Test values for now.
        double targetAngle = botAngle * 1.5;

        // RotationSpeed Per Second.
        double rotationSpeed = ONE_RADIAN * .15;


        // normalize radians.
        targetAngle = normalizeRadians(targetAngle);


        double leftFrontPower = ;
        double rightFrontPower = ;
        double leftBackPower = ;
        double rightBackPower = ;

        */
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
            gear = Math.max(gear - 1, MIN_GEAR);
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
        }
    }

    public void ButtonLeftTrigger(float pressAmount)
    {
        final float DEADZONE_THRESHOLD = 0.075f;
        double conveyorPower = 0f;
        float multiplier = .6f;
        
        if(pressAmount >= DEADZONE_THRESHOLD)
        {   conveyorPower = scaleInput(pressAmount);
        }
        
        hub.conveyorMotor.setPower(conveyorPower * multiplier);
    }

    public void ButtonRightTrigger(float pressAmount)
    {
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
    }
}
