package org.firstinspires.ftc.teamcode;


import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TankDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.TwoDeadWheelLocalizer;
import com.acmerobotics.roadrunner.*;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.Math;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


@Autonomous
public class AutoOPMain extends LinearOpMode  {
    ControlHub hub;
    FtcDashboard dash = FtcDashboard.getInstance();

    Pose2d START_POSE = new Pose2d(0, 0, Math.toRadians(0));
    Pose2d TARGET_POSE = new Pose2d(10, 10, Math.toRadians(90));


    @Override
    public void runOpMode() throws InterruptedException
    {
        hub = new ControlHub(hardwareMap, START_POSE);
        while (opModeIsActive() && !isStopRequested())
        {
            Actions.runBlocking(
                    hub.drive.actionBuilder(new Pose2d(0, 0, 0))
                            .lineToX(10)
                            .lineToX(0)
                            .build());
            dash.sendTelemetryPacket(new TelemetryPacket());
        }
    }
}
