package ProductObjects;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* This class can be used to hold variations of the 
 * existing objects in this class. It is used outside 
 * of the package to attain API information and to 
 * provide information on the prospective PV system. */

public class FullSystem implements java.io.Serializable {
	// Objects of the system's parts
	public Panel panel;
	public Inverter inverter;
	public Racking rack;
	public Battery battery;
	public BatteryController batteryControl;
	public BatteryMeter batteryMeter;
	public DCACDisconnect dcacDisconnect;
	public BatteryWire batteryWire;
	public PVWire pvWires;

	// all the table names that one could expect from the database:
	public static String[] productType = { "Panels", "Inverters", "Racking",
			"Batteries", "BatteryControllers", "BatteryMeter", "DCACDisconnect",
			"BatteryWires", "PVWires" };

	// System qualities
	public double cost;
	public double loss;
	public double yearlyDC;
	public double yearlyAC;
	public double yearlyEnergy;

	public String address;
	public double latitude;
	public double longitude;

	// for nonspecific initialization
	public FullSystem() {
	}

	// constructor to initialize with products
	public FullSystem(double[] coordinates, Object... product) {
		this.latitude = coordinates[0];
		this.longitude = coordinates[1];
		addProduct(product);

		// things to do if the user has entered a full system
		if (isComplete()) {
			calculateCost();
			calculateLoss();
		}
	}

	public FullSystem(String address, Object... product) {
		this.address = address;
		addProduct(product);

		// things to do if the user has entered a full system
		if (isComplete()) {
			calculateCost();
			calculateLoss();
		}
	}

	// primary method for loading products into the system
	public void addProduct(Object... product) {
		for (int i = 0; i < product.length; i++) {
			if (product[i].getClass() == Panel.class) {
				panel = (Panel) product[i];
			} else if (product[i].getClass() == Inverter.class) {
				inverter = (Inverter) product[i];
			} else if (product[i].getClass() == Racking.class) {
				rack = (Racking) product[i];
			} else if (product[i].getClass() == Battery.class) {
				battery = (Battery) product[i];
			} else if (product[i].getClass() == BatteryController.class) {
				batteryControl = (BatteryController) product[i];
			} else if (product[i].getClass() == BatteryMeter.class) {
				batteryMeter = (BatteryMeter) product[i];
			} else if (product[i].getClass() == DCACDisconnect.class) {
				dcacDisconnect = (DCACDisconnect) product[i];
			} else if (product[i].getClass() == BatteryWire.class) {
				batteryWire = (BatteryWire) product[i];
			} else if (product[i].getClass() == PVWire.class) {
				pvWires = (PVWire) product[i];
			}
		}
	}

	// get the total cost of the system
	// returns zero if the system is incomplete
	public double calculateCost() {
		if (isComplete()) {
			this.cost += panel.price;
			this.cost += inverter.price;
			this.cost += rack.price;
			this.cost += battery.price;
			this.cost += batteryControl.price;
			this.cost += batteryMeter.price;
			this.cost += dcacDisconnect.price;
			this.cost += batteryWire.price;
			this.cost += pvWires.price;
			return cost;
		}
		return 0.0;
	}

	// calculates percent of energy one expects to lose from
	// the system (percent stored as a double from 0 to 100)
	public void calculateLoss() {
		if (isComplete() == false) {
			System.out.println(
					"Incomplete system. Complete this system to use this method.");
			return;
		}
		final int WIRE_LOSS = 2; // 2% lost from wires
		final double LIGHT_DEGREDATION = 1.5;
		final int AGE = 0; // we assume the panels will be new for the first
							// year
		int invLoss = 100 - inverter.efficiency;

		this.loss = WIRE_LOSS + LIGHT_DEGREDATION + AGE + invLoss
				+ panel.powerTolerance;
	}

	// method to verify that the system has all the essential parts
	public boolean isComplete() {
		if (panel == null)
			return false;
		if (inverter == null)
			return false;
		if (rack == null)
			return false;
		if (battery == null)
			return false;
		if (batteryMeter == null)
			return false;
		if (batteryControl == null)
			return false;
		if (dcacDisconnect == null)
			return false;
		if (batteryWire == null)
			return false;
		if (pvWires == null)
			return false;

		return true;
	}

