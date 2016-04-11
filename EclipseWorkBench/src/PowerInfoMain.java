import java.io.IOException;
import java.util.HashMap;

import ProductObjects.BatteryWire;

/* 
 * Current Version created 4-10-16
 * first time testing the Pricing Algorithm
 */

public class PowerInfoMain {
	// we use Tempe's coordinates for the test case
	public Location loc;

	public static void main(String[] args) throws IOException {

		// Using product container to compute the meters squared of its panels
		SystemManager sysMan = new SystemManager(
				new Location(33.4294, 111.9431));

		// budget: 9000 desired energy: .5KW available space: 30 m^2
		Goal goal = new Goal("pricing", 9000.0, .5, 30.0);
		sysMan.setGoal(goal);
		sysMan.setSystemsFromAlgorithm();
		
		// print systems
		for (int i = 0; i < ; i++) {
			System.out.println("System #" + i);
			System.out.println("\tCost: " + sysMan.getSystem().cost);
			System.out.println("\tYearly KWH: " + sysMan.getSystem().yearlyEnergy);
			System.out.println("\tSize (m): " + sysMan.getSystem().realPanelArea);
			System.out.println("\tPanel: " + sysMan.getSystem().panel.name);
			System.out.println("\tRack: " sysMan.getSystem().rack.name);
			System.out.println("\tInverter: " + sysMan.getSystem().inverter.name);
			System.out.println("\tBattery: " + sysMan.getSystem().battery.name);
			System.out.println("\tBattery Controller: " + sysMan.getSystem().batteryController.name);
			System.out.println("\tBattery Meter: " + sysMan.getSystem().batteryMeter.name);
			System.out.println("\tDisconnect: " + sysMan.getSystem().dcacDisconnect.name);
			System.out.println("\tPVWire: " + sysMan.getSystem().pvWire.name);
			System.out.println("\tBattery Wire: " + sysMan.getSystem().batteryWire.name);
			System.out.println();
		}
	}
}
