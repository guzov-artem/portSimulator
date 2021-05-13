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
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Statistic {

    private int unloadTime = 0;
    private int waitingTime = 0;
    private int delayTime = 0;
    private double averageWaitingTime = 0;
    private double averageDelayTime = 0;
    private double averageUnloadTime = 0;
    private int fine = 0;
    private int amountLiquide = 0;
    private int amountLoose = 0;
    private int amountContainer = 0;
    private int liquideCranes = 0;
    private int containerCranes = 0;
    private int looseCranes = 0;
    private List<ThreadStatistic.ShipStatistic> statLiquidList;
    private List<ThreadStatistic.ShipStatistic> statLooseList;
    private List<ThreadStatistic.ShipStatistic> statContainerList;

    {
        statLiquidList = Collections.synchronizedList(new ArrayList());
        statLooseList = Collections.synchronizedList(new ArrayList());
        statContainerList = Collections.synchronizedList(new ArrayList());
    }

    synchronized public  void addStatisticStruct(ThreadStatistic threadStatistic) {
        switch (threadStatistic.getType()) {
            case LIQUID: {
                statLiquidList = threadStatistic.getShipsStatistics();
                amountLiquide = statLiquidList.size();
                liquideCranes = threadStatistic.getCranes();
            }
            break;
            case LOOSE: {
                statLooseList = threadStatistic.getShipsStatistics();
                amountLoose = statLooseList.size();
                looseCranes = threadStatistic.getCranes();
            }
            break;
            case CONTAINERS: {
                statContainerList = threadStatistic.getShipsStatistics();
                amountContainer = statContainerList.size();
                containerCranes = threadStatistic.getCranes();
            }
            break;
        }
        unloadTime += threadStatistic.getUnloadTime();
        waitingTime += threadStatistic.getWaitingTime();
        delayTime += threadStatistic.getDelayTime();
        fine += threadStatistic.getFine();
        averageWaitingTime = waitingTime / (amountLoose + amountLiquide + amountContainer);
        averageDelayTime = delayTime / (amountLoose + amountLiquide + amountContainer);
        averageUnloadTime = unloadTime / (amountLoose + amountLiquide + amountContainer);
    }

    public void writeToFile(String name) throws IOException {
        JsonWriter writer = new JsonWriter(new FileWriter(System.getProperty("user.dir") + "/" + name));
        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss").setPrettyPrinting().create();
        Type TYPE = new TypeToken<Statistic>() {
        }.getType();
        gson.toJson(this, TYPE, writer);
        writer.close();
    }


}
class ThreadStatistic {
    public static class ShipStatistic {
        private String name;
        private Date arrivalTime;
        private long waitingTime = 0;
        private Date startUnloadTime;
        private Date endUnloadingTime;
        private long unloadTime = 0;
        private Cargo.CargoType type;
        private int delay;

        public ShipStatistic(Ship ship) {
            this.name = ship.getName();
            this.arrivalTime = ship.getArriveDate();
            this.startUnloadTime = ship.getStartUploading();
            this.waitingTime = TimeTable.getMinutes(startUnloadTime) - TimeTable.getMinutes(arrivalTime);
            this.unloadTime = TimeTable.getMinutes(ship.getEndUnloading())
                    - TimeTable.getMinutes(ship.getStartUploading());
            this.type = ship.getCargo().getType();
            this.delay = ship.getDelay();
            this.endUnloadingTime = ship.getEndUnloading();
        }
    }
    public ThreadStatistic(Cargo.CargoType type){
        shipsStatistics = Collections.synchronizedList(new ArrayList());
        this.type = type;
    }
    private List<ShipStatistic> shipsStatistics;
    private double fine = 0;
    private int waitingTime = 0;
    private int cranes = 0;
    public int delayTime = 0;
    private int unloadTime = 0;
    private static int craneCost = 30000;
    private Cargo.CargoType type;

    synchronized double getFine() {
        return fine;
    }

    synchronized public void setCranes(int cranes) {
        this.cranes = cranes;
    }

    synchronized public int getCranes() {
        return cranes;
    }

    synchronized public Cargo.CargoType getType() {
        return type;
    }

    synchronized public List<ShipStatistic> getShipsStatistics() {
        return shipsStatistics;
    }

    synchronized public int getDelayTime() {
        return delayTime;
    }

    synchronized public int getUnloadTime() {
        return unloadTime;
    }

    synchronized public int getWaitingTime() {
        return waitingTime;
    }

    synchronized private void addShipStatistic(ShipStatistic shipStatistic) {
        this.shipsStatistics.add(shipStatistic);
        if (shipStatistic.waitingTime > 0) {
            this.waitingTime += shipStatistic.waitingTime;
        }
        this.delayTime += shipStatistic.delay;
        this.unloadTime += shipStatistic.unloadTime;
        this.fine = (this.waitingTime / 60.0) * 100.0 + (this.cranes - 1) * this.craneCost;
    }
    synchronized public void addShipStatistic(Ship ship) {
        this.addShipStatistic(new ShipStatistic(ship));
    }
}
