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
    public void runOpMode() throws InterruptedException
    {
        //Initial pose is just so hub can be used for autonomous
        //TODO: Figure out starting position of robot at start of match
        Pose2d RedGoalInitialPose = new Pose2d(-49,49,Math.toRadians(-35));
        Pose2d BlueGoalInitialPose = new Pose2d(-49,-49,Math.toRadians(55));
        Pose2d RedAudienceSideInitialPose = new Pose2d(54,21,Math.toRadians(135));
        Pose2d BlueAudienceSideInitialPose = new Pose2d(54,-21,Math.toRadians(-135));

        Pose2d RedBaseZonePose = new Pose2d(38,-32.5,Math.toRadians(0));
        Pose2d BlueBaseZonePose = new Pose2d(38,32.5,Math.toRadians(0));
        Pose2d RedTestScoringPose = new Pose2d(-20,20,Math.toRadians(132));
        Pose2d BlueTestScoringPose = new Pose2d(-20,-20,Math.toRadians(-135));
        Pose2d RedLoadingZonePose = new Pose2d(60,60,Math.toRadians(-90));
        Pose2d BlueLoadingZonePose = new Pose2d(60,-60,Math.toRadians(90));

        Pose2d RedGateSetupPose = new Pose2d(8,50,Math.toRadians(-90));
        Pose2d BlueGateSetupPose = new Pose2d(8,-50,Math.toRadians(90));
        Pose2d RedGatePushPose = new Pose2d(8,55,Math.toRadians(-90));
        Pose2d BlueGatePushPose = new Pose2d(8,-55,Math.toRadians(90));

        Pose2d RedArtifactsFarPose = new Pose2d(-11.5,29,Math.toRadians(90));
        Pose2d RedArtifactsMiddlePose = new Pose2d(12.5,29,Math.toRadians(90));
        Pose2d RedArtifactsClosePose = new Pose2d(36,29,Math.toRadians(90));
        Pose2d RedArtifactsFarCollectPose = new Pose2d(-11.5,50,Math.toRadians(90));
        Pose2d RedArtifactsMiddleCollectPose = new Pose2d(12.5,50,Math.toRadians(90));
        Pose2d RedArtifactsCloseCollectPose = new Pose2d(36,50,Math.toRadians(90));


        Pose2d BlueArtifactsFarPose = new Pose2d(-11.5,-29,Math.toRadians(-90));
        Pose2d BlueArtifactsMiddlePose = new Pose2d(12.5,-29,Math.toRadians(-90));
        Pose2d BlueArtifactsClosePose = new Pose2d(36,-29,Math.toRadians(-90));
        Pose2d BlueArtifactsFarCollectPose = new Pose2d(-11.5,-50,Math.toRadians(-90));
        Pose2d BlueArtifactsMiddleCollectPose = new Pose2d(12.5,-50,Math.toRadians(-90));
        Pose2d BlueArtifactsCloseCollectPose = new Pose2d(36,-50,Math.toRadians(-90));

        Pose2d CenterFaceGoalsPose = new Pose2d(0,0,Math.toRadians(180));
        Pose2d CenterFaceAudiencePose = new Pose2d(0,0,Math.toRadians(0));

        hub = new ControlHub(hardwareMap, RedAudienceSideInitialPose);

        TrajectoryActionBuilder testMovement = hub.drive.actionBuilder(RedAudienceSideInitialPose)
                .waitSeconds(.5)
                .strafeToLinearHeading(BlueTestScoringPose.position,BlueTestScoringPose.heading)
                .waitSeconds(2)

                .strafeToLinearHeading(BlueArtifactsMiddlePose.position,BlueArtifactsMiddlePose.heading);
        Action action_testMovement = testMovement.build();
        waitForStart();

        Actions.runBlocking( action_testMovement      );
    }
}
