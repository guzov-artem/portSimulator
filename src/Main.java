import service_1.Cargo;
import service_1.Ship;
import service_1.TimeTable;
import service_1.Writer;
import service_2.JSONReader;
import service_3.PortSimulator;
import service_3.Statistic;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, CloneNotSupportedException, InterruptedException {
        Writer.writeToJson();
        ArrayList<Ship>[] ships = JSONReader.readFromJson();


        Date start = new Date(121, 3, 0, 0, 0, 0);
        Date end = new Date(121, 3, 5, 0, 0, 0);
        PortSimulator portSimulator = new PortSimulator(ships, start, end);
        portSimulator.findBest();
        Statistic stat = portSimulator.getStatistic();
        stat.generateStatistic();
        stat.writeToFile("statistic.json");
    }
}
