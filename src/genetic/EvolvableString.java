package genetic;

import java.util.Random;

public class EvolvableString extends Evolvable<String> {

    public EvolvableString(String target, String string) {
        super(target, string);
    }

    public EvolvableString(String target) {
        super(target, target.length());
    }

    @Override
    public double getScore() {
        int score = 0;
        for (int i = 0; i < getTarget().length(); i++) {
            if (getTarget().charAt(i) == getCurrent().charAt(i)) {
                score++;
            }
        }
        return (double) score / (double) getTarget().length();
    }

    @Override
    public void mutate(double rate) {
        char[] chars = new char[getCurrent().length()];
        for (int i = 0; i < getCurrent().length(); i++) {
            if (Math.random() <= rate) {
                chars[i] = randomChar();
            } else {
                chars[i] = getCurrent().charAt(i);
            }
        }
        setCurrent(String.valueOf(chars));
    }

    private static final char[] possibleChars = new char[52];

    static {
        int n = 0;
        for (int i = 65; i < 123; i++) {
            if (i < 91 || i > 96) {
                possibleChars[n] = (char) i;
                n++;
            }
        }
    }

    private char randomChar() {
        Random random = new Random();
        int i = random.nextInt(52);
        return possibleChars[i];
    }

    @Override
    protected String generate(double... parameters) {
        if (parameters.length > 0) {
            int length = (int) parameters[0];
            char[] generated = new char[length];

            for (int i = 0; i < length; i++) {
                generated[i] = randomChar();
            }

            return String.valueOf(generated);
        }
        return null;
    }

    @Override
    public EvolvableString mix(Evolvable<String> other, double fitnessDifference) {
        char[] newChars = new char[getCurrent().length()];

        for (int i = 0; i < newChars.length; i++) {
            newChars[i] = Math.random() < fitnessDifference + 0.5 ? other.getCurrent().charAt(i) : getCurrent().charAt(i);
        }

        return new EvolvableString(getTarget(), String.valueOf(newChars));
    }

    @Override
    public int getMicrostates() {
        return possibleChars.length;
    }
}
