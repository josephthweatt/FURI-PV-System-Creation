package ProductObjects;

public class BatteryController implements java.io.Serializable {
	public String name;
	public double price;
	public String bankVoltage;
	public String maxAmpPerController;

	// for nonspecific initialization
	public BatteryController() {
	}
	
	public BatteryController(String name, double price,
			String bankVoltage, String maxAmpPerController) {
		this.name = name;
		this.price = price;
		this.bankVoltage = bankVoltage;
		this.maxAmpPerController = maxAmpPerController;
	}

}
