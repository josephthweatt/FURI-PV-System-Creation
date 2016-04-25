package ProductObjects;

public class Racking {

	public String name;
	public double price;
	public String sizePerModule;
	public boolean roofMounted;
	
	private int widthInInches;

	// for nonspecific initialization
	public Racking() {
	}

	public Racking(String name, double price, String sizePerModule,
			int roofMounted) { 
		this.name = name;
		this.price = price;
		this.sizePerModule = sizePerModule;
		if (roofMounted > 0) {
			this.roofMounted = true;
		} else {
			this.roofMounted = false;
		}
	}
	
	public int width() {
		// assigns width if it hasn't already
		if (widthInInches == 0) {
			widthInInches = Integer.parseInt(sizePerModule.substring(0, 
					sizePerModule.indexOf("\"")));
		}
		return widthInInches;
	}
}
