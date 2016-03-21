import java.lang.reflect.Field;
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

	protected ImpossibleParameters parameters;

	public Algorithms(double budget, double energyInKW, double availableSpace,
			ProductContainer[] containers) {
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.containers = containers;

		parameters = new ImpossibleParameters();
	}

	// constructor called when the user wishes to have a specific voltage
	public Algorithms(double budget, double energyInKW, double availableSpace,
			double energyInVolts, ProductContainer[] containers) {
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = energyInVolts;
		this.containers = containers;

		parameters = new ImpossibleParameters();
	}

	// looks for panels that might work in the system
	public abstract void findViablePanels();

	// looks for racks that might work in the system
	public void findViableRacks() {

	}

	// looks for inverters that might work in the system
	public void findViableInverters() {

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

	// returns 'true' when user parameters cannot generate viable Systems
	public Boolean impossibleParameters() {
		if (parameters.valid == false) {
			return false;
		} else if (parameters.valid == true) {
			System.out.println(
					"Restrictive input found: " + parameters.badParameter);
			return true;
		}
		return false;
	}

	/****************************************************************************
	 * "restrictive". "Restrictive" in this case means that the algorithm cannot
	 * find an acceptable PV system to meet their requirements (e.g. the budget
	 * is too low. available space is too small)
	 ***************************************************************************/
	protected class ImpossibleParameters {
		// 'flase' means that there is at least one restrictive user input
		protected Boolean valid;

		protected String badParameter; // a string to hold the restrictive input

		public ImpossibleParameters() {
			valid = false;
			badParameter = null;
		}

		// method to acknowledge restrictive inputs
		public void badParameter(Field field) {
			valid = true;
			// bad Parameter can store multiple restrictive fields
			if (badParameter == null) {
				badParameter = field.getName();
			} else {
				badParameter += " and " + field.getName();
			}
		}

	}
}