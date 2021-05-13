package service_1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class TimeTable {
    public TimeTable()
    {
        try {
            readTimeTableParams();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        shipArrayList = new ArrayList[3];
        for (int i = 0; i < shipArrayList.length; i++){
            shipArrayList[i] = new ArrayList<Ship>();
        }
    }
    private void readTimeTableParams() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir") + "/timetableParams.json"));
        Map gsonParser = new Gson().fromJson(reader, Map.class);
        size_ = ((Double) gsonParser.get("size")).intValue();
        speed = ((ArrayList<Double>) gsonParser.get("speed"));
        names = (ArrayList<String>) gsonParser.get("names");


    }
    public void generateShips() {
        for (int i = 0; i < size_; i++) {
            Ship ship = makeRandomShip();
            shipArrayList[ship.getCargo().getType().getInt()].add(ship);
        }
    }

    public ArrayList<Ship>[] getShipArrayList() {
        return shipArrayList;
    }

    private static Ship makeRandomShip()
    {
      return new Ship(names.get(random.nextInt(names.size() - 1)), makeRandomCargo(), new Date(121,
        3, random.nextInt(30), random.nextInt(24),
        random.nextInt(60), random.nextInt(60)));
    }
    private static Cargo makeRandomCargo()
    {
        Integer temp = random.nextInt(3);
        Cargo.CargoType type = null;
        switch (temp)
        {
            case 0: type = Cargo.CargoType.LOOSE;
            break;

            case 1: type = Cargo.CargoType.LIQUID;
            break;

            case 2: type = Cargo.CargoType.CONTAINERS;
            break;
        }
        return new Cargo(type, random.nextDouble()* 1000, speed.get(temp));
    }

    public static long getMinutes(Date date) {
        return (date.getTime()/1000)/60;
    }
    static final Random random = new Random();
    //сделать чтение из файла
    static ArrayList<String> names;
    static ArrayList<Double> speed;
    private ArrayList<Ship>[] shipArrayList;
    private Integer size_;
}
