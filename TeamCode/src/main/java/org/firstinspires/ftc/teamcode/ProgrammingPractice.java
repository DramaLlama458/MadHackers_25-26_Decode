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
public class ProgrammingPractice extends LinearOpMode {
    ControlHub hub;
    private FtcDashboard dash = FtcDashboard.getInstance();

    double turnPower = .7;



    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null, telemetry);

        waitForStart();

        while (opModeIsActive()) {



            /*
            int value = 10;
            boolean condition = true;
            double decimal = 5.25;
            String text = "example";
             */

            /*
            int output = 1;
            if( condition ){
                output = output + 2;
            }


            boolean value2 = 20;

            if( value == value2 ){
                output -= 2;
            }else{
                output += 2;
            }

            if( false ){
                output *= output;
            }else if( false ){
                output /= output;
            }else if( true ){
                output = 8 % 2;
            }

            int iterations = 0;
            while( iterations < 5){
                //action
                iterations++;
            }

            // servo name . function name ( input )
            hub.inputServo.setPower(.5);

             */



            //wheelMovement();
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