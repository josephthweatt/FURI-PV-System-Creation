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
		SystemManager sysMan = new SystemManager(
				(new PowerInfoMain()).new Location(33.4294, 111.9431));

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

	// an object to store the location, which can either be an address (String)
	// or coordinates (Double). Note that the PVWatts class will try to use the
	// address string before the coordinates
	public class Location {
		private String address;
		private double latitude, longitude;

		public Location() {
		}

		// takes either an address or coordinates. If coordinates, submit as two
		// doubles (lat, long). If an address, submit as a url-ready String
		public Location(Object... location) {
			setLocation(location);
		}

		public void setLocation(Object... location) {
			if (location[0] instanceof Double) { // coordinates
				if ((Double) location[0] > 90) {
					System.out.println("latitude is too big");
					System.exit(0);
				} else if ((Double) location[1] > 180) {
					System.out.println("longitude is too big");
					System.exit(0);
				}

				latitude = (Double) location[0];
				longitude = (Double) location[1];
				address = null;
			} else if (location[0] instanceof String) { // string address
				address = (String) location[0];
				latitude = longitude = 0;
			} else {
				System.out.println("Invalid Objects given to setLocation");
			}
		}

		public double[] getCoordinates() {
			double[] coordinates = { latitude, longitude };
			return coordinates;
		}

		public String getAddress() {
			return address;
		}
	}
}
