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
	public static Location loc;

	public static void main(String[] args) throws IOException, JSONException {
		// this test method will try to sort all data in the container by name
		SystemManager sysMan = new SystemManager(loc);
		DBExtraction db = new DBExtraction("PVModels.db");
		db.loadAllProducts();
		sysMan.systemCreator.makeContainersWithDB();
		ProductsContainer pc = sysMan.systemCreator.containers[0];
		pc.quicksortHiToLoDouble(pc.products, 0, pc.products.size() - 1,
				"amps");

		// print
		for (int i = 0; i < pc.products.size(); i++) {
			System.out.println(pc.getDoubleFromField("amps", i));
		}
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
