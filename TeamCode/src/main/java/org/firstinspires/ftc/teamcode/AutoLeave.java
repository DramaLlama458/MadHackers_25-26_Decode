package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

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
public class AutoLeave  extends LinearOpMode {
    ControlHub hub;
    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null, telemetry);


        waitForStart();
        resetRuntime();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            hub.topOutputMotor.setVelocity(120*18);
            hub.bottomOutputMotor.setVelocity(120*18);
            while(hub.topOutputMotor.getVelocity()>2100&&hub.bottomOutputMotor.getVelocity()>2100){
                continue;
            }
            resetRuntime();
            while(getRuntime()<5){
                hub.conveyorMotor.setPower(.7);
                telemetry.addData("Runtime",getRuntime());
                telemetry.update();
            }
            resetRuntime();
            if(getRuntime()<1) {
                hub.drive.leftFront.setPower(-0.4);
                hub.drive.rightFront.setPower(0.4);
                hub.drive.leftBack.setPower(0.4);
                hub.drive.rightBack.setPower(-0.4);
            }

            if(getRuntime()>1) {
                hub.drive.leftFront.setPower(0);
                hub.drive.rightFront.setPower(0);
                hub.drive.leftBack.setPower(0);
                hub.drive.rightBack.setPower(0);
                break;
            }
            telemetry.addData("Runtime: ",getRuntime());
            updateTelemetry(telemetry);

        }
    }


}
