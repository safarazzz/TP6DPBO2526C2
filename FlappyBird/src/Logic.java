import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Logic implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    // pipa
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -2;

    View view;
    Image birdImage;
    Player player;

    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    private int score = 0;
    private boolean gameOver = false;
    private boolean gameStarted = false;

    private Sound sound;

    public Logic() {
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                placePipes();
            }
        });

        gameLoop = new Timer(1000 / 60, this);

        // Inisialisasi suara dan mulai putar theme song saat app dijalankan
        sound = new Sound();
        sound.playThemeSong();
    }

    public void setView(View view) {
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void startGame() {
        player.setPosX(playerStartPosX);
        player.setPosY(playerStartPosY);
        player.setVelocityY(0);
        player.setAngle(0);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = true;

        if (view != null) {
            view.updateScore(score);
            view.repaint();
        }

        pipesCooldown.restart();
        gameLoop.restart();
    }

    public void restartGame() {
        startGame();
        // Mulai lagi theme song ketika user menekan 'R'
        if (sound != null) {
            sound.playThemeSong();
        }
    }

    public void placePipes() {
        int randomPosY = (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;
        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpace + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public boolean collision(Player a, Pipe b) {
        return a.getPosX() < b.getPosX() + b.getWidth() &&
               a.getPosX() + a.getWidth() > b.getPosX() &&
               a.getPosY() < b.getPosY() + b.getHeight() &&
               a.getPosY() + a.getHeight() > b.getPosY();
    }

    public void move() {
        if (gameOver) {
            return;
        }

        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        if (player.getVelocityY() < 0) {
            player.setAngle(-25);
        } else if (player.getVelocityY() > 0) {
            player.setAngle(Math.min(70, player.getVelocityY() * 5));
        } else {
            player.setAngle(0);
        }

        if (player.getPosY() + player.getHeight() >= frameHeight) {
            player.setPosY(frameHeight - player.getHeight());
            gameOver = true;
        }

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            if (!pipe.isPassed() && player.getPosX() > pipe.getPosX() + pipe.getWidth()) {
                pipe.setPassed(true);
                // Tambah skor +1 untuk setiap pasang pipa (dihitung sekali via upperPipe)
                if (pipe.getImage() == upperPipeImage) {
                    score++;
                    if (view != null) {
                        view.updateScore(score);
                    }

                    if (sound != null) {
                        if (score % 5 == 0 && score > 0) {
                            sound.playEvery5();
                        } else {
                            sound.playScore();
                        }
                    }
                }
            }

            if (collision(player, pipe)) {
                gameOver = true;
            }
        }

        if (gameOver) {
            pipesCooldown.stop();
            gameLoop.stop();
            if (sound != null) {
                sound.stopThemeSong();
                sound.playFail();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        if (view != null) {
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver && gameStarted) {
                player.setVelocityY(-10);
                player.setAngle(-25); // Miring ke atas seketika saat jump
                // Sound effect untuk setiap jump pada flapping_wings
                if (sound != null) {
                    sound.playFlap();
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            // Setelah Game Over, fitur restart permainan melalui penekanan tombol "R"
            if (gameOver) {
                restartGame();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
