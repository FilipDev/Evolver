package genetic.arbiters;

import genetic.evolvable.Evolvable;

public interface Arbiter<E extends Evolvable> {

    double getScore(E evolvable);
}
