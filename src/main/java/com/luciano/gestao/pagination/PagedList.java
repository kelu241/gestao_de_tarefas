package com.luciano.gestao.pagination;

import java.util.ArrayList;
import java.util.List;

public class PagedList<T> extends ArrayList<T> implements IPagedList<T> {
    public int currentPage;
    public int totalPages;
    public int pageSize;
    public int totalCount;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean HasPrevious() {
        return currentPage > 1;
    }

    public boolean HasNext() {

        return currentPage < totalPages;
    }

    public PagedList(List<T> items, int count, int pageNumber, int pageSize) {
        setTotalCount(count);
        setPageSize(pageSize);
        setCurrentPage(pageNumber);
        setTotalPages((int) Math.ceil(count / (double) pageSize));

        addAll(items);
    }

    public static <T> PagedList<T> toPagedList(Iterable<T> source, int pageNumber, int pageSize) {
        var count = ((List<T>) source).size();
        var items = ((List<T>) source).stream()
                .skip((pageNumber - 1) * pageSize)
                .limit(pageSize)
                .toList();

        return new PagedList<T>(items, count, pageNumber, pageSize);
    }

}