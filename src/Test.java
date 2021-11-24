import genetic.display.Display;
import genetic.evolvable.Evolvable;
import genetic.Population;
import genetic.evolvable.evolvables.EvolvableImage;
import genetic.objects.Image;

import java.awt.*;
import java.util.Map;

public class Test {

    /*public static void main(String[] args) {
        Population<String> stringPopulation = new Population<>(800);
        stringPopulation.generateStrings("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf");
        Map<Evolvable<String>, Double> population = stringPopulation.getFitnesses();

        System.out.println(stringPopulation.getAverage());
        System.out.println(stringPopulation.getPossibleStates());

        long pre = System.currentTimeMillis();

        System.out.println("----------");
        boolean found = false;
        int i = 0;
        while (!found) {
                boolean update = stringPopulation.update(0.01, 1, 0);
                if (update) {
                    found = true;
                    System.out.println("found: " + i);
                }
                i++;
        }
        System.out.println(stringPopulation.getAverage());
        System.out.println(stringPopulation.getBest());
        System.out.println(System.currentTimeMillis() - pre);
    }*/

    public static void main(String[] args) throws InterruptedException {
        Display display = new Display("Image Generator", 200, 200, 4);


        Population<Image> images = new Population<>(2);

        images.generateImages((evolvable -> {
            double score = 0;
            evolvable.getCurrent().flatten();
            Color[][] image = evolvable.getCurrent().getImage();
            for (int x = 0; x < image.length; x++) {
                for (int y = 0; y < image[0].length; y++) {
                    score += (y > image[0].length / 2 ? -1 : 1) * image[x][y].getRed() / 255.0;
                }
            }
            //System.out.println("score: " + score);
            return (score / evolvable.getCurrent().getSizeX() / evolvable.getCurrent().getSizeY());
        }), 64, 64, 6);

        System.out.println(images.getPossibleStates());

        long pre = System.currentTimeMillis();

        boolean found = false;
        int i = 0;
        while (!found) {
            boolean update = images.update(0.1, 1, 0.995);
            display.setOutput(images.getBest().getCurrent(), 16, 16);
            display.update();
            if (update) {
                found = true;
                System.out.println("found: " + i);
            }
            i++;
            //Thread.sleep(10);
        }

        System.out.println(images.getAverage());
        System.out.println(images.getBest());
        System.out.println(System.currentTimeMillis() - pre);
    }
}
