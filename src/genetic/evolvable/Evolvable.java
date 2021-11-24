package genetic.evolvable;

public abstract class Evolvable<T> implements Comparable {

    private T current;

    public Evolvable(T current) {
        this.current = current;
    }

    public Evolvable(double... parameters) {
        this.current = generate(parameters);
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

    @Override
    public int compareTo(Object o) {
        if (getClass().isInstance(o)) {
            return (int) (((Evolvable<T>) o).getScore() - getScore());
        }
        return 0;
    }

    public abstract int getPossibleStates();
}
