package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

public class GPad
{
    Map<String, Consumer<Boolean>> input = new HashMap<>();
    Gamepad gamepad;
    ControlHub hub;

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
        double rx = scaleInput(r_xAxis*1.3);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftBackPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightBackPower = (y + x - rx) / denominator;

        double wheelPowerMultiplier = 0.75;

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

    public void ButtonY(boolean pressed)
    {
    }

    public void ButtonLeftBumper(boolean pressed)
    {
    }

    public void ButtonRightBumper(boolean pressed)
    {
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
