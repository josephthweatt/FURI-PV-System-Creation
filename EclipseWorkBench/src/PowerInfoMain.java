import java.io.IOException;
import org.json.JSONException;

import ProductObjects.*;

/* Current Version created 2-3-16. 
 * At Present will only display some PV data for a certain location. 
 * In this commit, I will be comparing the results of this API against
 * the real-time data recorded in Macau, China over a given year. 
 * To learn more about the Macau project, see the Reading on Ouput
 * Prediction Methods document in my Research Notes.
 * Next step will be to use the data with research to find what 
 * systems (out of a given database) would work most efficiently.
 */

public class PowerInfoMain {
	// we use Tempe's coordinates for the test case
	public static double latitudeInput = 111.9431;;
	public static double longitudeInput = 33.4294;

	public static void main(String[] args) throws IOException, JSONException {
		// test that FullSystem class works with the api class
		DBExtraction db = new DBExtraction("PVModels.db");
		db.loadAllProducts();

		// create a dummy system
		FullSystem system = db.commitProductsToSystem("newSys", new Panel(),
				new Battery(), new BatteryMeter(), new BatteryController(),
				new Inverter(), new DCACDisconnect(), new Racking(),
				new BatteryWire(), new PVWires());

		// essential assignments for testing PVwatts manager
		system.panel.systemCap = 5; 
		system.loss = 5;
		system.panel.moduleType = 0;

		system.getDataFromAPI(null);
		System.out.println("Yearly AC " + system.yearlyAC);
		System.out.println("Yearly DC " + system.yearlyDC);
		System.exit(0);
	}
}
