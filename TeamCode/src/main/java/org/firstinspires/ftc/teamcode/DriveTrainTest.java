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

@TeleOp
public class DriveTrainTest extends LinearOpMode {
    ControlHub hub;
    private FtcDashboard dash = FtcDashboard.getInstance();


    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null, telemetry);

        //double conveyorPower = 0;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            wheelMovement();



            dash.sendTelemetryPacket(new TelemetryPacket());
            updateTelemetry(telemetry);



            if(gamepad1.x){
                hub.conveyorMotor.setPower(.4);
            }else if(gamepad1.y){
                hub.conveyorMotor.setPower(-.4);
            }else{
                hub.conveyorMotor.setPower(0);
            }

            if(gamepad1.left_trigger>.5){
                hub.inputServo.setPower(1);
            }else if(gamepad1.right_trigger>.5){
                hub.inputServo.setPower(-1);
            }else{
                hub.inputServo.setPower(0);
            }

            if(gamepad1.a){
                hub.bottomOutputMotor.setPower(.3);
                hub.topOutputMotor.setPower(1);
            }else if(gamepad1.b){
                hub.bottomOutputMotor.setPower(-.2);
                hub.topOutputMotor.setPower(-.5);
            }else{
                hub.bottomOutputMotor.setPower(0);
                hub.topOutputMotor.setPower(0);
            }


            /*
            if(gamepad1.right_bumper){
                hub.conveyorSpeed+=.05;
            }
            if(gamepad1.left_bumper){
                hub.conveyorSpeed-=.05;
            }
            if(gamepad1.b){
                hub.conveyorSpeed=0;
            }

            hub.conveyorMotor.setPower(hub.conveyorSpeed);

            if(gamepad1.y){
                hub.outputMotor.setPower(.5);
            }else{
                hub.outputMotor.setPower(0);
            }
            */





        }
    }

    private void wheelMovement() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftBackPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightBackPower = (y + x - rx) / denominator;

        hub.drive.leftFront.setPower(leftFrontPower*.90);
        hub.drive.rightFront.setPower(rightFrontPower*.90);
        hub.drive.leftBack.setPower(leftBackPower*.90);
        hub.drive.rightBack.setPower(rightBackPower*.90);

        telemetry.addData("X",x);
        telemetry.addData("Y",y);
        telemetry.addData("RX",rx);
        updateTelemetry(telemetry);
    }
}