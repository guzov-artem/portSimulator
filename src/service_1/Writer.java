package service_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Writer {
    public static void writeToJson() throws IOException {
        TimeTable timeTable = new TimeTable(50);
        timeTable.generateShips();
        JsonWriter  writer = new JsonWriter(new FileWriter(System.getProperty("user.dir") + "/timetable.json"));
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
        Type TYPE = new TypeToken<ArrayList<Ship>[]>() {}.getType();
        gson.toJson(timeTable.getShipArrayList_(), TYPE, writer);
        writer.close();
    }
}
