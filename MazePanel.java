import java.awt.*;
import javax.swing.*;

public class MazePanel extends JPanel {
    private MazeModel mazeModel;
    private final Image travelerImage;

    public MazePanel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;
        this.travelerImage = new ImageIcon("podroznik.jpg").getImage(); // Użyj obrazu podróżnika
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        int cellSize = Math.min(getWidth() / mazeModel.getWidth(), getHeight() / mazeModel.getHeight());

        // Rysowanie ścian poziomych
        for (int y = 0; y <= mazeModel.getHeight(); y++) {
            for (int x = 0; x < mazeModel.getWidth(); x++) {
                if (mazeModel.getHorizontalWalls()[y][x]) {
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(
                        x * cellSize,
                        y * cellSize,
                        (x + 1) * cellSize,
                        y * cellSize
                    );
                }
            }
        }

        // Rysowanie ścian pionowych
        for (int y = 0; y < mazeModel.getHeight(); y++) {
            for (int x = 0; x <= mazeModel.getWidth(); x++) {
                if (mazeModel.getVerticalWalls()[y][x]) {
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(
                        x * cellSize,
                        y * cellSize,
                        x * cellSize,
                        (y + 1) * cellSize
                    );
                }
            }
        }

        // Rysowanie podróżnika w kółku
        int travelerX = mazeModel.getTravelerX();
        int travelerY = mazeModel.getTravelerY();

        // Wyśrodkowanie kółka i obrazu
        int circleX = travelerX * cellSize;
        int circleY = travelerY * cellSize;

        // Rysowanie kółka
        g2d.setColor(Color.WHITE); // Kolor tła kółka
        g2d.fillOval(circleX, circleY, cellSize, cellSize);
        g2d.setColor(Color.BLACK); // Obramowanie kółka
        g2d.drawOval(circleX, circleY, cellSize, cellSize);

        // Przycięcie obrazu do kształtu kółka
        Shape originalClip = g2d.getClip();
        g2d.setClip(new java.awt.geom.Ellipse2D.Float(circleX, circleY, cellSize, cellSize));

        // Rysowanie obrazu wewnątrz kółka
        g2d.drawImage(travelerImage, circleX, circleY, cellSize, cellSize, this);

        // Przywrócenie pierwotnego klipu
        g2d.setClip(originalClip);
    }



    public void updateMaze(MazeModel mazeModel) {
        this.mazeModel = mazeModel;
        repaint();
    }
}
