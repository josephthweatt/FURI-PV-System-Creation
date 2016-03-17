import ProductObjects.*;

public class Algorithms {

	public double budget;
	public double energyInWatts;
	public double availableSpace; //in square meters
	public double energyInVolts; //an optional field. I may delete it later

	public Algorithms(double budget, double energyInWatts, double availableSpace) {
		this.budget = budget;
		this.energyInWatts = energyInWatts;
		this.availableSpace = availableSpace;
	} 

	public Algorithms(double budget, double energyInWatts, double availableSpace, double energyInVolts) {
		this.budget = budget;
		this.energyInWatts = energyInWatts;
		this.availableSpace = availableSpace;
		this.energyInVolts = energyInVolts;
	} 

}