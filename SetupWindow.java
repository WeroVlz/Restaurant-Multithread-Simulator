import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SetupWindow extends JFrame {
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Waiter> waiters = new ArrayList<>();
    private int maxCapacityBufferSize = 15;
    private int tableBufferSize = 8;

    public SetupWindow() {
        super("Restaurant Multithread Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 250);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        JLabel clientLabel = new JLabel("Number of clients to create:");
        JTextField clientTField = new JTextField();
        JLabel waiterLabel = new JLabel("Number of waiters attending:");
        JTextField waiterTField = new JTextField();
        JButton executeButton = new JButton("Execute");

        panel.add(clientLabel);
        panel.add(clientTField);
        panel.add(waiterLabel);
        panel.add(waiterTField);
        panel.add(executeButton);

        executeButton.addActionListener((e) -> {
            int clientNumberThreads;
            int waiterNumberThreads;

            String clientNumberText = clientTField.getText();
            String waiterNumberText = waiterTField.getText();

            if (clientNumberText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid value.");
            } else {
                try {
                    clientNumberThreads = Integer.parseInt(clientNumberText);
                    waiterNumberThreads = Integer.parseInt(waiterNumberText);
                    createClientThreads(clientNumberThreads);
                    createWaiterThreads(waiterNumberThreads);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Please only enter numeric values");
                }
            }

            new GeneralMonitoringWindow("General Monitoring Window", clients, waiters);
            new ClientControlWindow("Client Control Window", clients);
            new WaiterControlWindow("Waiter control Window", waiters);
            new AgentCommunicationLink(clients, waiters).start();

        });

        this.add(panel);
        setVisible(true);
    }

    private void createClientThreads(int threadNumber) {
        Semaphore maxCapacityBuffer = new Semaphore(maxCapacityBufferSize);
        Semaphore tableBuffer = new Semaphore(tableBufferSize);
        Object cz1 = new Object();
        Object cz2 = new Object();

        for (int i = 0; i < threadNumber; i++) {
            clients.add(new Client(i + 1, maxCapacityBuffer, tableBuffer, cz1, cz2));
            Thread client = new Thread(clients.get(i));
            client.start();
        }
    }

    private void createWaiterThreads(int threadNumber) {

        for (int i = 0; i < threadNumber; i++) {
            waiters.add(new Waiter(i + 1));
            Thread waiter = new Thread(waiters.get(i));
            waiter.start();
        }
    }
}
