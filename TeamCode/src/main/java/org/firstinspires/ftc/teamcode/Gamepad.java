package org.firstinspires.ftc.teamcode;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

public class Gamepad
{

    public static Map<String, Consumer<Object>> input = new HashMap<>();
    public boolean already_init = false;

    // Init code stuff
    public void __init()
    {
        if(already_init)
        {   return;
        }
        // Axis (Float)
        input.put("left_stick_x", o -> Axis0((Float) o));
        input.put("left_stick_y", o -> Axis1((Float) o));
        input.put("right_stick_x", o -> Axis2((Float) o));
        input.put("right_stick_y", o -> Axis3((Float) o));

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
    }

    // Left Joystick X
    public void Axis0(float xAxis)
    {
    }
    // Left Joystick Y (inv)
    public void Axis1(float yAxis)
    {
    }
    // Right Joystick X
    public void Axis2(float xAxis)
    {
    }
    // Right Joystick Y (inv)
    public void Axis3(float yAxis)
    {
    }

    //  X
    public void Button0(boolean pressed)
    {
    }

    // A
    public void Button1(boolean pressed)
    {
    }

    // []/B (Square)
    public void Button2(boolean pressed)
    {
    }

    // Y
    public void Button3(boolean pressed)
    {
    }

    // Left Bumper
    public void Button4(boolean pressed)
    {
    }

    // Right Bumper
    public void Button5(boolean pressed)
    {
    }

    // Left Trigger
    public void Button6(boolean pressed)
    {
    }

    // Right Trigger
    public void Button7(boolean pressed)
    {
    }

    // Back
    public void Button8(boolean pressed)
    {
    }

    // Start
    public void Button9(boolean pressed)
    {
    }

    // Left Joystick
    public void Button10(boolean pressed)
    {
    }

    // Right Joystick
    public void Button11(boolean pressed)
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

    public void HandleInput(com.qualcomm.robotcore.hardware.Gamepad gamepad)
    {
        __init();
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
            {   
                // If its not there??? Then we dont need to do anything.
            }
        }
    }
}








