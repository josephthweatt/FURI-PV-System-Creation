package ProductObjects;

public class DCACDisconnectObject {
	public String name;
	public double price;
	public int amps;
	public int volts;

	public DCACDisconnectObject(String name, double price, int amps,
			int volts) {
		this.name = name;
		this.price = price;
		this.amps = amps;
		this.volts = volts;
	}
}
