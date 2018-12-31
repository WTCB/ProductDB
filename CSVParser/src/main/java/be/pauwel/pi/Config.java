package be.pauwel.pi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class Config {

	private static String url;
	private static String user;
	private static String password;
	
	//public static String path = "C:\\Users\\pipauwel\\Desktop\\Git\\pipauwel\\ProductDB\\src\\main\\resources";
	//public static String path = "C:\\Users\\pipauwel\\Desktop\\Git\\pipauwel\\ProductDB\\src\\main\\resources";
	public static Driver driver;
	
	public Config() { }	
	
	public static void getData() throws IOException{
	    //Properties mainProperties = new Properties();
	    //FileInputStream file;
	    //String s = Config.path + "\\config.properties";
	    //file = new FileInputStream(s);
	    //mainProperties.load(file);
	    //file.close();
	    //url = mainProperties.getProperty("URL");
	    //user = mainProperties.getProperty("user");
	    //String qqq = url;
	    //password = mainProperties.getProperty("passw");
	    //if(user==null || user=="" || password==null || password =="")
	    	driver = GraphDatabase.driver("bolt://206.189.75.254:7687");
	    //else
	    //	driver = GraphDatabase.driver(url,AuthTokens.basic(user, password));
	}
}
