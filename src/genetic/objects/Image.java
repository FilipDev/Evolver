package genetic.objects;

import java.awt.*;

public class Image {

    /**
     * Three-dimensional array, 1st - scale, 2nd - x, 3rd - y.
     */
    private double[][][] values;
    private Color[][] image;
    private int sizeX, sizeY, scalings;
    private double range;

    /**
     * @param sizeX
     * @param sizeY
     * @param scalings How many powers of 2 to include
     */
    public Image(int sizeX, int sizeY, int scalings) {
        this.values = new double[scalings][sizeX][sizeY];
        this.scalings = scalings;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.image = new Color[sizeX][sizeY];
        for (int s = 0; s < scalings; s++) {
            this.range += 1 / Math.pow(2, s);
        }
    }

    public Image(double[][][] values) {
        this.values = values;
        this.scalings = values.length;
        this.sizeX = values[0].length;
        this.sizeY = values[0][0].length;
        this.image = new Color[sizeX][sizeY];
        flatten();
        for (int s = 0; s < scalings; s++) {
            this.range += 1 / Math.pow(2, s);
        }
    }

    public double[][][] getValues() {
        return values;
    }

    public Image copy() {
        double[][][] copied = new double[scalings][sizeX][sizeY];
        for (int s = 0; s < scalings; s++) {
            for (int x = 0; x < sizeX; x++) {
                System.arraycopy(values[s][x], 0, copied[s][x], 0, sizeY);
            }
        }
        return new Image(copied);
    }

    public void mutate(double rate, double amplitude) {
        for (int s = 0; s < scalings; s++) {
            int scale = (int) Math.pow(2, s);
            double[][] values = new double[sizeX / scale][sizeY / scale];
            boolean[][] change = new boolean[sizeX / scale][sizeY / scale];
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    if (x % scale == 0 && y % scale == 0) {
                        change[x / scale][y / scale] = Math.random() < rate;
                        values[x / scale][y / scale] = Math.random() * 2 - 1;
                    }
                    if (change[x / scale][y / scale]) {
                        this.values[s][x][y] = this.values[s][x][y] + values[x / scale][y / scale] * amplitude;
                    }
                }
            }
        }
        flatten();
    }

    public void flatten() {
        double[][] flattened = new double[sizeX][sizeY];
        for (int s = 0; s < scalings; s++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    flattened[x][y] += values[s][x][y] / (Math.pow(2, s));
                }
            }
        }
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                flattened[x][y] = transfer(flattened[x][y]);
            }
        }
        this.image = convert(flattened);
    }

    private double transfer(double value) {
        return Math.min(1, Math.max(0, value / range / 2.0 + 0.5));
    }

    public Color[][] convert(double[][] values) {
        Color[][] colors = new Color[values.length][values[0].length];
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[0].length; y++) {
                int value = Math.max(0, Math.min(255, (int) (values[x][y] * 255.0D)));
                colors[x][y] = new Color(value, value, value);
            }
        }
        return colors;
    }

    public void fill(double value) {
        for (int s = 0; s < scalings; s++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    this.values[s][x][y] = value;
                }
            }
        }
        flatten();
    }

    public Color[][] getImage() {
        return image;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getScalings() {
        return scalings;
    }
}
