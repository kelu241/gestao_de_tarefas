package com.luciano.gestao.filter;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import com.luciano.gestao.pagination.QueryStringParameters;

public class Filter<T> extends QueryStringParameters{

    private String search;

    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }

    public String searchParameter;
    public String getSearchParameter() {
        return searchParameter;
    }
    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }



    // LINQ-style: Where
    public Iterable<T> where(Iterable<T> items, Predicate<T> condition) {
        return StreamSupport.stream(items.spliterator(), false)
            .filter(condition)
            .collect(Collectors.toList());
    }

    // LINQ-style: Select (map)
    public <R> Iterable<R> select(Iterable<T> items, Function<T, R> mapper) {
        return StreamSupport.stream(items.spliterator(), false)
            .map(mapper)
            .collect(Collectors.toList());
    }

    // LINQ-style: First
    public Optional<T> first(Iterable<T> items, Predicate<T> condition) {
        return StreamSupport.stream(items.spliterator(), false)
            .filter(condition)
            .findFirst();
    }

    // LINQ-style: FirstOrDefault
    public T firstOrDefault(Iterable<T> items, Predicate<T> condition, T defaultValue) {
        return StreamSupport.stream(items.spliterator(), false)
            .filter(condition)
            .findFirst()
            .orElse(defaultValue);
    }

    // LINQ-style: Any
    public boolean any(Iterable<T> items, Predicate<T> condition) {
        return StreamSupport.stream(items.spliterator(), false)
            .anyMatch(condition);
    }

    // LINQ-style: All
    public boolean all(Iterable<T> items, Predicate<T> condition) {
        return StreamSupport.stream(items.spliterator(), false)
            .allMatch(condition);
    }

    // LINQ-style: Count
    public long count(Iterable<T> items, Predicate<T> condition) {
        return StreamSupport.stream(items.spliterator(), false)
            .filter(condition)
            .count();
    }

    // LINQ-style: OrderBy
       @SuppressWarnings("unchecked") 
    public Iterable<T> orderBy(Iterable<T> items, Function<T, ? extends Comparable<?>> keySelector) {
        return StreamSupport.stream(items.spliterator(), false)
            .sorted((a,b)-> {
                Comparable<Object> keyA = (Comparable<Object>)keySelector.apply(a);
                Comparable<Object> keyB = (Comparable<Object>)keySelector.apply(b);
                return keyA.compareTo(keyB);
            })
            .collect(Collectors.toList());
    }

    // LINQ-style: OrderByDescending
    @SuppressWarnings("unchecked") 
    public Iterable<T> orderByDescending(Iterable<T> items, Function<T, ? extends Comparable<?>> keySelector) {
        return StreamSupport.stream(items.spliterator(), false)
            .sorted((a,b)-> {
                Comparable<Object> keyA = (Comparable<Object>)keySelector.apply(a);
                Comparable<Object> keyB = (Comparable<Object>)keySelector.apply(b);
                return keyA.compareTo(keyB);
            })
            .collect(Collectors.toList());
    }

    // LINQ-style: Skip
    public Iterable<T> skip(Iterable<T> items, long count) {
        return StreamSupport.stream(items.spliterator(), false)
            .skip(count)
            .collect(Collectors.toList());
    }

    // LINQ-style: Take
    public Iterable<T> take(Iterable<T> items, long count) {
        return StreamSupport.stream(items.spliterator(), false)
            .limit(count)
            .collect(Collectors.toList());
    }

    // LINQ-style: GroupBy
    public <K> java.util.Map<K, List<T>> groupBy(Iterable<T> items, Function<T, K> keySelector) {
        return StreamSupport.stream(items.spliterator(), false)
            .collect(Collectors.groupingBy(keySelector));
    }

    // Método original melhorado com LINQ-style
    public Iterable<T> getFiltered(Iterable<T> items, String search, String criteria) {
        if (items == null || search == null || search.isEmpty()) {
            return items;
        }

        // Usando LINQ-style where
        return where(items, item -> matchesCriteria(item, search, criteria));
    }

    // Método combinado: Filter + OrderBy + Take (paginação)
    public Iterable<T> getFilteredAndPaged(Iterable<T> items, String search, String criteria, 
                                           Function<T, ? extends Comparable<?>> orderBy, 
                                           int pageNumber, int pageSize) {
        var filtered = where(items, item -> matchesCriteria(item, search, criteria));
        var ordered = orderBy(filtered, orderBy);
        var skipped = skip(ordered, (pageNumber - 1) * pageSize);
        return take(skipped, pageSize);
    }

    // Método auxiliar existente
    private boolean matchesCriteria(T item, String search, String criteria) {
        if (item == null || search == null) {
            return false;
        }
        
        String searchLower = search.toLowerCase();
        
        try {
            if (criteria != null && !criteria.isEmpty()) {
                java.lang.reflect.Field field = item.getClass().getDeclaredField(criteria);
                field.setAccessible(true);
                Object value = field.get(item);
                
                if (value != null) {
                    return value.toString().toLowerCase().contains(searchLower);
                }
            }
        } catch (Exception e) {
            // Se falhar na reflexão, usa toString() como fallback
        }
        
        return item.toString().toLowerCase().contains(searchLower);
    }



}
