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
	public Location loc;

	public static void main(String[] args) throws IOException, JSONException {

		// Using product container to compute the meters squared of its panels
		SystemManager sysMan = new SystemManager(new Location(33.4294, 111.9431));

		// make a fullsystem testcase
		sysMan.systemCreator.commitProductsToSystem("My System",
				sysMan.getContainer(0).products.get(0),
				sysMan.getContainer(1).products.get(0),
				sysMan.getContainer(2).products.get(0),
				sysMan.getContainer(3).products.get(0),
				sysMan.getContainer(4).products.get(0),
				sysMan.getContainer(5).products.get(0),
				sysMan.getContainer(6).products.get(0),
				sysMan.getContainer(7).products.get(0),
				sysMan.getContainer(8).products.get(0));

		FullSystem fullSys = sysMan.getSystemByName("My System");
		fullSys.panelCount = 5; // say there are five panels in the system

		// display square meters of the system (should be for five panel)
		double meterSquared = fullSys.panel.metersSquared;
		fullSys.findAnnualKWhPerPanel();
		System.out.println("MetersSquared: " + meterSquared + "\nAnnual kwH: "
				+ fullSys.yearlyEnergy + "\nAverage Energy Per Panel: "
				+ fullSys.annualKWhPerPanel);
	}
}
