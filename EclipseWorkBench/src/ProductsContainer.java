
// made for storing multiple products of the same type
// used for ranking individual objects and narrowing down the options
public class ProductsContainer {
	public Object[] products;
	
	public enum Objectives {
		COST, EFFICIENCY, OFF_GRID, POWER_OUTPUT
	}
	
	public ProductsContainer(Class<?> productType, Object[] products) {
		this.products = new Object[products.length];
		for (int i = 0; i < products.length; i++) {
			this.products[i] = productType.cast(products[i]);
		}
	}
	
	public void sortBy(Objectives intent) {
		if (intent == Objectives.COST) {
			
		} else if (intent == Objectives.EFFICIENCY) {
			
		} else if (intent == Objectives.OFF_GRID) {
			
		} else if (intent == Objectives.POWER_OUTPUT) {
			
		}
	}

}
