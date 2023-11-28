import java.util.ArrayList;
import java.util.Random;

public class AgentCommunicationLink extends Thread {
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Waiter> waiters = new ArrayList<>();
    private static boolean assignWaiter = false;
    Random random = new Random();

    public AgentCommunicationLink(ArrayList<Client> clients, ArrayList<Waiter> waiters) {
        this.clients.addAll(clients);
        this.waiters.addAll(waiters);
    }

    private void sleepThread(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            checkClientStatus();
        }

    }

    public void checkClientStatus() {

        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (c.getWantsFood()) {

                do {
                    int randomWaiter = random.nextInt(waiters.size() - 0);
                    if (!waiters.get(randomWaiter).getIsWaiterBusy()) {
                        waiters.get(randomWaiter).takeFood(c);
                        assignWaiter = true;
                    }
                    sleepThread(10);
                } while (!assignWaiter && c.getWantsFood());
                assignWaiter = false;
                c.setWantsFood(false);
            }

        }

    }
}
