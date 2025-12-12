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
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;

import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AprilTagDetector
{
    private AprilTagProcessor tagProcessor = null;
    private Vision vision = null;
    // There is 3 in this years competition.
    // One for Red team.  (Left/Right)
    // One for the motif. (Middle, always)
    // One for blue team. (Left/Right)
    private AprilTagDetection red = null;
    private AprilTagDetection lastSeenRed = null;
    private AprilTagDetection blue = null;
    private AprilTagDetection lastSeenBlue = null;
    private AprilTagDetection motif = null;
    private AprilTagDetection lastSeenMotif = null;
    private Color []motifOrder = null;
    private TeamColor teamColor = null;

    public enum TeamColor
    {
        Red,
        Blue
    }

    public enum Color
    {
        Green,
        Purple
    }

    public AprilTagDetector(Vision _v) {
        this.vision = _v;

        // Camera not detected?
        if(this.IsUnAvailable())
        {   return;
        }

        // start streaming if not streaming already.
        if(this.vision.GetPortal().getCameraState() != STREAMING)
        {
            this.vision.GetPortal().resumeStreaming();
            this.vision.GetPortal().resumeLiveView();
        }
        this.tagProcessor = this.vision.GetProcessor();
    }

    public boolean IsUnAvailable()
    {   return this.vision.GetProcessor() == null || this.vision.GetPortal() == null;
    }

    public void Update()
    {
        if(IsUnAvailable())
        {   return;
        }

        if(this.vision.GetPortal().getCameraState() != STREAMING)
        {
            this.vision.GetPortal().resumeStreaming();
            return;
        }
        ArrayList<AprilTagDetection> tags;

        final int blueTag = 20;
        final int motif1 = 21;
        final int motif2 = 22;
        final int motif3 = 23;
        final int redTag = 24;

        tags = this.tagProcessor.getDetections();

        // setup LastSeen
        if(this.red != null)
        {   this.lastSeenRed = this.red;
        }
        if(this.blue != null)
        {   this.lastSeenBlue = this.blue;
        }
        if(this.motif != null)
        {   this.lastSeenMotif = this.motif;
        }

        // Is this the first time?
        if(this.teamColor == null)
        {
            // Assume closest is team color.
            if(this.lastSeenRed != null && this.lastSeenBlue != null)
            {
                AprilTagPoseFtc redPose = this.lastSeenRed.ftcPose;
                AprilTagPoseFtc bluePose = this.lastSeenBlue.ftcPose;

                double redDistance = Math.sqrt(redPose.x * redPose.x + redPose.y * redPose.y + redPose.z * redPose.z);
                double blueDistance = Math.sqrt(bluePose.x * bluePose.x + bluePose.y * bluePose.y + bluePose.z * bluePose.z);

                if(redDistance > blueDistance)
                {   this.teamColor = TeamColor.Red;
                }
                else
                {   this.teamColor = TeamColor.Blue;
                }
            }
            else if(this.lastSeenRed != null)
            {   this.teamColor = TeamColor.Red;
            }
            else if(this.lastSeenBlue != null)
            {   this.teamColor = TeamColor.Blue;
            }
        }

        this.motif = null;
        this.red = null;
        this.blue = null;

        for (AprilTagDetection tag : tags)
        {
            final float MIN_MARGIN = .90f;
            float marginOfError =  tag.decisionMargin;

            if (marginOfError < MIN_MARGIN)
            {   continue;
            }

            switch(tag.id)
            {
                case blueTag:
                    this.blue = tag;
                    break;
                case redTag:
                    this.red = tag;
                    break;
                case motif1:
                case motif2:
                case motif3:
                    this.motif = tag;
                    break;
            }
        }

        // update motif patter if found.
        if(this.motif != null && this.motifOrder == null)
        {
            switch(this.motif.id)
            {
                case motif1:
                    this.motifOrder = new Color[]{ Color.Green, Color.Purple, Color.Purple };
                    break;
                case motif2:
                    this.motifOrder = new Color[]{ Color.Purple, Color.Green, Color.Purple };
                    break;
                case motif3:
                    this.motifOrder = new Color[] { Color.Purple, Color.Purple, Color.Green };
                    break;
            }
        }
    }

    public AprilTagDetection GetMotif()
    {
        AprilTagDetection m = null;

        m = this.motif;

        return m;
    }

    public AprilTagDetection GetRed()
    {
        AprilTagDetection r = null;

        r = this.red;

        return r;
    }

    public AprilTagDetection GetLastSeenRed()
    {
        AprilTagDetection r = null;

        r = this.lastSeenRed;

        return r;
    }

    public AprilTagDetection GetBlue()
    {
        AprilTagDetection b = null;

        b = this.blue;

        return b;
    }

    public AprilTagDetection GetLastSeenBlue()
    {
        AprilTagDetection b = null;

        b = this.lastSeenBlue;

        return b;
    }

    public void SwitchTeam()
    {
        if(this.teamColor == null)
        {   return;
        }

        switch(this.teamColor)
        {
            case Red:
                this.teamColor = TeamColor.Blue;
                break;
            case Blue:
                this.teamColor = TeamColor.Red;
                break;
        }
    }
    public AprilTagDetection GetTeam()
    {
        TeamColor col = this.GetTeamColor();
        AprilTagDetection tag = null;

        if(col != null)
        {
            switch(col)
            {
                case Red:
                    tag = this.GetRed();
                    break;
                case Blue:
                    tag = this.GetBlue();
                    break;
            }
        }

        return tag;
    }

    public AprilTagDetection GetLastSeenTeam()
    {
        TeamColor col = this.GetTeamColor();
        AprilTagDetection tag = null;

        if(col != null)
        {
            switch(col)
            {
                case Red:
                    tag = this.GetLastSeenRed();
                    break;
                case Blue:
                    tag = this.GetLastSeenBlue();
                    break;
            }
        }

        return tag;
    }

    public Color[] GetColorOrder()
    {
        Color[] col = null;

        col = this.motifOrder;

        return col;
    }

    public TeamColor GetTeamColor()
    {
        TeamColor col = null;

        col = this.teamColor;

        return col;
    }
}
