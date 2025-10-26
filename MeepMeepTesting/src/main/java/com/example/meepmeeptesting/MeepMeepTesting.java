package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        MeepMeep meepMeep = new MeepMeep(650, 100);
        Pose2d RedGoalInitialPose = new Pose2d(-49,49,Math.toRadians(-35));
        Pose2d BlueGoalInitialPose = new Pose2d(-49,-49,Math.toRadians(55));
        Pose2d RedAudienceSideInitialPose = new Pose2d(54,21,Math.toRadians(135));
        Pose2d BlueAudienceSideInitialPose = new Pose2d(54,-21,Math.toRadians(-135));
        Pose2d RedBaseZonePose = new Pose2d(38,-32.5,Math.toRadians(0));
        Pose2d BlueBaseZonePose = new Pose2d(38,32.5,Math.toRadians(0));

        Pose2d CenterPose = new Pose2d(0,0,Math.toRadians(90));


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(BlueBaseZonePose)
                .waitSeconds(10)
                .strafeToLinearHeading(CenterPose.position,CenterPose.heading)


                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
