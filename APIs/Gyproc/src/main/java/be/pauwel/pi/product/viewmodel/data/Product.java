package be.pauwel.pi.product.viewmodel.data;

import java.util.ArrayList;
import java.util.List;

public class Product {

    public static List<Product> prodList = new ArrayList<Product>();

    public Company company; //bedrijf

    public String id; //local id used by the company
    public String intrastatNumber;
    public GTIN gtin;
    public DOP dop;

    public ArrayList<Name> names = new ArrayList<Name>();
    public ArrayList<Description> descriptions = new ArrayList<Description>();
    public ArrayList<Image> images = new ArrayList<Image>();
    public ArrayList<String> categories = new ArrayList<String>();

    //public String family;
    //public String group;

    public String domain; //onderbouw, bovenbouw, ...
    public String publicationDate;
    public String edition; //1

    //Potentially to be made a separate class 'Type'
    public String typeID;

    public ArrayList<Type> types = new ArrayList<Type>();

    public String qualityLabel; //keurmerk
    public String brand; //X-model

    public ArrayList<Property> properties = new ArrayList<Property>();

    public Product() {
        prodList.add(this);
    }

    public Property getProperty(String name){
        for(int i = 0;i<properties.size();i++){
            Property prop = properties.get(i);
            if(prop.id.equalsIgnoreCase(name))
                return prop;
        }
        return null;
    }
}
