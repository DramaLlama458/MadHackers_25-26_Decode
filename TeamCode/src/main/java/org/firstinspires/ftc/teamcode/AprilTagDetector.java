package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.vision.VisionPortal.CameraState.STREAMING;

import android.graphics.Canvas;
import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;
import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AprilTagDetector
{
    AprilTagProcessor tagProcessor;
    Vision vision;
    // There is 3 in this years competition.
    // One for Red team.  (Left/Right)
    // One for the motif. (Middle, always)
    // One for blue team. (Left/Right)
    AprilTagDetection red;
    AprilTagDetection motif;
    AprilTagDetection blue;
    Thread mainThread;
    ReentrantLock mutex = new ReentrantLock();
    //Condition cond = mutex.newCondition();

    public enum Color
    {
        Green,
        Purple
    }

    public AprilTagDetector(Vision _v) {
        vision = _v;

        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(FTCDebug.IS_DEBUG_MODE)
                .setDrawCubeProjection(FTCDebug.IS_DEBUG_MODE)
                .setDrawTagID(FTCDebug.IS_DEBUG_MODE)
                .setDrawTagOutline(FTCDebug.IS_DEBUG_MODE)
                .build();

        // Attach to camera
        vision.GetPortal().setProcessorEnabled(tagProcessor, true);

        // Init thread.

        mainThread = new Thread(this::MotifThread);

        mainThread.start();
    }

    private void MotifThread() {
        while (true)
        {
            while (vision.GetPortal().getCameraState() == STREAMING)
            {
                ArrayList<AprilTagDetection> tags;
                final int blueTag = 20;

                final int[] motifTags = { 21, 22, 23 };
                final int redTag = 24;

                tags = tagProcessor.getDetections();

                for (AprilTagDetection tag : tags)
                {
                    final float MAX_MARGIN_OF_ERROR = .10f;
                    float marginOfError = 1 - tag.decisionMargin;

                    if (marginOfError < MAX_MARGIN_OF_ERROR)
                    {   continue;
                    }

                    this.mutex.lock();

                    switch(tag.id)
                    {
                        case blueTag:
                            this.blue = tag;
                            break;
                        case redTag:
                            this.red = tag;
                            break;
                    }

                    if(this.motif == null)
                    {
                        for(int tagNum : motifTags)
                        {
                            if (tag.id == tagNum)
                            {
                                this.motif = tag;
                                break;
                            }
                        }
                    }

                    this.mutex.unlock();
                }
                long  milliseconds = Math.round(1000f / vision.GetPortal().getFps());

                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public AprilTagDetection GetMotif()
    {
        AprilTagDetection m = null;
        boolean locked = false;

        locked = mutex.tryLock();

        if(locked)
        {
            m = motif;
            mutex.unlock();
        }

        return m;
    }

    public Color[] GetColorOrder()
    {
        AprilTagDetection m = GetMotif();

        if (m == null)
        {   return null;
        }

        switch(m.id)
        {
            case 21:
                return new Color[]{Color.Green, Color.Purple, Color.Purple};
            case 22:
                return new Color[]{Color.Purple, Color.Green, Color.Purple};
            case 23:
                return new Color[]{Color.Purple, Color.Purple, Color.Green};
            default:
                break;
        }

        throw new RuntimeException(String.format("Failed to get AprilTag Code: [%d]", m.id));
    }
}
