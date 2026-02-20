package org.firstinspires.ftc.teamcode;

import android.hardware.camera2.CameraDevice;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.names.WebcamNameImpl;

public class ControlHub
{
    MecanumDrive drive;


    //public DcMotor conveyorMotor;
    public Telemetry telemetry;
    public Vision vision;
    public AprilTagDetector detector;

    public DcMotorEx bottomOutputMotor;
    public DcMotorEx topOutputMotor;
    public CRServo conveyorServo;

    public CRServo inputServo;

    public ControlHub(HardwareMap map, Pose2d initialPose, Telemetry tel)
    {
        if (initialPose == null)
        {
            initialPose = new Pose2d(0, 0, Math.toRadians(0));
        }
        // Initial pose is just so hub can be used for autonomous

        inputServo = map.get(CRServo.class,"inputServo");
        inputServo.setDirection(CRServo.Direction.REVERSE);

        conveyorServo = map.get(CRServo.class,"conveyorServo");
        conveyorServo.setDirection(CRServo.Direction.REVERSE);
        bottomOutputMotor = map.get(DcMotorEx.class,"bottomOutputMotor");
        topOutputMotor = map.get(DcMotorEx.class,"topOutputMotor");


        WebcamName webName;

        try
        {
            webName = map.get(WebcamName.class, "Webcam 1");
        }
        catch(Exception e)
        {   webName = null;
        }

        vision = new Vision(webName);
        detector = new AprilTagDetector(vision);
        drive = new MecanumDrive(map, initialPose); //This is for autonomous and not teleop
        telemetry = tel;

        try {
            telemetry.addData("1", vision.GetPortal());
            telemetry.addData("1", vision.GetPortal().getCameraState());
        }
        catch(Exception e)
        {
        }
        // usb hub is facing backwards.
        map.get(IMU.class, "imu").initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
        ));

        //conveyorMotor = map.get(DcMotor.class, "conveyorMotor");
        //This is the code setup for the future motors and servos on the robot
        /*
        outputMotor = map.get(DcMotor.class,"outputMotor");
        inputServo = map.get(CRServo.class,"inputServo");
        */
    }
}
