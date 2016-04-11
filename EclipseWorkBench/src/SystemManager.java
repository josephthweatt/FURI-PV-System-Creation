import java.util.HashMap;

import ProductObjects.*;

public class SystemManager {
	public SystemCreator systemCreator;
	private FullSystem[] systems;
	private Algorithms algorithm;

	private Goal goal;

	public SystemManager() {
		systemCreator = new SystemCreator();
		systems = null;
	}

	// sets the goal for what kind of systems to search for
	public void setGoal(Goal goal) {
		this.goal = goal;
		if (goal.equals("pricing")) {
			algorithm = new Pricing(goal, systemCreator.containers);
		} // more will be added here as different algorithms are made
	}

	public FullSystem getSystem(int index) {
		return systems[index];
	}

	public ProductContainer getContainer(int i) {
		return systemCreator.containers[i];
	}

	public ProductContainer[] getContainers() {
		return systemCreator.containers;
	}
	
	// called to put the found systems into the systemMap
	public void setSystemsFromAlgorithm() {
		algorithm.runAlgorithm();
		systems = algorithm.getSystems();
	}
	
	public FullSystem[] getSystems() {
		return systems;
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
					containers[0].add(product);
				} else if (product instanceof Inverter) {
					containers[1].add(product);
				} else if (product instanceof Racking) {
					containers[2].add(product);
				} else if (product instanceof Battery) {
					containers[3].add(product);
				} else if (product instanceof BatteryController) {
					containers[4].add(product);
				} else if (product instanceof BatteryMeter) {
					containers[5].add(product);
				} else if (product instanceof DCACDisconnect) {
					containers[6].add(product);
				} else if (product instanceof BatteryWire) {
					containers[7].add(product);
				} else if (product instanceof PVWire) {
					containers[8].add(product);
				} else {
					System.out.println("could not place product!");
				}
			}
		}
	}
}
