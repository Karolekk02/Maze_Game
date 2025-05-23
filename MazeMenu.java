import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MazeMenu {
    JFrame frame;
    Color wallColor = Color.YELLOW;
    Color backgroundColor = Color.BLACK;
    int width = 10;
    int height = 10;
    JMenuBar menu_bar;
    BufferedImage bgImage;
    private MazeGame activeGame;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeMenu::new);
    }

    public MazeMenu() {

        try {
            bgImage = ImageIO.read(new File("bg.jpg"));
        } catch (IOException e) {
            System.err.println("Nie udalo sie zaladowac obrazu tla: " + e.getMessage());
        }

        frame = new JFrame("Maze Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        JLabel titleLabel = new JLabel("Maze Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 72));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setOpaque(false);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(titleLabel, BorderLayout.CENTER);


        menu_bar = createMenuBar();
        frame.setJMenuBar(menu_bar);
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Gra
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

        // Menu Ustawienia
        JMenu settingsMenu = new JMenu("Ustawienia");
        settingsMenu.setMnemonic(KeyEvent.VK_U);

        JMenu colorMenu = new JMenu("Kolory");
        JMenuItem backgroundColorItem = new JMenuItem("Tlo");
        backgroundColorItem.addActionListener(e -> changeBackgroundColor());
        JMenuItem wallColorItem = new JMenuItem("sciany");
        wallColorItem.addActionListener(e -> changeWallColor());
        colorMenu.add(backgroundColorItem);
        colorMenu.add(wallColorItem);
        settingsMenu.add(colorMenu);

        JMenu sizeMenu = new JMenu("Rozmiar");
        JMenuItem setWidthItem = new JMenuItem("Szerokosc");
        setWidthItem.addActionListener(e -> setMazeWidth());
        JMenuItem setHeightItem = new JMenuItem("Wysokosc");
        setHeightItem.addActionListener(e -> setMazeHeight());
        sizeMenu.add(setWidthItem);
        sizeMenu.add(setHeightItem);
        settingsMenu.add(sizeMenu);

        // Menu Pomoc
        JMenu helpMenu = new JMenu("Pomoc");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        JMenuItem aboutAppItem = new JMenuItem("O aplikacji");
        aboutAppItem.addActionListener(e -> showAboutAppDialog());
        helpMenu.add(aboutAppItem);

        JMenuItem aboutAuthorItem = new JMenuItem("O autorze");
        aboutAuthorItem.addActionListener(e -> showAboutAuthorDialog());
        helpMenu.add(aboutAuthorItem);

        menuBar.add(gameMenu);
        menuBar.add(settingsMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void changeBackgroundColor() {
        Color newColor = JColorChooser.showDialog(null, "Wybierz kolor tla", backgroundColor);
        if (newColor != null) {
            backgroundColor = newColor;
        }
    }

    private void changeWallColor() {
        Color newColor = JColorChooser.showDialog(null, "Wybierz kolor scian", wallColor);
        if (newColor != null) {
            wallColor = newColor;
        }
    }

    private void setMazeWidth() {
        String input = JOptionPane.showInputDialog("Podaj szerokosc labiryntu:");
        try {
            this.width = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nieprawidlowa szerokosc!");
        }
    }

    private void setMazeHeight() {
        String input = JOptionPane.showInputDialog("Podaj wysokosc labiryntu:");
        try {
            this.height = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nieprawidlowa wysokosc!");
        }
    }

    private void startGame() {
        activeGame = new MazeGame(frame, wallColor, backgroundColor, width, height, menu_bar);
    }
    

    private void resignGame() {
        resetGameAndReturnToMenu();
    }    
    
    private void resetGameAndReturnToMenu() {
        if (activeGame != null) {
            activeGame.resetGame(frame);
            activeGame = null;
        }
        recreateMainMenu();
        JOptionPane.showMessageDialog(frame, "Zrezygnowales z gry!");
    }
    
    private void recreateMainMenu() {
        frame.getContentPane().removeAll();
        menu_bar = createMenuBar();
        frame.setJMenuBar(menu_bar);
        frame.getContentPane().repaint();
        frame.getContentPane().revalidate();
        
        JLabel titleLabel = new JLabel("Maze Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 72));
        titleLabel.setForeground(Color.YELLOW);
        frame.add(titleLabel, BorderLayout.CENTER);
    }
    
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void showAboutAppDialog() {
        JOptionPane.showMessageDialog(frame, """
                                             Labirynt to gra, w ktorej celem jest dotarcie do wyjscia.
                                             Poruszaj sie za pomoca strzalek na klawiaturze.
                                             Ustawienia pozwalaja zmieniac kolory i rozmiar labiryntu.""",
                "O aplikacji",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showAboutAuthorDialog() {
        JOptionPane.showMessageDialog(frame, """
                                             Gra stworzona przez Karol Burczyk.
                                             Kontakt: 340614@uwr.edu.pl""",
                "O autorze",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
