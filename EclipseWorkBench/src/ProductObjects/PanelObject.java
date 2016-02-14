package ProductObjects;

public class PanelObject {
	public String name;
	public double price;
	public int systemCap; // this is in KW
	public double amps;
	public double volts;
	public int powerTolerance;
	public int moduleType;

	public PanelObject(String name, double price, int systemCap, double amps,
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