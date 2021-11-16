import genetic.Evolvable;
import genetic.EvolvableString;
import genetic.Population;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        Population<String> stringPopulation = new Population<>("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf", 800);
        Map<Evolvable<String>, Double> population = stringPopulation.getFitnesses();

        System.out.println(stringPopulation.getTarget().length());

        System.out.println(stringPopulation.getAverage());

        long pre = System.currentTimeMillis();

        System.out.println("----------");
        boolean found = false;
        int i = 0;
        while (!found) {
                boolean update = stringPopulation.update();
                if (update) {
                    found = true;
                    System.out.println("found: " + i);
                }
                i++;
        }
        System.out.println(stringPopulation.getAverage());
        System.out.println(stringPopulation.getBest());
        System.out.println(System.currentTimeMillis() - pre);
    }
}
