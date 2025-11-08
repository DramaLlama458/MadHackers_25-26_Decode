package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class    MeepMeepTesting {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        MeepMeep meepMeep = new MeepMeep(650, 100);
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


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        myBot.setPose(BlueAudienceSideInitialPose);
        myBot.runAction(myBot.getDrive().actionBuilder(BlueAudienceSideInitialPose)

                //Currently example of potential roadrunner program with just movement
                .waitSeconds(.5)
                .strafeToLinearHeading(BlueTestScoringPose.position,BlueTestScoringPose.heading)
                .waitSeconds(2)

                .strafeToLinearHeading(BlueArtifactsMiddlePose.position,BlueArtifactsMiddlePose.heading)
                /*
                .strafeToLinearHeading(BlueArtifactsMiddleCollectPose.position,BlueArtifactsMiddleCollectPose.heading)

                .waitSeconds(.5)
                .splineTo(BlueGateSetupPose.position,BlueGateSetupPose.heading)
                .strafeToLinearHeading(BlueGatePushPose.position,BlueGatePushPose.heading)
                .waitSeconds(1)

                .strafeToLinearHeading(BlueArtifactsMiddlePose.position,BlueArtifactsMiddlePose.heading)
                .strafeToLinearHeading(BlueTestScoringPose.position,BlueTestScoringPose.heading)
                .waitSeconds(1.8)

                .strafeToLinearHeading(BlueArtifactsClosePose.position,BlueArtifactsClosePose.heading)
                .strafeToLinearHeading(BlueArtifactsCloseCollectPose.position,BlueArtifactsCloseCollectPose.heading)
                .strafeToLinearHeading(BlueArtifactsClosePose.position,BlueArtifactsClosePose.heading)
                .strafeToLinearHeading(BlueTestScoringPose.position,BlueTestScoringPose.heading)
                .waitSeconds(1.8)

                .strafeToLinearHeading(BlueArtifactsFarPose.position,BlueArtifactsFarPose.heading)
                .strafeToLinearHeading(BlueArtifactsFarCollectPose.position,BlueArtifactsFarCollectPose.heading)
                .strafeToLinearHeading(BlueArtifactsFarPose.position,BlueArtifactsFarPose.heading)
                .strafeToLinearHeading(BlueTestScoringPose.position,BlueTestScoringPose.heading)
                .waitSeconds(1.5)
                //This turn is just here to make sure the waitSeconds command doesn't break and cause the robot to jump to the center.
                //Replace it at any time.
                .turn(.01)

                 */



                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
