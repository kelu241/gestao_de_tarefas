package com.luciano.gestao.repository;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.luciano.gestao.pagination.PagedList;

public interface IGerericRepository<T> {
    CompletableFuture<T> saveAsync(T entity);
    CompletableFuture<T> findByIdAsync(Long id);
    CompletableFuture<T> deleteByIdAsync(Long id);
    CompletableFuture<Iterable<T>> findAllAsync();
    CompletableFuture<Long> countAsync();
    CompletableFuture<PagedList<T>> paginateAsync(int pageNumber, int pageSize);
    CompletableFuture<Iterable<T>> searchAsync(Predicate<T> search);
}
