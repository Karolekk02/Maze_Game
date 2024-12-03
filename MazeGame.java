import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class MazeGame {
    private Timer ghostTimer;
    private MazeModel mazeModel = new MazeModel(10, 10);
    private MazePanel mazePanel = new MazePanel(mazeModel);
    private Color backgroundColor = Color.WHITE;  // Domyślny kolor tła
    private Color wallColor = Color.BLACK;        // Domyślny kolor ścian

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGame::new);
    }

    private void startGhost() {
        ghostTimer = new Timer(1000, e -> {
            mazeModel.blockRandomWall();
            mazePanel.repaint(); // Odśwież planszę po zmianie ścian
        });
        ghostTimer.start();
    }

    private void stopGhost() {
        if (ghostTimer != null) {
            ghostTimer.stop();
        }
    }

    public MazeGame() {
        JFrame frame = new JFrame("Labirynt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        startGhost();

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> dy = -1;
                    case KeyEvent.VK_DOWN -> dy = 1;
                    case KeyEvent.VK_LEFT -> dx = -1;
                    case KeyEvent.VK_RIGHT -> dx = 1;
                }

                if (mazeModel.moveTraveler(dx, dy)) {
                    mazePanel.repaint();
                    if (mazeModel.isAtExit()) {
                        stopGhost();
                        JOptionPane.showMessageDialog(frame, "Gratulacje! Dotarłeś do wyjścia!");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        });

        frame.add(mazePanel, BorderLayout.CENTER);
        frame.setJMenuBar(createMenuBar());
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu "Gra"
        JMenu gameMenu = new JMenu("Gra");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        JMenuItem startItem = new JMenuItem("Start");
        startItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        startItem.addActionListener(e -> startGame());
        gameMenu.add(startItem);

        JMenuItem resignItem = new JMenuItem("Rezygnacja");
        resignItem.setAccelerator(KeyStroke.getKeyStroke("control R"));
        resignItem.addActionListener(e -> resignGame());
        gameMenu.add(resignItem);

        JMenuItem exitItem = new JMenuItem("Koniec");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("control K"));
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        // Menu "Ustawienia"
        JMenu settingsMenu = new JMenu("Ustawienia");
        settingsMenu.setMnemonic(KeyEvent.VK_U);
        
        JMenu colorMenu = new JMenu("Kolory");
        JMenuItem backgroundColorItem = new JMenuItem("Tło");
        backgroundColorItem.addActionListener(e -> changeBackgroundColor());
        JMenuItem wallColorItem = new JMenuItem("Ściany");
        wallColorItem.addActionListener(e -> changeWallColor());
        colorMenu.add(backgroundColorItem);
        colorMenu.add(wallColorItem);
        settingsMenu.add(colorMenu);

        JMenu sizeMenu = new JMenu("Rozmiar");
        JMenuItem setWidthItem = new JMenuItem("Szerokość");
        setWidthItem.addActionListener(e -> setMazeWidth());
        JMenuItem setHeightItem = new JMenuItem("Wysokość");
        setHeightItem.addActionListener(e -> setMazeHeight());
        sizeMenu.add(setWidthItem);
        sizeMenu.add(setHeightItem);
        settingsMenu.add(sizeMenu);

        // Menu "Ruchy"
        JMenu movesMenu = new JMenu("Ruchy");
        JMenuItem upItem = new JMenuItem("W górę");
        upItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        upItem.addActionListener(e -> moveTraveler(0, -1));
        movesMenu.add(upItem);

        JMenuItem rightItem = new JMenuItem("W prawo");
        rightItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        rightItem.addActionListener(e -> moveTraveler(1, 0));
        movesMenu.add(rightItem);

        JMenuItem downItem = new JMenuItem("W dół");
        downItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        downItem.addActionListener(e -> moveTraveler(0, 1));
        movesMenu.add(downItem);

        JMenuItem leftItem = new JMenuItem("W lewo");
        leftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        leftItem.addActionListener(e -> moveTraveler(-1, 0));
        movesMenu.add(leftItem);

        // Menu "Pomoc"
        JMenu helpMenu = new JMenu("Pomoc");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        menuBar.add(gameMenu);
        menuBar.add(settingsMenu);
        menuBar.add(movesMenu);
        menuBar.add(Box.createHorizontalGlue()); // Przesunięcie Pomoc na prawo
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void startGame() {
        String widthInput = JOptionPane.showInputDialog("Podaj szerokość labiryntu:");
        String heightInput = JOptionPane.showInputDialog("Podaj wysokość labiryntu:");
        try {
            int width = Integer.parseInt(widthInput);
            int height = Integer.parseInt(heightInput);
            if (width > 0 && height > 0) {
                mazeModel = new MazeModel(width, height);
                mazePanel.updateMaze(mazeModel);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nieprawidłowy rozmiar!");
        }
    }

    private void resignGame() {
        stopGhost();
        JOptionPane.showMessageDialog(null, "Zrezygnowałeś z gry!");
    }

    private void changeBackgroundColor() {
        Color newColor = JColorChooser.showDialog(null, "Wybierz kolor tła", backgroundColor);
        if (newColor != null) {
            backgroundColor = newColor;
            mazePanel.repaint();
        }
    }

    private void changeWallColor() {
        Color newColor = JColorChooser.showDialog(null, "Wybierz kolor ścian", wallColor);
        if (newColor != null) {
            wallColor = newColor;
            mazePanel.repaint();
        }
    }

    private void setMazeWidth() {
        String input = JOptionPane.showInputDialog("Podaj szerokość labiryntu:");
        try {
            int width = Integer.parseInt(input);
            mazeModel.setWidth(width);
            mazePanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nieprawidłowa szerokość!");
        }
    }

    private void setMazeHeight() {
        String input = JOptionPane.showInputDialog("Podaj wysokość labiryntu:");
        try {
            int height = Integer.parseInt(input);
            mazeModel.setHeight(height);
            mazePanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nieprawidłowa wysokość!");
        }
    }

    private void moveTraveler(int dx, int dy) {
        if (mazeModel.moveTraveler(dx, dy)) {
            mazePanel.repaint();
            if (mazeModel.isAtExit()) {
                stopGhost();
                JOptionPane.showMessageDialog(null, "Dotarłeś do wyjścia!");
            }
        }
    }
}
