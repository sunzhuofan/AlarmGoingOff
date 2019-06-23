package com.sunsoon.alarmgoingoff;

public class TextItem {

    private int id;
    private String curTitle;
    private String curDetail;

    public TextItem() {
        this.curTitle = "";
        this.curDetail = "";
    }

    public TextItem(String curTitle, String curDetail) {
        this.curTitle = curTitle;
        this.curDetail = curDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurTitle() {
        return curTitle;
    }

    public void setCurTitle(String curTitle) {
        this.curTitle = curTitle;
    }

    public String getCurDetail() { return curDetail; }

    public void setCurDetail(String curDetail) {
        this.curDetail = curDetail;
    }


}
