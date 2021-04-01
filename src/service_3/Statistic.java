package service_3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import service_1.Cargo;
import service_1.Ship;
import service_1.TimeTable;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Statistic {

    public int unloadTime = 0;
    public int averageWaitingTime = 0;
    public int averageDelayTime = 0;
    public int fine = 0;
    public int amountLiquide = 0;
    public int amountLoose = 0;
    public int amountContainer = 0;
    public int liquideCranes = 0;
    public int containerCranes = 0;
    public int looseCranes = 0;

    public void addStatisticStruct(StatisticStruct statisticStruct) {
        switch (statisticStruct.shipsStatistics.get(0).type) {
            case LIQUID: statLiquidList = statisticStruct.shipsStatistics;
            liquideCranes = statisticStruct.cranes;
            break;
            case LOOSE: statLooseList = statisticStruct.shipsStatistics;
                looseCranes = statisticStruct.cranes;
            break;
            case CONTAINERS: statContainerList = statisticStruct.shipsStatistics;
                containerCranes = statisticStruct.cranes;
            break;
        }
        fine += statisticStruct.fine;
    }

    public void generateStatistic(){
        int waitingTime = 0;
        int delayTime = 0;
        for (ShipStatistic shipStatistic: statLiquidList) {
            waitingTime += shipStatistic.waitingTime;
            delayTime +=shipStatistic.delay;
            unloadTime += shipStatistic.unloadTime;
        }
        for (ShipStatistic shipStatistic: statLooseList) {
            waitingTime += shipStatistic.waitingTime;
            delayTime +=shipStatistic.delay;
            unloadTime += shipStatistic.unloadTime;
        }
        for (ShipStatistic shipStatistic: statContainerList) {
            waitingTime += shipStatistic.waitingTime;
            delayTime +=shipStatistic.delay;
            unloadTime += shipStatistic.unloadTime;
        }
        amountContainer = statContainerList.size();
        amountLiquide = statLiquidList.size();
        amountLoose = statLooseList.size();
        averageDelayTime = delayTime / (amountLoose + amountLiquide + amountContainer);
        averageWaitingTime = waitingTime / (amountLoose + amountLiquide + amountContainer);
    }

    public void writeToFile(String name) throws IOException {
        JsonWriter writer = new JsonWriter(new FileWriter(System.getProperty("user.dir") + "/" + name));
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").create();
        Type TYPE = new TypeToken<Statistic>() {
        }.getType();
        gson.toJson(this, TYPE, writer);
        writer.close();
    }


    public ArrayList<ShipStatistic> statLiquidList;
    public ArrayList<ShipStatistic> statLooseList;
    public ArrayList<ShipStatistic> statContainerList;
    {
        statLiquidList = new ArrayList<>();
        statLooseList = new ArrayList<>();
        statContainerList = new ArrayList<>();
    }
}

class ShipStatistic {
    public String name;
    public Date arrivalTime;
    public long waitingTime = 0;
    public Date startUploadTime;
    public long unloadTime = 0;
    public Cargo.CargoType type;
    public int delay;

    public ShipStatistic() {}
    public ShipStatistic(Ship ship) {
        this.name = ship.getName_();
        this.arrivalTime = ship.getArriveDate();
        this.startUploadTime = ship.getStartUploading();
        this.waitingTime = TimeTable.getMinutes(startUploadTime) - TimeTable.getMinutes(arrivalTime);
        this.unloadTime = TimeTable.getMinutes(ship.getEndUploading())
                - TimeTable.getMinutes(ship.getStartUploading());
        this.type = ship.getCargo_().getType();
        this.delay = ship.getDelay();
    }
    public ShipStatistic(ShipStatistic ss) {
        name = ss.name;
        arrivalTime = ss.arrivalTime;
        waitingTime = ss.waitingTime;
        startUploadTime = ss.startUploadTime;
        unloadTime = ss.unloadTime;
    }
}

class StatisticStruct{
    public StatisticStruct(){
        shipsStatistics = new ArrayList<ShipStatistic>();
    }
    public ArrayList<ShipStatistic> shipsStatistics;
    public int fine = 0;
    public int waitingTime = 0;
    public int cranes = 0;
    private static int craneCost = 200;
    int getFine() {
        return fine;
    }
    public void addShipStatistic(ShipStatistic shipStatistic) {
        shipsStatistics.add(shipStatistic);
        waitingTime += shipStatistic.waitingTime;
        fine = waitingTime / 60 * 100 + cranes*craneCost;
    }
    public void addShipStatistic(Ship ship) {
        this.addShipStatistic(new ShipStatistic(ship));
    }
}
