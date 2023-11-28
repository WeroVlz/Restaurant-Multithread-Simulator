import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GeneralMonitoringWindow extends JFrame {

    private final String[] columnNames = { "Type", "Name", "Current Agents" };
    private static int clientEntranceCount;
    private static int clientWaitingCount;
    private static int clientEatingCount;
    private static int clientPayingCount;
    private static int clientLeftCount;
    private static int waiterWaitingCount;
    private static int waiterFoodCount;
    private static int waiterMoneyCount;

    Object[][] data = {
            { "Buffer", "Max Capacity Buffer", 0 },
            { "Buffer", "Table Buffer", 0 },
            { "Buffer", "Order Buffer", 0 },
            { "Critical Section", "Table Asignation", 0 },
            { "Critical Section", "Payment", 0 },
            { "Agent", "Total Clients", 0 },
            { "State", "Clients at entrance", 0 },
            { "State", "Clients waiting table", 0 },
            { "State", "Clients eating", 0 },
            { "State", "Clients paying", 0 },
            { "State", "Clients that left", 0 },
            { "Agent", "Total Waiters", 0 },
            { "State", "Waiters waiting", 0 },
            { "State", "Waiters delivering food", 0 },
            { "State", "Waiters picking money", 0 }
    };

    public GeneralMonitoringWindow(String title, List<Client> clients, List<Waiter> waiters) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        add(new JScrollPane(table));

        pack();
        setVisible(true);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(13);

        executor.scheduleAtFixedRate(() -> {

            model.setValueAt(clientWaitingCount, 0, 2);
            model.setValueAt(clientEatingCount + clientPayingCount, 1, 2);
            model.setValueAt(Client.getIsSeatingTable() == true ? 1 : 0, 3, 2);
            model.setValueAt(clientPayingCount == 1 ? 1 : 0, 4, 2);
            clientEntranceCount = clientWaitingCount = clientEatingCount = clientPayingCount = clientLeftCount = 0;

            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                switch (c.state) {
                    case WALKING_ENTRANCE:
                        clientEntranceCount++;
                        break;
                    case WAITING_TABLE:
                        clientWaitingCount++;
                        break;
                    case AT_TABLE_EATING:
                        clientEatingCount++;
                        break;
                    case AT_TABLE_PAYING:
                        clientPayingCount++;
                        break;
                    case WALKING_OUT:
                        clientLeftCount++;
                        break;
                    default:
                        break;
                }
            }

            model.setValueAt(clients.size(), 5, 2);
            model.setValueAt(clientEntranceCount, 6, 2);
            model.setValueAt(clientWaitingCount, 7, 2);
            model.setValueAt(clientEatingCount, 8, 2);
            model.setValueAt(clientPayingCount, 9, 2);
            model.setValueAt(clientLeftCount, 10, 2);

            waiterWaitingCount = waiterFoodCount = waiterMoneyCount = 0;

            for (int i = 0; i < waiters.size(); i++) {
                Waiter w = waiters.get(i);
                switch (w.state) {
                    case WAITING:
                        waiterWaitingCount++;
                        break;
                    case TAKING_FOOD:
                        waiterFoodCount++;
                        break;
                    case PICKUP_MONEY:
                        waiterMoneyCount++;
                        break;
                    default:
                        break;
                }
            }

            model.setValueAt(waiters.size(), 11, 2);
            model.setValueAt(waiterWaitingCount, 12, 2);
            model.setValueAt(waiterFoodCount, 13, 2);
            model.setValueAt(waiterMoneyCount, 14, 2);

        }, 0, 20, TimeUnit.MILLISECONDS);
    }
}
