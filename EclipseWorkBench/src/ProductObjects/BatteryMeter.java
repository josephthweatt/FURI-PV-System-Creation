package ProductObjects;

public class BatteryMeter implements java.io.Serializable {
	public String name;
	public double price;
	public String features;
	
	// for nonspecific initialization
	public BatteryMeter() {
	}
	
	public BatteryMeter(String name, double price, String features) {
		this.name = name;
		this.price = price;
		this.features = features;
	}
}
