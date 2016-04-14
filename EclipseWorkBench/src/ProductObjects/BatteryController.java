package ProductObjects;

import java.util.ArrayList;
import java.util.HashMap;

public class BatteryController {
	public String name;
	public double price;
	public String bankVoltage;
	public String maxAmpPerController;

	// measured as <amps, bank voltage>
	public HashMap<Integer, ArrayList<Integer>> maxAmps; 

	// for nonspecific initialization
	public BatteryController() {
	}

	public BatteryController(String name, double price, String bankVoltage,
			String maxAmpPerController) {
		this.name = name;
		this.price = price;
		this.bankVoltage = bankVoltage;
		this.maxAmpPerController = maxAmpPerController;
		storeMaxAmps();
	}

	// stores the amps and the corresponding bank voltages to a hashmap. Each
	// amp can have multiple bank voltages
	private void storeMaxAmps() {
		String[] ampSegments = maxAmpPerController.split(",");
		for (int i = 0; i < ampSegments.length; i++) {
			String[] amps = ampSegments[i].split("/");
			String[] voltages = amps[1].split("&");
			Integer ampsInt = Integer.parseInt(amps[0]);
			maxAmps = new HashMap<Integer, ArrayList<Integer>>();
			
			ArrayList<Integer> voltArray = new ArrayList<Integer>();
			for (int j = 0; j < voltages.length; j++) {
				voltArray.add(Integer.parseInt(voltages[j]));
			}
			maxAmps.put(ampsInt, voltArray);
		}
	}
}
