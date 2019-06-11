package be.pauwel.pi.product.viewmodel.data;

import java.util.ArrayList;
import java.util.List;

public class DOP {

    public String number;
    public List<Url> URL = new ArrayList<Url>();

    public DOP(){

    }

    public DOP(String number){
        this.number = number;
    }
}
