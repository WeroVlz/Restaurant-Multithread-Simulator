import java.util.Random;
import java.util.concurrent.Semaphore;

public class Client extends Thread {

    public ClientStateSettings.State state;
    Semaphore maxCapacityBuffer;
    Semaphore tableBuffer;
    private static boolean isSeatingTable = false;
    private boolean finishEating = false;
    private boolean wantsFood = false;
    private boolean wantsToPay = false;
    private final Object criticalZone1;
    private final Object criticalZone2;
    Random random = new Random();

    Client(int i, Semaphore maxCapacityBuffer, Semaphore tableBuffer, Object cz1, Object cz2) {
        super("Client " + i);
        this.criticalZone1 = cz1;
        this.criticalZone2 = cz2;
        this.state = ClientStateSettings.State.BORN;
        this.maxCapacityBuffer = maxCapacityBuffer;
        this.tableBuffer = tableBuffer;
    }

    public static boolean getIsSeatingTable() {
        return isSeatingTable;
    }

    public boolean getWantsFood() {
        return this.wantsFood;
    }

    public void setWantsFood(boolean value) {
        this.wantsFood = value;
    }

    public boolean getWantsToPay() {
        return this.wantsToPay;
    }

    public void setWantsToPay(boolean value) {
        this.wantsToPay = value;
    }

    public boolean getFinishEating() {
        return this.finishEating;
    }

    public void setFinishEating(boolean value) {
        this.finishEating = value;
    }

    private void sleepThread(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private void acquirePermit(Semaphore s) {
        try {
            s.acquire();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    private void releasePermit(Semaphore s) {
        s.release();
    }

    public ClientStateSettings.State getClientState() {
        return state;
    }

    @Override
    public void run() {

        sleepThread(1250);
        this.state = ClientStateSettings.State.WALKING_ENTRANCE;
        sleepThread(1000);
        acquirePermit(maxCapacityBuffer);
        this.state = ClientStateSettings.State.WAITING_TABLE;
        sleepThread(1000);
        synchronized (criticalZone1) {

            acquirePermit(tableBuffer);
            releasePermit(maxCapacityBuffer);
            isSeatingTable = true;
            this.state = ClientStateSettings.State.AT_TABLE_EATING;
            sleepThread(750);
        }
        isSeatingTable = false;
        wantsFood = true;
        do {
            sleepThread(50);

        } while (!finishEating);

        synchronized (criticalZone2) {
            wantsToPay = true;
            this.state = ClientStateSettings.State.AT_TABLE_PAYING;
            sleepThread(1500);
        }

        this.state = ClientStateSettings.State.WALKING_OUT;
        releasePermit(tableBuffer);
        sleepThread(50);
    }

    public static class ClientStateSettings {
        public static enum State {
            BORN, WALKING_ENTRANCE, WAITING_TABLE, AT_TABLE_EATING, AT_TABLE_PAYING, WALKING_OUT
        }

        public static String getClientState(State s) {
            String currentState = switch (s) {
                case BORN -> "Being born";
                case WALKING_ENTRANCE -> "Walking toward entrance";
                case WAITING_TABLE -> "Waiting for a table";
                case AT_TABLE_EATING -> "EATING";
                case AT_TABLE_PAYING -> "PAYING";
                case WALKING_OUT -> "Walking toward exit";
            };
            return currentState;
        }
    }
}
