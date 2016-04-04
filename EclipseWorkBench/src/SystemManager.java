import java.util.HashMap;

import ProductObjects.*;

public class SystemManager {
	public SystemCreator systemCreator;
	private HashMap<String, FullSystem> systemMap;
	private Algorithms algorithm;

	private Location location;
	private Goal goal;

	public SystemManager() {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
	}

	public SystemManager(Location location) {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
		this.location = location;
	}
	
	public SystemManager(Location location, Goal goal) {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
		this.location = location;
		this.setGoal(goal);
	}
	
	// sets the goal for what kind of systems to search for
	public void setGoal(Goal goal) {
		this.goal = goal;
		if (goal.equals("pricing")) {
			algorithm = new Pricing(goal, systemCreator.containers);
		} // more will be added here as different algorithms are made
	}

	public FullSystem getSystemByName(String systemName) {
		return systemMap.get(systemName);
	}

	public ProductContainer getContainer(int i) {
		return systemCreator.containers[i];
	}

	public ProductContainer[] getContainers() {
		return systemCreator.containers;
	}

	/******************************************************************************
	 * The SystemCreator subclass is dedicated to creating new PVSystems. It is
	 * also the primary implementor of the ProductsContainer class
	 ******************************************************************************/
	public class SystemCreator {
		private ProductContainer[] containers;
		private DBExtraction db;

		public SystemCreator() {
			db = new DBExtraction("PVModels.db");
			db.loadAllProducts();

			containers = new ProductContainer[9];
			// sets the ClassTypes of the containers
			containers[0] = new ProductContainer(Panel.class);
			containers[1] = new ProductContainer(Inverter.class);
			containers[2] = new ProductContainer(Racking.class);
			containers[3] = new ProductContainer(Battery.class);
			containers[4] = new ProductContainer(BatteryController.class);
			containers[5] = new ProductContainer(BatteryMeter.class);
			containers[6] = new ProductContainer(DCACDisconnect.class);
			containers[7] = new ProductContainer(BatteryWire.class);
			containers[8] = new ProductContainer(PVWire.class);

			makeContainersWithDB(); // sets db informaiton into containers
		}

		// grabs products and inserts them into a full System (products must
		// already be made as objects). Will also return a system if it is
		// needed
		public FullSystem commitProductsToSystem(String systemName,
				Object... product) {
			if (location.getAddress() == null) {
				systemMap.put(systemName,
						new FullSystem(location.getCoordinates(), product));
			} else {
				systemMap.put(systemName, new FullSystem(location.getAddress(),
						product));
			}
			return systemMap.get(systemName);
		}

		// changes the DBExtraction's database, also resets the
		public void setNewDatabase(String dbName) {
			db = new DBExtraction(dbName);
			db.loadAllProducts();
			makeContainersWithDB();
		}

		private void makeContainersWithDB() {
			HashMap<String, Object> productMap = DBExtraction.getProducts();
			for (Object product : productMap.values()) {
				if (product instanceof Panel) {
					containers[0].addProduct(product);
				} else if (product instanceof Inverter) {
					containers[1].addProduct(product);
				} else if (product instanceof Racking) {
					containers[2].addProduct(product);
				} else if (product instanceof Battery) {
					containers[3].addProduct(product);
				} else if (product instanceof BatteryController) {
					containers[4].addProduct(product);
				} else if (product instanceof BatteryMeter) {
					containers[5].addProduct(product);
				} else if (product instanceof DCACDisconnect) {
					containers[6].addProduct(product);
				} else if (product instanceof BatteryWire) {
					containers[7].addProduct(product);
				} else if (product instanceof PVWire) {
					containers[8].addProduct(product);
				} else {
					System.out.println("could not place product!");
				}
			}
		}
	}
}
