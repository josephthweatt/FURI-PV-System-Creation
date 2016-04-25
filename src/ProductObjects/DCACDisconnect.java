package ProductObjects;

public class DCACDisconnect {
	public String name;
	public double price;
	public int amps;
	public int volts;
	
	// for nonspecific initialization
	public DCACDisconnect() {
	}

	public DCACDisconnect(String name, double price, int amps,
			int volts) {
		this.name = name;
		this.price = price;
		this.amps = amps;
		this.volts = volts;
	}
}
