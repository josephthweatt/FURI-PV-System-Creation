import java.util.ArrayList;

import ProductObjects.*;

public abstract class Algorithms {

	public double budget;
	public double energyInKW; // user's desired energy
	public double availableSpace; // in square meters
	public double energyInVolts; // an optional field. I may delete it later

	protected ProductContainer[] containers; // this is used to evaluate
												// products

	/*
	 * ViableProductLists will contain all products that have the potential to
	 * be used in a system, it will be used to experiment with viable systems
	 * and pass them into the systemCreator
	 */
	protected ArrayList<Panel> viablePanels;
	protected ArrayList<Inverter> viableInverters;
	protected ArrayList<Racking> viableRacks;
	protected ArrayList<Battery> viableBatteries;
	protected ArrayList<BatteryController> viableBatteryControllers;
	protected ArrayList<BatteryMeter> viableBatteryMeters;
	protected ArrayList<DCACDisconnect> viableDCACDisconnects;
	protected ArrayList<BatteryWire> viableBatteryWires;
	protected ArrayList<PVWire> viablePVWires;

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

	/************************* public methods **********************************/

	// have not yet decided if this will return anything
	public void runAlgorithm() {
		// Overriding this method ought to implement 'super' to call findViables
		findViablePanels();
		findViableRacks();
		findViableInverters();
		findViableBatteryControllers();
		findViableBatteries();
		findViableDCACDisconnect();
		findViableBatteryMeters();
		findViablePVWires();
		findViableBatteryWires();

		if (impossibleParameters()) {
			return;
		}
	}

	// returns 'true' when user parameters cannot generate viable Systems
	public Boolean impossibleParameters() {
		if (parameters.valid == false) {
			return false;
		} else if (parameters.valid == true) {
			System.out.println("Restrictive input(s) found: "
					+ parameters.badParameter);
			System.out.println("No viable products found for: "
					+ parameters.restrictedProduct);
			return true;
		}
		return false;
	}

	/********************** private/protected methods **************************/

	// below algorithms look for viable products in the system

	protected abstract void findViablePanels();

	protected abstract void findViableRacks();

	protected abstract void findViableInverters();

	protected abstract void findViableBatteries();

	protected abstract void findViableBatteryControllers();

	protected abstract void findViableDCACDisconnect();

	// the following non-abstract methods will just store the cheapest product
	// to their respective list unless otherwise changed

	protected void findViableBatteryMeters() {
		viableBatteryMeters = new ArrayList<BatteryMeter>();
		containers[5].loToHi("price");
		viableBatteryMeters.add((BatteryMeter) containers[5].products.get(0));
	}

	protected void findViableBatteryWires() {
		viableBatteryWires = new ArrayList<BatteryWire>();
		containers[7].loToHi("price");
		viableBatteryWires.add((BatteryWire) containers[7].products.get(0));
	}

	protected void findViablePVWires() {
		viablePVWires = new ArrayList<PVWire>();
		containers[8].loToHi("price");
		viablePVWires.add((PVWire) containers[8].products.get(0));
	}

	/****************************************************************************
	 * ImpossibleParameters looks for user input that may be considered
	 * "restrictive". "Restrictive" in this case means that the algorithm cannot
	 * find an acceptable PV system to meet their requirements (e.g. the budget
	 * is too low. available space is too small)
	 ***************************************************************************/
	protected class ImpossibleParameters {
		// 'flase' means that there is at least one restrictive user input
		protected Boolean valid;

		protected String badParameter; // a string to hold the restrictive input
		protected String restrictedProduct; // products with no viable options

		public ImpossibleParameters() {
			valid = false;
			badParameter = "";
			restrictedProduct = "";
		}

		// method to acknowledge restrictive inputs
		public void badParameter(String product, String... fieldName) {
			valid = true;
			// bad Parameter can store multiple restrictive fields
			for (int i = 0; i < fieldName.length; i++) {
				if (badParameter.equals("")) {
					badParameter = fieldName[i];
				} else if (badParameter.contains(fieldName[i])) {
					// skip any parameters which have already been added
					continue;
				} else {
					badParameter += " and " + fieldName[i];
				}
			}

			// makes note of which product types had no viable products
			if (restrictedProduct.equals("")) {
				restrictedProduct += product;
			} else {
				restrictedProduct = restrictedProduct.replaceAll(" and ", ", ");
				restrictedProduct += " and " + product;
			}
		}
	}
}