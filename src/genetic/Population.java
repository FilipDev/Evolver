package genetic;

import genetic.arbiters.Arbiter;
import genetic.evolvable.Evolvable;
import genetic.evolvable.evolvables.EvolvableImage;
import genetic.evolvable.evolvables.EvolvableString;

import java.util.*;

public class Population<T> {

    private Map<Evolvable<T>, Double> fitnesses = new HashMap<>();
    private Set<Evolvable<T>> population = new HashSet<>();
    private int maxPopulation, epoch, found, possibleStates = 0;

    public Population(int population) {
        this.maxPopulation = population;
    }

    public void generateStrings(String target) {
        for (int i = 0; i < maxPopulation; i++) {
            EvolvableString evolvableString = new EvolvableString(target);
            if (possibleStates == 0) possibleStates = evolvableString.getPossibleStates();
            this.population.add((Evolvable<T>) evolvableString);
            fitnesses.put((Evolvable<T>) evolvableString, evolvableString.getScore());
        }
    }

    public void generateImages(Arbiter<EvolvableImage> arbiter, int sizeX, int sizeY, int scalings) {
        for (int i = 0; i < maxPopulation; i++) {
            EvolvableImage evolvableImage = new EvolvableImage(arbiter, sizeX, sizeY, scalings);
            evolvableImage.fill(0.5D);
            if (possibleStates == 0) possibleStates = evolvableImage.getPossibleStates();
            this.population.add((Evolvable<T>) evolvableImage);
            fitnesses.put((Evolvable<T>) evolvableImage, evolvableImage.getScore());
        }
    }

    public boolean update(double mutationRate, double selectionDifference, double reproductionBaseRate) {
        mutate(mutationRate);
        select(selectionDifference);
        reproduce(reproductionBaseRate);

        updatePopulation();
        updateFitnesses();

        epoch++;

        if (epoch > 500) {
        }

        return found > 0;
    }

    public void mutate(double rate) {
        for (Evolvable<T> evolvable : population) {
            evolvable.mutate(rate);
            double score = evolvable.getScore();
            fitnesses.put(evolvable, score);
        }
    }

    public void select(double stds) {
        double avg = getAverage();
        double standardDeviation = getStandardDeviation();

        found = 0;

        for (Map.Entry<Evolvable<T>, Double> entry : fitnesses.entrySet()) {
            if (entry.getValue() == 1.0) {
                found++;
            } else if (entry.getValue() / avg < 1 + standardDeviation * stds) {//1 + difference) {//(entry.getValue() < Math.min(1, avg + difference)) {
                if ((double) population.size() > maxPopulation / 4.0) {
                    population.remove(entry.getKey());
                }
            }
        }
    }

    public void reproduce(double baseChance) {
        while (newPopulation.size() < maxPopulation) {
            for (Evolvable<T> evolvable1 : population) {
                for (Evolvable<T> evolvable2 : population) {
                    if (Math.random() < baseChance + fitnesses.get(evolvable1) * fitnesses.get(evolvable2)) {
                        Evolvable<T> mix = mix(evolvable1, evolvable2);
                        newPopulation.add(mix);
                    }
                }
            }
        }
    }

    private Set<Evolvable<T>> newPopulation = new HashSet<>();

    private void updatePopulation() {
        this.population.clear();
        for (Evolvable<T> evolvable : this.newPopulation) {
            if (this.population.size() >= maxPopulation) break;
            this.population.add(evolvable);
        }
        this.newPopulation.clear();
    }

    private void updateFitnesses() {
        Map<Evolvable<T>, Double> newFitnesses = new HashMap<>();
        for (Evolvable<T> evolvable : population) {
            newFitnesses.put(evolvable, evolvable.getScore());
        }
        this.fitnesses = newFitnesses;
    }

    public Evolvable<T> mix(Evolvable<T> a, Evolvable<T> b) {
        return a.mix(b, (fitnesses.get(b) - fitnesses.get(b)));
    }

    public Map<Evolvable<T>, Double> getFitnesses() {
        return fitnesses;
    }

    public Set<Evolvable<T>> getPopulation() {
        return population;
    }

    public double getAverage() {
        double total = 0.0;
        int i = 0;
        for (Map.Entry<Evolvable<T>, Double> entry : fitnesses.entrySet()) {
            total += entry.getValue();
            i++;
        }
        return total / (double) i;
    }

    public double getStandardDeviation() {
        double average = getAverage();
        double dev = 0.0;

        for (Map.Entry<Evolvable<T>, Double> entry : fitnesses.entrySet()) {
            dev += Math.pow(entry.getValue() - average, 2);
        }

        return Math.sqrt(dev / fitnesses.size());
    }

    public Evolvable<T> getBest() {
        Evolvable<T> best = null;
        for (Evolvable<T> evolvable : getPopulation()) {
            if (best == null) best = evolvable;
            if (fitnesses.get(evolvable) > fitnesses.get(best)) {
                best = evolvable;
            }
        }
        return best;
    }

    public int getPossibleStates() {
        return possibleStates;
    }
}
