package be.pauwel.pi.product.viewmodel.servers;

import be.pauwel.pi.product.viewmodel.data.Company;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.AuthTokens;

import java.util.ArrayList;

public class Server {

    public static ArrayList<Server> serverList = new ArrayList<Server>();
    public Company company; //bedrijf

    public String username;
    public String password;

    public DBType theDBType;
    public String URL;

    public Driver driver;

    public Server() {
        serverList.add(this);
    }

    public static void initiate(){
        DBType dbt = new DBType();
        dbt.id = 0;
        dbt.name = "neo4j";
        dbt.version = "";

        Server s = new Server();
        s.theDBType = dbt;
        s.URL = "bolt://206.189.75.254:7687";
        s.username = "neo4j";
        s.password = "neo4j";
        s.driver = GraphDatabase.driver( s.URL, AuthTokens.basic( s.username, s.password ) );
        Company c = Company.getCompany("SVK");
        s.company = c;
        c.setServer(s);
    }
}
