import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RestaurantCanvas extends JFrame {
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Waiter> waiters = new ArrayList<>();
    private final ArrayList<Integer> clientImgPositionX = new ArrayList<>();
    private final ArrayList<Integer> clientImgPositionY = new ArrayList<>();
    private final ArrayList<Integer> waiterImgPositionY = new ArrayList<>();
    Random random = new Random();

    public RestaurantCanvas(ArrayList<Client> clients, ArrayList<Waiter> waiters) {
        super("Restaurant");
        this.clients.addAll(clients);
        this.waiters.addAll(waiters);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new JLabel(new ImageIcon("assets/tables.jpg")));
        pack();
        setVisible(true);

        for (int i = 0; i < clients.size(); i++) {
            int randomPositionX = random.nextInt(getWidth() - ((getWidth() / 2) - 100)) + ((getWidth() / 2) - 100);
            int randomPositionY = random.nextInt(getHeight() - 100);
            clientImgPositionX.add(randomPositionX);
            clientImgPositionY.add(randomPositionY);
        }

        for (int index = 0; index < waiters.size(); index++) {
            int randomPositionY = random.nextInt((getHeight() - 100) - 100) + 100;
            waiterImgPositionY.add(randomPositionY);
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(clients.size());

        executor.scheduleAtFixedRate(() -> {

            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                if (c.getClientState() == Client.ClientStateSettings.State.AT_TABLE_EATING
                        || c.getClientState() == Client.ClientStateSettings.State.AT_TABLE_PAYING) {

                    ImagePainter imagePainter = new ImagePainter(clientImgPositionX.get(i), clientImgPositionY.get(i),
                            getGraphics(),
                            c.getClientImage());
                    imagePainter.start();
                }
            }
            for (int i = 0; i < waiters.size(); i++) {
                Waiter w = waiters.get(i);
                if (w.getWaiterState() == Waiter.WaiterStateSettings.State.WAITING) {
                    ImagePainter imgPainter = new ImagePainter(170, waiterImgPositionY.get(i), getGraphics(),
                            w.getWaiterImage());
                    imgPainter.start();
                } else if (w.getWaiterState() == Waiter.WaiterStateSettings.State.TAKING_FOOD
                        || w.getWaiterState() == Waiter.WaiterStateSettings.State.PICKUP_MONEY) {
                    ImagePainter imgPainter = new ImagePainter(470, waiterImgPositionY.get(i), getGraphics(),
                            w.getWaiterImage());
                    imgPainter.start();
                }
            }
            repaint();
        }, 0, 250, TimeUnit.MILLISECONDS);
    }
}
