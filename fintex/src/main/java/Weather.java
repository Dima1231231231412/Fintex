import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class Weather {
    private int idReg;
    private String reg;
    private int temp;
    private Date dateTime;

    public Weather(int idReg,String reg, int temp, Date dateTime) {
        this.idReg = idReg;
        this.reg = reg;
        this.temp = temp;
        this.dateTime = dateTime;
    }
}