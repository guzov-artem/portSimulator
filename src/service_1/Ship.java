package service_1;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

public class Ship implements Cloneable{
    public Ship(String name, Cargo cargo, Date date) {
        this.name = name;
        this.cargo = cargo;
        this.arriveDate = date;
        this.startUploading = Optional.empty();
        this.endUploading = Optional.empty();
        this.uploadingDelay = Optional.empty();
        this.numberCranes = 0;
        this.delay = 0;
    }
    private Ship() {}

    public Object clone() throws CloneNotSupportedException {
        Ship temp = new Ship();
        temp.name = this.name;
        temp.cargo = (Cargo) this.cargo.clone();
        temp.arriveDate = (Date) this.arriveDate.clone();
        temp.numberCranes = this.numberCranes;
        temp.delay = this.delay;
        temp.uploadingDelay = this.uploadingDelay;
        if (startUploading.isPresent()) {
            temp.startUploading = Optional.of((Date) this.startUploading.get().clone());
        }
        else {
            temp.startUploading = Optional.empty();
        }
        if (endUploading.isPresent()) {
            temp.endUploading = Optional.of((Date) this.endUploading.get().clone());
        }
        else {
            temp.endUploading = Optional.empty();
        }
        return temp;
    }

    public synchronized Cargo getCargo() {
        return cargo;
    }

    public synchronized boolean isStartedUnloading() {
        return startUploading.isPresent();
    }

    public String getName() {
        return name;
    }

    public synchronized void decreaseCargo(Double number) {
        cargo.changeWeight(-number);
    }

    public synchronized int getNumberCranes() {
        return numberCranes;
    }

    public synchronized void changeNumberCranes(int number) {
        this.numberCranes += number;
    }

    public synchronized void setDelay(int delay){
        this.delay = delay;
    }

    public synchronized int getUploadingDelay(){
        return uploadingDelay.orElseThrow();
    }

    public synchronized void setUploadingDelay(int uploadingDelay){
        this.uploadingDelay = Optional.of(uploadingDelay);
    }

    public synchronized int getDelay(){
        return delay;
    }

    public synchronized Date getArriveDate() {
        return arriveDate;
    }

    public synchronized void setStartUploading(Date startUploading) {
        this.startUploading = Optional.ofNullable(startUploading);
    }

    public synchronized Date getStartUploading() {
        return startUploading.orElseThrow();
    }

    public synchronized void setEndUnloading(Date endUploading) {
        this.endUploading = Optional.ofNullable(endUploading);
    }

    public synchronized Date getEndUnloading() {
        return endUploading.orElseThrow();
    }

    public synchronized boolean isUnloaded() {
        return endUploading.isPresent() ;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name_='" + name + '\'' +
                ", cargo_=" + cargo +
                ", data_=" + arriveDate +
                '}';
    }

    public static class ShipComparator implements Comparator<Ship> {
        public int compare(Ship first, Ship second) {
            return (int)( (TimeTable.getMinutes(first.getArriveDate()) + first.getDelay())
              - (TimeTable.getMinutes(second.getArriveDate()) + second.getDelay()));
        }
    }

    private String name;
    private Cargo cargo;//tons
    private Date arriveDate;
    private Integer numberCranes = 0;
    private Integer delay = 0;
    private Optional<Date> startUploading;
    private Optional<Date> endUploading;
    private Optional<Integer> uploadingDelay;

}
