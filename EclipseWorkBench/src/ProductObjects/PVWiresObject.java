package ProductObjects;

/* It should be noted that the PVWires table in the PVModels
 * database has an extra column called "CableType". Please 
 * ignore this, as it was an attempt to lump the battery and
 * PV cables together, but was too complicated (and you 
 * cannot delete rows from sqlite tables)
 */

public class PVWiresObject {
	public String name;
	public double price;
	public int lengthInFeet;

	public PVWiresObject(String name, double price, int lengthInFeet) {
		this.name = name;
		this.price = price;
		this.lengthInFeet = lengthInFeet;
	}
}
