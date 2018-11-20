package be.pauwel.pi.product.csvloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Normalizer;

import org.neo4j.driver.v1.Session;

import be.pauwel.pi.product.csvloader.DataLoader;
import be.pauwel.pi.product.viewmodel.Company;
import be.pauwel.pi.product.viewmodel.Product;
import be.pauwel.pi.product.viewmodel.Property;

public class DataLoader {
	

	private static String path;

	public DataLoader() { }

	public static void main(String[] args) {
		String basePath = new File("").getAbsolutePath();
		path = basePath + "\\src\\main\\resources";
		
		DataLoader l = new DataLoader();
		Config.path = path;
		try {
			Config.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Empty database...");
		l.emptyDatabase();

		System.out.println("Parse CSV Gyproc...");
		l.parseGyprocProducten();
		//l.reconfigureProductProperties();
		
		//l.parseRensonProducten();

		l.loadOtherProducten();
		l.loadTIATAB();

		System.out.println("Load into database...");
		l.loadPropertiesInDB();
		System.out.println("Done!");
	}
	
	private void parseGyprocProducten(){		
		BufferedReader br = null;
		String strLine = "";
		String csvSplitBy = ";";
		InputStream in = DataLoader.class.getResourceAsStream("/producten.csv");		

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

		String[] propertyNames = {"artikelNummer","oudArtikelNummer", "status", "naamNL", "naamFR", "omschrijvingNL", 
				"omschrijvingFR", "samenstellingNL", "samenstellingFR", "benamingPrijslijstNL", "benamingPrijslijstFR", 
				"productnorm", "typeKort", "typeLangNL", "typeLangFR", "reactieBijBrand", "reactieBijBrandNorm", "DOPNummer"
				, "kantvorm", "dikte", "breedte", "lengte", "dikteIsolatie", "totaleDikte", "oppervlaktegewicht", 
				"thermischeGeleidbaarheidl", "thermischeGeleidbaarheidIsolatiel", "lineaireUitzettingsCoefficientTgvTemp", 
				"lineaireUitzettingTempVWNL", "lineaireUitzettingTempVWFR", "waterdampdiffusieWeerstandsgetalMu", 
				"waterdampdiffusieWeerstandsgetalIsolatieMu", "waterdampdiffusieWeerstand", "lineaireUitzettingsCoefficientTgvRV", "lineaireUitzettingVochtVWNL", 
				"lineaireUitzettingVochtVWFR", "basishoeveelheidseenheid", "EAN", 
				"EANEenheid", "intrastatNummer", "DOPURLNL", "DOPURLFR", "PIBURLNL", "PIBURLFR", "Image"};
		
		br = new BufferedReader(new InputStreamReader(in));
		try {
			try {
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 0 && i>0) {
						String[] elements = strLine.split(csvSplitBy, -1);	
						if(elements.length==46){		
							Product p = new Product();
							p.company = c;
							p.id = "Gyproc"+elements[0];
							p.nameNL = "Gyproc"+elements[0];
							
							for(int j=0;j<=44;j++){
								Property prop = new Property();							
								prop.name = propertyNames[j];
								prop.value = elements[j];
								p.properties.add(prop);
							}			
						}
						else
						{
							System.out.println("Error!!");
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
	
//	private void reconfigureProductProperties(){
//		for(int i = 0; i<Product.prodList.size();i++){
//			Product p = Product.prodList.get(i);			
//			
//			if(p.reactieBijBrand!="" && p.reactieBijBrand!=null){
//				Property prop = new Property();
//				prop.id = "reactieBijBrand";
//				prop.name = "Reactie bij brand";
//				//System.out.println(p.reactieBijBrand);
//				prop.value = p.reactieBijBrand;
//				//prop.unit = p.reactieBijBrand;
//				prop.externalDocument = p.reactieBijBrandNorm;
//				prop.category = "Fire Safety";
//				p.properties.add(prop);
//			}
//			
//			if(p.kantvorm!="" && p.kantvorm!=null){
//				Property prop = new Property();
//				prop.id = "kantvorm";
//				prop.name = "Kantvorm";
//				//System.out.println(prop.name + " - " + p.kantvorm);
//				prop.value = p.kantvorm;
//				//prop.unit = p.kantvorm;
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}		
//
//			if(p.dikte!="" && p.dikte!=null){
//				Property prop = new Property();
//				prop.id = "dikte";
//				prop.name = "Dikte";
//				//System.out.println(prop.name + " - " + p.dikte);
//				prop.value = p.dikte;
//				prop.unit = "mm";
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}		
//
//			if(p.breedte!="" && p.breedte!=null){
//				Property prop = new Property();
//				prop.id = "breedte";
//				prop.name = "Breedte";
//				//System.out.println(prop.name + " - " + p.breedte);
//				prop.value = p.breedte;
//				prop.unit = "mm";
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}			
//
//			if(p.lengte!="" && p.lengte!=null){
//				Property prop = new Property();
//				prop.id = "lengte";
//				prop.name = "Lengte";
//				//System.out.println(prop.name + " - " + p.lengte);
//				prop.value = p.lengte;
//				prop.unit = "mm";
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}		
//
//			if(p.dikteIsolatie!="" && p.dikteIsolatie!=null){
//				Property prop = new Property();
//				prop.id = "dikteIsolatie";
//				prop.name = "Dikte isolatie";
//				//System.out.println(prop.name + " - " + p.dikteIsolatie);
//				prop.value = p.dikteIsolatie;
//				prop.unit = "mm";
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}	
//
//			if(p.totaleDikte!="" && p.totaleDikte!=null){
//				Property prop = new Property();
//				prop.id = "totaleDikte";
//				prop.name = "Totale dikte";
//				//System.out.println(prop.name + " - " + p.totaleDikte);
//				prop.value = p.totaleDikte;
//				prop.unit = "mm";
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}	
//
//			if(p.oppervlaktegewicht!="" && p.oppervlaktegewicht!=null){
//				Property prop = new Property();
//				prop.id = "oppervlaktegewicht";
//				prop.name = "Oppervlaktegewicht";
//				//System.out.println(prop.name + " - " + p.oppervlaktegewicht);
//				prop.value = p.oppervlaktegewicht;
//				prop.unit = "kg/m²";
//				prop.category = "Form and Shape";
//				p.properties.add(prop);
//			}	
//
//			if(p.thermischeGeleidbaarheidl!="" && p.thermischeGeleidbaarheidl!=null){
//				Property prop = new Property();
//				prop.id = "thermischeGeleidbaarheidI";
//				prop.name = "Thermische geleidbaarheid I";
//				//System.out.println(prop.name + " - " + p.thermischeGeleidbaarheidl);
//				prop.value = p.thermischeGeleidbaarheidl;
//				prop.unit = "W/mK";
//				prop.category = "Thermal";
//				p.properties.add(prop);
//			}	
//
//			if(p.thermischeGeleidbaarheidIsolatiel!="" && p.thermischeGeleidbaarheidIsolatiel!=null){
//				Property prop = new Property();
//				prop.id = "thermischeGeleidbaarheidIsolatiel";
//				prop.name = "Thermische geleidbaarheid isolatie I";
//				//System.out.println(prop.name + " - " + p.thermischeGeleidbaarheidIsolatiel);
//				prop.value = p.thermischeGeleidbaarheidIsolatiel;
//				prop.unit = "W/mK";
//				prop.category = "Thermal";
//				p.properties.add(prop);
//			}	
//
//			if(p.lineaireUitzettingsCoefficientTgvTemp!="" && p.lineaireUitzettingsCoefficientTgvTemp!=null){
//				Property prop = new Property();
//				prop.id = "lineaireUitzettingsCoefficientTgvTemp";
//				prop.name = "Lineaire uitzettingscoefficient Tgv Temp";
//				//System.out.println(prop.name + " - " + p.lineaireUitzettingsCoefficientTgvTemp);
//				prop.value = p.lineaireUitzettingsCoefficientTgvTemp;
//				//prop.unit = "1/K";
//				prop.category = "Thermal";
//				p.properties.add(prop);
//			}	
//
//			if(p.lineaireUitzettingTempVWNL!="" && p.lineaireUitzettingTempVWNL!=null){
//				Property prop = new Property();
//				prop.id = "lineaireUitzettingTempVWNL";
//				prop.name = "Lineaire uitzetting temp VW NL";
//				//System.out.println(prop.name + " - " + p.lineaireUitzettingTempVWNL);
//				prop.value = p.lineaireUitzettingTempVWNL;
//				//prop.unit = "1/K";
//				prop.category = "Thermal";
//				p.properties.add(prop);
//			}		
//
//			if(p.lineaireUitzettingTempVWFR!="" && p.lineaireUitzettingTempVWFR!=null){
//				Property prop = new Property();
//				prop.id = "lineaireUitzettingTempVWFR";
//				prop.name = "Lineaire uitzetting temp VW FR";
//				//System.out.println(prop.name + " - " + p.lineaireUitzettingTempVWFR);
//				prop.value = p.lineaireUitzettingTempVWFR;
//				//prop.unit = "1/K";
//				prop.category = "Thermal";
//				p.properties.add(prop);
//			}		
//
//			if(p.waterdampdiffusieWeerstandsgetalMu!="" && p.waterdampdiffusieWeerstandsgetalMu!=null){
//				Property prop = new Property();
//				prop.id = "waterdampdiffusieWeerstandsgetalMu";
//				prop.name = "Waterdampdiffusieweerstandsgetal Mu";
//				//System.out.println(prop.name + " - " + p.waterdampdiffusieWeerstandsgetalMu);
//				prop.value = p.waterdampdiffusieWeerstandsgetalMu;
//				//prop.unit = p.waterdampdiffusieWeerstandsgetalMu;
//				prop.category = "Hygroscopic";
//				p.properties.add(prop);
//			}	
//
//			if(p.waterdampdiffusieWeerstandsgetalIsolatieMu!="" && p.waterdampdiffusieWeerstandsgetalIsolatieMu!=null){
//				Property prop = new Property();
//				prop.id = "waterdampdiffusieWeerstandsgetalIsolatieMu";
//				prop.name = "Waterdampdiffusieweerstandsgetal Isolatie Mu";
//				//System.out.println(prop.name + " - " + p.waterdampdiffusieWeerstandsgetalIsolatieMu);
//				prop.value = p.waterdampdiffusieWeerstandsgetalIsolatieMu;
//				//prop.unit = p.waterdampdiffusieWeerstandsgetalIsolatieMu;
//				prop.category = "Hygroscopic";
//				p.properties.add(prop);
//			}	
//
//			if(p.waterdampdiffusieWeerstandsgetalMud!="" && p.waterdampdiffusieWeerstandsgetalMud!=null){
//				Property prop = new Property();
//				prop.id = "waterdampdiffusieWeerstandsgetalMud";
//				prop.name = "Waterdampdiffusieweerstandsgetal Mud";
//				//System.out.println(prop.name + " - " + p.waterdampdiffusieWeerstandsgetalMud);
//				prop.value = p.waterdampdiffusieWeerstandsgetalMud;
//				//prop.unit = p.waterdampdiffusieWeerstandsgetalMud;
//				prop.category = "Hygroscopic";
//				p.properties.add(prop);
//			}	
//
//			if(p.lineaireUitzettingsCoefficientTgvRV!="" && p.lineaireUitzettingsCoefficientTgvRV!=null){
//				Property prop = new Property();
//				prop.id = "lineaireUitzettingsCoefficientTgvRV";
//				prop.name = "Lineaire uitzettingscoefficient tgv RV";
//				//System.out.println(prop.name + " - " + p.lineaireUitzettingsCoefficientTgvRV);
//				prop.value = p.lineaireUitzettingsCoefficientTgvRV;
//				//prop.unit = p.lineaireUitzettingsCoefficientTgvRV;
//				prop.category = "Hygroscopic";
//				prop.unit = "m/m";
//				p.properties.add(prop);
//			}	
//
//			if(p.lineaireUitzettingVochtVWNL!="" && p.lineaireUitzettingVochtVWNL!=null){
//				Property prop = new Property();
//				prop.id = "lineaireUitzettingVochtVWNL";
//				prop.name = "Lineaire uitzetting vocht VW NL";
//				//System.out.println(prop.name + " - " + p.lineaireUitzettingVochtVWNL);
//				prop.value = p.lineaireUitzettingVochtVWNL;
//				prop.category = "Hygroscopic";
//				//prop.unit = "m/m";
//				p.properties.add(prop);
//			}	
//
//			if(p.lineaireUitzettingVochtVWFR!="" && p.lineaireUitzettingVochtVWFR!=null){
//				Property prop = new Property();
//				prop.id = "lineaireUitzettingVochtVWFR";
//				prop.name = "Lineaire uitzetting vocht VW FR";
//				//System.out.println(prop.name + " - " + p.lineaireUitzettingVochtVWFR);
//				prop.value = p.lineaireUitzettingVochtVWFR;
//				prop.category = "Hygroscopic";
//				//prop.unit = "m/m";
//				p.properties.add(prop);
//			}	
//		}
//	}
		
	private void loadOtherProducten(){
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
			//p.typeKort = "gevelsteen";
			
			p.typeNL = "gevelsteen";
			p.typeFR = "brique de façades";			
			
			p.properties.add(new Property("GSTA170501_001", "Tolerantiecategorie", "T1", "", "Identity", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_002", "Maatspreidingcategorie", "R1", "", "Identity", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_003", "Configuratie", "Vol - met frog", "", "Identity", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_004", "Vormmethode", "Handvorm", "", "Identity", "EN 771-1:2011+A1:2015"));
			
			p.properties.add(new Property("GSTA170501_005", "Gedeclareerde gemiddelde druksterkte", "15", "N/mm²", "Structural", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_006", "Vochtexpansie", "NPD", "mm/m", "Hygroscopic", "EN 771-1:2011+A1:2015"));		
			p.properties.add(new Property("GSTA170501_007", "Hechtsterkte", "0,15", "N/mm²", "Structural", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_008", "Gehalte actieve oplosbare zouten", "S2", "", "Structural", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_009", "Reactie bij brand", "A1", "", "Fire Safety", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_010", "Wateropslorping", "less or equal than 12%", "", "Hygroscopic", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_011", "Dampdoorlaatbaarheid", "5/10", "", "Hygroscopic", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_012", "Duurzaamheid aan vorst", "F2", "", "Hygroscopic", "EN 771-1:2011+A1:2015"));
			p.properties.add(new Property("GSTA170501_013", "Thermische geleidbaarheid", "1,06", "W/mK", "Thermal", "EN 771-1:2011+A1:2015"));
			
			p.properties.add(new Property("GSTA170501_014", "Hoogte", hoogtes[i], "", "Identity", ""));
			p.properties.add(new Property("GSTA170501_015", "Breedte", breedtes[i], "", "Identity", ""));
			p.properties.add(new Property("GSTA170501_016", "lengte", lengtes[i], "", "Identity", ""));

			p.properties.add(new Property("GSTA170501_017", "DOPNummer", "GSTA170501", "", "Identity", ""));
			p.properties.add(new Property("GSTA170501_017", "DOPURLNL", "http://www.svk.be/en/downloads/download/file/2795", "", "Identity", ""));
			p.properties.add(new Property("GSTA170501_017", "DOPURLFR", "http://www.svk.be/en/downloads/download/file/2795", "", "Identity", ""));
		}
	}
	
	private void loadTIATAB(){
		
	}
	
	private void loadPropertiesInDB(){
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "\\query.txt"), "utf-8"));
			
			for(int i = 0; i<Product.prodList.size();i++){
				Product p = Product.prodList.get(i);			
				boolean first = true;
				
				String query = "";
				query += "CREATE (n" + p.id+":Subject { ";
				
					for(int j = 0; j<p.properties.size(); j++){
						Property prop = p.properties.get(j);
						//System.out.println(prop.name);
						
						if(!first) {query+=", ";}
						query += prop.name.replaceAll("\\s+","") + ": \"" + prop.value + "\"";
						first=false;						
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

				
//				for(Property prop : p.properties){
//					if(prop.externalDocument!=null && prop.externalDocument!=""){
//						if(prop.unit!=null && prop.unit!="")
//							query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"', unit: '" + prop.unit + "' , externalDocument: '" + prop.externalDocument + "' })";
//						else
//							query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"', externalDocument: '" + prop.externalDocument + "' })";
//					}
//					else if(prop.unit!=null && prop.unit!="")
//						query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"', unit: '" + prop.unit + "' })";
//					else
//						query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"'})";
//					
//					query += "\r\n";
//					query += "CREATE (n" + p.artikelNummer+")-[:hasProperty]->(" + prop.id + ")";
//					query += "\r\n";					
//				}

				Session session = Config.driver.session();
				session.run(query);
				session.close();
				
				query+="\r\n";
				writer.write(query);
				writer.flush();
			}
		
			writer.flush();
			writer.close();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String removeOddCharacters(String x){	

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
	}
	
	private void emptyDatabase(){
		Session session = Config.driver.session();
		String queryString = "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r";
		session.run(queryString);
		session.close();
	}
}
