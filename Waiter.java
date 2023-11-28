public class Waiter extends Thread {

    public WaiterStateSettings.State state;
    private boolean hasLeft = false;
    private final Object object;

    public Waiter(int i, Object object) {
        super("Waiter " + i);
        this.state = WaiterStateSettings.State.BORN;
        this.object = object;
    }

    private void sleepThread(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public WaiterStateSettings.State getWaiterState() {
        return this.state;
    }

    @Override
    public void run() {
        sleepThread(1250);
        this.state = WaiterStateSettings.State.WAITING;
        do {
            synchronized (object) {
                try {
                    sleep(750);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

            }
        } while (!hasLeft);
    }

    public class WaiterStateSettings {
        public static enum State {
            BORN, WAITING, TAKING_FOOD, PICKUP_MONEY
        }

        public static String getWaiterState(State s) {
            String currentState = switch (s) {
                case BORN -> "Being born.";
                case WAITING -> "Waiting for client.";
                case TAKING_FOOD -> "Taking food";
                case PICKUP_MONEY -> "Picking up money.";
            };
            return currentState;
        }
    }
}
