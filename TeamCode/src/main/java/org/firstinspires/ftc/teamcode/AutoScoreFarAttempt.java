package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutoScoreFarAttempt extends LinearOpMode {
    ControlHub hub;

    private int storedAmount = 1;
    @Override
    public void runOpMode() throws InterruptedException {
        hub = new ControlHub(hardwareMap, null, telemetry);


        waitForStart();
        resetRuntime();
        if (isStopRequested()) return;
        launchBall();
        resetRuntime();
        while (opModeIsActive()) {

            telemetry.addLine("Launch Done");

            if (getRuntime() < .75) {
                hub.drive.leftFront.setPower(-.5);
                hub.drive.rightFront.setPower(-.5);
                hub.drive.leftBack.setPower(-.5);
                hub.drive.rightBack.setPower(-.5);
            }

            if (getRuntime() > .75) {
                hub.drive.leftFront.setPower(0);
                hub.drive.rightFront.setPower(0);
                hub.drive.leftBack.setPower(0);
                hub.drive.rightBack.setPower(0);
                break;
            }
            telemetry.addData("Runtime: ", getRuntime());
            updateTelemetry(telemetry);

        }
    }

        public void launchBall(){

            boolean triggered = false;

            hub.topOutputMotor.setVelocity(88 * 28);
            hub.bottomOutputMotor.setVelocity(88 * 28);
            while (true){
            if (hub.topOutputMotor.getVelocity() > 5000/60*28&&hub.bottomOutputMotor.getVelocity() > 5000/60*28) {
                triggered = true;

            }else if(!triggered){
                resetRuntime();
                telemetry.addLine("Time reset during launch");
            }
            if (triggered&&getRuntime()>1) {
                hub.conveyorServo.setPower(1);
                if (getRuntime()>2) {
                    hub.conveyorServo.setPower(0);
                    if(getRuntime()>4.5) {
                        hub.conveyorServo.setPower(1);
                        if(getRuntime()>8){
                            hub.topOutputMotor.setPower(0);
                            hub.bottomOutputMotor.setPower(0);
                            hub.inputServo.setPower(1);
                        }
                        if (getRuntime()>9.5) {
                            hub.inputServo.setPower(0);
                            hub.conveyorServo.setPower(0);
                            break;
                        }
                    }
                }
            }
            telemetry.addData("Top RPM",hub.topOutputMotor.getVelocity()/28*60);
            telemetry.addData("Bottom RPM",hub.bottomOutputMotor.getVelocity()/28*60);
            telemetry.addLine();
            telemetry.addData("Triggered",triggered);
            telemetry.addData("Runtime",getRuntime());
            telemetry.update();

            }
            storedAmount--;
            if(storedAmount>0){
                launchBall();
            }
        }


    }







