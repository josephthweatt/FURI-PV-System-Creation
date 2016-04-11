package ProductObjects;
// Object class to contain the parameters the user has for their system
public class Goal {
	public String goal;
	public double budget;
	public double energyInKW; // user's desired energy
	public double availableSpace; // in square meters
	public double energyInVolts;

	public Location location;

	public Goal() {
	}

	public Goal(String goal) {
		this.goal = goal;
	}

	public Goal(String goal, double budget, double energyInKW,
			double availableSpace, double[] coordinates) {
		this.goal = goal;
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = -1;

		location = new Location(coordinates);
	}

	public Goal(String goal, double budget, double energyInKW,
			double availableSpace, String address) {
		this.goal = goal;
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = -1;

		location = new Location(address);
	}

	public Goal(String goal, double budget, double energyInKW,
			double availableSpace, double energyInVolts, double[] coordinates) {
		this.goal = goal;
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = energyInVolts;

		location = new Location(coordinates);
	}

	public Goal(String goal, double budget, double energyInKW,
			double availableSpace, double energyInVolts, String address) {
		this.goal = goal;
		this.budget = budget;
		this.energyInKW = energyInKW;
		this.availableSpace = availableSpace;
		this.energyInVolts = energyInVolts;

		location = new Location(address);
	}

	// an object to store the location, which can either be an address (String)
	// or coordinates (Double). Note that the PVWatts class will try to use the
	// address string before the coordinates
	public class Location {
		private String address;
		private double latitude, longitude;

		public Location() {
		}

		// takes either an address or coordinates. If coordinates, submit as two
		// doubles (lat, long). If an address, submit as a url-ready String
		public Location(Object... location) {
			setLocation(location);
		}

		public void setLocation(Object... location) {
			if (location[0] instanceof Double) { // coordinates
				if ((Double) location[0] > 90) {
					System.out.println("latitude is too big");
					System.exit(0);
				} else if ((Double) location[1] > 180) {
					System.out.println("longitude is too big");
					System.exit(0);
				}

				latitude = (Double) location[0];
				longitude = (Double) location[1];
				address = null;
			} else if (location[0] instanceof String) { // string address
				address = (String) location[0];
				latitude = longitude = 0;
			} else {
				System.out.println("Invalid Objects given to setLocation");
			}
		}

		public double getLatitude() {
			return latitude;
		}
		
		public double getLongitude() {
			return longitude;
		}

		public String getAddress() {
			return address;
		}
	}

}
