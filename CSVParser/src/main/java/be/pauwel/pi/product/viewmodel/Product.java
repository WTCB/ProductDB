package be.pauwel.pi.product.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class Product {
	
	public static List<Product> prodList = new ArrayList<Product>();

	public Company company; //bedrijf
	
	public String id;
	public String nameNL;
	public String nameFR;
	public String nameDE;
	public String nameEN;
	
	//public String family;
	//public String group;
	public String domain; //onderbouw, bovenbouw, ...	
	public String publicationDate;
	public String edition; //1
	public String typeNL;
	public String typeFR;
	public String qualityLabel; //keurmerk
	public String brand; //X-model
	public String Image;
	
//	public String artikelNummer;
//	//public String oudArtikelNummer;
//	//public String status;
//	public String naamNL;//PIBNaamNL;
//	public String naamFR;//PIBNaamFR;
//	public String naamDE;
//	public String naamEN;
//	public String omschrijvingNL;//PIBOndertitelNL;
//	public String omschrijvingFR;//PIBOndertitelFR;
//	public String brand;
//	public String samenstellingNL;
//	public String samenstellingFR;
//	//public String benamingPrijslijstNL;
//	//public String benamingPrijslijstFR;
//	public String productnorm;
//	public String typeKort;
//	public String typeLangNL;
//	public String typeLangFR;
//	public String reactieBijBrand;
//	public String reactieBijBrandNorm;
//	public String DOPNummer;
//	public String kantvorm;
//	public String dikte;
//	public String breedte;
//	public String lengte;
//	public String hoogte;
//	public String dikteIsolatie;
//	public String totaleDikte;
//	public String oppervlaktegewicht;
//	public String thermischeGeleidbaarheidl;
//	public String thermischeGeleidbaarheidIsolatiel;
//	public String lineaireUitzettingsCoefficientTgvTemp;
//	public String lineaireUitzettingTempVWNL;
//	public String lineaireUitzettingTempVWFR;
//	public String waterdampdiffusieWeerstandsgetalMu;
//	public String waterdampdiffusieWeerstandsgetalIsolatieMu;
//	public String waterdampdiffusieWeerstandsgetalMud;
//	public String lineaireUitzettingsCoefficientTgvRV;
//	public String lineaireUitzettingVochtVWNL;
//	public String lineaireUitzettingVochtVWFR;
//	public String basishoeveelheidseenheid;
//	public String EAN;
//	public String EANEenheid;
//	public String intrastatNummer;
//	public String DOPURLNL;
//	public String DOPURLFR;
//	public String PIBURLNL;
//	public String PIBURLFR;
//	public String Image;
	
	public ArrayList<Property> properties = new ArrayList<Property>();
	
	public Product() {
		prodList.add(this);
	}

}
