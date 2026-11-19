import javax.swing.*;
import java.awt.*;

public class SinePlotter extends JPanel{
    private final double xMin = -2 * Math.PI;
    private final double xMax = 2 * Math.PI;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int pad = 50;

        int plotWidth = width - 2 * pad;
        int plotHeight = height - 2 * pad;

        // Отображение математических координат в пиксели экрана
        int originX = pad + (int) ((-xMin / (xMax - xMin)) * plotWidth);
        int originY = pad + plotHeight / 2;
        double yScale = plotHeight / 2.4; // масштаб по Y (от -1.2 до 1.2)

        // Оси координат
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(pad, originY, width - pad, originY); // ось X
        g2d.drawLine(originX, pad, originX, height - pad); // ось Y

        // Подписи осей
        g2d.setColor(Color.BLACK);
        g2d.drawString("y = sin(x)", pad, 25);
        g2d.drawString("X", width - pad + 10, originY + 4);
        g2d.drawString("Y", originX - 4, pad - 10);
        g2d.drawString("1", originX - 18, (int) (originY - yScale) + 4);
        g2d.drawString("-1", originX - 22, (int) (originY + yScale) + 4);

        // Отрисовка кривой y = sin(x)
        g2d.setColor(new Color(220, 20, 60));
        g2d.setStroke(new BasicStroke(2.5f));

        int prevX = -1;
        int prevY = -1;

        for (int px = pad; px <= width - pad; px++) {
            double x = xMin + ((double) (px - pad) / plotWidth) * (xMax - xMin);
            double y = Math.sin(x);
            int py = (int) Math.round(originY - y * yScale);

            if (prevX != -1) {
                g2d.drawLine(prevX, prevY, px, py);
            }
            prevX = px;
            prevY = py;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("График функции y = sin(x)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 450);
            frame.add(new SinePlotter());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
