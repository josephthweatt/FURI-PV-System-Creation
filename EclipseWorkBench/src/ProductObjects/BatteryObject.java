package ProductObjects;

public class BatteryObject {
	public String name;
	public double price;
	public int voltage;
	public int ampHours;
	
	public BatteryObject(String name, double price, 
								int voltage, int ampHours) {
		this.name = name;
		this.price = price;
		this.voltage = voltage;
		this.ampHours = ampHours;
	}
}
