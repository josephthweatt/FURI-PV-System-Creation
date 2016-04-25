package com.example.family.furi.ProductObjects;

public class Panel {
	public String name;
	public double price;
	public int systemCap; // this is in Watts
	public double amps;
	public double volts;
	public int powerTolerance;
	public int moduleType;
	public String dimensions;

	public double heightInInches;
	public double widthInInches;
	public double areaInMeters; // the area of the panel
	public int panelCount = 1; // panels needed for the system
	private double estimatedEnergyPerPanel;

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
		getDimensions();

		dimensionsToMeters();
	}

	/*
	 * This method takes the first two integers within the dimensions case (they
	 * are separated by '/') and multiplies them to get the Panel's square
	 * meters. This is passed in as the PVWatts 'size' parameter
	 */
	public double dimensionsToMeters() {
		double heightMeters = heightInInches / 39.370;
		double widthMeters = widthInInches / 39.370;

		return areaInMeters = heightMeters * widthMeters;
	}

	/*
	 * gives an estimate of how much energy one can expect per panel by using
	 * the panels efficiency and systemCapacity. This is used when trying to
	 * estimate the Panels performance, and does not account for factors like
	 * location and other parts of the system. This returns energy in KWatts
	 */
	public double estimatedEnergyPerPanel() {
		if (estimatedEnergyPerPanel < 0) {
			return estimatedEnergyPerPanel;
		}
		// if est. EnergyPerPanel hasn't been found yet, we find it here...
		double wattToKW = .001;
		estimatedEnergyPerPanel = systemCap * wattToKW;
		// the returned estimated energy will factor in the powerTolerance,
		// which typically equates to percent loss on the panel
		return estimatedEnergyPerPanel -= estimatedEnergyPerPanel
				* (.01 * powerTolerance);
	}

	private void getDimensions() {
		String[] dims = dimensions.split("/");
		if (heightInInches == 0) {
			heightInInches = Double.parseDouble(dims[0]);
		}
		if (widthInInches == 0) {
			widthInInches = Double.parseDouble(dims[1]);
		}
	}
}