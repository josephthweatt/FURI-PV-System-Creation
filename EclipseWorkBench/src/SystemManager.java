import java.util.HashMap;

import ProductObjects.*;

public class SystemManager {
	public SystemCreator systemCreator;
	private HashMap<String, FullSystem> systemMap;

	public Location location;

	public SystemManager() {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
	}

	public SystemManager(Location location) {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
		this.location = location;
	}

	public FullSystem getSystemByName(String systemName) {
		return systemMap.get(systemName);
	}

	public ProductContainer getContainer(int i) {
		return systemCreator.containers[i];
	}

	/******************************************************************************
	 * The SystemCreator subclass is dedicated to creating new PVSystems. It is
	 * also the primary implementor of the ProductsContainer class
	 ******************************************************************************/
	public class SystemCreator {
		private ProductContainer[] containers;
		public DBExtraction db;

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
			makeContainersWithDB();
		}

		public void makeContainersWithDB() {
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
				}
			}
		}

		// grabs products and inserts them into a full System (products must
		// already be made as objects). Will also return a system if it is
		// needed
		public FullSystem commitProductsToSystem(String systemName,
				Object... product) {
			// try-catch will determine whether the System will use an address
			// or coordinates
			try {
				systemMap.put(systemName,
						new FullSystem(location.getAddress(), product));
			} catch (NullPointerException e) {
				systemMap.put(systemName,
						new FullSystem(location.getCoordinates(), product));
			}
			return systemMap.get(systemName);
		}
	}
}
