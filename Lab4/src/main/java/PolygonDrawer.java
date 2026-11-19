import javax.swing.*;
import java.awt.*;

public class PolygonDrawer extends JPanel {
    private final int sides;

    public PolygonDrawer(int sides) {
        this.sides = Math.max(3, sides); // минимум 3 вершины
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides - Math.PI / 2;
            xPoints[i] = centerX + (int) Math.round(radius * Math.cos(angle));
            yPoints[i] = centerY + (int) Math.round(radius * Math.sin(angle));
        }

        g2d.setColor(new Color(70, 130, 180));
        g2d.fillPolygon(xPoints, yPoints, sides);

        g2d.setColor(new Color(25, 25, 112));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawPolygon(xPoints, yPoints, sides);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("Многоугольник: вершин = " + sides, 15, 25);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Многоугольник (Вариант 2)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 450);
            frame.add(new PolygonDrawer(6));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

