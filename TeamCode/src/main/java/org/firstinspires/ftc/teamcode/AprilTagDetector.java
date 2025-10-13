package org.firstinspires.ftc.teamcode;


import android.graphics.Canvas;
import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class AprilTagDetector {
    AprilTagProcessor tagProcessor;
    VisionPortal visionPortal;

    // There is 3 in this years competition.
    // One for Red team.  (Left/Right)
    // One for the motif. (Middle, always)
    // One for blue team. (Left/Right)
    AprilTagDetection red;
    AprilTagDetection motif;
    AprilTagDetection blue;

    final static Size resolution = new Size(640, 480);

    public AprilTagDetector() {

        // Debug
        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(FTCDebug.IS_DEBUG_MODE)
                .setDrawCubeProjection(FTCDebug.IS_DEBUG_MODE)
                .setDrawTagID(FTCDebug.IS_DEBUG_MODE)
                .setDrawTagOutline(FTCDebug.IS_DEBUG_MODE)
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                // .setCamera() // TODO
                .setCameraResolution(resolution)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG) // TODO: Test Other Stream format.
                .build();
    }


    // Turn on camera processing.
    // Resource intensive, do not use every second or every 5 seconds.
    public void StartStreaming()
    {   visionPortal.resumeStreaming();
    }

    // Turn off camera processing.
    // Resource intensive, do not use every second or every 5 seconds.
    public void StopStreaming()
    {   visionPortal.stopStreaming();
    }

    public AprilTagDetection GetMotif()
    {
        // TODO
        return null;
    }

}
