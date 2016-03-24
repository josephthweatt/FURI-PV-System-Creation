import java.io.IOException;
import org.json.JSONException;

/* 
 * Current Version created 3-24-16
 * A test case for the findViable() methods in the Pricing class
 */

public class PowerInfoMain {
	// we use Tempe's coordinates for the test case
	public Location loc;

	public static void main(String[] args) throws IOException, JSONException {

		// Using product container to compute the meters squared of its panels
		SystemManager sysMan = new SystemManager(
				new Location(33.4294, 111.9431));
		// budget: 6000 desired energy: .5KW available space: 10 m^2
		Algorithms alg = new Pricing(6000.0, .5, 10.0, sysMan.getContainers());
		// NOTE: this method should always be the first FindViable() called
		alg.findViablePanels();
		alg.findViableRacks();
		alg.findViableInverters();
		alg.findViableBatteryControllers();
		alg.findViableBatteries();
		alg.findViableDCACDisconnect();
		alg.findViableBatteryMeters();
		alg.findViablePVWires();
		alg.findViableBatteryWires();

		// print a few products to see if everything's working
		System.out.println("Panels: ");
		for (int i = 0; i < alg.viablePanels.size(); i++) {
			System.out.printf("%10.s|%.2d", alg.viablePanels.get(i).name,
					alg.viablePanels.get(i).price);
		}
		System.out.println("Batteries: ");
		for (int i = 0; i < alg.viableBatteries.size(); i++) {
			System.out.printf("%10.s|%.2d", alg.viableBatteries.get(i).name,
					alg.viableBatteries.get(i).price);
		}
		System.out.println("Inverters: ");
		for (int i = 0; i < alg.viableInverters.size(); i++) {
			System.out.printf("%10.s|%.2d", alg.viableInverters.get(i).name,
					alg.viableInverters.get(i).price);
		}
	}
}
