package org.firstinspires.ftc.teamcode;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

public class Vision
{
    Camera cam;
    VisionPortal visionPortal;
    final static Size resolution = new Size(640, 480);

    public Vision(Camera camera)
    {
        cam = camera;
        visionPortal = new VisionPortal.Builder()
                .setCamera(cam.getCameraName())
                .setCameraResolution(resolution)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG) // TODO: Test Other Stream format.
                .build();

        visionPortal.resumeStreaming();
    }

    public Camera GetCamera()
    {   return cam;
    }

    public VisionPortal GetPortal()
    {   return visionPortal;
    }
}
