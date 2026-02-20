package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutoScoreCloseAttempt extends LinearOpMode {
    ControlHub hub;

    private int storedAmount = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null, telemetry);


        waitForStart();
        resetRuntime();
        if (isStopRequested()) return;

        while (opModeIsActive()) {


            if (getRuntime() < 1.25) {
                hub.drive.leftFront.setPower(.5);
                hub.drive.rightFront.setPower(.5);
                hub.drive.leftBack.setPower(.5);
                hub.drive.rightBack.setPower(.5);
            }

            if (getRuntime() > 1.25 && getRuntime() < 1.5) {
                hub.drive.leftFront.setPower(0);
                hub.drive.rightFront.setPower(0);
                hub.drive.leftBack.setPower(0);
                hub.drive.rightBack.setPower(0);
            }
            telemetry.addData("Runtime: ", getRuntime());
            updateTelemetry(telemetry);

            if (getRuntime() > 1.5) {
                launchBall();

            }
            if(storedAmount==0){
                break;
            }
        }
    }

        public void launchBall(){

            int delay = 0;
            int delay2 =0;
            int delay3 = 0;

            hub.topOutputMotor.setVelocity(120 * 18);
            hub.bottomOutputMotor.setVelocity(120 * 18);
            while (true){
            if (hub.topOutputMotor.getVelocity() > 4700) {
                delay++;

            }
            if (delay > 1000) {
                hub.conveyorServo.setPower(1);
                delay2++;
                if (delay2 > 250) {
                    hub.conveyorServo.setPower(1);
                    hub.topOutputMotor.setPower(0);
                    hub.bottomOutputMotor.setPower(0);
                    delay3++;
                    if (delay3>1000){
                        hub.conveyorServo.setPower(0);
                        break;
                    }
                }
            }
            }
        }


    }







