import java.util.ArrayList;

import ProductObjects.*;

public abstract class Algorithms {

	public double budget;
	public double energyInKW; // user's desired energy
	public double availableSpace; // in square meters
	public double energyInVolts; // an optional field. I may delete it later

	public ProductContainer[] containers; // this is used to evaluate products

	/*
	 * ViableProductLists will contain all products that have the potential to
	 * be used in a system, it will be used to experiment with viable systems
	 * and pass them into the systemCreator
	 */
	public ArrayList<Panel> viablePanels;
	public ArrayList<Inverter> viableInverters;
	public ArrayList<Racking> viableRacks;
	public ArrayList<Battery> viableBatteries;
	public ArrayList<BatteryController> viableBatteryControllers;
	public ArrayList<BatteryMeter> viableBatteryMeters;
	public ArrayList<DCACDisconnect> viableDCACDisconnects;
	public ArrayList<BatteryWire> viableBatteryWires;
	public ArrayList<PVWire> viablePVWires;

	public Algorithms(double budget, double energyInKW, double availableSpace,
			ProductContainer[] containers) {
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.containers = containers;
	}

	// constructor called when the user wishes to have a specific voltage
	public Algorithms(double budget, double energyInKW, double availableSpace,
			double energyInVolts, ProductContainer[] containers) {
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = energyInVolts;
		this.containers = containers;
	}

	// looks for panels that might work in the system
	public abstract void findViablePanels();

	// looks for inverters that might work in the system
	public void findViableInverters() {

	}

	// looks for racks that might work in the system
	public void findViableRacks() {

	}

	// looks for batteries that might work in the system
	public void findViableBatteries() {

	}

	// looks for battery controllers that might work in the system
	public void findViableBatteryControllers() {

	}

	// looks for battery meters that might work in the system
	public void findViableBatteryMeters() {

	}

	// looks for DCACDisconnect that might work in the system
	public void findViableDCACDisconnet() {

	}

	// looks for battery wires that might work in the system
	public void findViableBatteryWires() {

	}

	// looks for PVWires that might work in the system
	public void findViablePVWires() {

	}

}