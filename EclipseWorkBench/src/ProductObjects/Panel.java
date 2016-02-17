package ProductObjects;

public class Panel implements java.io.Serializable {
	public String name;
	public double price;
	public int systemCap; // this is in KW
	public double amps;
	public double volts;
	public int powerTolerance;
	public int moduleType;
	
	// for nonspecific initialization
	public Panel() {
	}

	public Panel(String name, double price, int systemCap, double amps,
			double volts, int powerTolerance, int moduleType) {
		this.name = name;
		this.price = price;
		this.systemCap = systemCap;
		this.amps = amps;
		this.volts = volts;
		this.powerTolerance = powerTolerance;
		this.moduleType = moduleType;
	}
}