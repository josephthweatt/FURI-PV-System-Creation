import java.util.ArrayList;
import java.util.Map;

import ProductObjects.*;

public class Pricing extends Algorithms {

	public Pricing(Goal goal, ProductContainer[] containers) {
		super(goal, containers);
	}

	@Override
	public void runAlgorithm() {
		super.runAlgorithm();
		FullSystem tempSystem;
		// sets default system wires to the default wires, which are the
		// cheapest of the wires in the database
		system = new FullSystem();
		system.addProduct(viablePVWires.get(0));
		system.addProduct(viableBatteryWires.get(0));
		system.addProduct(viableBatteryMeters.get(0));

		// this for loop will gather all available systems to viableSystems
		for (int i = 0; i < viablePanels.size(); i++) {
			// find each panels optimal rack
			findBestRack(viablePanels.get(i));

			// calling this method creates a chain of similar methods to get
			// systems of all combinations for this panel
			findMatchingDCACDisconnects(viablePanels.get(i));
		}
		if (viableSystems.size() < 1) {
			parameters.noViableSystems();
		} else {
			final int HOURS_PER_YEAR = 8760; // assumes non-leap year
			// loop through systems, check that they satisfy user's parameters
			for (int i = 0; i < viableSystems.size(); i++) {
				if (viableSystems.get(i).findRealPanelArea() <= availableSpace
						&& viableSystems.get(i).calculateCost() <= budget) {
					viableSystems.get(i).calculateLoss();
					viableSystems.get(i).getDataFromAPI(null);
					// checks to see if the system gets enough energy per year
					if (viableSystems.get(i).yearlyEnergy
							/ HOURS_PER_YEAR < energyInKW) {
						viableSystems.remove(i);
					}
				} else {
					viableSystems.remove(i);
				}
			}
			rankSystems();
		}
	}

	private void findBestRack(Panel panel) {
		// looks through the racks (cheapest to priciest). The cheapest
		// rack to fit the panel will be added as the only rack for the panel
		for (int i = 0; i < viableRacks.size(); i++) {
			if (panel.heightInInches <= viableRacks.get(i).width()
					|| panel.widthInInches <= viableRacks.get(i).width()) {
				system.addProduct(viableRacks.get(i));
				break;
			}
		}
		return;
	}

	private void findMatchingDCACDisconnects(Panel panel) {
		for (int i = 0; i < viableDCACDisconnects.size(); i++) {
			if (panel.volts <= viableDCACDisconnects.get(i).volts
					&& panel.amps <= viableDCACDisconnects.get(i).amps) {
				system.addProduct(viableDCACDisconnects.get(i));
				findMatchingBatteryControllers(panel);
			}
		}
	}

	private void findMatchingBatteryControllers(Panel panel) {
		Boolean viableController = false;
		int voltage = 0; // for corresponding amps
		for (int i = 0; i < viableBatteryControllers.size(); i++) {
			// cycle through values of the battery controller amps and voltages
			for (Map.Entry<Integer, ArrayList<Integer>> set : viableBatteryControllers
					.get(i).maxAmps.entrySet()) {
				if (panel.amps <= set.getKey().intValue()) {
					for (int j = 0; j < set.getValue().size(); j++) {
						if (panel.volts <= set.getValue().get(j)) {
							voltage = set.getValue().get(j);
							viableController = true;
							break;
						}
					}
				}
				if (viableController) {
					system.addProduct(viableBatteryControllers.get(i));
					findMatchingInverters(voltage);
					
					viableController = false;
					break;
				}
			}
		}
	}

	private void findMatchingInverters(int voltage) {
		for (int i = 0; i < viableInverters.size(); i++) {
			if (voltage <= viableInverters.get(i).inputV) {
				system.addProduct(viableInverters.get(i));
				findMatchingBatteries(voltage);
			}
		}
	}
	
