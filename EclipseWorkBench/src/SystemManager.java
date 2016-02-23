import java.util.HashMap;

import ProductObjects.*;

public class SystemManager {
	public SystemCreator systemCreator;
	private HashMap<String, FullSystem> systemMap;

	public PowerInfoMain.Location location;

	public SystemManager() {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
	}

	public SystemManager(PowerInfoMain.Location location) {
		systemCreator = new SystemCreator();
		systemMap = new HashMap<String, FullSystem>();
		location = location;
	}

	public FullSystem getSystemByName(String systemName) {
		return systemMap.get(systemName);
	}

	/******************************************************************************
	 * The SystemCreator subclass is dedicated to creating new PVSystems. It is
	 * also the primary implementor of the ProductsContainer class
	 ******************************************************************************/
	public class SystemCreator {
		public ProductsContainer[] containers;

		public SystemCreator() {
			containers = new ProductsContainer[9];
			// sets the ClassTypes of the containers
			containers[0] = new ProductsContainer(Panel.class);
			containers[1] = new ProductsContainer(Inverter.class);
			containers[2] = new ProductsContainer(Racking.class);
			containers[3] = new ProductsContainer(Battery.class);
			containers[4] = new ProductsContainer(BatteryController.class);
			containers[5] = new ProductsContainer(BatteryMeter.class);
			containers[6] = new ProductsContainer(DCACDisconnect.class);
			containers[7] = new ProductsContainer(BatteryWire.class);
			containers[8] = new ProductsContainer(PVWire.class);
		}

		public void makeContainers() {
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
