package com.nubiz.answerandwin.dao;

public class DrawerItem {

    private String name;
    private int id;
    private String icon;
    private String count = "0";
    private boolean menuType;
    private boolean isCounterVisible = false;

    public DrawerItem() {
    }

    public DrawerItem(int id, String name, String icon, boolean menuType) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.menuType = menuType;
    }

    public DrawerItem(int id, String name, String icon, boolean isCounterVisible, String count) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean getCounterVisibility() {
        return this.isCounterVisible;
    }

    public void setCounterVisibility(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMenuType() {
        return menuType;
    }

    public void setMenuType(boolean menuType) {
        this.menuType = menuType;
    }
}
