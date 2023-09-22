import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class Weather {
    private int id_reg;
    private String reg;
    private int temp;
    private Date dateTime;

    public Weather(String reg, int temp, Date dateTime) {
        this.id_reg = Main.generateId(reg);
        this.reg = reg;
        this.temp = temp;
        this.dateTime = dateTime;
    }

}