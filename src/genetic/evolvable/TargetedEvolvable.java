package genetic.evolvable;

public abstract class TargetedEvolvable<T> extends Evolvable<T> {

    private T target;

    public TargetedEvolvable(T target, T current) {
        super(current);
        this.target = target;
    }

    public TargetedEvolvable(T target, double... parameters) {
        super(parameters);
        this.target = target;
    }

    public T getTarget() {
        return target;
    }
}
