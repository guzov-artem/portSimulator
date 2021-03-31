package service_3;

import service_1.Ship;
import service_1.TimeTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class PortSimulator {
    private ArrayList<Ship>[] ships;
    private Statistic statistic;
    private Date startDate;
    private Date endDate;

    public PortSimulator() {

    }

    public class UnloadingTask extends Thread {
        private ArrayList<Ship> ships;
        private Statistic statistic;
        private int cranes;
        private Date currentDate;


        public UnloadingTask(ArrayList<Ship> ships, Statistic statistic, int cranes) {
            this.ships = ships;
            this.statistic = statistic;
            this.cranes = cranes;
        }

        @Override
        public void run() {
            getPenalty();
        }

        int getPenalty(){
            ArrayList<CraneThread> threads = new ArrayList<>();
            for (int i = 0; i < cranes; i++) {
                threads.add(new CraneThread(ships, statistic));
            }
            currentDate = (Date) startDate.clone();
            boolean isFree = true;
            while (currentDate.before(endDate) || !isFree) {
                isFree = threads.get(0).isFree();
                for (int j = 0; j < cranes; j++) {
                    threads.get(j).setDate(currentDate);
                    threads.get(j).run();
                    isFree = isFree && threads.get(j).isFree();
                }

                try {
                    for (int j = 0; j < cranes; j++) {
                        threads.get(j).join();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentDate.setMinutes(currentDate.getMinutes() + 1);
            }
            int waitingTime = 0;
            for (int i = 0; i < statistic.statLooseList.size(); i++) {
                waitingTime += statistic.statLooseList.get(i).waitingTime;
            }
            return (waitingTime / 60) * 100;
        }
        }

        class CraneThread extends Thread {
            private ArrayList<Ship> ships;
            private Ship currentShip;
            private Statistic statistic;
            private Statistic.ShipStatistic shipStatistic;
            private boolean free;
            Date date;

            public CraneThread(ArrayList<Ship> ships, Statistic statistic) {
                this.ships = ships;
                this.statistic = statistic;
                free = true;
            }

            public boolean isFree() {
                return free;
            }

            public void setDate(Date date) {
                this.date = date;
            }

            @Override
            public void run() {
                if (free) {
                    for (Ship ship : ships) {
                        if ((ship.getNumberCranes() < 2) && (ship.getCargo_().getWeight() >=0)
                          && (TimeTable.getMinutes(ship.getArriveDate()) + ship.getDelay())
                                <= TimeTable.getMinutes(date)) {
                            currentShip = ship;
                            currentShip.changeNumberCranes(1);
                            if (currentShip.getNumberCranes() == 1) {
                              currentShip.setStartUploading((Date) date.clone());
                            }
                            free = false;
                            break;
                        }
                    }
                }
                if (currentShip != null) {
                    currentShip.decreaseCargo(currentShip.getCargo_().getSpeed_());
                    if (currentShip.getCargo_().getWeight() <= 0) {
                        if (currentShip.getEndUploading() == null) {
                            Date temp = (Date) date.clone();
                            temp.setMinutes(temp.getMinutes() + 1);
                            currentShip.setEndUploading(temp);
                            shipStatistic = new Statistic.ShipStatistic(currentShip);
                            statistic.addShipStatistic(shipStatistic);
                        }
                        if (currentShip.getUploadingDelay() <= 0) {
                            free = true;
                        }
                        else {
                            currentShip.setUploadingDelay(currentShip.getUploadingDelay() - 1);
                        }
                    }
                }

            }
        }

    public PortSimulator(ArrayList<Ship>[] ships, Date startDate, Date endDate) {
        this.ships = ships;
        this.startDate = startDate;
        this.endDate = endDate;
        statistic = new Statistic();
    }

    public void findBest() {
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].size(); j++) {
                ships[i].get(j).setDelay(1);
                ships[i].get(j).setUploadingDelay(2);
            }
        }
        ships[0].sort(new Ship.ShipComparator());
        UnloadingTask task = new UnloadingTask(ships[0], statistic, 2);
        task.run();
    }

    static final Random random = new Random();
}
