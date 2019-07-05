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
        //Server renson = new Server();

        //DBType dbt = new DBType();
        //dbt.id = 0;
        //dbt.name = "neo4j";
        //dbt.version = "";

        Server s = new Server();
        //s.theDBType = dbt;
        s.URL = "http://207.154.219.241:8080/gyproc";
        s.username = "neo4j";
        s.password = "neo4j";
        //s.driver = GraphDatabase.driver( s.URL, AuthTokens.basic( s.username, s.password ) );
        Company c = Company.getCompany("Gyproc");
        s.company = c;
        c.setServer(s);

        Server s1 = new Server();
        //s1.theDBType = dbt;
        s1.URL = "http://46.101.132.181:8080/SVK";
        s1.username = "neo4j";
        s1.password = "neo4j";
        //s1.driver = GraphDatabase.driver( s1.URL, AuthTokens.basic( s1.username, s1.password ) );
        Company c1 = Company.getCompany("SVK");
        s1.company = c1;
        c1.setServer(s1);

        /*Server s2 = new Server();
        s2.theDBType = dbt;
        s2.URL = "bolt://206.189.75.254:7687";
        s2.username = "neo4j";
        s2.password = "neo4j";
        s2.driver = GraphDatabase.driver( s2.URL, AuthTokens.basic( s2.username, s2.password ) );
        Company c2 = Company.getCompany("Renson");
        s2.company = c2;
        c2.setServer(s2);*/
    }
}
