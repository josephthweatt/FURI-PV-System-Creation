import java.io.IOException;
import org.json.JSONException;

import ProductObjects.*;

/* Current Version created 3-20-16
 * Needed to fix products container class. The main method is being used to test
 */

public class PowerInfoMain {
	// we use Tempe's coordinates for the test case
	public Location loc;

	public static void main(String[] args) throws IOException, JSONException {

		// Using product container to compute the meters squared of its panels
		SystemManager sysMan = new SystemManager(
				new Location(33.4294, 111.9431));
		sysMan.getContainer(0).loToHi("price");
		for (int i = 0; i < sysMan.getContainer(0).products.size(); i++) {
			System.out.println(
					sysMan.getContainer(0).getStringFromField("name", i));
		}
	}
}
