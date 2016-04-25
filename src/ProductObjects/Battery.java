package ProductObjects;

public class Battery {
	public String name;
	public double price;
	public int voltage;
	public int ampHours; // in Ah
	
	public int batteryCount = 1;
	
	// for nonspecific initialization
	public Battery() {
	}
	
	public Battery(String name, double price, 
								int voltage, int ampHours) {
		this.name = name;
		this.price = price;
		this.voltage = voltage;
		this.ampHours = ampHours;
	}
}
