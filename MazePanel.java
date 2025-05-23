import java.awt.*;
import javax.swing.*;

public class MazePanel extends JPanel {
    private MazeModel mazeModel;
    private final Image travelerImage;
    private final Image finishImage;
    Color wallColor;

    public MazePanel(MazeModel mazeModel, Color wallColor) {
        this.wallColor = wallColor;
        this.mazeModel = mazeModel; 

        ImageIcon traveler_icon = new ImageIcon("jp2.jpg");
        if (traveler_icon.getIconWidth() == -1) {
            System.out.println("Nie udalo sie zaladowac obrazu!");
        }
        this.travelerImage = traveler_icon.getImage();

        ImageIcon finish_icon = new ImageIcon("kielon.png");
        if (finish_icon.getIconWidth() == -1) {
            System.out.println("Nie udalo sie zaladowac obrazu!");
        }
        this.finishImage = finish_icon.getImage();
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        int cellSize = Math.min(getWidth() / mazeModel.getWidth(), getHeight() / mazeModel.getHeight());

        for (int y = 0; y <= mazeModel.getHeight(); y++) {
            for (int x = 0; x < mazeModel.getWidth(); x++) {
                if (mazeModel.getHorizontalWalls()[y][x] == 1) {
                    g2d.setColor(wallColor);
                    g2d.drawLine(
                        x * cellSize,
                        y * cellSize,
                        (x + 1) * cellSize,
                        y * cellSize
                    );
                }
                if (mazeModel.getHorizontalWalls()[y][x] == 2) {
                    g2d.setColor(Color.RED);
                    g2d.drawLine(
                        x * cellSize,
                        y * cellSize,
                        (x + 1) * cellSize,
                        y * cellSize
                    );
                }
            }
        }

        for (int y = 0; y < mazeModel.getHeight(); y++) {
            for (int x = 0; x <= mazeModel.getWidth(); x++) {
                if (mazeModel.getVerticalWalls()[y][x] == 1) {
                    g2d.setColor(wallColor);
                    g2d.drawLine(
                        x * cellSize,
                        y * cellSize,
                        x * cellSize,
                        (y + 1) * cellSize
                    );
                }
                if (mazeModel.getVerticalWalls()[y][x] == 2) {
                    g2d.setColor(Color.RED);
                    g2d.drawLine(
                        x * cellSize,
                        y * cellSize,
                        x * cellSize,
                        (y + 1) * cellSize
                    );
                }
            }
        }

        int travelerX = mazeModel.getTravelerX();
        int travelerY = mazeModel.getTravelerY();
        int circleX = travelerX * cellSize;
        int circleY = travelerY * cellSize;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(circleX, circleY, cellSize, cellSize);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(circleX, circleY, cellSize, cellSize);
        Shape originalClip = g2d.getClip();
        g2d.setClip(new java.awt.geom.Ellipse2D.Float(circleX, circleY, cellSize, cellSize));
        g2d.drawImage(travelerImage, circleX, circleY, cellSize, cellSize, this);
        g2d.setClip(originalClip);

        int finishX = (mazeModel.getWidth() - 1) * cellSize;
        int finishY = (mazeModel.getHeight() - 1) * cellSize;
        g2d.drawImage(finishImage, finishX, finishY, cellSize, cellSize, this);

    }

    public void updateMaze(MazeModel mazeModel) {
        this.mazeModel = mazeModel;
        repaint();
    }
}
