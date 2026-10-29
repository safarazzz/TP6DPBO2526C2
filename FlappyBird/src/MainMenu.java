import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private Image backgroundImage;
    private Image birdImage;
    private JButton startButton;
    private JButton exitButton;

    public MainMenu(ActionListener onStart, ActionListener onExit) {
        setPreferredSize(new Dimension(360, 640));
        setLayout(new GridBagLayout());

        try {
            backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
            birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        } catch (Exception e) {
            
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Judul Game
        JLabel titleLabel = new JLabel("FLAPPY BIRD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 15, 20);
        add(titleLabel, gbc);

        // Ikon Burung
        if (birdImage != null) {
            JLabel birdLabel = new JLabel(new ImageIcon(birdImage.getScaledInstance(68, 48, Image.SCALE_SMOOTH)), SwingConstants.CENTER);
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 20, 35, 20);
            add(birdLabel, gbc);
        }

        // Tombol Mulai Permainan
        startButton = new JButton("Mulai Permainan");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setPreferredSize(new Dimension(220, 45));
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(onStart);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 20, 15, 20);
        add(startButton, gbc);

        // Tombol Keluar
        exitButton = new JButton("Keluar");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setPreferredSize(new Dimension(220, 45));
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(onExit);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 20, 10, 20);
        add(exitButton, gbc);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

