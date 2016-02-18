import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	// API structure for PVWatts power prediction api
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
	final static private String dataset = "dataset=intl"; // for places beyond
															// U.S
	final static String amp = "&";

	/*
	 * Variables for JSON Params. This is a test case using the Chinese panel It
	 * will be changeable in later versions
	 */
	public static String addressInput;
	public static double latitudeInput;
	public static double longitudeInput;
	public static byte arrayTypInput = 0;
	public static double tiltInput;
	public static double azimuthInput;
	public static double size = 1; // default value (is 1 m^2)
	public static List<Panel> pvModels;

	// JSON Object keys for the PVWatts API
	final static String JSONOutputs = "outputs"; // gives a JSONObject
	final static String annualACKey = "ac_annual"; // gives a double
	final static String annualDCKey = "dc_monthly"; // gives a JSONArray
	final static String annualSolRadKey = "poa_monthly"; // gives a JSONArray

	public static void main(String[] args) throws IOException, JSONException {
		// used to test the loadAllProducts method, as well as the setObject
		// method
		DBExtraction extract = new DBExtraction("PVModels.db");
		extract.loadAllProducts();
		System.exit(0);

		// we use Tempe's coordinates for the test case
		longitudeInput = 111.9431;
		latitudeInput = 33.4294;

	}

	// Method to retrieve API key without showing it on git (for safety reasons)
	public static String getPVWattsKey() {
		try {
			return Files.readAllLines(Paths.get("C:/PVWattsKey.txt")).get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return "Error retrieving Key: is the file in the GitHub Directory?";
		}
	}

	// takes all API params & variables and returns a single http string
	private static String compileURL(FullSystem pvSys) {
		if (addressInput != null) {
			return apiSite + version + format + apiKey + amp + address + amp
					+ systemCap + pvSys.panel.systemCap + amp + moduleTyp
					+ pvSys.panel.moduleType + amp + loss + (int) pvSys.loss
					+ amp + arrayTyp + arrayTypInput + amp + tilt + tiltInput
					+ amp + azimuth + azimuthInput;
		} else {
			return apiSite + version + format + apiKey + amp + latitude
					+ latitudeInput + amp + longitude + longitudeInput + amp
					+ systemCap + pvSys.panel.systemCap + amp + moduleTyp
					+ pvSys.panel.moduleType + amp + loss + (int) pvSys.loss
					+ amp + arrayTyp + arrayTypInput + amp + tilt + tiltInput
					+ amp + azimuth + azimuthInput;
		}
	}
}
