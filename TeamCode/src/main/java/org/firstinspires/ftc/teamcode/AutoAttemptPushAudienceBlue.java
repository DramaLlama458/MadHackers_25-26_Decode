package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutoAttemptPushAudienceBlue extends LinearOpMode {
    ControlHub hub;
    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null);


        waitForStart();
        resetRuntime();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if(getRuntime()<1.5) {
                wheelMovement(.4,0,0);
            }
            if(getRuntime()<3&&getRuntime()>1.5) {
                wheelMovement(0,-.4,0);
            }
            if(getRuntime()<4.15&&getRuntime()>3) {
                wheelMovement(-.4,-.15,0);
            }
            if(getRuntime()>4.15) {
                wheelMovement(0,0,0);
                break;
            }

            telemetry.addData("Runtime: ",getRuntime());
            updateTelemetry(telemetry);


        }
    }
    public void wheelMovement(double yInput, double xInput, double rxInput) {
        double y = yInput; // Remember, Y stick value is reversed
        double x = xInput; // Counteract imperfect strafing
        double rx = rxInput;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double leftFrontPower = (y + x + rx) / denominator;
        double leftBackPower = (y - x + rx) / denominator;
        double rightFrontPower = (y - x - rx) / denominator;
        double rightBackPower = (y + x - rx) / denominator;

        hub.drive.leftFront.setPower(leftFrontPower);
        hub.drive.rightFront.setPower(rightFrontPower);
        hub.drive.leftBack.setPower(leftBackPower);
        hub.drive.rightBack.setPower(rightBackPower);

        telemetry.addData("X",x);
        telemetry.addData("Y",y);
        telemetry.addData("RX",rx);
        updateTelemetry(telemetry);
    }


}
