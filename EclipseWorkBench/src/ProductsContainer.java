import java.util.ArrayList;

import java.lang.reflect.Field;

// made for storing multiple products of the same type
// used for ranking individual objects and narrowing down the options
public class ProductsContainer {
	public Class<?> productType;
	public ArrayList<Object> products;

	public enum Objectives {
		COST, EFFICIENCY, OFF_GRID, POWER_OUTPUT
	}

	public ProductsContainer(Class<?> productType) {
		this.productType = productType;
	}

	// constructor with products
	public ProductsContainer(Class<?> productType, Object[] products) {
		this.productType = productType;
		this.products = new ArrayList<Object>();
		// casts products as their original objects, then adds them to ArrayList
		for (int i = 0; i < products.length; i++) {
			this.products.add(productType.cast(products[i]));
		}
	}

	public void addProduct(Object product) {
		if (productType != null) {
			this.products.add(productType.cast(product));

		} else {
			System.out.println("Product Type must be initialized");
		}
	}

	public void sortHiToLo() {
		
	}

	public void sortLoToHi() {

	}

}
