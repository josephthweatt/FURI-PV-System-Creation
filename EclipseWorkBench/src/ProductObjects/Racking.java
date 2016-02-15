package ProductObjects;

public class Racking {
	public String name;
	public double price;
	public String bankVoltage;
	public String maxAmpPerController;
	
	// for nonspecific initialization
	public Racking() {
	}

	public Racking(String name, double price, String bankVoltage,
			String maxAmpPerController) {
		this.name = name;
		this.price = price;
		this.bankVoltage = bankVoltage;
		this.maxAmpPerController = maxAmpPerController;
	}
}
