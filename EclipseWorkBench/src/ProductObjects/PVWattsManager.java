package ProductObjects;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PVWattsManager {
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
	public String addressInput;
	public double latitudeInput;
	public double longitudeInput;
	public byte arrayTypeInput = 0;
	public double tiltInput = 45; //default is 45 degrees
	public double azimuthInput = 180; //default is facing south
	public double size = 1; // default value (is 1 m^2)
	
	// JSON Object keys for the PVWatts API
	final static String JSONOutputs = "outputs"; // gives a JSONObject
	final static String annualACKey = "ac_annual"; // gives a double
	final static String annualDCKey = "dc_monthly"; // gives a JSONArray
	final static String annualSolRadKey = "poa_monthly"; // gives a JSONArray

	public FullSystem system;

	// nullary constructor
	public PVWattsManager() {
	}

	// constructor with address given
	public PVWattsManager(FullSystem system, String address) {
		this.system = system;
		this.addressInput = address;
	}

	// constructor with coordinates given
	public PVWattsManager(FullSystem system, double latitude,
			double longitude) {
		this.system = system;
		this.latitudeInput = latitude;
		this.longitudeInput = longitude;
	}

	// gets information from the API
	public void getData() throws JSONException {
		String url = compileURL();
		String source = null;
		try {
			source = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
		} catch (IOException outOfUS) {
			// fixes the 422 error by adding 'intl' to regions outside the US
			url += amp + dataset;
			try {
				source = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
			} catch (IOException malformedURL) {
				malformedURL.printStackTrace();
			}
		}
		JSONObject mainObject = new JSONObject(source);

		// JSONOutputs gives us the object with all the requested data
		mainObject = mainObject.getJSONObject(JSONOutputs);

		// now we assign some data to be displayed...
		system.yearlyAC = mainObject.getDouble(annualACKey) * size;
		JSONArray monthlyDC = mainObject.getJSONArray(annualDCKey);
		system.yearlyDC = 0;
		for (int x = 0; x < 12; x++) {
			system.yearlyDC += monthlyDC.getDouble(x);
		}

		system.yearlyDC *= size; // adjusts data to include the system's size
	}

	// Method to retrieve API key without showing it on git (for safety reasons)
	private static String getPVWattsKey() {
		try {
			return Files.readAllLines(Paths.get("C:/PVWattsKey.txt")).get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return "Error retrieving Key: is the file in the GitHub Directory?";
		}
	}

	// takes all API params & variables and returns a single http string
	private String compileURL() {
		if (addressInput != null) {
			return apiSite + version + format + apiKey + amp + address + amp
					+ systemCap + system.panel.systemCap + amp + moduleTyp
					+ system.panel.moduleType + amp + loss + (int) system.loss
					+ amp + arrayTyp + arrayTypeInput + amp + tilt + tiltInput
					+ amp + azimuth + azimuthInput;
		} else {
			return apiSite + version + format + apiKey + amp + latitude
					+ latitudeInput + amp + longitude + longitudeInput + amp
					+ systemCap + system.panel.systemCap + amp + moduleTyp
					+ system.panel.moduleType + amp + loss + (int) system.loss
					+ amp + arrayTyp + arrayTypeInput + amp + tilt + tiltInput
					+ amp + azimuth + azimuthInput;
		}
	}

}
