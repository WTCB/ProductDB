package be.pauwel.pi.product.viewmodel.ui;

import java.util.ArrayList;

public class MenuItem {

    public static ArrayList<MenuItem> menuItemList = new ArrayList<MenuItem>();

    private int id;
    private String label = "";
    public ArrayList<SubItem> subItems = new ArrayList<SubItem>();

    public MenuItem() {
        menuItemList.add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