	// this function will store the complete system to viableSystems
	public void findMatchingBatteries(int voltage) {
		for (int i = 0; i < viableBatteries.size(); i++) {
			// Battery voltage must be under the BatteryController volts
			if (viableBatteries.get(i).voltage <= voltage) {
				// find the amount of batteries needed to supply enough amp
				// hours for one night
				final int NIGHT_HOURS = 9;
				int batteryCount = 1;
				double KWHours = (viableBatteries.get(i).ampHours
						* viableBatteries.get(i).voltage) / 1000;
				// add extra batteries until there's at least enough to cover
				// a nights worth of consistent energy
				while (KWHours < NIGHT_HOURS * energyInKW) {
					batteryCount++;
					KWHours *= 2;
				}
				if (batteryCount * viableBatteries.get(i).price < budget) {
					viableBatteries.get(i).batteryCount = batteryCount;
					system.addProduct(viableBatteries.get(i));
					// we store the system in a list to verify that it works
					viableSystems.add((FullSystem) system.cloneFullSystem());
				}
			}
		}
	}

	@Override
	protected void rankSystems() {
		// uses ProductContainer's QuickSort to sort the systems
		ProductContainer systems = new ProductContainer(FullSystem.class,
				viableSystems.toArray());
		systems.loToHi("cost");
	}

	/**************** FINDVIABLE() METHODS *********************************/

	@Override
	// looks for panels that might work in the system
	protected void findViablePanels() {
		viablePanels = new ArrayList<Panel>();
		Panel panel;
		int panelCount;
		double temp;
		double budget;

		// checks through ALL panels in the database
		for (int i = 0; i < containers[0].products.size(); i++) {
			panel = (Panel) containers[0].products.get(i);
			/**************************** ENERGY *******************************/
			// first we must check how many panels are needed to get the user's
			// desired energy (in KW).
			temp = energyInKW / panel.estimatedEnergyPerPanel();

			// temp is created so that we can decide whether the Panel count
			// should be rounded up. We round up if there is a remainder in
			// temp, because that would indicate that temp holds one less than
			// the amount of Panels needed to satisfy energy requirements.
			if (temp % 1 != 0) {
				panelCount = (int) temp + 1;
			} else {
				panelCount = (int) temp;
			}
			/**************************** DIMENSIONS ***************************/
			// if the amount of panels we need do not fit into our available
			// space, we move on to the next product
			if (panelCount * panel.areaInMeters > availableSpace) {
				continue;
			}

			/***************************** BUDGET ******************************/
			/*******************************************************************
			 * now, we will need to check that the price of all the panels are
			 * significantly lower than the budget (the budget must also account
			 * for other parts of the system). NOTE: This step exists so that
			 * expensive panels don't accidently get added to the viable
			 * products list, it is not a definite estimation of the entire
			 * system's cost.
			 *******************************************************************/
			budget = this.budget;
			budget -= 1500; // average cost of inverter
			budget -= 1500; // average cost of battery
			budget -= 100; // the approx. cost of a cheap battery meter
			budget -= 500; // average cost of battery controller
			budget -= 500; // average cost of DCAC disconnect

			if (panelCount * panel.price > budget) {
				continue;
			}

			panel.panelCount = panelCount;

			// if these three conditions are satisfied, we will add the product
			// to the viablePanels list
			viablePanels.add(panel);
		}
		// if no viable Panels were found in the database, we make note of it
		if (viablePanels.size() == 0) {
			parameters.badParameter("Panels", "budget", "energyInKW",
					"availableSpace");
		}
	}

	@Override
	protected void findViableRacks() {
		viableRacks = new ArrayList<Racking>();
		Racking rack;
		String[] panelDims, rackDims;
		double rackLengthInches;
		double panelHeightInches, panelWidthInches;

		containers[2].loToHi("price"); // racks will be stored according to
										// price

		for (int i = 0; i < containers[2].products.size(); i++) {
			rack = (Racking) containers[2].products.get(i);

			// First checks to see if the racking is made for roof mounting
			if (!rack.roofMounted) {
				continue; // stop evaluating the current rack
			}

			/**************************** DIMENSIONS ***************************/
			// Verify that a rack can span at least one viable panel.
			// We look at both dimensions to see if both sides fits
			for (int j = 0; j < viablePanels.size(); j++) {
				rackDims = rack.sizePerModule.split("X");
				rackLengthInches = Double.parseDouble(
						rackDims[0].substring(0, rackDims[0].indexOf("\"")));

				viablePanels.get(j).dimensions.trim();
				panelDims = viablePanels.get(j).dimensions.split("/");
				panelHeightInches = Double.parseDouble(panelDims[0]);
				panelWidthInches = Double.parseDouble(panelDims[1]);

				// adds the rack to viableRacks and moves to the next rack
				if (rackLengthInches >= panelHeightInches
						|| rackLengthInches >= panelWidthInches) {
					viableRacks.add(rack);
					break;
				}
			}
		}
		// if no viable Racks were found in the database, we make note of it
		if (viableRacks.size() == 0) {
			parameters.badParameter("Racking");
		}
	}

