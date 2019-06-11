package be.pauwel.pi.product.viewmodel.data;

public class GTIN {

    public String EAN;
    public String unit;

    public GTIN(){

    }

    public GTIN(String EAN, String unit){
        this.EAN = EAN;
        this.unit = unit;
    }
}
