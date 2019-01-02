package be.pauwel.pi.product.csvloader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.Session;

import be.pauwel.pi.product.csvloader.DataLoader;
import be.pauwel.pi.product.viewmodel.Company;
import be.pauwel.pi.product.viewmodel.Product;
import be.pauwel.pi.product.viewmodel.Property;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import static be.pauwel.pi.product.csvloader.Config.driver;

public class DataLoader {
	

	private static String path;

	public DataLoader() { }

	public static void main(String[] args) {
		
		DataLoader l = new DataLoader();
		Config c = new Config();

		try {
			Config.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Empty database...");
		l.emptyDatabase();

		System.out.println("Parse CSV Gyproc...");
		l.parseGyprocProducten();
		l.cleanGyprocProperties();
		
		//l.parseRensonProducten();

		l.loadSVKProducten();
		//l.loadTIATAB();

		System.out.println("Load into database...");
		l.loadPropertiesInDB();
		System.out.println("Done!");
	}

	private void emptyDatabase(){
		Session session = driver.session();
		String queryString = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
		session.run(queryString);
		session.close();
	}
	
	private void parseGyprocProducten(){		
		BufferedReader br = null;
		String strLine = "";
		String csvSplitBy = ";";
		InputStream in = DataLoader.class.getResourceAsStream("/producten_utf8.csv");

		Company c = new Company();
		c.setName("Gyproc");
		c.setLogo("http://www.bricoleurgyproc.be/sites/all/themes/gyproc/images/svg/logo.svg");
		c.setAddress("Saint-Gobain Construction Products Belgium NV\n" + 
						"Sint-Jansweg 9\n" + 
						"Haven 1602\n" + 
						"9130 Kallo\n" + 
						"Belgium");
		c.setPhone("+32 (0)3 360.22.11");
		c.setMail("info@gyproc.be");
		c.setWebsite("http://www.gyproc.be");

		String[] propertyIds = {"artikelNummer","oudArtikelNummer", "status", "naamNL", "naamFR", "omschrijvingNL",
				"omschrijvingFR", "samenstellingNL", "samenstellingFR", "benamingPrijslijstNL", "benamingPrijslijstFR",
				"productnorm", "typeKort", "typeLangNL", "typeLangFR", "reactieBijBrand", "reactieBijBrandNorm", "DOPNummer"
				, "kantvorm", "dikte", "breedte", "lengte", "dikteIsolatie", "totaleDikte", "oppervlaktegewicht",
				"thermischeGeleidbaarheidl", "thermischeGeleidbaarheidIsolatiel", "lineaireUitzettingsCoefficientTgvTemp",
				"lineaireUitzettingTempVWNL", "lineaireUitzettingTempVWFR", "waterdampdiffusieWeerstandsgetalMu",
				"waterdampdiffusieWeerstandsgetalIsolatieMu", "waterdampdiffusieWeerstand", "lineaireUitzettingsCoefficientTgvRV", "lineaireUitzettingVochtVWNL",
				"lineaireUitzettingVochtVWFR", "basishoeveelheidseenheid", "EAN",
				"EANEenheid", "intrastatNummer", "DOPURLNL", "DOPURLFR", "PIBURLNL", "PIBURLFR", "Image"};
		String[] propertyNames = {"Artikelnummer","Oud artikelnummer", "Status", "Naam (NL)", "Naam (FR)", "Omschrijving (NL)",
				"Omschrijving (FR)", "Samenstelling (NL)", "Samenstelling (FR)", "Benaming op prijslijst (NL)", "Benaming op prijslijst (FR)",
				"Productnorm", "Type kort", "Type lang (NL)", "Type lang (FR)", "Reactie bij brand", "Reactie bij brand norm", "DOP Nummer"
				, "Kantvorm", "Dikte", "Breedte", "Lengte", "Dikte isolatie", "Totale dikte", "Oppervlaktegewicht",
				"Thermische geleidbaarheid l", "Thermische geleidbaarheid isolatie l", "Lineaire uitzettingscoëfficiënt ten gevolge van temperatuur",
				"Lineaire uitzetting Temp VW (NL)", "Lineaire uitzetting Temp VW (FR)", "Waterdampdiffusie weerstandsgetal Mu",
				"Waterdampdiffusie weerstandsgetal isolatie Mu", "Waterdampdiffusie weerstand", "Lineaire uitzettingscoëfficiënt ten gevolge van RV", "Lineaire uitzetting vocht VW (NL)",
				"Lineaire uitzetting vocht VW (FR)", "Basishoeveelheidseenheid", "EAN",
				"EAN-eenheid", "Intrastatnummer", "DOP URL (NL)", "DOP URL (FR)", "PIB URL (NL)", "PIB URL (FR)", "Afbeelding"};
		String[] propertyCategories = {"Identity","Identity", "Identity", "Identity", "Identity", "Identity",
				"Identity", "Form and shape", "Form and shape", "Identity", "Identity",
				"Norm", "Identity", "Identity", "Identity", "Fire Safety", "Fire Safety", "Identity"
				, "Form and shape", "Form and shape", "Form and shape", "Form and shape", "Form and shape", "Form and shape", "Form and shape",
				"Thermal", "Thermal", "Thermal",
				"Thermal", "Thermal", "Hygroscopic",
				"Hygroscopic", "Hygroscopic", "Hygroscopic", "Hygroscopic",
				"Hygroscopic", "Identity", "Identity",
				"Identity", "Identity", "Identity", "Identity", "Identity", "Identity", "Identity"};
		
		br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		try {
			try {
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 0 && i>0) {
						String[] elements = strLine.split(csvSplitBy, -1);	
						if(elements.length==46){
							//System.out.println("Loading 46 properties - OK");
							Product p = new Product();
							p.company = c;
							p.id = "Gyproc"+elements[0];
							p.nameNL = "Gyproc"+elements[0];
							
							for(int j=0;j<=44;j++){
								if(j==1 || j==2 || j==9 || j==10) continue; //skip less relevant fields in gyproc dataset
								Property prop = new Property();							
								prop.name = propertyNames[j];
								prop.value = elements[j];
								prop.category = propertyCategories[j];
								prop.id = propertyIds[j];
								p.properties.add(prop);
							}			
						}
						else
						{
							System.out.println("Error: parseGyprocProducten() - 46 properties expected, but found different number!!!");
						}
					}
					i++;
				}		
			} finally {
					br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseRensonProducten(){
		BufferedReader br = null;
		String strLine = "";
		String csvSplitBy = ";";
		InputStream inattr = DataLoader.class.getResourceAsStream("/renson_export_attributes.csv");
		InputStream inprod = DataLoader.class.getResourceAsStream("/renson_export_products.csv");
		
		br = new BufferedReader(new InputStreamReader(inattr));
		try {
			try {
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 0 && i>0) {
						String[] elements = strLine.split(csvSplitBy, -1);	
						
						//TODO:loadProperties
						
					}
					i++;
				}		
			} finally {
					br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		br = new BufferedReader(new InputStreamReader(inprod));
		try {
			try {
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 0 && i>0) {
						String[] elements = strLine.split(csvSplitBy, -1);	

						Product p = new Product();
						//TODO:loadProperties
					}
					i++;
				}		
			} finally {
					br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void cleanGyprocProperties(){
		for(int i = 0; i<Product.prodList.size();i++){
			Product p = Product.prodList.get(i);
			ArrayList<Property> properties = p.properties;
			ArrayList<Property> toBeDeletedProps = new ArrayList<Property>();

			System.out.println("Cleaning the properties");
			System.out.println("Product id : "+ p.id);

			//DELETE EMPTY PROPERTIES
			for(Property prop : properties){
				if(prop.value.equalsIgnoreCase("(blanco)") || prop.value == null || prop.value.equalsIgnoreCase("")|| prop.value.equalsIgnoreCase("--")|| prop.value.equalsIgnoreCase("(niet op website)")){
					toBeDeletedProps.add(prop);
				}
			}

			for(Property prop : toBeDeletedProps)
				p.properties.remove(prop);

			//Add product attributes
			Property dummy = p.getProperty("naamNL");
			if(dummy!=null) p.nameNL = dummy.value;
			dummy = p.getProperty("naamFR");
			if(dummy!=null) p.nameFR = dummy.value;

			dummy = p.getProperty("artikelNummer");
			if(dummy!=null) p.id = dummy.value;
			dummy = p.getProperty("EAN");
			if(dummy!=null) p.EAN = dummy.value;
			dummy = p.getProperty("EANEenheid");
			if(dummy!=null) p.EANEenheid = dummy.value;
			dummy = p.getProperty("intrastatNummer");
			if(dummy!=null) p.intrastatNummer = dummy.value;

			p.publicationDate = LocalDateTime.now().toString();
			p.edition = "0.1";
			dummy = p.getProperty("image");
			if(dummy!=null) p.image = dummy.value;

			dummy = p.getProperty("omschrijvingNL");
			if(dummy!=null) p.descriptionNL = dummy.value;
			dummy = p.getProperty("omschrijvingFR");
			if(dummy!=null) p.descriptionFR = dummy.value;

			dummy = p.getProperty("typeKort");
			if(dummy!=null) p.typeID = dummy.value;
			dummy = p.getProperty("typeLangNL");
			if(dummy!=null) p.typeNL = dummy.value;
			dummy = p.getProperty("typeLangFR");
			if(dummy!=null) p.typeFR = dummy.value;

			dummy = p.getProperty("DOPNummer");
			if(dummy!=null) p.DOPNumber = dummy.value;
			dummy = p.getProperty("DOPURLNL");
			if(dummy!=null) p.DOPURLNL = dummy.value;
			dummy = p.getProperty("DOPURLFR");
			if(dummy!=null) p.DOPURLFR = dummy.value;
			dummy = p.getProperty("PIBURLNL");
			if(dummy!=null) p.PIBURLNL = dummy.value;
			dummy = p.getProperty("PIBURLFR");
			if(dummy!=null) p.PIBURLFR = dummy.value;

			toBeDeletedProps.add(p.getProperty("naamNL"));
			toBeDeletedProps.add(p.getProperty("naamFR"));
			toBeDeletedProps.add(p.getProperty("artikelNummer"));
			toBeDeletedProps.add(p.getProperty("EAN"));
			toBeDeletedProps.add(p.getProperty("EANEenheid"));
			toBeDeletedProps.add(p.getProperty("intrastatNummer"));
			toBeDeletedProps.add(p.getProperty("omschrijvingNL"));
			toBeDeletedProps.add(p.getProperty("omschrijvingFR"));
			toBeDeletedProps.add(p.getProperty("typeKort"));
			toBeDeletedProps.add(p.getProperty("typeLangNL"));
			toBeDeletedProps.add(p.getProperty("typeLangFR"));
			toBeDeletedProps.add(p.getProperty("image"));
			toBeDeletedProps.add(p.getProperty("DOPNummer"));
			toBeDeletedProps.add(p.getProperty("DOPURLNL"));
			toBeDeletedProps.add(p.getProperty("DOPURLFR"));
			toBeDeletedProps.add(p.getProperty("PIBURLNL"));
			toBeDeletedProps.add(p.getProperty("PIBURLFR"));

			//Add units
			Property dikte = p.getProperty("dikte");
			if(dikte!=null) dikte.unit = "mm";
			Property breedte = p.getProperty("breedte");
			if(breedte!=null) breedte.unit = "mm";
			Property lengte = p.getProperty("lengte");
			if(lengte!=null) lengte.unit = "mm";
			Property hoogte = p.getProperty("hoogte");
			if(hoogte!=null) hoogte.unit = "mm";
			Property dikteIsolatie = p.getProperty("dikteIsolatie");
			if(dikteIsolatie!=null) dikteIsolatie.unit = "mm";
			Property totaleDikte = p.getProperty("totaleDikte");
			if(totaleDikte!=null) totaleDikte.unit = "mm";
			Property oppervlaktegewicht = p.getProperty("oppervlaktegewicht");
			if(oppervlaktegewicht!=null) oppervlaktegewicht.unit = "kg/m²";
			Property thermischeGeleidbaarheidl = p.getProperty("thermischeGeleidbaarheidl");
			if(thermischeGeleidbaarheidl!=null) thermischeGeleidbaarheidl.unit = "W/mK";
			Property thermischeGeleidbaarheidIsolatiel = p.getProperty("thermischeGeleidbaarheidIsolatiel");
			if(thermischeGeleidbaarheidIsolatiel!=null) thermischeGeleidbaarheidIsolatiel.unit = "W/mK";
			Property lineaireUitzettingsCoefficientTgvTemp = p.getProperty("lineaireUitzettingsCoefficientTgvTemp");
			if(lineaireUitzettingsCoefficientTgvTemp!=null) lineaireUitzettingsCoefficientTgvTemp.unit = "1/K";
			Property lineaireUitzettingTempVWNL = p.getProperty("lineaireUitzettingTempVWNL");
			if(lineaireUitzettingTempVWNL!=null) lineaireUitzettingTempVWNL.unit = "1/K";
			Property lineaireUitzettingTempVWFR = p.getProperty("lineaireUitzettingTempVWFR");
			if(lineaireUitzettingTempVWFR!=null) lineaireUitzettingTempVWFR.unit = "1/K";
			Property lineaireUitzettingsCoefficientTgvRV = p.getProperty("lineaireUitzettingsCoefficientTgvRV");
			if(lineaireUitzettingsCoefficientTgvRV!=null) lineaireUitzettingsCoefficientTgvRV.unit = "m/m";
			Property lineaireUitzettingVochtVWNL = p.getProperty("lineaireUitzettingVochtVWNL");
			if(lineaireUitzettingVochtVWNL!=null) lineaireUitzettingVochtVWNL.unit = "m/m";
			Property lineaireUitzettingVochtVWFR = p.getProperty("lineaireUitzettingVochtVWFR");
			if(lineaireUitzettingVochtVWFR!=null) lineaireUitzettingVochtVWFR.unit = "m/m";

			Property reactieBijBrandNorm = p.getProperty("reactieBijBrandNorm");
			Property reactieBijBrand = p.getProperty("reactieBijBrand");
			if(reactieBijBrandNorm!=null && reactieBijBrand!=null)
			{
				reactieBijBrand.externalDocument = reactieBijBrandNorm.value;
				toBeDeletedProps.add(reactieBijBrandNorm);
			}

			//Delete also all the other unneeded properties
			for(Property prop : toBeDeletedProps)
				p.properties.remove(prop);

			for(Property prop : p.properties)
				System.out.println("Property : " + prop.name + " - " + prop.value + " - " + prop.unit);
		}
	}
		
	private void loadSVKProducten(){
		//SVK data
		String[] lengtes = {"210","210","210","190","210","210"};
		String[] breedtes = {"100","100","100","65","65","65"};
		String[] hoogtes = {"40","50","65","48","50","65"};		
		
		Company c = new Company();
		c.setName("SVK");
		c.setLogo("http://www.svk.be/sites/all/themes/svkzen/svk-baseline.png");
		c.setAddress("SVK\n" +
						"Aerschotstraat 114\n" + 
						"9100 Sint-Niklaas\n" +
						"Belgium");
		c.setPhone("+32 (0)3 760 49 00");
		c.setMail("info@svk.be");
		c.setWebsite("http://www.svk.be");
		
		for(int i = 0;i<6;i++){		
			Product p = new Product();
			p.company = c;
			
			p.id = "GSTA170501";
			p.nameNL = "SVK handvormgevelsteen type A";
			p.nameFR = "SVK briques de façades fait main type A";
			p.nameDE = "SVK Handformverblender Typ A";
			p.nameEN = "SVK hand-moulded facing bricks type A";
			p.brand = "Artisan";
			
			p.qualityLabel = "EN 771-1:2011+A1:2015"; //TODO: check!

			p.typeID = "gevelsteen";
			p.typeNL = "gevelsteen";
			p.typeFR = "brique de façades";

			p.DOPNumber = "GSTA170501";
			p.DOPURLNL = "http://www.svk.be/en/downloads/download/file/2795";
			p.DOPURLFR = "http://www.svk.be/en/downloads/download/file/2795";
			
			p.properties.add(new Property("GSTA170501_001", "Tolerantiecategorie", "T1", "", "Form and shape", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_002", "Maatspreidingcategorie", "R1", "", "Form and shape", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_003", "Configuratie", "Vol - met frog", "", "Form and shape", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_004", "Vormmethode", "Handvorm", "", "Form and shape", "EN 771-1:2011+A1:2015"));
			
			p.properties.add(new Property("GSTA170501_005", "Gedeclareerde gemiddelde druksterkte", "15", "N/mm²", "Structural", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_006", "Vochtexpansie", "NPD", "mm/m", "Hygroscopic", "EN 771-1:2011+A1:2015"));		
			p.properties.add(new Property("GSTA170501_007", "Hechtsterkte", "0,15", "N/mm²", "Structural", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_008", "Gehalte actieve oplosbare zouten", "S2", "", "Structural", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_009", "Reactie bij brand", "A1", "", "Fire Safety", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_010", "Wateropslorping", "less or equal than 12%", "", "Hygroscopic", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_011", "Dampdoorlaatbaarheid", "5/10", "", "Hygroscopic", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_012", "Duurzaamheid aan vorst", "F2", "", "Hygroscopic", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_013", "Thermische geleidbaarheid", "1,06", "W/mK", "Thermal", "EN 771-1:2011+A1:2015"));
			
			p.properties.add(new Property("GSTA170501_014", "Hoogte", hoogtes[i], "", "Form and shape", ""));
			p.properties.add(new Property("GSTA170501_015", "Breedte", breedtes[i], "", "Form and shape", ""));
			p.properties.add(new Property("GSTA170501_016", "Lengte", lengtes[i], "", "Form and shape", ""));
		}
	}
	
	private void loadTIATAB(){
		
	}
	
	private void loadPropertiesInDB(){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Config.path + "/query.txt"), "utf-8"));
			
			for(int i = 0; i<Product.prodList.size();i++){
				Product p = Product.prodList.get(i);
				
				String query = "";
				query += "CREATE (n" + p.id+":Subject { ";

				if(p.id == null) System.out.println("Error: The ID of a product should NEVER be null. The resulting query string cannot be correct if it is null.");

				Map<String, Object> params = new HashMap<>();
				if(p.id!=null) {
					query += "id: $pid";
					final String pid = p.id;
					params.put("pid", pid);
				}
				if(p.nameNL!=null){
					query += ", " + "nameNL: $pnameNL";
					final String pnameNL = p.nameNL;
					params.put("pnameNL", pnameNL);
				}
				if(p.nameFR!=null){
					query += ", " + "nameFR: $pnameFR";
					final String pnameFR = p.nameFR;
					params.put("pnameFR", pnameFR);
				}
				if(p.EAN!=null){
					query += ", " + "EAN: $pEAN";
					final String pEAN = p.EAN;
					params.put("pEAN", pEAN);
				}
				if(p.EANEenheid!=null) {
					query += ", " + "EANEenheid: $pEANEenheid";
					final String pEANEenheid = p.EANEenheid;
					params.put("pEANEenheid", pEANEenheid);
				}
				if(p.intrastatNummer!=null) {
					query += ", " + "intrastatNummer: $pintrastatNummer";
					final String pintrastatNummer = p.intrastatNummer;
					params.put("pintrastatNummer", pintrastatNummer);
				}
				if(p.publicationDate!=null) {
					query += ", " + "publicationDate: $ppublicationDate";
					final String ppublicationDate = p.publicationDate;
					params.put("ppublicationDate", ppublicationDate);
				}
				if(p.image!=null) {
					query += ", " + "image: $pimage";
					final String pimage = p.image;
					params.put("pimage", pimage);
				}
				if(p.edition!=null) {
					query += ", " + "edition: $pedition";
					final String pedition = p.edition;
					params.put("pedition", pedition);
				}
				if(p.descriptionNL!=null) {
					query += ", " + "descriptionNL: $pdescriptionNL";
					final String pdescriptionNL = p.descriptionNL;
					params.put("pdescriptionNL", pdescriptionNL);
				}
				if(p.descriptionFR!=null) {
					query += ", " + "descriptionFR: $pdescriptionFR";
					final String pdescriptionFR = p.descriptionFR;
					params.put("pdescriptionFR", pdescriptionFR);
				}
				if(p.typeID!=null) {
					query += ", " + "typeID: $ptypeID";
					final String ptypeID = p.typeID;
					params.put("ptypeID", ptypeID);
				}
				if(p.typeNL!=null) {
					query += ", " + "typeNL: $ptypeNL";
					final String ptypeNL = p.typeNL;
					params.put("ptypeNL", ptypeNL);
				}
				if(p.typeFR!=null) {
					query += ", " + "typeFR: $ptypeFR";
					final String ptypeFR = p.typeFR;
					params.put("ptypeFR", ptypeFR);
				}
				if(p.DOPNumber!=null) {
					query += ", " + "DOPNumber: $pDOPNumber";
					final String pDOPNumber = p.DOPNumber;
					params.put("pDOPNumber", pDOPNumber);
				}
				if(p.DOPURLNL!=null) {
					query += ", " + "DOPURLNL: $pDOPURLNL";
					final String pDOPURLNL = p.DOPURLNL;
					params.put("pDOPURLNL", pDOPURLNL);
				}
				if(p.DOPURLFR!=null) {
					query += ", " + "DOPURLFR: $pDOPURLFR";
					final String pDOPURLFR = p.DOPURLFR;
					params.put("pDOPURLFR", pDOPURLFR);
				}
				if(p.PIBURLNL!=null) {
					query += ", " + "PIBURLNL: $pPIBURLNL";
					final String pPIBURLNL = p.PIBURLNL;
					params.put("pPIBURLNL", pPIBURLNL);
				}
				if(p.PIBURLFR!=null) {
					query += ", " + "PIBURLFR: $pPIBURLFR";
					final String pPIBURLFR = p.PIBURLFR;
					params.put("pPIBURLFR", pPIBURLFR);
				}
					
				query+="})";		
				query+="\r\n";

//				//query += "productnorm: \""+p.productnorm + "\", "; // hasExternalDocument
//				if(p.productnorm!=null && p.productnorm!=""){
//					query+="MERGE (doc:ExternalDocument { name: '"+p.productnorm+"' })";
//					query+="\r\n";
//					query += "CREATE (n" + p.artikelNummer+")-[:hasProductNorm]->(doc)";
//					query+="\r\n";
//				}
				
				query+="MERGE (company:Company { name: '"+p.company.getName()+"', logo: '"+p.company.getLogo()+"', address: '"+p.company.getAddress()+"', phone: '"+p.company.getPhone()+"', mail: '"+p.company.getMail()+"', website: '"+p.company.getWebsite()+"' })";
				query+="\r\n";
				query += "CREATE (company)-[:hasProduct]->(n" + p.id +")";
				query+="\r\n";
				
				for(Property prop : p.properties){
					query += "MERGE (" + prop.id + ":Property { ";

					if(prop.id!=null) {
						query += "id: $" + prop.id+"id";
						final String propid = prop.id;
						params.put(prop.id+"id", propid);
					}
					if(prop.name!=null) {
						query += ", " + "name: $" + prop.id+"name";
						final String propname = prop.name;
						params.put(prop.id+"name", propname);
					}
					if(prop.category!=null) {
						query += ", " + "category: $" + prop.id+"category";
						final String propcategory = prop.category;
						params.put(prop.id+"category", propcategory);
					}
					if(prop.value!=null) {
						query += ", " + "value: $" + prop.id+"value";
						final String propvalue = prop.value;
						params.put(prop.id+"value", propvalue);
					}
					if(prop.unit!=null) {
						query += ", " + "unit: $" + prop.id+"unit";
						final String propunit = prop.unit;
						params.put(prop.id+"unit", propunit);
					}
					if(prop.externalDocument!=null) {
						query += ", " + "externalDocument: $" + prop.id+"externalDocument";
						final String propexternalDocument = prop.externalDocument;
						params.put(prop.id+"externalDocument", propexternalDocument);
					}
					query += " })";

					query += "\r\n";
					query += "CREATE (n" + p.id+")-[:hasProperty]->(" + prop.id + ")";
					query += "\r\n";
				}

				//System.out.println("QUERY GOING TO DB: " + query);

				//Start query
				final String qString = query;

				Session session = driver.session();
				try(Transaction tx = session.beginTransaction())
				{
					tx.run(qString, params);
					tx.success();
				}
				session.close();
				
				query+="\r\n";
				writer.write(query);
				writer.flush();
			}
		
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*private String removeOddCharacters(String x){

		if(x.length()<=0)
			return x;
		
		String returnval = x.replaceAll("'", "");
		returnval = returnval.replaceAll("\"", "");
		returnval = returnval.replaceAll("/", "");
		returnval = returnval.replaceAll("-", "");
		if(returnval.contains("+"))
			returnval = returnval.replaceAll("\\+", "PLUS");
		returnval = returnval.replaceAll("\\(", "");
		returnval = returnval.replaceAll("\\)", "");
		returnval = returnval.replaceAll("&", "");
		returnval = returnval.replaceAll("&", "");
		returnval = returnval.replaceAll("\\.", "");
		returnval = returnval.replaceAll("\\,", "_");
		returnval = returnval.replaceAll("%", "percent");

		returnval = Normalizer.normalize(returnval, Normalizer.Form.NFD);
		returnval = returnval.replaceAll("[^\\p{ASCII}]", "");
		
			char c = x.charAt(0);
			boolean isDigit = (c >= '0' && c <= '9');
			if(isDigit)
				returnval = "_" + returnval;
		
			
		return returnval;
	}*/

}
