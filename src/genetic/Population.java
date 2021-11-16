package genetic;

import java.util.*;

public class Population<T> {

    private Map<Evolvable<T>, Double> fitnesses = new HashMap<>();
    private Set<Evolvable<T>> population = new HashSet<>();
    private T target;
    private int maxPopulation, epoch, found, microstates = -1;

    public Population(String target, int population) {
        this.target = (T) target;
        this.maxPopulation = population;
        for (int i = 0; i < population; i++) {
            EvolvableString evolvableString = new EvolvableString(target);
            if (microstates == -1) microstates = evolvableString.getMicrostates();
            this.population.add((Evolvable<T>) evolvableString);
            fitnesses.put((Evolvable<T>) evolvableString, evolvableString.getScore());
        }
    }

    public boolean update() {
        mutate(0.008);
        select(0.12);
        reproduce(0);
        updatePopulation();
        updateFitnesses();
        epoch++;

        if (epoch > 500) {
            System.out.println(getBest());
        }

        return found > 0;
    }

    public void mutate(double rate) {
        for (Evolvable<T> evolvable : population) {
            evolvable.mutate(rate);
            fitnesses.put(evolvable, evolvable.getScore());
        }
    }

    public void select(double difference) {
        double avg = getAverage();

        found = 0;

        for (Map.Entry<Evolvable<T>, Double> entry : fitnesses.entrySet()) {
            if (entry.getValue() == 1.0) {
                found++;
            } else if (entry.getValue() + (Math.random() / 20) - 0.01 < Math.min(1, avg + difference)) {
                if (population.size() > entry.getKey().getMicrostates() * 2) {
                    //if (Math.random() / 1.2 > entry.getValue())
                    population.remove(entry.getKey());
                }
            }
        }
        if (found > 0) {
            //System.out.println("Found " + found + " at " + epoch);
        }
    }

    public void reproduce(double baseChance) {
        while (newPopulation.size() < 200) {
            for (Evolvable<T> evolvable1 : population) {
                for (Evolvable<T> evolvable2 : population) {
                    if (Math.random() < baseChance + fitnesses.get(evolvable1) * fitnesses.get(evolvable2)) {
                        Evolvable<T> mix = mix(evolvable1, evolvable2);
                        newPopulation.add(mix);
                        fitnesses.put(mix, mix.getScore());
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

    public T getTarget() {
        return target;
    }
}
