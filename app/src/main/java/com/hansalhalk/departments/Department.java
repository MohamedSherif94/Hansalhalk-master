package com.hansalhalk.departments;

import java.io.Serializable;

public class Department implements Serializable {

    private int id;
    private String title;
    private int ordder;
    private Boolean isSelected = false;

    public Department() {
    }

    public Department(int id, String title, int ordder) {
        this.id = id;
        this.title = title;
        this.ordder = ordder;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrdder(int ordder) {
        this.ordder = ordder;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getOrdder() {
        return ordder;
    }

    public Boolean getSelected() {
        return isSelected;
    }
}
