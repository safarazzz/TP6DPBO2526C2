import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360;
    int height = 640;

    private Logic logic;
    private JLabel scoreLabel;
    private Image backgroundImage;

    public View(Logic logic) {
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);
        setLayout(new BorderLayout());

        try {
            backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        } catch (Exception e) {
            
        }

        // JLabel untuk menampilkan skor
        scoreLabel = new JLabel("Skor: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFocusable(false);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(scoreLabel, BorderLayout.NORTH);

        // Pengaturan focus untuk menerima input keyboard
        setFocusable(true);
        addKeyListener(logic);

        // Memastikan panel mendapatkan fokus saat diklik
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    public void updateScore(int score) {
        scoreLabel.setText("Skor: " + score);
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Gambar background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, null);
        }

        // Gambar player dengan rotasi sesuai arah lompat/jatuh
        Player player = logic.getPlayer();
        if (player != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            // Haluskan tepi gambar saat dirotasi
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int centerX = player.getPosX() + player.getWidth() / 2;
            int centerY = player.getPosY() + player.getHeight() / 2;

            g2d.rotate(Math.toRadians(player.getAngle()), centerX, centerY);
            g2d.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);
            g2d.dispose();
        }

        // Gambar pipa
        ArrayList<Pipe> pipes = logic.getPipes();
        if (pipes != null) {
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }

        // Tampilan Game Over
        if (logic.isGameOver()) {
            // Panel overlay gelap
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, height / 2 - 65, width, 130);

            // Teks "GAME OVER"
            g.setColor(new Color(255, 60, 60));
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm1 = g.getFontMetrics();
            String overStr = "GAME OVER";
            int x1 = (width - fm1.stringWidth(overStr)) / 2;
            g.drawString(overStr, x1, height / 2 - 15);

            // Teks "Tekan 'R' untuk Restart"
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm2 = g.getFontMetrics();
            String restartStr = "Tekan 'R' untuk Restart";
            int x2 = (width - fm2.stringWidth(restartStr)) / 2;
            g.drawString(restartStr, x2, height / 2 + 25);
        }
    }
}
