package service_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import service_1.Ship;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONReader {
    public static ArrayList<Ship>[] readFromJson() throws IOException {
        JsonReader reader = new JsonReader(new FileReader(System.getProperty("user.dir") + "/timetable.json"));
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
        Type SHIP_TYPE = new TypeToken<ArrayList<Ship>[]>() {}.getType();
        ArrayList<Ship>[] ships = gson.fromJson(reader, SHIP_TYPE);
        reader.close();
        return ships;
    }
}
