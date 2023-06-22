package com.sourcefuse.jarc.core.filters.interfaces;

/**
 * Represents a function that takes three arguments and produces a result.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <V> the type of the third argument
 * @param <R> the type of the result
 */
public interface TriFunction<T, U, V, R> {
  /**
   * Applies this function to the given arguments and produces a result.
   *
   * @param t the first argument
   * @param u the second argument
   * @param v the third argument
   * @return the result of the function
   */
  R apply(T t, U u, V v);
}
