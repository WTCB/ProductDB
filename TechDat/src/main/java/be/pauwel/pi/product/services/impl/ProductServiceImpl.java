package be.pauwel.pi.product.services.impl;

import be.pauwel.pi.product.services.ProductService;
import be.pauwel.pi.product.viewmodel.data.*;
import be.pauwel.pi.product.viewmodel.servers.Server;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

@Service
public class ProductServiceImpl implements ProductService {

    private final String USER_AGENT = "Mozilla/5.0";

    @Override
    public List<Product> getAllProductsWithoutProps() {
        List<Product> prodList = new ArrayList<Product>();

        for(Company c:Company.companyList){
            if(c.getServer()!=null)
                prodList.addAll(getProductsFromCompany(c.getName(),c.getServer(), false));
        }

        return prodList;
    }

    @Override
    public List<Product> getAllProductsWithProps() {
        List<Product> prodList = new ArrayList<Product>();

        for(Company c:Company.companyList){
            if(c.getServer()!=null)
                prodList.addAll(getProductsFromCompany(c.getName(), c.getServer(), true));
        }

        return prodList;
    }

    @Override
    public List<Product> getAllProductsWithProperties(List<Property> properties) {
        List<Product> prodList = new ArrayList<Product>();

        for(Company c:Company.companyList){
            getProductsFromCompany(c.getName(),c.getServer(), true);
        }

        return prodList;
    }

    @Override
    public Product getProductWithId(String id) {
        List<Product> prodList = new ArrayList<Product>();

        //G124584

        for(Company c:Company.companyList){
            if(c.getServer()!=null) {
                prodList.addAll(getProductsFromCompany(c.getName(), c.getServer(),true));
                for(Product p : prodList){
                    if(p.id.equalsIgnoreCase(id))
                        return p;
                }
            }
        }

        return null;
    }

    private List<Product> getProductsFromCompany(String name, Server server, boolean withProps){
        List<Product> listProducts = null;

        try{
            String url = server.URL + "/ws/products";// "http://www.google.com/search?q=mkyong";

            URL baseUri = new URL(url);
            HashMap<String,String> urlParameters = new HashMap<>();
            urlParameters.put("withProps", String.valueOf(withProps));
            URI uri = applyParameters(baseUri, urlParameters);
            HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper objectMapper = new ObjectMapper();
            String json = response.toString();
            listProducts = objectMapper.readValue(json, new TypeReference<List<Product>>(){});

            return listProducts;
        }
        catch (IOException fio){
            System.err.println("IOException");
        }

        return listProducts;
    }

    private URI applyParameters(URL baseUri, HashMap<String,String> urlParameters){
        StringBuilder query = new StringBuilder();
        boolean first = true;

        Iterator it = urlParameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (first) {
                first = false;
            } else {
                query.append("&");
            }

            query.append(pair.getKey()).append("=").append(pair.getValue());
        }

        try {
            return baseUri.toURI().resolve(query.toString());
        }
        catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
}

}




