package service_3;

import service_1.Ship;
import service_1.TimeTable;

import java.util.*;
import java.util.concurrent.Semaphore;

public class PortSimulator {
    private ArrayList<Ship>[] ships;
    private Statistic statistic;
    private Date startDate;
    private Date endDate;

    public class UnloadingTask extends Thread {
        private ArrayList<Ship> ships;
        private Date currentDate;
        private ThreadStatistic threadStatistic;


        public UnloadingTask(ArrayList<Ship> ships) {
            this.ships = ships;
            this.threadStatistic = new ThreadStatistic(ships.get(0).getCargo().getType());
            this.currentDate = new Date();
        }

        @Override
        public void run() {
            try {
                int cranes = 1;
                ThreadStatistic currentstatisticThread = simulate(cranes);
                ThreadStatistic nextThreadStatistic = simulate(++cranes);
                while (currentstatisticThread.getFine() > nextThreadStatistic.getFine()) {
                    currentstatisticThread = simulate(cranes++);
                    nextThreadStatistic = simulate(cranes);
                }
                threadStatistic = currentstatisticThread;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        private boolean isUnloaded(ArrayList<Ship> shipsCheck) {
            for (Ship ship : shipsCheck) {
                if (!ship.isUnloaded()) {
                    return false;
                }
            }
            return shipsCheck.get(shipsCheck.size() - 1).getUploadingDelay() <= 0;
        }

        ThreadStatistic simulate(int cranes) throws CloneNotSupportedException {
            ThreadStatistic threadStatistic = new ThreadStatistic(this.threadStatistic.getType());
            threadStatistic.setCranes(cranes);
            ArrayList<CraneThread> threads = new ArrayList<>();
            ArrayList<Ship> temp = new ArrayList<Ship>();
            for (Ship ship : ships) {
                temp.add((Ship) ship.clone());
            }
            currentDate = (Date) startDate.clone();
            Object mutexEndUnloading = new Object();
            for (int i = 0; i < cranes; i++) {
                threads.add(new CraneThread(temp, threadStatistic, currentDate,
                    new Semaphore(0), new Semaphore(1), mutexEndUnloading));
                threads.get(i).start();
            }

            while(!isUnloaded(temp)) {
                for (CraneThread task : threads) {
                    try {
                        task.getSemaphore1().acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                currentDate.setTime(currentDate.getTime() + 60*1000);
                for (CraneThread task : threads) {
                    task.getSemaphore2().release();
                }
            }
            for (CraneThread task : threads) {
                task.stopWorking();
                task.getSemaphore2().release();
            }
            return threadStatistic;
        }

        public ThreadStatistic getStatisticStruct() {
            return threadStatistic;
        }
    }

        class CraneThread extends Thread {
            private List<Ship> ships;
            private Ship currentShip;
            private ThreadStatistic threadStatistic;
            private boolean free;
            private boolean isWorking;
            private Semaphore semaphore1;
            private Semaphore semaphore2;
            private Object endUnloadingMutex;
            Date date;

            public CraneThread(ArrayList<Ship> ships, ThreadStatistic threadStatistic, Date currentDate,
                Semaphore semaphore1, Semaphore semaphore2, Object endUnloadingMutex) {
                this.ships = Collections.synchronizedList(ships);
                this.threadStatistic = threadStatistic;
                this.date = currentDate;
                this.semaphore1 = semaphore1;
                this.semaphore2 = semaphore2;
                isWorking = true;
                free = true;
                this.endUnloadingMutex = endUnloadingMutex;
            }

            public void stopWorking() {
                isWorking = false;
            }

            public Semaphore getSemaphore1() {
                return semaphore1;
            }

            public Semaphore getSemaphore2() {
                return semaphore2;
            }

            @Override
            public void run() {
                while (isWorking) {
                    try {
                        semaphore2.acquire();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (free) {

                        for (Ship ship : ships) {
                            if ((ship.getNumberCranes() < 2) && (ship.getCargo().getWeight() > 0)
                                    && (TimeTable.getMinutes(ship.getArriveDate()) + ship.getDelay())
                                    <= TimeTable.getMinutes(date)) {
                                currentShip = ship;
                                currentShip.changeNumberCranes(1);
                                if (!currentShip.isStartedUnloading()) {
                                    currentShip.setStartUploading((Date) date.clone());
                                }
                                free = false;
                                break;
                            }
                        }
                    }
                    if (currentShip != null) {
                        currentShip.decreaseCargo(currentShip.getCargo().getSpeed_());
                        if (currentShip.getCargo().getWeight() <= 0) {
                            synchronized (endUnloadingMutex) {
                                if (!currentShip.isUnloaded()) {
                                    Date temp = (Date) date.clone();
                                    temp.setMinutes(temp.getMinutes() + currentShip.getUploadingDelay());
                                    currentShip.setEndUnloading(temp);
                                    threadStatistic.addShipStatistic(currentShip);
                                }
                            }
                            if (currentShip.getUploadingDelay() <= 0) {
                                free = true;
                            } else {
                                currentShip.setUploadingDelay(currentShip.getUploadingDelay() - 1);
                            }
                        }
                    }
                    semaphore1.release();
                }
            }
        }

    public PortSimulator(ArrayList<Ship>[] ships, Date startDate, Date endDate) {
        this.ships = ships;
        this.startDate = startDate;
        this.endDate = endDate;
        this.statistic = new Statistic();
    }
    private void generateDelays() {
        Random random = new Random();
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[i].size(); j++) {
                ships[i].get(j).setDelay(random.nextInt(60*24*7) - 2*60*24*7);
                ships[i].get(j).setUploadingDelay(random.nextInt(1400));
            }
        }
    }
    private void sortShips() {

        for (int i =0; i < ships.length; i++) {
            ships[i].sort(new Ship.ShipComparator());
        }
    }

    public void findBestStatistic() throws InterruptedException {
        generateDelays();
        sortShips();
        List<UnloadingTask> threads = new ArrayList<>();
        for (int i =0; i < ships.length; i++) {
            threads.add(new UnloadingTask(ships[i]));
            threads.get(i).start();
        }
        for (int i =0; i < ships.length; i++) {
            threads.get(i).join();
            statistic.addStatisticStruct(threads.get(i).getStatisticStruct());
        }
    }

    public Statistic getStatistic()
    {
        return statistic;
    }

}
