import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class EntranceCanvas extends JFrame {
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Integer> clientImgPositionX = new ArrayList<>();
    Random random = new Random();

    public EntranceCanvas(ArrayList<Client> clients) {
        super("Restaurant Entrance");
        this.clients.addAll(clients);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new JLabel(new ImageIcon("assets/entrance.jpg")));
        pack();
        setVisible(true);

        for (int i = 0; i < clients.size(); i++) {
            int randomPositionX = random.nextInt(getWidth());
            clientImgPositionX.add(randomPositionX);
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(clients.size());

        executor.scheduleAtFixedRate(() -> {

            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                if (c.getClientState() == Client.ClientStateSettings.State.WALKING_ENTRANCE) {

                    ImagePainter imagePainter = new ImagePainter(clientImgPositionX.get(i), getHeight() - 200,
                            getGraphics(),
                            c.getClientImage());
                    imagePainter.start();
                }
            }
            repaint();
        }, 0, 250, TimeUnit.MILLISECONDS);
    }
}
