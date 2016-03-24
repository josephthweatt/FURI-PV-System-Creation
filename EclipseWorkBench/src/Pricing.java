import ProductObjects.*;

public class Pricing extends Algorithms {

	public Pricing(double budget, double energyInKW, double availableSpace,
			ProductContainer[] containers) {
		super(budget, energyInKW, availableSpace, containers);
	}

	public Pricing(double budget, double energyInKW, double availableSpace,
			double energyInVolts, ProductContainer[] containers) {
		super(budget, energyInKW, availableSpace, energyInVolts, containers);
	}

	@Override
	// looks for panels that might work in the system
	public void findViablePanels() {
		Panel panel;
		int panelsNeeded;
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
				panelsNeeded = (int) temp + 1;
			} else {
				panelsNeeded = (int) temp;
			}
			/**************************** DIMENSIONS ***************************/
			// if the amount of panels we need do not fit into our available
			// space, we move on to the next product
			if (panelsNeeded * panel.areaInMeters > availableSpace) {
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

			if (panelsNeeded * panel.price > budget) {
				continue;
			}

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
	public void findViableRacks() {
		Racking rack;
		String[] panelDims, rackDims;
		double rackLengthInches;
		double panelHeightInches, panelWidthInches;

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
				rackDims = rack.sizePerModule.split("/");
				rackLengthInches = Double.parseDouble(rackDims[0]);

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
	public void findViableInverters() {
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
			} else {
				viable = false; // resets the validity check
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
	public void findViableBatteryControllers() {
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
	public void findViableBatteries() {
		Battery battery;
		Boolean validVoltage = false;
		final int NIGHT_HOURS = 12;
		double KWhours;

		for (int i = 0; i < containers[3].products
				.size(); i++, validVoltage = false) {
			battery = (Battery) containers[3].products.get(i);
			KWhours = (battery.ampHours * battery.voltage) / 1000;

			/**************************** ENERGY *******************************/
			// check that the battery can handle the system's voltage
			for (int j = 0; j < viablePanels.size(); j++) {
				if (battery.voltage >= viablePanels.get(j).volts) {
					validVoltage = true;
					break;
				}
			}
			// we need to see if the battery will be able to cover the
			// amount of Watt hours used in a night. For more
			// information, see the Progress log entry for 3-23-16
			if (validVoltage) { // makes sure voltage requirements were met
				if (KWhours >= NIGHT_HOURS * energyInKW) {
					viableBatteries.add(battery);
				}
			}
		}
		if (viableBatteries.size() == 0) {
			parameters.badParameter("Batteries", "energyInKW");
		}
	}

	public void findViableDCACDisconnect() {
		DCACDisconnect disconnect;

		for (int i = 0; i < containers[5].products.size(); i++) {
			disconnect = (DCACDisconnect) containers[5].products.get(i);

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
