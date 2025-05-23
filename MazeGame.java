import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class MazeGame {
    private Timer ghostTimer;
    private final MazeModel mazeModel;
    private final MazePanel mazePanel;
    private final JMenuBar menu_bar;

    private void startGhost() {
        ghostTimer = new Timer(2000, e -> {
            mazeModel.blockRandomWall();
            mazePanel.repaint();
        });
        ghostTimer.start();
    }

    private void stopGhost() {
        if (ghostTimer != null) {
            ghostTimer.stop();
        }
    }

    public MazeGame(JFrame frame, Color wallColor, Color backgroundColor, int width, int height, JMenuBar menu_bar) {
        this.mazeModel = new MazeModel(width, height);
    
        this.mazePanel = new MazePanel(mazeModel, wallColor);
        this.mazePanel.setBackground(backgroundColor);
    
        this.menu_bar = menu_bar;
        moves_menu_bar();
        startGhost();
    
        frame.setLayout(new BorderLayout());
        frame.add(mazePanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    

    private void move_traveler(int dx, int dy) {
        if (mazeModel.canMove(dx, dy)) {
            if (mazeModel.moveTraveler(dx, dy)) {
                mazePanel.repaint();
                if (mazeModel.isAtExit()) {
                    stopGhost();
                    JOptionPane.showMessageDialog(null, "Znalazles wyjscie!");
                }
            }
        }
    }

    private void moves_menu_bar() {
        JMenu movesMenu = new JMenu("Ruchy");
        JMenuItem upItem = new JMenuItem("W gore");
        upItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        upItem.addActionListener(e -> move_traveler(0, -1));
        movesMenu.add(upItem);

        JMenuItem rightItem = new JMenuItem("W prawo");
        rightItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        rightItem.addActionListener(e -> move_traveler(1, 0));
        movesMenu.add(rightItem);

        JMenuItem downItem = new JMenuItem("W dol");
        downItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        downItem.addActionListener(e -> move_traveler(0, 1));
        movesMenu.add(downItem);

        JMenuItem leftItem = new JMenuItem("W lewo");
        leftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        leftItem.addActionListener(e -> move_traveler(-1, 0));
        movesMenu.add(leftItem);

        JMenu helpMenu = new JMenu("Pomoc");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        menu_bar.add(movesMenu);
    }

    public void resetGame(JFrame frame) {
        stopGhost();
        frame.getContentPane().remove(mazePanel);
        frame.setJMenuBar(menu_bar);
        frame.revalidate();
        frame.repaint();
    }    
    
}
