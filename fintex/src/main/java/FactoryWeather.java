import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FactoryWeather
{
    //словарь для сопоставления региона с его идентификатором
     Map<String, Integer> mapIdReg = new HashMap<>();

    public int generateId (String reg){
        if (!mapIdReg.containsKey(reg)) {
            mapIdReg.put(reg, mapIdReg.size() + 1);
        }
        return mapIdReg.get(reg);
    }

    //создание объекта класса Weather и присвоение идентификатора
    public Weather createWeather(String reg, int temp, Date dateTime) {
        return new Weather(generateId(reg),reg,temp,dateTime);
    }
}