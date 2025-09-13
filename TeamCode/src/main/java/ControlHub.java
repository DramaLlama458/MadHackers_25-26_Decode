import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ControlHub {
    public DcMotor leftFront;
    public DcMotor rightFront;
    public DcMotor leftBack;
    public DcMotor rightBack;

    public void init(HardwareMap map){
        leftFront = map.get(DcMotor.class,"leftFront");
        rightFront = map.get(DcMotor.class,"rightFront");
        leftBack = map.get(DcMotor.class,"leftBack");
        rightBack = map.get(DcMotor.class,"rightBack");
    }
}
