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

        DBType neo4j = new DBType();
        neo4j.id = 0;
        neo4j.name = "neo4j";
        neo4j.version = "";

        Server gyproc = new Server();
        gyproc.theDBType = neo4j;
        gyproc.URL = "bolt://206.189.75.254:7687";
        gyproc.username = "neo4j";
        gyproc.password = "neo4j";
        gyproc.driver = GraphDatabase.driver( gyproc.URL, AuthTokens.basic( gyproc.username, gyproc.password ) );
    }
}
