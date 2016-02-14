package ProductObjects;

public class BatteryWireObject {
	public String name;
	public double price;
	public int lengthInFeet;
	public String gauge;

	public BatteryWireObject(String name, double price, int lengthInFeet,
			String gauge) {
		this.name = name;
		this.price = price;
		this.lengthInFeet = lengthInFeet;
		this.gauge = gauge;
	}
}
