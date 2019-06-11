package be.pauwel.pi.product.csvloader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.driver.v1.Session;

import be.pauwel.pi.product.viewmodel.Company;
import be.pauwel.pi.product.viewmodel.Product;
import be.pauwel.pi.product.viewmodel.Property;
import org.neo4j.driver.v1.Transaction;

import static be.pauwel.pi.product.csvloader.Config.driver;

public class DataLoader {

	private static String path;

	public DataLoader() { }

	public static void main(String[] args) {
		
		DataLoader l = new DataLoader();
		Config c = new Config();


		/*

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
		//l.createJSON();

		l.loadSVKProducten();
		*/
		
		l.parseRensonProducten();

		//l.loadTIATAB();

		System.out.println("Load into database...");
		//l.loadPropertiesInDB();
		System.out.println("Done!");
	}

	private void createJSON(){
		ObjectMapper mapper = new ObjectMapper();


		try {
			for(int i = 0; i<2;i++) {
				Product p = Product.prodList.get(i);
				mapper.writeValue(new File(Config.path + "/product.json"), p);

				String jsonInString = mapper.writeValueAsString(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Object to JSON in file

		//Object to JSON in String
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
		Map<String, String> propnamesNL = new HashMap<>();
		Map<String, String> propnamesFR = new HashMap<>();

		Map<String, String> propCategories = new HashMap<>();
		Map<String, String> propDataTypes = new HashMap<>();

//		List<String> propertyIds = new ArrayList<String>();
//		List<String> propertyNames = new ArrayList<String>();
//		List<String> propertyCategories = new ArrayList<String>();

		br = new BufferedReader(new InputStreamReader(inattr));
		try {
			try {
				int i = 0;
				while ((strLine = br.readLine()) != null) {
					if (strLine.length() > 0 && i>0) {

						String[] elements = strLine.split(csvSplitBy, -1);
						propnamesNL.put(elements[0],elements[3]);
						propnamesFR.put(elements[0],elements[1]);
						propCategories.put(elements[0],elements[10]);
						String datatype = elements[22].split("pim_catalog_")[1];
						propDataTypes.put(elements[0],elements[22]);

						//System.out.println("Property: " + elements[0] + " - " + elements[3] + " - " + elements[1] + " - " + elements[10] + " - " + datatype);
						

						//String[] elements = strLine.split(csvSplitBy, -1);
						//propnamesNL.put(elements[0,3]);
						//propnamesFR.put(elements[0,1]);
						
						//TODO:loadPropertyNames
						//code;label-fr_BE;label-en_US;label-nl_BE;allowed_extensions;available_locales;
						// date_max;date_min;decimals_allowed;default_metric_unit;group;localizable;
						// max_characters;max_file_size;metric_family;minimum_input_length;negative_allowed;
						// number_max;number_min;reference_data_name;scopable;sort_order;type;unique;
						// useable_as_grid_filter;validation_regexp;validation_rule;wysiwyg_enabled

						//vb: sku;Code unique;SKU;Unieke code;;;;;0;;general;0;;;;0;0;;;;0;0;pim_catalog_identifier;1;1;;;0
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

						//Product p = new Product();
						//TODO:loadData


						//sku;categories;enabled;family;groups;acoustic_insulation;acoustic_retrofit_module;airflow_class;airtight_class;alignment;app;bim;BIM_2015-fr_BE;BIM_2015-nl_BE;BIM_2016-fr_BE;BIM_2016-nl_BE;BIM_2017-fr_BE;BIM_2017-nl_BE;BIM_2018-fr_BE;BIM_2018-nl_BE;blind_angle;blind_height;blind_material;blind_max_strain;blind_shape;blind_step;blind_width;breeze_function;brochure-fr_BE;brochure-nl_BE;buildin_depth;builtin_height;burglar_repellent;cd_coefficient;certificate_warranty;ce_certified;ce_coefficient;cladding_options;clipping_material;clipping_type;clips_material;clips_type;color;compatibel_thickness_window;components_ventilation;connectable;console_type;control;controls_sunprotection;control_industrial_vents;cubic_flow;cubic_flow_max;cubic_flow_text-fr_BE;cubic_flow_text-nl_BE;demand_driven;depth;depth_beugel;depth_case;description-fr_BE;description-nl_BE;description_short-fr_BE;description_short-nl_BE;design;diameter;diameter_attachment_multi;diameter_flow;diameter_variant;diy_application;diy_control;diy_material;diy_shape;domotics;dop_declaration;download_software-fr_BE;download_software-nl_BE;drainage_dry_rooms;dwg_files-fr_BE;dwg_files-nl_BE;ean_code;ecolabel-fr_BE;ecolabel-nl_BE;education_mandatory;electricity_consumption;epb;external_sensors;facet_rank;filling_material;finish-fr_BE;finish-nl_BE;finishing_profile;flow_2pa;flow_limiter;flow_regulation;frame_assessment;frame_height;frame_material;frame_type;frame_width;freestanding;glass_deduction;glass_thickness;glass_thickness_multi;glazing_rubber;heat_recuperation;height;height_case;height_mounting_brackets;height_variant;image_1;image_2;image_3;insect_repellent;install;install_options;install_ss;integrated_ventilator;i_flux_technology;joinable;joinable_ss;Kit-groups;Kit-products;k_factor_exhaust;k_factor_exhaust_without_netting;k_factor_exhaust_with_netting;k_factor_suction;k_factor_suction_without_netting;k_factor_suction_with_netting;leaflet-fr_BE;leaflet-nl_BE;length;length_semidec;manual-fr_BE;manual-nl_BE;max_area;max_area_joined;max_decrease_epeil;max_height;max_height_joined;max_height_screen;max_length;max_length_motorised;max_length_rope;max_length_traverse_installatie;max_protrusion;max_width;max_width_joined;max_width_left;max_width_right;max_width_screen;mechvent_combine;mechvent_control;mechvent_feed;mechvent_installguide-fr_BE;mechvent_installguide-nl_BE;mechvent_measurement_airquality;mechvent_reduction_factor;mechvent_usp_1;mechvent_usp_1_variant;mechvent_usp_2;mechvent_usp_2_variant;mechvent_usp_3;mechvent_usp_3_variant;mechvent_usp_4;mechvent_usp_4_variant;mechvent_usp_else;min_angle;min_height;min_height_screen;min_protrusion;min_width;min_width_engine_excl;min_width_engine_inc;min_width_screen;name-fr_BE;name-nl_BE;net_type;ordercode;outside_cover;packaging-fr_BE;packaging-nl_BE;passage_physical;passage_visual;pollux_filter;preheater;pressure_drop;roof_opening;rooms_to_vent;rooster_kleur_variant;sandwich_panel;screen_acryl;screen_b92;screen_cristal;screen_fiberglass;screen_insect_netting_tuff;screen_isr;screen_polyester;screen_polyester_soltis;screen_polyester_soltis_dark;screen_rensonscreen_dark;screen_satine21154_dark;screen_type;screen_waterproof;self_regulation_class;slide_system;slotheight;smarthome;sound_insulating;sound_insulation_closed;sound_insulation_open;sound_level;specification_text-fr_BE;specification_text-nl_BE;step_control;struc_install;struc_options;struc_position_blinds;sunprotection_application;sunprotection_design;sunp_technology;support_depth;support_material;support_max_strain;support_type;support_width;surface_treatment;systeem_slidefix;system;system_control;tech_drawing-fr_BE;tech_drawing-nl_BE;test_report_2-fr_BE;test_report_2-nl_BE;thermal_interruption;thickness_roof;total_measurement;type_aansluiting;type_blind;type_building;type_concept;type_endura_delta;type_lamel_SS-fr_BE;type_lamel_SS-nl_BE;type_lamel_variant;type_support;type_uitvoering;type_ventilation;type_window_ventilation;usp_1;usp_1_variant;usp_2;usp_2_variant;usp_3;usp_3_variant;usp_4;usp_4_variant;usp_alu_1;usp_alu_1_variant;usp_alu_2;usp_alu_2_variant;usp_alu_3;usp_alu_3_variant;usp_alu_4;usp_alu_4_variant;usp_alu_else;usp_alu_else_variant;usp_diy_1;usp_diy_1_variant;usp_diy_2;usp_diy_2_variant;usp_diy_3;usp_diy_3_variant;usp_diy_4;usp_diy_4_variant;usp_diy_else;usp_diy_else_variant;usp_else;usp_else_variant;usp_gevelbekleding_1;usp_gevelbekleding_2;usp_gevelbekleding_3;usp_gevelbekleding_4;usp_gevelbekleding_else;usp_outdoor_1;usp_outdoor_2;usp_outdoor_3;usp_outdoor_4;usp_outdoor_else;usp_roosters_1;usp_roosters_1_variant;usp_roosters_2;usp_roosters_2_variant;usp_roosters_3;usp_roosters_3_variant;usp_roosters_4;usp_roosters_4_variant;usp_roosters_else;usp_roosters_else_variant;u_value;variant_description-fr_BE;variant_description-nl_BE;variant_description_short-fr_BE;variant_description_short-nl_BE;variant_image_1;variant_image_2;variant_image_3;variant_type_acoustic;variant_type_placement;vent_acoustic_roof;vent_acoustic_window;vent_dimensioning;vent_extra_features;vent_installation;vent_material;vent_roller_shutter;vent_size_type;vent_slotvent;vent_type_variant;vent_usp_1;vent_usp_1_variant;vent_usp_2;vent_usp_2_variant;vent_usp_3;vent_usp_3_variant;vent_usp_4;vent_usp_4_variant;vent_usp_else;vent_usp_else_variant;vent_waterproof_closed;vent_waterproof_open;video-fr_BE;video-nl_BE;warranty;warranty_engines_automation-fr_BE;warranty_engines_automation-nl_BE;warranty_fixscreen_technology;warranty_lacquer-fr_BE;warranty_lacquer-nl_BE;water_resistance_class;width;width_case;width_variant;window_type;wind_resistance;wind_resistance_blackout;wind_resistance_class_EN13561;wind_resistance_freestanding
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
