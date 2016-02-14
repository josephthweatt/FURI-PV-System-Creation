package ProductObjects;

public class RackingObject {
	public String name;
	public double price;
	public String bankVoltage;
	public String maxAmpPerController;
	
	public RackingObject(String name, double price, 
							String bankVoltage, 
							String maxAmpPerController) {
		this.name = name;
		this.price = price;
		this.bankVoltage = bankVoltage;
		this.maxAmpPerController = maxAmpPerController;
	}
}
