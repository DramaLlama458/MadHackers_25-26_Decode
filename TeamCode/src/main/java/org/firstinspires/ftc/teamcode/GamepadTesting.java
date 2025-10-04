package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.*;

@TeleOp
public class GamepadTesting{

    public static Map<String, Consumer<Object>> input = new HashMap<>();

    // Init code stuff
    static 
    {
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

    
    public static Gamepad GetGamePad1()
    {   return gamepad1;
    }

    public static Gamepad GetGamePad2()
    {   return gamepad2;
    }

    // Left Joystick X
    public static void Axis0(float xAxis)
    {
    }
    // Left Joystick Y (inv)
    public static void Axis1(float yAxis)
    {
    }
    // Right Joystick X
    public static void Axis2(float xAxis)
    {
    }
    // Right Joystick Y (inv)
    public static void Axis3(float yAxis)
    {
    }

    //  X
    public static void Button0(boolean pressed)
    {
    }

    // A
    public static void Button1(boolean pressed)
    {
    }

    // []/B (Square)
    public static void Button2(boolean pressed)
    {
    }

    // Y
    public static void Button3(boolean pressed)
    {
    }

    // Left Bumper
    public static void Button4(boolean pressed)
    {
    }

    // Right Bumper
    public static void Button5(boolean pressed)
    {
    }

    // Left Trigger
    public static void Button6(boolean pressed)
    {
    }

    // Right Trigger
    public static void Button7(boolean pressed)
    {
    }

    // Back
    public static void Button8(boolean pressed)
    {
    }

    // Start
    public static void Button9(boolean pressed)
    {
    }

    // Left Joystick
    public static void Button10(boolean pressed)
    {
    }

    // Right Joystick
    public static void Button11(boolean pressed)
    {
    }

    public static void DpadUp(boolean pressed)
    {
    }

    public static void DpadDown(boolean pressed)
    {
    }

    public static void DpadLeft(boolean pressed)
    {
    }

    public static void DpadRight(boolean pressed)
    {
    }

    public static void HandleInput()
    {
        Gamepad gamepad = new Gamepad();
        //gamepad.copy();
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








