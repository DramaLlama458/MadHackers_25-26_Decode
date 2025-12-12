package org.firstinspires.ftc.teamcode;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class Vision
{
    private VisionPortal visionPortal;
    private AprilTagProcessor processor;
    final static Size resolution = new Size(640, 480);

    public Vision(CameraName web)
    {
        if(web == null || web != null)
        {
            this.processor = null;
            this.visionPortal = null;
            return;
        }

        this.processor = new AprilTagProcessor.Builder()
                .setDrawAxes(FTCDebug.IS_DEBUG_MODE)
                .setDrawCubeProjection(FTCDebug.IS_DEBUG_MODE)
                .setDrawTagID(FTCDebug.IS_DEBUG_MODE)
                .setDrawTagOutline(FTCDebug.IS_DEBUG_MODE)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.RADIANS)
                .build();

        this.visionPortal = new VisionPortal.Builder()
                .setCamera(web)
                .setCameraResolution(resolution)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG) // TODO: Test Other Stream format.
                .addProcessor(this.processor)
                .build();

        this.visionPortal.setProcessorEnabled(this.processor, true);
    }

    public VisionPortal GetPortal()
    {   return this.visionPortal;
    }

    public AprilTagProcessor GetProcessor()
    {   return this.processor;
    }
}
