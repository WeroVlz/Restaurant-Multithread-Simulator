import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaiterControlWindow extends JFrame {
    private final String[] columnNames = { "Names", "Status", "Attending" };

    public WaiterControlWindow(String title, List<Waiter> waiters) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(columnNames, waiters.size());
        JTable table = new JTable(model);
        add(new JScrollPane(table));
        pack();

        setVisible(true);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(waiters.size());

        executor.scheduleAtFixedRate(() -> {
            for (int i = 0; i < waiters.size(); i++) {
                Waiter w = waiters.get(i);
                model.setValueAt(w.getName(), i, 0);
                model.setValueAt(w.getWaiterState(), i, 1);
                model.setValueAt(w.getWaiterState() == Waiter.WaiterStateSettings.State.TAKING_FOOD
                        || w.getWaiterState() == Waiter.WaiterStateSettings.State.PICKUP_MONEY
                                ? w.getAssignedClient().getName()
                                : "",
                        i, 2);
            }
        }, 0, 20, TimeUnit.MILLISECONDS);

    }
}
