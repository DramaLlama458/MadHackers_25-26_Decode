package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.comp.Todo;

@Autonomous
public class AutoTest extends LinearOpMode{
    ControlHub hub;
    private FtcDashboard dash = FtcDashboard.getInstance();
    //TODO: Setup roadrunner variables in robot constants and then set up coefficients here
    @Override
    public void runOpMode() throws InterruptedException{
        hub = new ControlHub();
        //Initial pose is just so hub can be used for autonomous
        //TODO: Figure out starting position of robot at start of match
        Pose2d initialPose = new Pose2d(0,0, Math.toRadians(0));
        hub.init(hardwareMap, initialPose);


    }
}