	@Override
	protected void findViableInverters() {
		viableInverters = new ArrayList<Inverter>();
		Inverter inverter;
		Boolean viable;

		for (int i = 0; i < containers[1].products.size(); i++) {
			inverter = (Inverter) containers[1].products.get(i);

			/**************************** ENERGY *******************************/
			// Verify that the inverter can receive as many or more volts as at
			// least one viable Panel
			viable = false;
			for (int j = 0; j < viablePanels.size(); j++) {
				if (inverter.inputV >= viablePanels.get(j).volts) {
					viable = true;
					break;
				}
			}
			if (!viable) {
				continue; // skips to the next product
			}

			// Verify the inverter will output enough energy to meet the user's
			// energy requirement
			if (inverter.watts * 1000 >= energyInKW) {
				viableInverters.add(inverter);
			}
		}
		if (viableInverters.size() == 0) {
			parameters.badParameter("Inverter", "energyInKW");
		}
	}

	@Override
	protected void findViableBatteryControllers() {
		viableBatteryControllers = new ArrayList<BatteryController>();
		BatteryController control;
		int controlHighestVoltage;

		for (int i = 0; i < containers[4].products.size(); i++) {
			control = (BatteryController) containers[4].products.get(i);

			/**************************** ENERGY *******************************/
			// Verify that the controller will support at least one panel
			// voltage
			String[] voltages = control.bankVoltage.split("/");
			controlHighestVoltage = Integer
					.parseInt(voltages[voltages.length - 1]); // highest bank
																// voltage
			for (int j = 0; j < viablePanels.size(); j++) {
				if (controlHighestVoltage >= viablePanels.get(j).volts) {
					viableBatteryControllers.add(control);
					break;
				}
			}
		}
		if (viableBatteryControllers.size() == 0) {
			parameters.badParameter("Battery Controller");
		}
	}

	@Override
	protected void findViableBatteries() {
		viableBatteries = new ArrayList<Battery>();
		Battery battery;
		final int NIGHT_HOURS = 9;
		double KWhours;

		for (int i = 0; i < containers[3].products.size(); i++) {
			battery = (Battery) containers[3].products.get(i);
			KWhours = (battery.ampHours * battery.voltage) / 1000;

			/**************************** ENERGY *******************************/
			// we need to see if the battery will be able to cover the
			// amount of Watt hours used in a night. For more
			// information, see the Progress log entry for 3-23-16
			if (KWhours >= NIGHT_HOURS * energyInKW) {
				viableBatteries.add(battery);
			}
		}
		if (viableBatteries.size() == 0) {
			parameters.badParameter("Batteries", "energyInKW");
		}
	}

	protected void findViableDCACDisconnect() {
		viableDCACDisconnects = new ArrayList<DCACDisconnect>();
		DCACDisconnect disconnect;

		for (int i = 0; i < containers[6].products.size(); i++) {
			disconnect = (DCACDisconnect) containers[6].products.get(i);

			/**************************** ENERGY *******************************/
			// check that disconnect can handle the amps & volts of a system
			for (int j = 0; j < viablePanels.size(); j++) {
				if (disconnect.amps >= viablePanels.get(i).amps) {
					if (disconnect.volts >= viablePanels.get(i).volts) {
						viableDCACDisconnects.add(disconnect);
						break;
					}
				}
			}
		}
		if (viablePanels.size() == 0) {
			parameters.badParameter("DCACDisconnect");
		}
	}
}
