import java.util.ArrayList;

public class AgentCommunicationLink extends Thread {
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Waiter> waiters = new ArrayList<>();

    public AgentCommunicationLink(ArrayList<Client> clients, ArrayList<Waiter> waiters) {
        this.clients.addAll(clients);
        this.waiters.addAll(waiters);
    }

    @Override
    public void run() {
        checkClientStatus();
    }

    public void checkClientStatus() {
        while (true) {
            for (int i = 0; i < clients.size(); i++) {
                System.out.println(clients.get(i).getClientState());
            }
        }
    }
}
