package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

// lgkfjlkgfjgflgjfflgjflgdfjgfldgjffjgflkgjflk4234gf
public class ControlHub {
    MecanumDrive drive;
    public IMU imu;
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor leftBack;
    public DcMotor rightBack;

    /*
    public DcMotor conveyorMotor;
    public DcMotor outputMotor;
    public CRServo inputServo;
    public double conveyorSpeed = 0;

    public boolean inputOn = false;
    */


    public void init(HardwareMap map, Pose2d initialPose){
        imu = map.get(IMU.class,"imu");
        leftFront = map.get(DcMotor.class,"leftFront");
        rightFront = map.get(DcMotor.class,"rightFront");
        leftBack = map.get(DcMotor.class,"leftBack");
        rightBack = map.get(DcMotor.class,"rightBack");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        drive = new MecanumDrive(map, initialPose); //This is for autonomous and not teleop

        //This is the code setup for the future motors and servos on the robot
        /*
        conveyorMotor = map.get(DcMotor.class,"conveyorMotor");
        outputMotor = map.get(DcMotor.class,"outputMotor");
        inputServo = map.get(CRServo.class,"inputServo");
        */


    }
}
