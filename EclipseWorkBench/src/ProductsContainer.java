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

	/***************************************************************************
	 * The sorting algorithms will sort the Arraylist according to the fieldName
	 * passed in the parameters.
	 **************************************************************************/
	public void sortHiToLo(String fieldName) {
		// this 'if' checks if the fieldName is a String
		if (getStringFromField(fieldName, 0) != null) {
			// we work with the string compare if we're using strings

		} else { // all other datatypes would be considered real numbers

		}
	}

	public void sortLoToHi(String fieldName) {
		// this 'if' checks if the fieldName is a String
		if (getStringFromField(fieldName, 0) != null) {
			// we work with the string compare if we're using strings

		} else { // all other datatypes would be considered real numbers

		}
	}

	private String getStringFromField(String fieldName, int position) {
		try {
			return (String) productType.getDeclaredField(fieldName)
					.get(products.get(position));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			// do nothing here, the method returns null at this point
		}
		return null;
	}

	private int getIntFromField(String fieldName, int position) {
		try {
			return (int) productType.getDeclaredField(fieldName)
					.get(products.get(position));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return 0; // if the program fails to find the field
	}

	private double getDoubleFromField(String fieldName, int position) {
		try {
			return (double) productType.getDeclaredField(fieldName)
					.get(products.get(position));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return 0; // if the program fails to find the field
	}

}
