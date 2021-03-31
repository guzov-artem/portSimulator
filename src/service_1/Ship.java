package service_1;

import service_3.Statistic;

import java.util.Comparator;
import java.util.Date;

public class Ship {
    public Ship(String name, Cargo cargo, Date date) {
        name_ = name;
        cargo_ = cargo;
        arriveDate = date;
    }

    public synchronized Cargo getCargo_() {
        return cargo_;
    }



    public String getName_() {
        return name_;
    }

    public synchronized void decreaseCargo(Double number) {
        cargo_.changeWeight(-number);
    }

    public synchronized int getNumberCranes() {
        return numberCranes;
    }

    public synchronized void changeNumberCranes(int number) {
        numberCranes +=number;
    }

//    public synchronized Statistic.ShipStatistic getShipStatistic() {
//        return shipStatistic;
//    }

    public synchronized void setDelay(int delay){
        this.delay = delay;
    }

    public synchronized int getUploadingDelay(){
        return uploadingDelay;
    }

    public synchronized void setUploadingDelay(int uploadingDelay){
        this.uploadingDelay = uploadingDelay;
    }

    public synchronized int getDelay(){
        return delay;
    }

    public synchronized void setArriveDate(Date arriveDate) {
        this.arriveDate = arriveDate;
    }

    public synchronized Date getArriveDate() {
        return arriveDate;
    }

    public synchronized void setStartUploading(Date startUploading) {
        this.startUploading = startUploading;
    }

    public synchronized Date getStartUploading() {
        return startUploading;
    }

    public synchronized void setEndUploading(Date endUploading) {
        this.endUploading = endUploading;
    }

    public synchronized Date getEndUploading() {
        return endUploading;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name_='" + name_ + '\'' +
                ", cargo_=" + cargo_ +
                ", data_=" + arriveDate +
                '}';
    }

    public static class ShipComparator implements Comparator<Ship> {
        public int compare(Ship first, Ship second) {
            return (int)( (TimeTable.getMinutes(first.getArriveDate()) + first.getDelay())
              - (TimeTable.getMinutes(second.getArriveDate()) + second.getDelay()));
        }
    }

    private String name_;
    private Cargo cargo_;//tons
    private Date arriveDate;
    private Date startUploading;
    private Date endUploading;
    private int delay = 0;
    private int uploadingDelay = 0;
    private int numberCranes = 0;
}
