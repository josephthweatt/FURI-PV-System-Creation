package ProductObjects;

public class Panel {
	public String name;
	public double price;
	public int systemCap; // this is in KW
	public double amps;
	public double volts;
	public int powerTolerance;
	public int moduleType;
	public String dimensions;

	public double metersSquared; // found with stringToMeters

	// for nonspecific initialization
	public Panel() {
	}

	public Panel(String name, double price, int systemCap, double amps,
			double volts, int powerTolerance, int moduleType,
			String dimensions) {
		this.name = name;
		this.price = price;
		this.systemCap = systemCap;
		this.amps = amps;
		this.volts = volts;
		this.powerTolerance = powerTolerance;
		this.moduleType = moduleType;
		this.dimensions = dimensions;
	}

	/*
	 * This method takes the first two integers within the dimensions case (they
	 * are separated by '/') and multiplies them to get the Panel's square
	 * meters. This is passed in as the PVWatts 'size' parameter
	 */
	public void stringToMeters() {
		dimensions.trim();
		String[] dims = dimensions.split("/");
		
		// the dimensions string gives us measurements in inches:
		double heightInches = Double.parseDouble(dims[0]);
		double widthInches = Double.parseDouble(dims[1]);
		
		// now we convert it to meters (39.370 inches = 1 meter)
		double heightMeters = heightInches / 39.370;
		double widthMeters = widthInches / 39.370;
		
		metersSquared = heightMeters * widthMeters;
	}
}