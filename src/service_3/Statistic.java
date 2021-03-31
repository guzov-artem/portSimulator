package service_3;

import service_1.Cargo;
import service_1.Ship;
import service_1.TimeTable;

import java.util.ArrayList;
import java.util.Date;

public class Statistic {
    public int unloadTime = 0;
    public int averageSheldue = 0;
    public int averageWaitingTime = 0;
    public int averageDelayTime = 0;
    public int fine = 0;
    public int amountLiquide = 0;
    public int amountLoose = 0;
    public int amountContainer = 0;

    public static class ShipStatistic {
        public String name;
        public Date arrivalTime;
        public long waitingTime = 0;
        public Date startUploadTime;
        public long unloadTime = 0;
        public Cargo.CargoType type;

        public ShipStatistic() {}
        public ShipStatistic(Ship ship) {
            this.name = ship.getName_();
            this.arrivalTime = ship.getArriveDate();
            this.startUploadTime = ship.getStartUploading();
            this.waitingTime = TimeTable.getMinutes(startUploadTime) - TimeTable.getMinutes(arrivalTime);
            this.unloadTime = TimeTable.getMinutes(ship.getEndUploading())
              - TimeTable.getMinutes(ship.getStartUploading());
            this.type = ship.getCargo_().getType();
        }
        public ShipStatistic(ShipStatistic ss) {
            name = ss.name;
            arrivalTime = ss.arrivalTime;
            waitingTime = ss.waitingTime;
            startUploadTime = ss.startUploadTime;
            unloadTime = ss.unloadTime;
        }
    }
    public ArrayList<ShipStatistic> statLiquidList;
    public ArrayList<ShipStatistic> statLooseList;
    public ArrayList<ShipStatistic> statContainerList;
    {
        statLiquidList = new ArrayList<>();
        statLooseList = new ArrayList<>();
        statContainerList = new ArrayList<>();
    }

    public void addShipStatistic(ShipStatistic shipStatistic) {
        switch (shipStatistic.type) {
            case LOOSE -> statLooseList.add(shipStatistic);
            case LIQUID -> statLiquidList.add(shipStatistic);
            case CONTAINERS -> statContainerList.add(shipStatistic);
        }
    }
}
