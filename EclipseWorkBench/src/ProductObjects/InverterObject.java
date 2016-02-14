package ProductObjects;

public class InverterObject {
	public String name;
	public double price;
	public int efficiency;
	public int watts;
	public int inputV; // in volts
	public int outputV; // also in volts

	public InverterObject(String name, double price, int efficiency, int watts,
			int inputV, int outputV) {
		this.name = name;
		this.price = price;
		this.efficiency = efficiency;
		this.watts = watts;
		this.inputV = inputV;
		this.outputV = outputV;
	}
}
