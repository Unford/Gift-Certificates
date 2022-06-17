package com.epam.esm.core.model;

/**
 * The type Pair.
 *
 * @param <A> the type parameter
 * @param <B> the type parameter
 */
public final class Pair<A, B> {
    private final A first;
    private final B second;


    /**
     * Instantiates a new Pair.
     *
     * @param first  the first
     * @param second the second
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public A getFirst() {
        return first;
    }

    /**
     * Gets second.
     *
     * @return the second
     */
    public B getSecond() {
        return second;
    }

    /**
     * Of pair.
     *
     * @param <A> the type parameter
     * @param <B> the type parameter
     * @param a   the a
     * @param b   the b
     * @return the pair
     */
    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }
}
