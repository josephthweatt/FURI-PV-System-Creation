package ProductObjects;

public class DCACDisconnect implements java.io.Serializable {
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
