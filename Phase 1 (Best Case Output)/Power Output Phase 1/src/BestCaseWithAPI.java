import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* Current Version created 2-3-16. 
 * At Present will only display some PV data for a certain location. 
 * Next step will be to use the data with research to find what 
 * systems (out of a given database) would work most efficiently.
 */

public class BestCaseWithAPI {
	
	//API structure for PVWatts power prediction api
	final static private String apiSite = "http://developer.nrel.gov/api/pvwatts/";
	final static private String version = "v5.";
	final static private String format = "json?";
	final static private String apiKey = "api_key=" + getPVWattsKey();
	final static private String address = "address=";
	final static private String systemCap = "system_capacity=";
	final static private String moduleTyp = "module_type=";
	final static private String loss = "losses=";
	final static private String arrayTyp = "array_type=";
	final static private String tilt = "tilt=";
	final static private String azimuth = "azimuth=";
	final static String amp = "&";
	
	/*Variables for JSON Params. This is a test case using the SPR-E20-327-COM.
	* It will be changeable in later versions */
	public static String addressInput = "1711%20S%20Rural%20Rd"; //ASU's address
	public static double systemCapInput = .327; //this is in KW
	public static byte moduleTypInput = 0;
	public static byte lossInput = 10;
	public static byte arrayTypInput = 0;
	public static double tiltInput = 0;
	public static double azimuthInput = 0;
	
	//JSON keys for the PVWatts API
	final static String JSONOutputs = "outputs"; //gives a JSONObject
	final static String annualACKey = "ac_annual"; //gives a double
	final static String annualDCKey = "dc_monthly"; //gives a JSONArray
	final static String annualSolRadKey = "poa_monthly"; //gives a JSONArray

	public static void main(String[] args) throws IOException, JSONException {
		String url = compileURL();
		String source = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
		JSONObject mainObject = new JSONObject(source);
		
		//JSONOutputs gives us the object with all the requested data
		mainObject = mainObject.getJSONObject(JSONOutputs);
		
		//now we assign some data to be displayed...
		double annualAC = mainObject.getDouble(annualACKey);
		JSONArray monthlyDC = mainObject.getJSONArray(annualDCKey);
		double annualDC = 0;
		for (int i = 0; i < 12; i++) {
			annualDC += monthlyDC.getDouble(i);
		}
		JSONArray solRad = mainObject.getJSONArray(annualSolRadKey);
		double solRadJan = solRad.getDouble(0);
		
		System.out.println("Annual AC: " + annualAC);
		System.out.println("Annual DC: " + annualDC);
		System.out.println("Annual Solar Radiation: " + solRadJan);
	}
	
	//Method made to retrieve the key without showing it on github (for safety reasons)
	public static String getPVWattsKey() {
		try {
			return Files.readAllLines(Paths.get("C:/PVWattsKey.txt")).get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return "Error retrieving Key: is the file in the GitHub Directory?";
		}
	}
	
	//takes all API params & variables and returns a single http string
	public static String compileURL() {
		return apiSite + version + format + apiKey + amp
				 + address + addressInput + amp 
				 + systemCap + systemCapInput + amp
				 + moduleTyp + moduleTypInput + amp
				 + loss + lossInput + amp
				 + arrayTyp + arrayTypInput + amp
				 + tilt + tiltInput + amp
				 + azimuth + azimuthInput;
	}
}
