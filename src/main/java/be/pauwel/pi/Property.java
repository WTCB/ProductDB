package be.pauwel.pi;

import java.util.ArrayList;
import java.util.List;

public class Property {
	
	public static List<Property> propList = new ArrayList<Property>();
	
	public String id;
	public String name;
	public String unit;
	public String value;
	public String category;
	public String externalDocument;

	public Property() {
		propList.add(this);
	}
	

	public Property(String id, String name, String unit, String value, String category, String externalDocument) {
		propList.add(this);
		this.id = id;
		this.name = name;
		this.unit = unit;
		this.value = value;
		this.category = category;
		this.externalDocument = externalDocument;
	}

}
