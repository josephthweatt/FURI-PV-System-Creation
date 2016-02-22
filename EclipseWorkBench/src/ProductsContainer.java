
// made for storing multiple products of the same type
// used for ranking individual objects and narrowing down the options
public class ProductsContainer {
	public Class<?> productType;
	public Object[] products;
	
	public enum Objectives {
		COST, EFFICIENCY, OFF_GRID, POWER_OUTPUT
	}
	
	public ProductsContainer(Class<?> productType, Object[] products) {
		this.productType = productType;
		this.products = new Object[products.length];
		// casts products as their original objects
		for (int i = 0; i < products.length; i++) {
			this.products[i] = productType.cast(products[i]);
		}
	}
	
	public void sortHiToLo() {
		
	}
	
	public void sortLoToHi() {
		
	}

}
