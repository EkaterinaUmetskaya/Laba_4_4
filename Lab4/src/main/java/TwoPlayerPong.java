import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class TwoPlayerPong extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    private static final int PADDLE_WIDTH = 12;
    private static final int PADDLE_HEIGHT = 90;
    private static final int BALL_SIZE = 16;
    private static final int PADDLE_SPEED = 6;

    private int leftPaddleY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int rightPaddleY = HEIGHT / 2 - PADDLE_HEIGHT / 2;

    private double ballX = WIDTH / 2.0 - BALL_SIZE / 2.0;
    private double ballY = HEIGHT / 2.0 - BALL_SIZE / 2.0;
    private double ballVX = 4.0;
    private double ballVY = 3.0;

    private int leftScore = 0;
    private int rightScore = 0;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Timer timer;

    public TwoPlayerPong() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(25, 30, 38));
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    private void resetBall(int direction) {
        ballX = WIDTH / 2.0 - BALL_SIZE / 2.0;
        ballY = HEIGHT / 2.0 - BALL_SIZE / 2.0;
        ballVX = 4.0 * direction;
        ballVY = (Math.random() > 0.5 ? 1 : -1) * (2.0 + Math.random() * 2.5);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Центральная пунктирная линия
        g2d.setColor(new Color(90, 100, 115));
        for (int y = 10; y < HEIGHT; y += 26) {
            g2d.fillRect(WIDTH / 2 - 2, y, 4, 14);
        }

        // Ракетки игроков
        g2d.setColor(new Color(64, 156, 255));
        g2d.fillRoundRect(25, leftPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT, 6, 6);

        g2d.setColor(new Color(255, 99, 71));
        g2d.fillRoundRect(WIDTH - 25 - PADDLE_WIDTH, rightPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT, 6, 6);

        // Мяч
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int) Math.round(ballX), (int) Math.round(ballY), BALL_SIZE, BALL_SIZE);

        // Счёт
        g2d.setFont(new Font("SansSerif", Font.BOLD, 36));
        g2d.drawString(String.valueOf(leftScore), WIDTH / 2 - 70, 50);
        g2d.drawString(String.valueOf(rightScore), WIDTH / 2 + 48, 50);

        // Подсказка управления
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.setColor(new Color(170, 180, 195));
        g2d.drawString("Игрок 1: W / S   |   Игрок 2: ↑ / ↓   |   R: сброс счёта", 20, HEIGHT - 15);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Движение ракеток по зажатым клавишам
        if (pressedKeys.contains(KeyEvent.VK_W) && leftPaddleY > 0) {
            leftPaddleY -= PADDLE_SPEED;
        }
        if (pressedKeys.contains(KeyEvent.VK_S) && leftPaddleY < HEIGHT - PADDLE_HEIGHT) {
            leftPaddleY += PADDLE_SPEED;
        }
        if (pressedKeys.contains(KeyEvent.VK_UP) && rightPaddleY > 0) {
            rightPaddleY -= PADDLE_SPEED;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN) && rightPaddleY < HEIGHT - PADDLE_HEIGHT) {
            rightPaddleY += PADDLE_SPEED;
        }

        // Движение мяча
        ballX += ballVX;
        ballY += ballVY;

        // Отскок от верхнего и нижнего краёв
        if (ballY <= 0) {
            ballY = 0;
            ballVY = -ballVY;
        } else if (ballY + BALL_SIZE >= HEIGHT) {
            ballY = HEIGHT - BALL_SIZE;
            ballVY = -ballVY;
        }

        Rectangle ballRect = new Rectangle((int) ballX, (int) ballY, BALL_SIZE, BALL_SIZE);
        Rectangle leftPaddleRect = new Rectangle(25, leftPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        Rectangle rightPaddleRect = new Rectangle(WIDTH - 25 - PADDLE_WIDTH, rightPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Столкновение с левой ракеткой
        if (ballVX < 0 && ballRect.intersects(leftPaddleRect)) {
            ballX = 25 + PADDLE_WIDTH;
            ballVX = Math.abs(ballVX) * 1.04;
            double hitOffset = ((ballY + BALL_SIZE / 2.0) - (leftPaddleY + PADDLE_HEIGHT / 2.0)) / (PADDLE_HEIGHT / 2.0);
            ballVY = hitOffset * 5.0;
        }

        // Столкновение с правой ракеткой
        if (ballVX > 0 && ballRect.intersects(rightPaddleRect)) {
            ballX = WIDTH - 25 - PADDLE_WIDTH - BALL_SIZE;
            ballVX = -Math.abs(ballVX) * 1.04;
            double hitOffset = ((ballY + BALL_SIZE / 2.0) - (rightPaddleY + PADDLE_HEIGHT / 2.0)) / (PADDLE_HEIGHT / 2.0);
            ballVY = hitOffset * 5.0;
        }

        // Голы
        if (ballX + BALL_SIZE < 0) {
            rightScore++;
            resetBall(1);
        } else if (ballX > WIDTH) {
            leftScore++;
            resetBall(-1);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            leftScore = 0;
            rightScore = 0;
            resetBall(1);
        } else {
            pressedKeys.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Пинг-Понг — 2 игрока");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new TwoPlayerPong());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
