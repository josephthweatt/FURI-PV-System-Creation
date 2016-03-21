import ProductObjects.Panel;

public class Pricing extends Algorithms {

	public Pricing(double budget, double energyInKW, double availableSpace,
			ProductContainer[] containers) {
		super(budget, energyInKW, availableSpace, containers);

	}

	public Pricing(double budget, double energyInKW, double availableSpace,
			double energyInVolts, ProductContainer[] containers) {
		super(budget, energyInKW, availableSpace, energyInVolts, containers);
	}
	
	// looks for panels that might work in the system
	public void findViablePanels() {
		Panel panel;
		int panelsNeeded;
		double temp;
		double budget;

		// checks through ALL panels in the database
		for (int i = 0; i < containers[0].products.size(); i++) {
			panel = (Panel) containers[0].products.get(i);

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

			// if the amount of panels we need do not fit into our available
			// space, we move on to the next product
			if (panelsNeeded * panel.areaInMeters > availableSpace) {
				continue;
			}

			/*
			 * now, we will need to check that the price of all the panels are
			 * significantly lower than the budget (the budget must also account
			 * for other parts of the system). NOTE: This step exists so that
			 * expensive panels don't accidently get added to the viable
			 * products list, it is not a definite estimation of the entire
			 * system's cost.
			 */
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
	}
}
