package genetic;

public abstract class Evolvable<T> implements Comparable {

    private T target;
    private T current;

    public Evolvable(T target, T current) {
        this.current = current;
        this.target = target;
    }

    public Evolvable(T target, double... parameters) {
        this.current = generate(parameters);
        this.target = target;
    }

    public T getCurrent() {
        return current;
    }

    protected void setCurrent(T newCurrent) {
        this.current = newCurrent;
    }

    public abstract double getScore();

    protected abstract T generate(double... parameters);

    public abstract void mutate(double rate);

    public abstract Evolvable<T> mix(Evolvable<T> other, double fitnessDifference);

    @Override
    public String toString() {
        return getCurrent().toString();
    }

    public T getTarget() {
        return target;
    }

    @Override
    public int compareTo(Object o) {
        if (getClass().isInstance(o)) {
            return (int) (((Evolvable<T>) o).getScore() - getScore());
        }
        return 0;
    }

    public abstract int getMicrostates();
}
