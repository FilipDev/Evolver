package genetic.evolvable.evolvables;

import genetic.arbiters.Arbiter;
import genetic.evolvable.ArbitratedEvolvable;
import genetic.evolvable.Evolvable;
import genetic.objects.Image;

public class EvolvableImage extends ArbitratedEvolvable<Image, EvolvableImage> {

    private int sizeX, sizeY;

    public EvolvableImage(Arbiter<EvolvableImage> arbiter, double... params) {
        super(arbiter, params);
        this.sizeX = (int) params[0];
        this.sizeY = (int) params[1];
    }

    public EvolvableImage(Arbiter<EvolvableImage> arbiter, Image image) {
        super(arbiter, image);
        this.sizeX = image.getSizeX();
        this.sizeY = image.getSizeY();
    }

    @Override
    protected Image generate(double... parameters) {
        return new Image((int) parameters[0], (int) parameters[1], parameters.length > 2 ? (int) parameters[2] : 3);
    }

    public void fill(double value) {
        getCurrent().fill(value);
    }

    @Override
    public void mutate(double rate) {
        getCurrent().mutate(rate, 1);
    }

    @Override
    public Evolvable<Image> mix(Evolvable<Image> other, double fitnessDifference) {
        double[][][] newImage = new double[getCurrent().getScalings()][sizeX][sizeY];
        double currValue = Math.random();
        for (int s = 0; s < getCurrent().getScalings(); s++) {
            double scale = Math.pow(2, s);
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    if (x % scale == 0 && y % scale == 0) currValue = Math.random();
                    newImage[s][x][y] = currValue < fitnessDifference + 0.5 ? other.getCurrent().getValues()[s][x][y] : getCurrent().getValues()[s][x][y];
                }
            }
        }
        EvolvableImage evolvableImage = new EvolvableImage(getArbiter(), new Image(newImage));
        return evolvableImage;
    }

    /**
     * Todo: truly account for scalings
     */
    @Override
    public int getPossibleStates() {
        return getCurrent().getSizeX() * getCurrent().getSizeY() * getCurrent().getScalings();
    }
}
