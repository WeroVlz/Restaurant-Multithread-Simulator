import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Waiter extends Thread {

    public WaiterStateSettings.State state;
    private boolean waiterBusy = false;
    private boolean takingFood = false;
    private boolean pickUpMoney = false;
    private int minTime = 1500;
    private int maxTime = 4000;
    private Image waiterImg;
    private Client assignedClient;
    Random random = new Random();

    public Waiter(int i) {
        super("Waiter " + i);
        this.state = WaiterStateSettings.State.WAITING;
        this.waiterImg = new ImageIcon("assets/mesero.png").getImage();
    }

    private void sleepThread(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public boolean getIsWaiterBusy() {
        return this.waiterBusy;
    }

    public boolean getIsTakingFood() {
        return this.takingFood;
    }

    public Client getAssignedClient() {
        return this.assignedClient;
    }

    public Image getWaiterImage() {
        return this.waiterImg;
    }

    public WaiterStateSettings.State getWaiterState() {
        return this.state;
    }

    public void takeFood(Client c) {
        this.waiterBusy = true;
        this.takingFood = true;
        this.state = WaiterStateSettings.State.TAKING_FOOD;
        this.assignedClient = c;
    }

    public void pickUpMoney() {
        this.state = WaiterStateSettings.State.PICKUP_MONEY;
    }

    public void goToWaitingState() {
        this.state = WaiterStateSettings.State.WAITING;
        this.assignedClient = null;
    }

    @Override
    public void run() {
        while (true) {
            while (takingFood) {
                int sleepTime = random.nextInt(maxTime - minTime) + minTime;
                sleepThread(sleepTime);
                int finishEatingProbability = random.nextInt(11 - 1) + 1;
                if (finishEatingProbability == 10) {
                    assignedClient.setFinishEating(true);
                    takingFood = false;
                    pickUpMoney = true;
                }
            }
            while (pickUpMoney) {
                this.state = WaiterStateSettings.State.PICKUP_MONEY;
                sleepThread(1500);
                pickUpMoney = false;
            }
            goToWaitingState();
            waiterBusy = false;
            sleepThread(50);
        }
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
