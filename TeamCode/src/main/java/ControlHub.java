import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

// lgkfjlkgfjgflgjfflgjflgdfjgflgjffjgflkgjflk4234gf
public class ControlHub {
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor leftBack;
    public DcMotor rightBack;

    /*
    public DcMotor conveyorMotor;
    public DcMotor outputMotor;
    public CRServo inputServo;
    public double conveyorSpeed = 0;
    */

    public void init(HardwareMap map){
        FtcDashboard dashboard = FtcDashboard.getInstance();

        leftFront = map.get(DcMotor.class,"leftFront");
        rightFront = map.get(DcMotor.class,"rightFront");
        leftBack = map.get(DcMotor.class,"leftBack");
        rightBack = map.get(DcMotor.class,"rightBack");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


        /*
        conveyorMotor = map.get(DcMotor.class,"conveyorMotor");
        outputMotor = map.get(DcMotor.class,"outputMotor");
        inputServo = map.get(CRServo.class,"inputServo");
        */


    }
}
