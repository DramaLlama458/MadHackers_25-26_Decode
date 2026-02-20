package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@TeleOp
public class DriveTrainTest extends LinearOpMode {
    ControlHub hub;
    private FtcDashboard dash = FtcDashboard.getInstance();

    double turnPower = .7;


    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null, telemetry);

        //double conveyorPower = 0;
        double close = 80;
        double far = 88;
        double speed = close;
        //double bottomMult =1;
        //double topMult =1;

        //hub.drive.lazyImu.get().resetYaw();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            wheelMovement();



            dash.sendTelemetryPacket(new TelemetryPacket());
            updateTelemetry(telemetry);



            if(gamepad1.right_bumper){
                hub.conveyorServo.setPower(1);
            }else if(gamepad1.left_bumper){
                hub.conveyorServo.setPower(-1);
            }else{
                hub.conveyorServo.setPower(0);
            }

            if(gamepad1.left_trigger>.5){
                hub.inputServo.setPower(-1);
            }else if(gamepad1.right_trigger>.5){
                hub.inputServo.setPower(1);
            }else{
                hub.inputServo.setPower(0);
            }

            // 28 tics per rotation

            if(gamepad1.a){
                hub.bottomOutputMotor.setVelocity(speed*28);
                hub.topOutputMotor.setVelocity(speed*28);
            }else if(gamepad1.b){
                hub.bottomOutputMotor.setPower(-.5);
                hub.topOutputMotor.setPower(-.5);
            }else{
                hub.bottomOutputMotor.setPower(0);
                hub.topOutputMotor.setPower(0);
            }

            double speedaddr = .05;

            if(gamepad1.dpad_up){
                speed += speedaddr;
            }
            if(gamepad1.dpad_down){
                speed -= speedaddr;
            }

            if(gamepad1.y){
                speed=close;
            }
            if(gamepad1.x){
                speed=far;
            }
            /*
            if(gamepad1.dpad_right){
                hub.drive.lazyImu.get().resetYaw();
            }

             */

            /*

            double multipleraddr=.003;

            if(gamepad1.a){
                bottomMult+=multipleraddr;
            }
            if(gamepad1.b){
                bottomMult-=multipleraddr;

            }
            if(gamepad1.x){
                topMult+=multipleraddr;

            }
            if(gamepad1.y){

                topMult-=multipleraddr;

            }


            if(gamepad1.right_bumper){
                hub.bottomOutputMotor.setVelocity(speed*18*bottomMult);
                hub.topOutputMotor.setVelocity(speed*18*topMult);
            }
            if(gamepad1.left_bumper){
                hub.bottomOutputMotor.setPower(0);
                hub.topOutputMotor.setPower(0);
            }
            if(gamepad1.left_trigger>0.5){
                hub.conveyorMotor.setPower(.5);
            }else{
                hub.conveyorMotor.setPower(0);
            }
            */

            if(gamepad1.dpad_left){
                turnPower=.3;
            }else{
                turnPower=.7;
            }





            telemetry.addData("Top RPM",hub.topOutputMotor.getVelocity()/28*60);
            telemetry.addData("Bottom RPM",hub.bottomOutputMotor.getVelocity()/28*60);
            telemetry.addData("Speed",speed);
            telemetry.addLine();
            String distance;
            if(speed==close){
                distance ="Close";
            }else if(speed==far){
                distance ="Far";

            }else{
                distance ="Other";

            }
            telemetry.addData("Shooting from: ",distance);
            //telemetry.addData("TopMult",topMult);
            //telemetry.addData("BottomMult",bottomMult);
            updateTelemetry(telemetry);





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
        double rx = gamepad1.right_stick_x*turnPower;


        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftBackPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightBackPower = (y + x - rx) / denominator;


        /*
        double botAngle = hub.drive.lazyImu.get().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotationX = x * Math.cos(-botAngle) - y * Math.sin(-botAngle);
        double rotationY = x * Math.sin(-botAngle) + y * Math.cos(-botAngle);

        leftFrontPower = (rotationY + rotationX + rx);
        leftBackPower = (rotationY - rotationX + rx);
        rightFrontPower = (rotationY - rotationX - rx);
        rightBackPower = (rotationY + rotationX - rx);

         */

        hub.drive.leftFront.setPower(leftFrontPower*.90);
        hub.drive.rightFront.setPower(rightFrontPower*.90);
        hub.drive.leftBack.setPower(leftBackPower*.90);
        hub.drive.rightBack.setPower(rightBackPower*.90);


    }
}