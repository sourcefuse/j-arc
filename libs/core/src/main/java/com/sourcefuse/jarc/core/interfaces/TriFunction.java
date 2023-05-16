package com.sourcefuse.jarc.core.interfaces;

public interface TriFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
