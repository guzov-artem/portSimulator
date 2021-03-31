import service_1.Cargo;
import service_1.Ship;
import service_1.TimeTable;
import service_1.Writer;
import service_2.JSONReader;
import service_3.PortSimulator;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        Writer.writeToJson();
        ArrayList<Ship>[] ships = JSONReader.readFromJson();
        ArrayList<Ship> s = ships[0];
        Date start = new Date(121, 3, 0, 0, 40, 0);
        Date end = new Date(121, 3, 0, 0, 60, 0);
        PortSimulator portSimulator = new PortSimulator(ships, start, end);
        portSimulator.findBest();
    }
}
