import java.io.IOException;
import java.util.HashMap;

import ProductObjects.BatteryWire;

/* 
 * Current Version created 3-24-16
 * A test case for the findViable() methods in the Pricing class
 */

public class PowerInfoMain {
	// we use Tempe's coordinates for the test case
	public Location loc;

	public static void main(String[] args) throws IOException {

		// Using product container to compute the meters squared of its panels
		SystemManager sysMan = new SystemManager(
				new Location(33.4294, 111.9431));
		
		// budget: 9000 desired energy: .5KW available space: 30 m^2
		Algorithms alg = new Pricing(9000.0, .5, 30.0, sysMan.getContainers());
		// NOTE: this method should always be the first FindViable() called

		// verify there was no issue with the parameters
		if (alg.impossibleParameters()) {
			System.exit(0);
		}

		// print a few products to see if everything's working
		System.out.println("Panels: ");
		for (int i = 0; i < alg.viablePanels.size(); i++) {
			System.out.println(alg.viablePanels.get(i).name + " | "
					+ alg.viablePanels.get(i).price);
		}
		System.out.println("Batteries: ");
		for (int i = 0; i < alg.viableBatteries.size(); i++) {
			System.out.println(alg.viableBatteries.get(i).name + " | "
					+ alg.viableBatteries.get(i).price);
		}
		System.out.println("Inverters: ");
		for (int i = 0; i < alg.viableInverters.size(); i++) {
			System.out.println(alg.viableInverters.get(i).name + " | "
					+ alg.viableInverters.get(i).price);
		}
		// from non abstract methods
		System.out.println("BatteryWires: " + alg.viableBatteryWires.get(0).name
				+ " | " + alg.viableBatteryWires.get(0).price);
		System.out
				.println("BatteryMeter: " + alg.viableBatteryMeters.get(0).name
						+ " | " + alg.viableBatteryMeters.get(0).price);
		System.out.println("PVWires: " + alg.viablePVWires.get(0).name + " | "
				+ alg.viablePVWires.get(0).price);
	}
}
