package it.polimi.se2019.utils;

/**
 * Simple pair of two elements of types E and F.
 *
 * @param <E> type of the first element.
 * @param <F> type of the second element.
 */
public class Pair<E, F> {
    private E first;
    private F second;

    public Pair(E first, F second) {
        this.first = first;
        this.second = second;
    }

    public E getFirst() {
        return first;
    }

    public F getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair of " + first.toString() + " and " + second.toString();
    }
}
