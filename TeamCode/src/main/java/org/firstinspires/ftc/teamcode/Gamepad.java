package org.firstinspires.ftc.teamcode;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

public class Gamepad
{

    public static Map<String, Consumer<Object>> input = new HashMap<>();
    public boolean already_init = false;
    private Gamepad gamepad;
    private ControlHub hub;

    // Init code stuff
    public void __init(ControlHub hb, Gamepad gmp)
    {
        if(already_init)
        {   return;
        }


        // Buttons (Boolean)
        input.put("x", o -> Button0((Boolean) o));
        input.put("a", o -> Button1((Boolean) o));
        input.put("b", o -> Button2((Boolean) o));
        input.put("y", o -> Button3((Boolean) o));
        input.put("left_bumper", o -> Button4((Boolean) o));
        input.put("right_bumper", o -> Button5((Boolean) o));
        input.put("left_trigger", o -> Button6((Boolean) o));
        input.put("right_trigger", o -> Button7((Boolean) o));
        input.put("back", o -> Button8((Boolean) o));
        input.put("start", o -> Button9((Boolean) o));
        input.put("left_stick_button", o -> Button10((Boolean) o));
        input.put("right_stick_button", o -> Button11((Boolean) o));
        input.put("dpad_up", o -> DpadUp((Boolean) o));
        input.put("dpad_down", o -> DpadDown((Boolean) o));
        input.put("dpad_left", o -> DpadLeft((Boolean) o));
        input.put("dpad_right", o -> DpadRight((Boolean) o));

        gamepad = gmp;
        hub = hb;


        // Init Controller stuff.
        gamepad.setJoystickDeadzone(.08);
    }

    public void Joystick(float l_xAxis, float l_yAxis, float r_xAxis, float y_xAxis)
    {
        double y = -l_yAxis; // Remember, Y stick value is reversed
        double x = l_xAxis* 1.1; // Counteract imperfect strafing
        double rx = r_xAxis;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftBackPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightBackPower = (y + x - rx) / denominator;

        hub.leftFront.setPower(leftFrontPower);
        hub.rightFront.setPower(rightFrontPower);
        hub.leftBack.setPower(leftBackPower);
        hub.rightBack.setPower(rightBackPower);
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

    public void HandleInput(ControlHub hb, Gamepad gmp)
    {
        __init(hb, gmp);
        Class<?> _class = gamepad.getClass();
        Consumer<Object> func;
        String key;
        Field field;
        Object value;

        for (Map.Entry<String, Consumer<Object>> i : input.entrySet())
        {
            try
            {
                key = i.getKey();
                func = i.getValue();

                field = _class.getDeclaredField(key);
                value = field.get(gamepad);

                func.accept(value);
            }
            catch (Exception e)
            {   // If its not there??? Then we dont need to do anything.
            }
        }

        Joystick(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x, gamepad.right_stick_y);
    }
}