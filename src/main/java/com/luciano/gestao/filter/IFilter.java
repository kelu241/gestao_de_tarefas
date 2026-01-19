package com.luciano.gestao.filter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IFilter<T> {

    Iterable<T> where(Iterable<T> items, Predicate<T> condition);
    <R> Iterable<R> select(Iterable<T> items, Function<T, R> mapper);
    Optional<T> first(Iterable<T> items, Predicate<T> condition);
    T firstOrDefault(Iterable<T> items, Predicate<T> condition, T defaultValue);
    boolean any(Iterable<T> items, Predicate<T> condition);
    boolean all(Iterable<T> items, Predicate<T> condition);
    long count(Iterable<T> items, Predicate<T> condition);
    Iterable<T> orderBy(Iterable<T> items, Function<T, ? extends Comparable<?>> keySelector);
    Iterable<T> orderByDescending(Iterable<T> items, Function<T, ? extends Comparable<?>> keySelector);
    Iterable<T> skip(Iterable<T> items, long count);
    Iterable<T> take(Iterable<T> items, long count);
    <K> Map<K, List<T>> groupBy(Iterable<T> items, Function<T, K> keySelector);
    Iterable<T> getFiltered(Iterable<T> items, String search, String criteria);
}
