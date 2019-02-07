package be.pauwel.pi.product.viewmodel.ui;

import java.util.ArrayList;

public class SubItem {

    private static ArrayList<SubItem> subItemList = new ArrayList<SubItem>();

    private int id;
    private String label = "";
    private String type = "";
    private String unit = "";
    private double min;
    private double max;

    public SubItem() {
        subItemList.add(this);
    }
    public SubItem(int id, String label, String type, String unit, double min, double max) {
        subItemList.add(this);
        this.id = id;
        this.label = label;
        this.type = type;
        this.unit = unit;
        this.min = min;
        this.max = max;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

}
