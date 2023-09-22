import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Weather weather1 = new Weather("Владимир", 21, ft.parse("2023-09-13 10:30"));
        Weather weather2 = new Weather("Владимир", 23, ft.parse("2023-09-13 12:00"));
        Weather weather3 = new Weather("Владимир", 24, ft.parse("2023-09-13 13:30"));
        Weather weather4 = new Weather("Рязань", 23, ft.parse("2023-09-14 15:00"));
        Weather weather5 = new Weather("Рязань", 22, ft.parse("2023-09-14 16:30"));
        Weather weather6 = new Weather("Рязань", 26, ft.parse("2023-09-14 13:00"));
        Weather weather7 = new Weather("Москва", 25, ft.parse("2023-09-11 12:30"));
        Weather weather8 = new Weather("Москва", 23, ft.parse("2023-09-11 15:30"));
        Weather weather9 = new Weather("Москва", 24, ft.parse("2023-09-11 16:00"));


        // список объектов
        List<Weather> weathers = new ArrayList<>(
                Arrays.asList(weather1, weather2, weather3, weather4, weather5, weather6, weather7, weather8, weather9)
        );
        // рассчитать среднее значение температуры в регионах
        System.out.println(weathers.stream().collect(Collectors.groupingBy(Weather::getReg, Collectors.averagingInt(Weather::getTemp))));

        // создать функцию для поиска регионов, больше какой-то определенной температуры
        System.out.println(searchReg(weathers, 25));

        // преобразовать список в Map, у которой ключ - уникальный идентификатор, значение - список со значениями температур
        Map<Integer, List<Integer>> temperatureMap = weathers.stream()
                .collect(Collectors.groupingBy(Weather::getId_reg,
                        Collectors.mapping(Weather::getTemp, Collectors.toList())));

        System.out.println(temperatureMap);

        //преобразовать список в Map, у которой ключ - температура, значение - коллекция объектов Weather, которым соответствует температура, указанная в ключе
        Map<Integer, List<Weather>> temperatureMap2 = weathers.stream()
                .collect(Collectors.groupingBy(Weather::getTemp));

        System.out.println(temperatureMap2);
    }

    public static Set<String> searchReg(List<Weather> weathers, Integer temp) {
        return weathers.stream()
                .filter(x -> x.getTemp() > temp)
                .collect(Collectors.groupingBy(Weather::getReg))
                .keySet();
    }


    //словарь для сопоставления региона с его идентификатором
    static Map<String, Integer> generateIdReg = new HashMap<>();
    public static int generateId(String reg) {
        if (!generateIdReg.containsKey(reg)) {
            generateIdReg.put(reg, generateIdReg.size() + 1);
        }
        return generateIdReg.get(reg);
    }
}
