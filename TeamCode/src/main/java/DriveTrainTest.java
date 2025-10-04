import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp
public class DriveTrainTest extends LinearOpMode {
    ControlHub hub;
    private FtcDashboard dash = FtcDashboard.getInstance();


    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub();
        //Initial pose is just so hub can be used for autonomous
        Pose2d initialPose = new Pose2d(0,0, Math.toRadians(0));
        hub.init(hardwareMap, initialPose);



        waitForStart();


        if (isStopRequested()) return;

        while (opModeIsActive()) {
            wheelMovement();
            dash.sendTelemetryPacket(new TelemetryPacket());



            /*
            if(gamepad1.x && !hub.inputOn){
                hub.inputServo.setPower(1);
                hub.inputOn=true;
            }else{
                hub.inputServo.setPower(0);
            }
            if(gamepad1.xWasReleased()){
                hub.inputOn=false;
            }



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

        hub.leftFront.setPower(leftFrontPower);
        hub.rightFront.setPower(rightFrontPower);
        hub.leftBack.setPower(leftBackPower);
        hub.rightBack.setPower(rightBackPower);
    }
}