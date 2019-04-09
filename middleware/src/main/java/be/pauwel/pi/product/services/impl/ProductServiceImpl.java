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

    /*@Override
    public List<Product> getAllProducts() {

        try (Session session = Server.serverList.get(0).driver.session())
        {
            // Auto-commit transactions are a quick and easy way to wrap a read.
            //WHERE a.name STARTS WITH {x}
            StatementResult result = session.run(
                    "MATCH (a:Subject) <-[hasProduct]- (c:Company) " +
                            "RETURN a.nameNL as name");


            //<id>:195EAN: 5413218577458EANEenheid: STPIBURLFR: http://www.gyproc.be/downloadpib?PIB=G132294&TYPE=PDFFRPIBURLNL: http://www.gyproc.be/downloadpib?PIB=G132294&TYPE=PDFNLdescriptionFR: Amélioration acoustique dans des pièces existantesdescriptionNL: Akoestische verbetering in bestaande ruimtesedition: 0.1id: G132294image: http://www.gyproc.be/downloadpib?PIB=G132294&TYPE=IMGintrastatNummer: 68091900nameFR: Gyptone® InstantnameNL: Gyptone® InstantpublicationDate: 2019-02-15T16:54:45.374

            //MATCH (wallstreet:Movie { title: 'Wall Street' })<-[:ACTED_IN]-(actor)
            //    RETURN actor.name
            // Each Cypher execution returns a stream of records.
            while (result.hasNext())
            {
                Record record = result.next();
                Product p = new Product();
                System.out.println(record.get("name").asString());
            }
        }

        return Product.prodList;
    }*/

    @Override
    public List<Product> getAllProducts() {

        try (Session session = Server.serverList.get(0).driver.session())
        {
            // Auto-commit transactions are a quick and easy way to wrap a read.
            //WHERE a.name STARTS WITH {x}
            StatementResult result = session.run(
                    "MATCH (a:Subject) <-[hasProduct]- (c:Company { name: 'Gyproc' }) " +
                            "RETURN a");

            // Each Cypher execution returns a stream of records.
            while (result.hasNext())
            {
                Record record = result.next();
                Product p = new Product();
                Node n = record.get(0).asNode();
                System.out.println(n.keys());
                System.out.println(n.values());
                p.names.add(new Name(n.get("nameFR").asString(),"fr"));
                p.names.add(new Name(n.get("nameNL").asString(),"nl"));
                p.id = n.get("id").asString();
                p.intrastatNumber = n.get("intrastatNummer").asString();
                p.publicationDate = n.get("publicationDate").asString();
                p.gtin = new GTIN(n.get("EAN").asString(),n.get("EANEenheid").asString());
                String dopurlnl = n.get("DOPURLNL").asString();
                if(dopurlnl!=null && !dopurlnl.isEmpty() && dopurlnl!="null") {
                    String dopurlnlnumber = dopurlnl.split("\\?DOP=")[1].split("_NL.pdf")[0];
                    p.dop = new DOP(dopurlnlnumber);
                    p.dop.URL.add(new Url(dopurlnl, "nl"));
                }
                String dopurlfr = n.get("DOPURLFR").asString();
                if(dopurlfr!=null && !dopurlfr.isEmpty() && dopurlfr!="null") {
                    String dopurlfrnumber = dopurlnl.split("\\?DOP=")[1].split("_FR.pdf")[0];
                    p.dop.URL.add(new Url(dopurlfr, "fr"));
                }
                p.images.add(new Image("image",n.get("image").asString()));
                p.edition = n.get("edition").asString();
                p.categories.add("ETIM: Gypsum cardboard board");
                p.categories.add(n.get("typeFR").asString());
                p.categories.add(n.get("typeNL").asString());
                p.typeID = n.get("typeID").asString();
            }
        }

        return Product.prodList;
    }
}




