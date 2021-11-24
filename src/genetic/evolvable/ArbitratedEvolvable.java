package genetic.evolvable;

import genetic.arbiters.Arbiter;
import genetic.objects.Image;

public abstract class ArbitratedEvolvable<T, E extends Evolvable<T>> extends Evolvable<T> {

    private Arbiter<E> arbiter;

    public ArbitratedEvolvable(Arbiter<E> arbiter, T current) {
        super(current);
        this.arbiter = arbiter;
    }

    public ArbitratedEvolvable(Arbiter<E> arbiter, double... params) {
        super(params);
        this.arbiter = arbiter;
    }

    @Override
    public double getScore() {
        return arbiter.getScore((E) this);
    }

    public Arbiter<E> getArbiter() {
        return arbiter;
    }
}
