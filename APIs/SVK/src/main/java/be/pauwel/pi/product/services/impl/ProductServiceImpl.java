package be.pauwel.pi.product.services.impl;

import be.pauwel.pi.product.services.ProductService;
import be.pauwel.pi.product.viewmodel.data.*;
import be.pauwel.pi.product.viewmodel.servers.Server;
import org.springframework.stereotype.Service;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.*;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product> getAllProducts() {

        Product.prodList.clear();

        Server server = Company.companyList.get(0).getServer();
        String name = Company.companyList.get(0).getName();

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
            }
        }

        return Product.prodList;
    }

    /*private void getProductsFromCompany(String name, Server server){
        //TODO: enable retrieving products from other companies
        if(!name.equalsIgnoreCase("Gyproc"))
            return;

        if(server.theDBType.name.equalsIgnoreCase("neo4j")){

        }
    }*/
}




