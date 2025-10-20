package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

public class GPad
{
    Map<String, Consumer<Boolean>> input = new HashMap<>();
    Gamepad gamepad;
    ControlHubTest hub;

    public GPad(ControlHubTest hb, Gamepad gmp)
    {
    // Buttons (Boolean)
        input.put("x", this::ButtonX);
        input.put("a", this::ButtonA);
        input.put("b", this::ButtonB);
        input.put("y", this::ButtonY);
        input.put("left_bumper", this::ButtonLeftBumper);
        input.put("right_bumper", this::ButtonRightBumper);
        input.put("left_trigger", this::ButtonLeftTrigger);
        input.put("right_trigger", this::ButtonRightTrigger);
        input.put("back", this::ButtonBack);
        input.put("start", this::ButtonStart);
        input.put("left_stick_this::Button", this::ButtonLeftJoystick);
        input.put("right_stick_this::Button", this::ButtonRightJoystick);
        input.put("dpad_up", this::DpadUp);
        input.put("dpad_down", this::DpadDown);
        input.put("dpad_left", this::DpadLeft);
        input.put("dpad_right", this::DpadRight);

        gamepad = gmp;
        hub = hb;
    }

    public double scaleInput(double input) 
    {
        /* old code */
        //return input * input * input;

        return input * input * input * 0.8 + input * 0.2;
    }
    
    public void Joystick(float l_xAxis, float l_yAxis, float r_xAxis, float y_xAxis)
    {
        double y = scaleInput(-l_yAxis); // Remember, Y stick value is reversed
        double x = scaleInput(l_xAxis) * 1.1; // Counteract imperfect strafing
        double rx = scaleInput(r_xAxis);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftBackPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightBackPower = (y + x - rx) / denominator;

        double wheelPowerMultiplier = 0.75;

        hub.leftFront.setPower(leftFrontPower * wheelPowerMultiplier);
        hub.rightFront.setPower(rightFrontPower * wheelPowerMultiplier);
        hub.leftBack.setPower(leftBackPower * wheelPowerMultiplier);
        hub.rightBack.setPower(rightBackPower * wheelPowerMultiplier);
    }

    public void ButtonX(boolean pressed)
    {
    }

    public void ButtonA(boolean pressed)
    {
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

    public void ButtonLeftTrigger(boolean pressed)
    {
    }

    public void ButtonRightTrigger(boolean pressed)
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

        Joystick(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x, gamepad.right_stick_y);
    }
}
