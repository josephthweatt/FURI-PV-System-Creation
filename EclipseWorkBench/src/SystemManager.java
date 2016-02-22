import java.util.HashMap;

import ProductObjects.FullSystem;

public class SystemManager {
	private HashMap<String, FullSystem> systemMap;

	public SystemManager() {
		systemMap = new HashMap<String, FullSystem>();
	}

	public FullSystem getSystemByName(String systemName) {
		return systemMap.get(systemName);
	}

	// grabs products and inserts them into a full System (products must
	// already be made as objects). Will also return a system if it is needed
	public FullSystem commitProductsToSystem(String systemName,
			Object... product) {
		systemMap.put(systemName, new FullSystem());
		for (int i = 0; i < product.length; i++) {
			systemMap.get(systemName).addProduct(product);
		}
		return systemMap.get(systemName);
	}
}
