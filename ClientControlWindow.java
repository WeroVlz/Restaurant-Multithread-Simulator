import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ClientControlWindow extends JFrame {

    private final String[] columnNames = { "Name", "Born", "At Entrance", "Waiting for table", "Eating", "Paying",
            "Exiting" };

    public ClientControlWindow(String title, List<Client> clients) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        DefaultTableModel model = new DefaultTableModel(columnNames, clients.size());
        JTable table = new JTable(model);
        add(new JScrollPane(table));
        pack();

        setVisible(true);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(clients.size());

        executor.scheduleAtFixedRate(() -> {
            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                model.setValueAt(c.getName(), i, 0);
                model.setValueAt(c.getClientState() == Client.ClientStateSettings.State.BORN ? "X" : "", i, 1);
                model.setValueAt(c.getClientState() == Client.ClientStateSettings.State.WALKING_ENTRANCE ? "X" : "", i,
                        2);
                model.setValueAt(c.getClientState() == Client.ClientStateSettings.State.WAITING_TABLE ? "X" : "", i, 3);
                model.setValueAt(c.getClientState() == Client.ClientStateSettings.State.AT_TABLE_EATING ? "X" : "", i,
                        4);
                model.setValueAt(c.getClientState() == Client.ClientStateSettings.State.AT_TABLE_PAYING ? "X" : "", i,
                        5);
                model.setValueAt(c.getClientState() == Client.ClientStateSettings.State.WALKING_OUT ? "X" : "", i,
                        6);
            }
        }, 0, 20, TimeUnit.MILLISECONDS);
    }
}
