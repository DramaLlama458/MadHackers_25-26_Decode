package org.firstinspires.ftc.teamcode;

import android.hardware.camera2.CameraDevice;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    /*
    public DcMotor outputMotor;
    public CRServo inputServo;
    public double conveyorSpeed = 0;

    public boolean inputOn = false;
    */


    public ControlHub(HardwareMap map, Pose2d initialPose, Telemetry tel)
    {
        if (initialPose == null)
        {
            initialPose = new Pose2d(0, 0, Math.toRadians(0));
        }
        // Initial pose is just so hub can be used for autonomous

        /*
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
         */

        vision = new Vision(map.get(WebcamName.class, "Webcam 1"));
        detector = new AprilTagDetector(vision);
        drive = new MecanumDrive(map, initialPose); //This is for autonomous and not teleop
        telemetry = tel;
        //conveyorMotor = map.get(DcMotor.class, "conveyorMotor");
        //This is the code setup for the future motors and servos on the robot
        /*
        outputMotor = map.get(DcMotor.class,"outputMotor");
        inputServo = map.get(CRServo.class,"inputServo");
        */
    }
}
