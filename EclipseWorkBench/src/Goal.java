// Object class to contain the parameters the user has for their system
public class Goal {
	public String goal;
	public double budget;
	public double energyInKW; // user's desired energy
	public double availableSpace; // in square meters
	public double energyInVolts; 
	
	public Goal () {
	}
	
	public Goal (String goal) {
		this.goal = goal;
	}
	
	public Goal (String goal, double budget, double energyInKW, 
			double availableSpace) {
		this.goal = goal;
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = -1;
	}
	
	public Goal (String goal, double budget, double energyInKW, 
			double availableSpace, double energyInVolts) {
		this.goal = goal;
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = energyInVolts;
	}
}
