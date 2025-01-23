package com.project.weatherapp.SrtFcst;

public class Body {
    private String dataType;
    private Items items; // Itmes
    private int pageNo;
    private int numOfRows;
    private int totalCount;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Items getItmes() {
        return items;
    }

    public void setItmes(Items items) {
        this.items = items;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
