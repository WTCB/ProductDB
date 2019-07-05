package be.pauwel.pi.product.services.impl;

import be.pauwel.pi.product.services.ProductService;
import be.pauwel.pi.product.viewmodel.data.*;
import be.pauwel.pi.product.viewmodel.servers.Server;
import org.springframework.stereotype.Service;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product> getAllProductsWithProps() {
        Product.prodList.clear();

        Server server = Company.companyList.get(0).getServer();
        String name = Company.companyList.get(0).getName();

        GetAllProducts(server, name);

        for(Product p : Product.prodList){
            List<Property> propList =
            addProperties(p);
            p.properties.addAll(propList);
            System.out.println("finished Product property : " + p.id);
        }

        return Product.prodList;
    }

    @Override
    public List<Product> getAllProductsWithoutProps() {

        Product.prodList.clear();

        Server server = Company.companyList.get(0).getServer();
        String name = Company.companyList.get(0).getName();

        GetAllProducts(server, name);

        return Product.prodList;
    }

    @Override
    public List<Product> getAllProductsWithProperties(List<Property> properties) {
        //TODO implement this so that this actually searches among properties
        List<Product> prodList = new ArrayList<Product>();

        Server server = Company.companyList.get(0).getServer();
        String name = Company.companyList.get(0).getName();

        GetAllProducts(server, name);

        return prodList;
    }

    @Override
    public Product getProductWithId(String id) {
        Product.prodList.clear();

        Server server = Company.companyList.get(0).getServer();
        String name = Company.companyList.get(0).getName();

        GetAllProducts(server, name);

        //G124584

        for(Product p : Product.prodList){
            if(p.id.equalsIgnoreCase(id))
                return p;
        }

        return null;
    }

    private void GetAllProducts(Server server, String name) {
        try (Session session = server.driver.session()) {

            // Auto-commit transactions are a quick and easy way to wrap a read.
            //WHERE a.name STARTS WITH {x}
            StatementResult result = session.run(
                    "MATCH (a:Subject) <-[hasProduct]- (c:Company { name: '"+name+"' }) " +
                            "RETURN a");

            // Each Cypher execution returns a stream of records.
            while (result.hasNext()) {
                Record record = result.next();
                Node n = record.get(0).asNode();
                //System.out.println(n.keys());
                //System.out.println(n.values());
                Product p = new Product();
                p.names.add(new Name(n.get("nameFR").asString(), "fr"));
                p.names.add(new Name(n.get("nameNL").asString(), "nl"));
                p.id = n.get("id").asString();
                p.intrastatNumber = n.get("intrastatNummer").asString();
                p.publicationDate = n.get("publicationDate").asString();
                p.gtin = new GTIN(n.get("EAN").asString(), n.get("EANEenheid").asString());
                String dopurlnl = n.get("DOPURLNL").asString();
                if (dopurlnl != null && !dopurlnl.isEmpty() && dopurlnl != "null") {
                    String dopurlnlnumber = dopurlnl.split("\\?DOP=")[1].split("_NL.pdf")[0];
                    p.dop = new DOP(dopurlnlnumber);
                    p.dop.URL.add(new Url(dopurlnl, "nl"));
                }
                String dopurlfr = n.get("DOPURLFR").asString();
                if (dopurlfr != null && !dopurlfr.isEmpty() && dopurlfr != "null") {
                    String dopurlfrnumber = dopurlnl.split("\\?DOP=")[1].split("_FR.pdf")[0];
                    p.dop.URL.add(new Url(dopurlfr, "fr"));
                }
                p.images.add(new Image("image", n.get("image").asString()));
                p.edition = n.get("edition").asString();
                p.categories.add("ETIM: Gypsum cardboard board");
                p.categories.add(n.get("typeFR").asString());
                p.categories.add(n.get("typeNL").asString());
                p.typeID = n.get("typeID").asString();
                System.out.println("finished Product : " + p.id);
            }
        }
    }

    private List<Property> addProperties(Product p) {
        List<Property> props = new ArrayList<Property>();

        Server server = Company.companyList.get(0).getServer();
        String name = Company.companyList.get(0).getName();

        try (Session session = server.driver.session()) {
            StatementResult result = session.run(
                    "MATCH (p:Property) <-[hasProperty]-(a:Subject {id:'"+p.id+"'}) RETURN p");
                    //"MATCH (a:Subject) <-[hasProduct]- (c:Company { name: '"+name+"' }) " +
                    //        "RETURN a");
            while (result.hasNext()) {
                Record record = result.next();
                Node n = record.get(0).asNode();
                Property prop = new Property();
                prop.id = n.get("id").asString();
                prop.category = n.get("category").asString();
                String externalDocument = n.get("externalDocument").asString();
                if (externalDocument != null && !externalDocument.isEmpty() && externalDocument != "null") {
                    prop.externalDocument = n.get("externalDocument").asString();
                }
                prop.name = n.get("name").asString();
                String unit = n.get("unit").asString();
                if (unit != null && !unit.isEmpty() && unit != "null") {
                    prop.unit = n.get("unit").asString();
                }
                prop.value = n.get("value").asString();
                props.add(prop);
            }
        }

        p.properties.addAll(props);
        return props;
    }
}




