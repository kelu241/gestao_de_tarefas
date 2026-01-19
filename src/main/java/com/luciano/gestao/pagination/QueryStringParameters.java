package com.luciano.gestao.pagination;

public abstract class QueryStringParameters {
    private final int maxPageSize = 50;
    private int pagedNumber = 1;
    private int _pageSize = maxPageSize;

    public int getPagedNumber() {

        return pagedNumber;
    }

    public void setPagedNumber(int pagedNumber) {
        this.pagedNumber = pagedNumber;
    }


    public int get_pageSize() {
        return _pageSize;
    }

    public void set_pageSize(int _pageSize) {

        this._pageSize = (_pageSize > maxPageSize) ? maxPageSize : _pageSize;
    }

}
