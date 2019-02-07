package be.pauwel.pi.product.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class Product {

    public static List<Product> prodList = new ArrayList<Product>();

    public Company company; //bedrijf

    public String id; //local id used by the company
    public String EAN;
    public String EANEenheid;
    public String intrastatNummer;

    public String nameNL;
    public String nameFR;
    public String nameDE;
    public String nameEN;

    public String descriptionNL;
    public String descriptionFR;
    public String descriptionDE;
    public String descriptionEN;

    //public String family;
    //public String group;

    public String domain; //onderbouw, bovenbouw, ...
    public String publicationDate;
    public String edition; //1

    //Potentially to be made a separate class 'Type'
    public String typeID;
    public String typeNL;
    public String typeFR;
    public String typeDE;
    public String typeEN;

    //Potentially to be made a separate Class 'ExternalDocument'
    public String DOPNumber;
    public String DOPURLNL;
    public String DOPURLFR;
    public String PIBURLNL;
    public String PIBURLFR;

    public String qualityLabel; //keurmerk
    public String brand; //X-model
    public String image;

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
