package be.pauwel.pi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

import org.neo4j.driver.v1.Session;

import be.pauwel.pi.DataLoader;

public class DataLoader {
	
	private static String path = "C:\\Users\\pipauwel\\workspace\\ProdDB_DataLoader_Gyproc\\src\\main\\resources";

	public DataLoader() { }

	public static void main(String[] args) {
		DataLoader l = new DataLoader();
		Config.path = path;
		try {
			Config.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		l.emptyDatabase();
		
		l.parseGyprocProducten();
		l.reconfigureProductProperties();
		
		l.parseRensonProducten();
		
		//l.loadOtherProducten();
		l.loadTIATAB();
		
		l.loadPropertiesInDB();
	}
	
	private void parseGyprocProducten(){		
		BufferedReader br = null;
		String strLine = "";
		String csvSplitBy = ";";
		InputStream in = DataLoader.class.getResourceAsStream("/producten.csv");
		
		br = new BufferedReader(new InputStreamReader(in));
		try {
			try {
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 0 && i>0) {
						String[] elements = strLine.split(csvSplitBy, -1);	
						if(elements.length==46){		
							Product p = new Product();
							p.bedrijf = "Gyproc";
							
//							String nameWithoutViolatingChars = removeOddCharacters(elements[0]);
//							String nameWithoutSpaces = nameWithoutViolatingChars.replaceAll(" ", "_");	
							if(!elements[0].equalsIgnoreCase("--") && !elements[0].equalsIgnoreCase("(blanco)") && !elements[0].equalsIgnoreCase(""))
								p.artikelNummer = elements[0];
//							if(!elements[1].equalsIgnoreCase("--") && !elements[1].equalsIgnoreCase("(blanco)") && !elements[1].equalsIgnoreCase(""))
//								p.oudArtikelNummer = elements[1];
//							if(!elements[2].equalsIgnoreCase("--") && !elements[2].equalsIgnoreCase("(blanco)") && !elements[2].equalsIgnoreCase(""))
//								p.status = elements[2];
							if(!elements[3].equalsIgnoreCase("--") && !elements[3].equalsIgnoreCase("(blanco)") && !elements[3].equalsIgnoreCase(""))
								p.naamNL = elements[3]; //PIBNaamNL
							if(!elements[4].equalsIgnoreCase("--") && !elements[4].equalsIgnoreCase("(blanco)") && !elements[4].equalsIgnoreCase(""))
								p.naamFR = elements[4]; //PIBNaamFR
							if(!elements[5].equalsIgnoreCase("--") && !elements[5].equalsIgnoreCase("(blanco)") && !elements[5].equalsIgnoreCase(""))
								p.omschrijvingNL = elements[5]; //PIBOntertitelNL
							if(!elements[6].equalsIgnoreCase("--") && !elements[6].equalsIgnoreCase("(blanco)") && !elements[6].equalsIgnoreCase(""))
								p.omschrijvingFR = elements[6]; //PIBOndertitelFR
							if(!elements[7].equalsIgnoreCase("--") && !elements[7].equalsIgnoreCase("(blanco)") && !elements[7].equalsIgnoreCase(""))
								p.samenstellingNL = elements[7];
							if(!elements[8].equalsIgnoreCase("--") && !elements[8].equalsIgnoreCase("(blanco)") && !elements[8].equalsIgnoreCase(""))
								p.samenstellingFR = elements[8];
//							if(!elements[9].equalsIgnoreCase("--") && !elements[9].equalsIgnoreCase("(blanco)") && !elements[9].equalsIgnoreCase(""))
//								p.benamingPrijslijstNL = elements[9];
//							if(!elements[10].equalsIgnoreCase("--") && !elements[10].equalsIgnoreCase("(blanco)") && !elements[10].equalsIgnoreCase(""))
//								p.benamingPrijslijstFR = elements[10];
							if(!elements[11].equalsIgnoreCase("--") && !elements[11].equalsIgnoreCase("(blanco)") && !elements[11].equalsIgnoreCase(""))
								p.productnorm = elements[11];
							if(!elements[12].equalsIgnoreCase("--") && !elements[12].equalsIgnoreCase("(blanco)") && !elements[12].equalsIgnoreCase(""))
								p.typeKort = elements[12];
							if(!elements[13].equalsIgnoreCase("--") && !elements[13].equalsIgnoreCase("(blanco)") && !elements[13].equalsIgnoreCase(""))
								p.typeLangNL = elements[13];
							if(!elements[14].equalsIgnoreCase("--") && !elements[14].equalsIgnoreCase("(blanco)") && !elements[14].equalsIgnoreCase(""))
								p.typeLangFR = elements[14];
							if(!elements[15].equalsIgnoreCase("--") && !elements[15].equalsIgnoreCase("(blanco)") && !elements[15].equalsIgnoreCase(""))
								p.reactieBijBrand = elements[15];
							if(!elements[16].equalsIgnoreCase("--") && !elements[16].equalsIgnoreCase("(blanco)") && !elements[16].equalsIgnoreCase(""))
								p.reactieBijBrandNorm = elements[16];
							if(!elements[17].equalsIgnoreCase("--") && !elements[17].equalsIgnoreCase("(blanco)") && !elements[17].equalsIgnoreCase(""))
								p.DOPNummer = elements[17];
							if(!elements[18].equalsIgnoreCase("--") && !elements[18].equalsIgnoreCase("(blanco)") && !elements[18].equalsIgnoreCase(""))
								p.kantvorm = elements[18];
							if(!elements[19].equalsIgnoreCase("--") && !elements[19].equalsIgnoreCase("(blanco)") && !elements[19].equalsIgnoreCase(""))
								p.dikte = elements[19];
							if(!elements[20].equalsIgnoreCase("--") && !elements[20].equalsIgnoreCase("(blanco)") && !elements[20].equalsIgnoreCase(""))
								p.breedte = elements[20];
							if(!elements[21].equalsIgnoreCase("--") && !elements[21].equalsIgnoreCase("(blanco)") && !elements[21].equalsIgnoreCase(""))
								p.lengte = elements[21];
							if(!elements[22].equalsIgnoreCase("--") && !elements[22].equalsIgnoreCase("(blanco)") && !elements[22].equalsIgnoreCase(""))
								p.dikteIsolatie = elements[22];
							if(!elements[23].equalsIgnoreCase("--") && !elements[23].equalsIgnoreCase("(blanco)") && !elements[23].equalsIgnoreCase(""))
								p.totaleDikte = elements[23];
							if(!elements[24].equalsIgnoreCase("--") && !elements[24].equalsIgnoreCase("(blanco)") && !elements[24].equalsIgnoreCase(""))
								p.oppervlaktegewicht = elements[24];
							if(!elements[25].equalsIgnoreCase("--") && !elements[25].equalsIgnoreCase("(blanco)") && !elements[25].equalsIgnoreCase(""))
								p.thermischeGeleidbaarheidl = elements[25];
							if(!elements[26].equalsIgnoreCase("--") && !elements[26].equalsIgnoreCase("(blanco)") && !elements[26].equalsIgnoreCase(""))
								p.thermischeGeleidbaarheidIsolatiel = elements[26];
							if(!elements[27].equalsIgnoreCase("--") && !elements[27].equalsIgnoreCase("(blanco)") && !elements[27].equalsIgnoreCase(""))
								p.lineaireUitzettingsCoefficientTgvTemp = elements[27];
							if(!elements[28].equalsIgnoreCase("--") && !elements[28].equalsIgnoreCase("(blanco)") && !elements[28].equalsIgnoreCase(""))
								p.lineaireUitzettingTempVWNL = elements[28];
							if(!elements[29].equalsIgnoreCase("--") && !elements[29].equalsIgnoreCase("(blanco)") && !elements[29].equalsIgnoreCase(""))
								p.lineaireUitzettingTempVWFR = elements[29];
							if(!elements[30].equalsIgnoreCase("--") && !elements[30].equalsIgnoreCase("(blanco)") && !elements[30].equalsIgnoreCase(""))
								p.waterdampdiffusieWeerstandsgetalMu = elements[30];
							if(!elements[31].equalsIgnoreCase("--") && !elements[31].equalsIgnoreCase("(blanco)") && !elements[31].equalsIgnoreCase(""))
								p.waterdampdiffusieWeerstandsgetalIsolatieMu = elements[31];
							if(!elements[32].equalsIgnoreCase("--") && !elements[32].equalsIgnoreCase("(blanco)") && !elements[32].equalsIgnoreCase(""))
								p.waterdampdiffusieWeerstandsgetalMud = elements[32];
							if(!elements[33].equalsIgnoreCase("--") && !elements[33].equalsIgnoreCase("(blanco)") && !elements[33].equalsIgnoreCase(""))
								p.lineaireUitzettingsCoefficientTgvRV = elements[33];
							if(!elements[34].equalsIgnoreCase("--") && !elements[34].equalsIgnoreCase("(blanco)") && !elements[34].equalsIgnoreCase(""))
								p.lineaireUitzettingVochtVWNL = elements[34];
							if(!elements[35].equalsIgnoreCase("--") && !elements[35].equalsIgnoreCase("(blanco)") && !elements[35].equalsIgnoreCase(""))
								p.lineaireUitzettingVochtVWFR = elements[35];
							if(!elements[36].equalsIgnoreCase("--") && !elements[36].equalsIgnoreCase("(blanco)") && !elements[36].equalsIgnoreCase(""))
								p.basishoeveelheidseenheid = elements[36];
							if(!elements[37].equalsIgnoreCase("--") && !elements[37].equalsIgnoreCase("(blanco)") && !elements[37].equalsIgnoreCase(""))
								p.EAN = elements[37];
							if(!elements[38].equalsIgnoreCase("--") && !elements[38].equalsIgnoreCase("(blanco)") && !elements[38].equalsIgnoreCase(""))
								p.EANEenheid = elements[38];
							if(!elements[39].equalsIgnoreCase("--") && !elements[39].equalsIgnoreCase("(blanco)") && !elements[39].equalsIgnoreCase(""))
								p.intrastatNummer = elements[39];
							if(!elements[40].equalsIgnoreCase("--") && !elements[40].equalsIgnoreCase("(blanco)") && !elements[40].equalsIgnoreCase(""))
								p.DOPURLNL = elements[40];
							if(!elements[41].equalsIgnoreCase("--") && !elements[41].equalsIgnoreCase("(blanco)") && !elements[41].equalsIgnoreCase(""))
								p.DOPURLFR = elements[41];
							if(!elements[42].equalsIgnoreCase("--") && !elements[42].equalsIgnoreCase("(blanco)") && !elements[42].equalsIgnoreCase(""))
								p.PIBURLNL = elements[42];
							if(!elements[43].equalsIgnoreCase("--") && !elements[43].equalsIgnoreCase("(blanco)") && !elements[43].equalsIgnoreCase(""))
								p.PIBURLFR = elements[43];
							if(!elements[44].equalsIgnoreCase("--") && !elements[44].equalsIgnoreCase("(blanco)") && !elements[44].equalsIgnoreCase(""))
								p.Image = elements[44];							
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
		
	}
	
	private void reconfigureProductProperties(){
		for(int i = 0; i<Product.prodList.size();i++){
			Product p = Product.prodList.get(i);			
			
			if(p.reactieBijBrand!="" && p.reactieBijBrand!=null){
				Property prop = new Property();
				prop.id = "reactieBijBrand";
				prop.name = "Reactie bij brand";
				//System.out.println(p.reactieBijBrand);
				prop.value = p.reactieBijBrand;
				//prop.unit = p.reactieBijBrand;
				prop.externalDocument = p.reactieBijBrandNorm;
				prop.category = "Fire Safety";
				p.properties.add(prop);
			}				
			
			if(p.kantvorm!="" && p.kantvorm!=null){
				Property prop = new Property();
				prop.id = "kantvorm";
				prop.name = "Kantvorm";
				//System.out.println(prop.name + " - " + p.kantvorm);
				prop.value = p.kantvorm;
				//prop.unit = p.kantvorm;
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}		

			if(p.dikte!="" && p.dikte!=null){
				Property prop = new Property();
				prop.id = "dikte";
				prop.name = "Dikte";
				//System.out.println(prop.name + " - " + p.dikte);
				prop.value = p.dikte;
				prop.unit = "mm";
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}		

			if(p.breedte!="" && p.breedte!=null){
				Property prop = new Property();
				prop.id = "breedte";
				prop.name = "Breedte";
				//System.out.println(prop.name + " - " + p.breedte);
				prop.value = p.breedte;
				prop.unit = "mm";
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}			

			if(p.lengte!="" && p.lengte!=null){
				Property prop = new Property();
				prop.id = "lengte";
				prop.name = "Lengte";
				//System.out.println(prop.name + " - " + p.lengte);
				prop.value = p.lengte;
				prop.unit = "mm";
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}		

			if(p.dikteIsolatie!="" && p.dikteIsolatie!=null){
				Property prop = new Property();
				prop.id = "dikteIsolatie";
				prop.name = "Dikte isolatie";
				//System.out.println(prop.name + " - " + p.dikteIsolatie);
				prop.value = p.dikteIsolatie;
				prop.unit = "mm";
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}	

			if(p.totaleDikte!="" && p.totaleDikte!=null){
				Property prop = new Property();
				prop.id = "totaleDikte";
				prop.name = "Totale dikte";
				//System.out.println(prop.name + " - " + p.totaleDikte);
				prop.value = p.totaleDikte;
				prop.unit = "mm";
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}	

			if(p.oppervlaktegewicht!="" && p.oppervlaktegewicht!=null){
				Property prop = new Property();
				prop.id = "oppervlaktegewicht";
				prop.name = "Oppervlaktegewicht";
				//System.out.println(prop.name + " - " + p.oppervlaktegewicht);
				prop.value = p.oppervlaktegewicht;
				prop.unit = "kg/m²";
				prop.category = "Form and Shape";
				p.properties.add(prop);
			}	

			if(p.thermischeGeleidbaarheidl!="" && p.thermischeGeleidbaarheidl!=null){
				Property prop = new Property();
				prop.id = "thermischeGeleidbaarheidI";
				prop.name = "Thermische geleidbaarheid I";
				//System.out.println(prop.name + " - " + p.thermischeGeleidbaarheidl);
				prop.value = p.thermischeGeleidbaarheidl;
				prop.unit = "W/mK";
				prop.category = "Thermal";
				p.properties.add(prop);
			}	

			if(p.thermischeGeleidbaarheidIsolatiel!="" && p.thermischeGeleidbaarheidIsolatiel!=null){
				Property prop = new Property();
				prop.id = "thermischeGeleidbaarheidIsolatiel";
				prop.name = "Thermische geleidbaarheid isolatie I";
				//System.out.println(prop.name + " - " + p.thermischeGeleidbaarheidIsolatiel);
				prop.value = p.thermischeGeleidbaarheidIsolatiel;
				prop.unit = "W/mK";
				prop.category = "Thermal";
				p.properties.add(prop);
			}	

			if(p.lineaireUitzettingsCoefficientTgvTemp!="" && p.lineaireUitzettingsCoefficientTgvTemp!=null){
				Property prop = new Property();
				prop.id = "lineaireUitzettingsCoefficientTgvTemp";
				prop.name = "Lineaire uitzettingscoefficient Tgv Temp";
				//System.out.println(prop.name + " - " + p.lineaireUitzettingsCoefficientTgvTemp);
				prop.value = p.lineaireUitzettingsCoefficientTgvTemp;
				//prop.unit = "1/K";
				prop.category = "Thermal";
				p.properties.add(prop);
			}	

			if(p.lineaireUitzettingTempVWNL!="" && p.lineaireUitzettingTempVWNL!=null){
				Property prop = new Property();
				prop.id = "lineaireUitzettingTempVWNL";
				prop.name = "Lineaire uitzetting temp VW NL";
				//System.out.println(prop.name + " - " + p.lineaireUitzettingTempVWNL);
				prop.value = p.lineaireUitzettingTempVWNL;
				//prop.unit = "1/K";
				prop.category = "Thermal";
				p.properties.add(prop);
			}		

			if(p.lineaireUitzettingTempVWFR!="" && p.lineaireUitzettingTempVWFR!=null){
				Property prop = new Property();
				prop.id = "lineaireUitzettingTempVWFR";
				prop.name = "Lineaire uitzetting temp VW FR";
				//System.out.println(prop.name + " - " + p.lineaireUitzettingTempVWFR);
				prop.value = p.lineaireUitzettingTempVWFR;
				//prop.unit = "1/K";
				prop.category = "Thermal";
				p.properties.add(prop);
			}		

			if(p.waterdampdiffusieWeerstandsgetalMu!="" && p.waterdampdiffusieWeerstandsgetalMu!=null){
				Property prop = new Property();
				prop.id = "waterdampdiffusieWeerstandsgetalMu";
				prop.name = "Waterdampdiffusieweerstandsgetal Mu";
				//System.out.println(prop.name + " - " + p.waterdampdiffusieWeerstandsgetalMu);
				prop.value = p.waterdampdiffusieWeerstandsgetalMu;
				//prop.unit = p.waterdampdiffusieWeerstandsgetalMu;
				prop.category = "Hygroscopic";
				p.properties.add(prop);
			}	

			if(p.waterdampdiffusieWeerstandsgetalIsolatieMu!="" && p.waterdampdiffusieWeerstandsgetalIsolatieMu!=null){
				Property prop = new Property();
				prop.id = "waterdampdiffusieWeerstandsgetalIsolatieMu";
				prop.name = "Waterdampdiffusieweerstandsgetal Isolatie Mu";
				//System.out.println(prop.name + " - " + p.waterdampdiffusieWeerstandsgetalIsolatieMu);
				prop.value = p.waterdampdiffusieWeerstandsgetalIsolatieMu;
				//prop.unit = p.waterdampdiffusieWeerstandsgetalIsolatieMu;
				prop.category = "Hygroscopic";
				p.properties.add(prop);
			}	

			if(p.waterdampdiffusieWeerstandsgetalMud!="" && p.waterdampdiffusieWeerstandsgetalMud!=null){
				Property prop = new Property();
				prop.id = "waterdampdiffusieWeerstandsgetalMud";
				prop.name = "Waterdampdiffusieweerstandsgetal Mud";
				//System.out.println(prop.name + " - " + p.waterdampdiffusieWeerstandsgetalMud);
				prop.value = p.waterdampdiffusieWeerstandsgetalMud;
				//prop.unit = p.waterdampdiffusieWeerstandsgetalMud;
				prop.category = "Hygroscopic";
				p.properties.add(prop);
			}	

			if(p.lineaireUitzettingsCoefficientTgvRV!="" && p.lineaireUitzettingsCoefficientTgvRV!=null){
				Property prop = new Property();
				prop.id = "lineaireUitzettingsCoefficientTgvRV";
				prop.name = "Lineaire uitzettingscoefficient tgv RV";
				//System.out.println(prop.name + " - " + p.lineaireUitzettingsCoefficientTgvRV);
				prop.value = p.lineaireUitzettingsCoefficientTgvRV;
				//prop.unit = p.lineaireUitzettingsCoefficientTgvRV;
				prop.category = "Hygroscopic";
				prop.unit = "m/m";
				p.properties.add(prop);
			}	

			if(p.lineaireUitzettingVochtVWNL!="" && p.lineaireUitzettingVochtVWNL!=null){
				Property prop = new Property();
				prop.id = "lineaireUitzettingVochtVWNL";
				prop.name = "Lineaire uitzetting vocht VW NL";
				//System.out.println(prop.name + " - " + p.lineaireUitzettingVochtVWNL);
				prop.value = p.lineaireUitzettingVochtVWNL;
				prop.category = "Hygroscopic";
				//prop.unit = "m/m";
				p.properties.add(prop);
			}	

			if(p.lineaireUitzettingVochtVWFR!="" && p.lineaireUitzettingVochtVWFR!=null){
				Property prop = new Property();
				prop.id = "lineaireUitzettingVochtVWFR";
				prop.name = "Lineaire uitzetting vocht VW FR";
				System.out.println(prop.name + " - " + p.lineaireUitzettingVochtVWFR);
				prop.value = p.lineaireUitzettingVochtVWFR;
				prop.category = "Hygroscopic";
				//prop.unit = "m/m";
				p.properties.add(prop);
			}	
		}
	}
		
	private void loadOtherProducten(){
		//SVK data		
		String[] lengtes = {"210","210","210","190","210","210"};
		String[] breedtes = {"100","100","100","65","65","65"};
		String[] hoogtes = {"40","50","65","48","50","65"};
		
		for(int i = 0;i<6;i++){		
			Product p = new Product();
			p.bedrijf = "SVK";
			
			p.artikelNummer = "GSTA170501";
			p.naamNL = "SVK handvormgevelsteen type A";
			p.naamFR = "SVK briques de façades fait main type A";
			p.naamDE = "SVK Handformverblender Typ A";
			p.naamEN = "SVK hand-moulded facing bricks type A";
			p.brand = "Artisan";
			
			p.productnorm = "EN 771-1:2011+A1:2015";
			//p.typeKort = "gevelsteen";
			p.typeLangNL = "gevelsteen";
			p.typeLangFR = "brique de façades";
			
			p.hoogte = hoogtes[i];
			p.breedte = breedtes[i];
			p.lengte = hoogtes[i];
			
			p.DOPNummer = "GSTA170501";
			
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
			
			p.DOPURLNL = "http://www.svk.be/en/downloads/download/file/2795";
			p.DOPURLFR = "http://www.svk.be/en/downloads/download/file/2795";
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
				query += "CREATE (n" + p.artikelNummer+":Subject { ";
				if(p.artikelNummer!=null){
					if(!first) {query+=", ";}
					query += "artikelNummer: \"" + p.artikelNummer + "\", ";
					query += "name: \"" + p.artikelNummer + "\"";
					first=false;
				}
//				if(p.oudArtikelNummer!=null){
//					if(!first) {query+=", ";}
//					query += "oudArtikelNummer: \""+p.oudArtikelNummer + "\"";
//					first=false;
//				}
//				if(p.status!=null){
//					if(!first) {query+=", ";}
//					query += "status: \""+p.status + "\"";
//					first=false;
//				}
				if(p.naamNL!=null){
					if(!first) {query+=", ";}
					query += "PIBNaamNL: \""+p.naamNL + "\"";
					first=false;
				}
				if(p.naamFR!=null){
					if(!first) {query+=", ";}
					query += "PIBNaamFR: \""+p.naamFR + "\"";
					first=false;
				}
				if(p.omschrijvingNL!=null){
					if(!first) {query+=", ";}
					query += "PIBOndertitelNL: \""+p.omschrijvingNL + "\"";
					first=false;
				}
				if(p.omschrijvingFR!=null){
					if(!first) {query+=", ";}
					query += "PIBOndertitelFR: \""+p.omschrijvingFR + "\"";
					first=false;
				}
				if(p.samenstellingNL!=null){
					if(!first) {query+=", ";}
					query += "samenstellingNL: \""+p.samenstellingNL + "\"";
					first=false;
				}
				if(p.samenstellingFR!=null){
					if(!first) {query+=", ";}
					query += "samenstellingFR: \""+p.samenstellingFR + "\"";
					first=false;
				}
//				if(p.benamingPrijslijstNL!=null){
//					if(!first) {query+=", ";}
//					query += "benamingPrijslijstNL: \""+p.benamingPrijslijstNL + "\"";
//					first=false;
//				}
//				if(p.benamingPrijslijstFR!=null){
//					if(!first) {query+=", ";}
//					query += "benamingPrijslijstFR: \""+p.benamingPrijslijstFR + "\"";
//					first=false;
//				}
				if(p.typeKort!=null){
					if(!first) {query+=", ";}
					query += "typeKort: \""+p.typeKort + "\"";
					first=false;
				}
				if(p.typeLangNL!=null){
					if(!first) {query+=", ";}
					query += "typeLangNL: \""+p.typeLangNL + "\"";
					first=false;
				}
				if(p.typeLangFR!=null){
					if(!first) {query+=", ";}
					query += "typeLangFR: \""+p.typeLangFR + "\"";
					first=false;
				}
				if(p.basishoeveelheidseenheid!=null){
					if(!first) {query+=", ";}
					query += "basishoeveelheidseenheid: \""+p.basishoeveelheidseenheid + "\"";
					first=false;
				}
				if(p.EAN!=null){
					if(!first) {query+=", ";}
					query += "EAN: \""+p.EAN + "\"";
					first=false;
				}
				if(p.EANEenheid!=null){
					if(!first) {query+=", ";}
					query += "EANEenheid: \""+p.EANEenheid + "\"";
					first=false;
				}
				if(p.intrastatNummer!=null){
					if(!first) {query+=", ";}
					query += "intrastatNummer: \""+p.intrastatNummer + "\"";
					first=false;
				}
				if(p.DOPURLNL!=null){
					if(!first) {query+=", ";}
					query += "DOPURLNL: \""+p.DOPURLNL + "\"";
					first=false;
				}
				if(p.DOPURLFR!=null){
					if(!first) {query+=", ";}
					query += "DOPURLFR: \""+p.DOPURLFR + "\"";
					first=false;
				}
				if(p.PIBURLNL!=null){
					if(!first) {query+=", ";}
					query += "PIBURLNL: \""+p.PIBURLNL + "\"";
					first=false;
				}
				if(p.PIBURLFR!=null){
					if(!first) {query+=", ";}
					query += "PIBURLFR: \""+p.PIBURLFR + "\"";
					first=false;
				}
				if(p.Image!=null){
					if(!first) {query+=", ";}
					query += "Image: \""+p.Image + "\"";
					first=false;
				}
				query+="})";		
				query+="\r\n";

				//query += "productnorm: \""+p.productnorm + "\", "; // hasExternalDocument
				if(p.productnorm!=null && p.productnorm!=""){
					query+="MERGE (doc:ExternalDocument { name: '"+p.productnorm+"' })";
					query+="\r\n";
					query += "CREATE (n" + p.artikelNummer+")-[:hasProductNorm]->(doc)";
					query+="\r\n";
				}
				
				query+="MERGE (bedrijf:Company { name: '"+p.bedrijf+"' })";
				query+="\r\n";
				query += "CREATE (bedrijf)-[:hasProduct]->(n" + p.artikelNummer+")";
				query+="\r\n";

				
				for(Property prop : p.properties){
					if(prop.externalDocument!=null && prop.externalDocument!=""){
						if(prop.unit!=null && prop.unit!="")
							query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"', unit: '" + prop.unit + "' , externalDocument: '" + prop.externalDocument + "' })";
						else
							query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"', externalDocument: '" + prop.externalDocument + "' })";
					}
					else if(prop.unit!=null && prop.unit!="")
						query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"', unit: '" + prop.unit + "' })";
					else
						query += "MERGE (" + prop.id + ":Property { name: '"+prop.name+"', inCategory: '"+prop.category+"', value: '"+prop.value+"'})";
					
					query += "\r\n";
					query += "CREATE (n" + p.artikelNummer+")-[:hasProperty]->(" + prop.id + ")";
					query += "\r\n";					
				}

				Session session = Config.driver.session();
				session.run(query);
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
