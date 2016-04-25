package com.example.family.furi.ProductObjects;

/* It should be noted that the PVWires table in the PVModels.db
 * database has an extra column called "CableType". Please 
 * ignore this, as it was an attempt to lump the battery and
 * PV cables together, but was too complicated 
 */

public class PVWire {
	public String name;
	public double price;
	public int lengthInFeet;
	
	// for nonspecific initialization
	public PVWire() {
	}

	public PVWire(String name, double price, int lengthInFeet) {
		this.name = name;
		this.price = price;
		this.lengthInFeet = lengthInFeet;
	}
}
