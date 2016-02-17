package ProductObjects;

public class BatteryWire implements java.io.Serializable {
	public String name;
	public double price;
	public int lengthInFeet;
	public String gauge;
	
	// for nonspecific initialization
	public BatteryWire() {
	}
	
	// standard constructor
	public BatteryWire (String name, double price, int lengthInFeet,
			String gauge) {
		this.name = name;
		this.price = price;
		this.lengthInFeet = lengthInFeet;
		this.gauge = gauge;
	}
}
