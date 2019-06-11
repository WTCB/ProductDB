package be.pauwel.pi.product.viewmodel.servers;

import java.util.ArrayList;

public class DBType {

    public static ArrayList<DBType> DBTypeList = new ArrayList<DBType>();

    public String name;
    public int id;
    public String version;

    public DBType() {
        DBTypeList.add(this);
    }

}