	/************************************************************************
	 * The Following methods will be used to get data from the PVWatts API
	 *************************************************************************/
	// gets power data from the API and inputs extra data with the hashmap
	public void getDataFromAPI(HashMap<String, Double> extraData) {
		PVWattsManager pvWatts;

		// method check to see if any non-product data must be entered
		if (extraData != null) {
			// will pass the HashMap through if extra Data exists
			pvWatts = new PVWattsManager(this, extraData);
		} else {
			pvWatts = new PVWattsManager(this);
		}

		try {
			pvWatts.getData();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static class PVWattsManager {
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
		final static private String dataset = "dataset=intl"; // for places
																// beyond
																// U.S
		final static String amp = "&";

		/*
		 * Variables for JSON Params. This is a test case using the Chinese
		 * panel It will be changeable in later versions
		 */
		public String addressInput;
		public double latitudeInput;
		public double longitudeInput;

		/*
		 * Non-product data (automatically assigned, but changeable) These are
		 * assigned through the second constructor, using the HashMap<>. The
		 * HashMap values must be, respectively: - "arrayType" - "tilt" -
		 * "azimuth" - "size"
		 */
		public byte arrayTypeInput = 0;
		public double tiltInput = 45; // default is 45 degrees
		public double azimuthInput = 180; // default is facing south
		public double size = 1; // default value (is 1 m^2)

		// JSON Object keys for the PVWatts API
		final static String JSONOutputs = "outputs"; // gives a JSONObject
		final static String annualACKey = "ac_annual"; // gives a double
		final static String monthlyDCKey = "dc_monthly"; // gives a JSONArray

		// poa is the monthly kWh per meters squared so it must be multiplied by
		// the Systems total area
		final static String monthlySolRadKey = "poa_monthly"; // gives a
																// JSONArray

		protected FullSystem system;

		public PVWattsManager(FullSystem system) {
			this.system = system;
			this.latitudeInput = system.latitude;
			this.longitudeInput = system.longitude;
			this.size = system.panel.metersSquared;
		}

		// constructor for entering non-product data
		public PVWattsManager(FullSystem system,
				HashMap<String, Double> extraData) {
			this.system = system;
			this.latitudeInput = system.latitude;
			this.longitudeInput = system.longitude;
			this.size = system.panel.metersSquared;

			if (extraData.containsKey("arrayType")) {
				this.arrayTypeInput = (byte) extraData.get("arrayType")
						.doubleValue();
			}
			if (extraData.containsKey("tilt")) {
				this.tiltInput = extraData.get("tilt").doubleValue();
			}
			if (extraData.containsKey("azimuth")) {
				this.azimuthInput = extraData.get("azimuth").doubleValue();
			}
			if (extraData.containsKey("size")) {
				this.size = extraData.get("size").doubleValue();
			}
		}

		// gets information from the API
		public void getData() throws JSONException {
			String url = compileURL();
			String source = null;
			try {
				source = IOUtils.toString(new URL(url),
						Charset.forName("UTF-8"));
			} catch (IOException outOfUS) {
				// fixes the 422 error by adding 'intl' to regions outside the
				// US
				url += amp + dataset;
				try {
					source = IOUtils.toString(new URL(url),
							Charset.forName("UTF-8"));
				} catch (IOException malformedURL) {
					malformedURL.printStackTrace();
				}
			}
			JSONObject mainObject = new JSONObject(source);

			// JSONOutputs gives us the object with all the requested data
			mainObject = mainObject.getJSONObject(JSONOutputs);

			// now we assign some data to be displayed...
			system.yearlyAC = mainObject.getDouble(annualACKey);
			JSONArray monthlyDC = mainObject.getJSONArray(monthlyDCKey);
			system.yearlyDC = 0;
			for (int x = 0; x < 12; x++) {
				system.yearlyDC += monthlyDC.getDouble(x);
			}

			// checks to see if the monthly plane of array (poa) was included in
			// the API. Energy is multiplied by size to account for the system's
			// area.
			if (mainObject.has(monthlySolRadKey)) {
				JSONArray monthlySolRad = mainObject
						.getJSONArray(monthlySolRadKey);
				system.yearlyEnergy = 0;
				for (int x = 0; x < 12; x++) {
					system.yearlyEnergy += monthlySolRad.getDouble(x) * size;
				}
			}
		}

		// Method to retrieve API key without showing it on git (for safety
		// reasons)
		private static String getPVWattsKey() {
			try {
				return Files.readAllLines(Paths.get("C:/PVWattsKey.txt"))
						.get(0);
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
						+ system.panel.moduleType + amp + loss
						+ (int) system.loss + amp + arrayTyp + arrayTypeInput
						+ amp + tilt + tiltInput + amp + azimuth + azimuthInput;
			} else {
				return apiSite + version + format + apiKey + amp + latitude
						+ latitudeInput + amp + longitude + longitudeInput + amp
						+ systemCap + system.panel.systemCap + amp + moduleTyp
						+ system.panel.moduleType + amp + loss
						+ (int) system.loss + amp + arrayTyp + arrayTypeInput
						+ amp + tilt + tiltInput + amp + azimuth + azimuthInput;
			}
		}

	}

}
