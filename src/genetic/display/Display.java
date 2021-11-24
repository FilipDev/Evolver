package genetic.display;

import genetic.objects.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Display extends JPanel {

    private int scale, scaledSizeX, scaledSizeY;
    private JFrame frame;
    private MouseEvent mouseEvent;
    private String title;
    private Color[][] output;

    public static Display display;

    public Display(String title, int sizeX, int sizeY, int scale) {
        this.title = title;
        this.scale = scale;
        this.scaledSizeX = sizeX * scale;
        this.scaledSizeY = sizeY * scale;
        this.output = new Color[scaledSizeX][scaledSizeY];
        instantiate();
        display = this;
    }

    public void update() {
        frame.repaint();
    }

    public Color[][] getOutput() {
        return this.output;
    }

    public void setOutput(Color[][] output) {
        this.output = output;
    }

    public void setOutput(Image image, int x, int y) {
        Color[][] output = image.getImage();
        for (int xn = 0; xn < output.length; xn++) {
            for (int yn = 0; yn < output[0].length; yn++) {
                if (xn + x >= 0 || xn + x < getSizeX()) {
                    if (yn + y >= 0 || yn + y < getSizeY()) {
                        this.output[x + xn][y + yn] = output[xn][yn];
                    }
                }
            }
        }
    }

    private static Color DEFAULT_COLOR = Color.BLACK;

    public void paint(Graphics g) {
        /* Background Color */
        g.setColor(DEFAULT_COLOR);
        g.fillRect(0, 0, scaledSizeX, scaledSizeY + 2);

        /* Draw Display */
        for (int xn = 0; xn < scaledSizeX; xn += scale) {
            for (int yn = 0; yn < scaledSizeY; yn += scale) {
                int x = xn / scale, y = yn / scale;
                if (output[x][y] != null) {
                    //System.out.println(output[x][y]);
                    g.setColor(output[x][y]);
                    g.fillRect(xn, yn, scale, scale);
                }
            }
        }
    }

    @Override
    public Point getMousePosition() throws HeadlessException {
        Point mousePosition = super.getMousePosition();
        if (mousePosition == null) return null;
        mousePosition.x /= scale;
        mousePosition.y /= scale;
        return mousePosition;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    private void instantiate() {
        this.frame = new JFrame(title);
        frame.getContentPane().add(this);
        frame.setSize(scaledSizeX + 12, scaledSizeY + 40);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseEvent = e;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseEvent = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    public int getSizeX() {
        return output.length;
    }

    public int getSizeY() {
        return output[0].length;
    }
}