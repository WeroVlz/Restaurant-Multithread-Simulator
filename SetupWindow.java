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

    SetupWindow() {
        setName("Setup Window");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));
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
        panel.add(new JLabel(new ImageIcon("logo.jpeg")));
        panel.add(new JLabel("Ingenieria en Sistemas Graficos y Computacionales"));
        panel.add(new JLabel("Fundamentos de Programacion en Paralelo"));
        panel.add(new JLabel("Dr. Juan Carlos Lopez Pimentel"));
        panel.add(new JLabel("28 de noviembre del 2023"));
        panel.add(new JLabel("0217557 - Edgar Velazquez Mercado"));
        panel.add(new JLabel("0228930 - Juan Samuel Langarica Mejia"));

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
        Semaphore maxCapacityBuffer = new Semaphore(8);
        Semaphore tableBuffer = new Semaphore(4);
        Object cz1 = new Object();
        Object cz2 = new Object();

        for (int i = 0; i < threadNumber; i++) {
            clients.add(new Client(i + 1, maxCapacityBuffer, tableBuffer, cz1, cz2));
            Thread client = new Thread(clients.get(i));
            client.start();
        }
    }

    private void createWaiterThreads(int threadNumber) {
        // Semaphore semaphore = new Semaphore(4);
        Object object = new Object();

        for (int i = 0; i < threadNumber; i++) {
            waiters.add(new Waiter(i + 1, object));
            Thread waiter = new Thread(waiters.get(i));
            waiter.start();
        }
    }
}
