package org.firstinspires.ftc.teamcode;

import android.service.controls.Control;

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


@TeleOp
public class TeleOpMain extends LinearOpMode  {
    ControlHub hub;
    FtcDashboard dash = FtcDashboard.getInstance();
    GPad gpad;

    @Override
    public void runOpMode() throws InterruptedException
    {
        hub = new ControlHub(hardwareMap, null);
        gpad = new GPad(hub, gamepad1, null);

        waitForStart();

        if (isStopRequested())
        {   return;
        }

        while (opModeIsActive())
        {
            gpad.HandleInput();
            dash.sendTelemetryPacket(new TelemetryPacket());
        }
    }
}
