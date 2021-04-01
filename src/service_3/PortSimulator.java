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
        private Date currentDate;


        public UnloadingTask(ArrayList<Ship> ships, Statistic statistic) {
            this.ships = ships;
            this.statistic = statistic;
        }

        @Override
        public void run() {
            try {
                int cranes = 1;
                StatisticStruct currentstatisticStruct = getStatisticStruct(cranes);
                StatisticStruct nextStatisticStruct = getStatisticStruct(++cranes);
                while (currentstatisticStruct.getFine() > nextStatisticStruct.getFine()) {
                    currentstatisticStruct = getStatisticStruct(cranes++);
                    nextStatisticStruct = getStatisticStruct(cranes);
                }
                statistic.addStatisticStruct(currentstatisticStruct);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        boolean isUnloaded(ArrayList<Ship> shipsCheck) {
            for (Ship ship: shipsCheck) {
                if (ship.getEndUploading() == null)
                {
                    return false;
                }
            }
            return true;
        }

        StatisticStruct getStatisticStruct(int cranes) throws CloneNotSupportedException {
            StatisticStruct statisticStruct = new StatisticStruct();
            statisticStruct.cranes = cranes;
            ArrayList<CraneThread> threads = new ArrayList<>();
            ArrayList<Ship> temp = new ArrayList<Ship>();
            for (Ship ship: ships) {
                temp.add((Ship) ship.clone());
            }
            for (int i = 0; i < cranes; i++) {
                threads.add(new CraneThread(temp, statisticStruct));
            }
            currentDate = (Date) startDate.clone();
            while (currentDate.before(endDate) || !isUnloaded(temp)) {
                for (int j = 0; j < cranes; j++) {
                    threads.get(j).setDate(currentDate);
                    threads.get(j).run();
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
            return statisticStruct;
        }
        }

        class CraneThread extends Thread {
            private ArrayList<Ship> ships;
            private Ship currentShip;
            private StatisticStruct statisticStruct;
            private boolean free;
            Date date;

            public CraneThread(ArrayList<Ship> ships, StatisticStruct statisticStruct) {
                this.ships = ships;
                this.statisticStruct = statisticStruct;
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
                        if ((ship.getNumberCranes() < 2) && (ship.getCargo_().getWeight() >0)
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
                            statisticStruct.addShipStatistic(currentShip);
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

    public void findBest() throws InterruptedException {
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].size(); j++) {
                ships[i].get(j).setDelay(1);
                ships[i].get(j).setUploadingDelay(2);
            }
        }
        for (int i =0; i < ships.length; i++) {
            ships[i].sort(new Ship.ShipComparator());
        }
        ArrayList<UnloadingTask> threads = new ArrayList<>();
        for (int i =0; i < ships.length; i++) {
            threads.add(new UnloadingTask(ships[i], statistic));
            threads.get(i).run();
        }
        for (int i =0; i < ships.length; i++) {
            threads.get(i).join();
        }
    }

    public Statistic getStatistic()
    {
        return statistic;
    }

    static final Random random = new Random();
}
