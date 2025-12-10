package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class FTCDebug
{
    final static boolean IS_DEBUG_MODE = true;

    public enum CompassDirection
    {
        North,
        NorthEast,
        East,
        SouthEast,
        South,
        SouthWest,
        West,
        NorthWest,
    }

    public static double GetRobotMovementAngle(double LF, double RF, double LB, double RB)
    {
        double vx = (LF + RB - RF - LB) / 4.0;
        double vy = (LF + RF + LB + RB) / 4.0;

        if (vx == 0 && vy == 0)
        {   return Double.NaN;
        }

        double angle = Math.toDegrees(Math.atan2(vx, vy));

        if (angle < 0)
        {   angle += 360;
        }

        return angle;
    }

    public static CompassDirection GetRobotMovementDirection(double LF, double RF, double LB, double RB)
    {
        double angle = GetRobotMovementAngle(LF, RF, LB, RB);

        if (Double.isNaN(angle))
        {   return null;
        }

        CompassDirection[] directions = {
            CompassDirection.North,
            CompassDirection.NorthEast,
            CompassDirection.East,
            CompassDirection.SouthEast,
            CompassDirection.South,
            CompassDirection.SouthWest,
            CompassDirection.West,
            CompassDirection.NorthWest,
        };

        double[] sectorAngles = {0, 45, 90, 135, 180, 225, 270, 315};

        CompassDirection nearest = CompassDirection.North;

        double minDiff = 360;

        for (int i = 0; i < sectorAngles.length; i++)
        {
            double diff = Math.abs(angle - sectorAngles[i]);

            diff = Math.min(diff, 360 - diff); // wrap

            if (diff < minDiff)
            {
                minDiff = diff;
                nearest = directions[i];
            }
        }

        return nearest;
    }

}
