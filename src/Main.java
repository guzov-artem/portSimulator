import service_1.Ship;
import service_1.Writer;
import service_2.JSONReader;
import service_3.PortSimulator;
import service_3.Statistic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, CloneNotSupportedException, InterruptedException {
        Writer.writeToJson();
        ArrayList<Ship>[] ships = JSONReader.readFromJson();
        Date start = new Date(121, 3, 0, 0, 0, 0);
        Date end = new Date(121, 4, 0, 0, 0, 0);
        PortSimulator portSimulator = new PortSimulator(ships, start, end);
        portSimulator.findBestStatistic();
        Statistic stat = portSimulator.getStatistic();
        stat.writeToFile("statistic.json");
    }
}
