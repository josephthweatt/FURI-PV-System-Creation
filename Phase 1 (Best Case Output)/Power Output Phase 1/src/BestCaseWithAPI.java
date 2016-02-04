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
 * In this commit, I will be comparing the results of this API against
 * the real-time data recorded in Macau, China over a given year. 
 * To learn more about the Macau project, see the Reading on Ouput
 * Prediction Methods document in my Research Notes.
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
	final static private String latitude = "lat=";
	final static private String longitude = "lon=";
	final static private String systemCap = "system_capacity=";
	final static private String moduleTyp = "module_type=";
	final static private String loss = "losses=";
	final static private String arrayTyp = "array_type=";
	final static private String tilt = "tilt=";
	final static private String azimuth = "azimuth=";
	final static private String dataset = "dataset=intl"; //only for beyond U.S
	final static String amp = "&";
	
	/*Variables for JSON Params. This is a test case using the Chinese panel
	* It will be changeable in later versions */
	public static String addressInput; 
	public static double latitudeInput = 22.10; //lats and longs of Macau
	public static double longitudeInput = 113.33;
	public static double systemCapInput = .175; //this is in KW
	public static byte moduleTypInput = 0;
	public static byte lossInput = 5; //max inverter efficiency was 95%
	public static byte arrayTypInput = 1; //panels were on a roof
	public static double tiltInput = 10; //ten degrees
	public static double azimuthInput = 135; //facing south east
	public static double size = 15.325; //In meters squared
	
	//JSON keys for the PVWatts API
	final static String JSONOutputs = "outputs"; //gives a JSONObject
	final static String annualACKey = "ac_annual"; //gives a double
	final static String annualDCKey = "dc_monthly"; //gives a JSONArray
	final static String annualSolRadKey = "poa_monthly"; //gives a JSONArray

	public static void main(String[] args) throws IOException, JSONException {
		String url = compileURL();;
		String source = null;
		try {
			source = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
		} catch (IOException e) {
			//fixes the 422 error by adding 'intl' to regions outside the US
			url += amp + dataset;
			source = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
		}
		JSONObject mainObject = new JSONObject(source);	
		
		//JSONOutputs gives us the object with all the requested data
		mainObject = mainObject.getJSONObject(JSONOutputs);
		
		//now we assign some data to be displayed...
		double annualAC = mainObject.getDouble(annualACKey) * size;
		JSONArray monthlyDC = mainObject.getJSONArray(annualDCKey);
		double annualDC = 0;
		for (int i = 0; i < 12; i++) {
			annualDC += monthlyDC.getDouble(i);
		}
		annualDC *= size;
		JSONArray solRad = mainObject.getJSONArray(annualSolRadKey);
		double solRadJan = solRad.getDouble(0) * size;
		
		System.out.printf("Annual AC: %.3f%n", annualAC);
		System.out.printf("Annual DC: %.3f%n", annualDC);
		System.out.printf("January Solar Radiation: %.3f%n", solRadJan);
	}
	
	//Method to retrieve API key without showing it on git (for safety reasons)
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
				 + latitude + latitudeInput + amp
				 + longitude + longitudeInput + amp
				 + systemCap + systemCapInput + amp
				 + moduleTyp + moduleTypInput + amp
				 + loss + lossInput + amp
				 + arrayTyp + arrayTypInput + amp
				 + tilt + tiltInput + amp
				 + azimuth + azimuthInput;
	}
}
