package ProductObjects;

public class Racking {

	public String name;
	public double price;
	public String sizePerModule;
	public boolean roofMounted;
	
	// for nonspecific initialization
	public Racking() {
	}

	public Racking(String name, double price, String sizePerModule, int roofMounted) {
		this.name = name;
		this.price = price;
		this.sizePerModule = sizePerModule;
		if (roofMounted > 0) {
			this.roofMounted = true;
		} else {
			this.roofMounted = false;
		}
	}
}
