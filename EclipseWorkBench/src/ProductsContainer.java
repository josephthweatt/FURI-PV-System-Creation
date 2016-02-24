import java.util.ArrayList;

import java.lang.reflect.Field;

// made for storing multiple products of the same type
// used for ranking individual objects and narrowing down the options
public class ProductsContainer {
	public Class<?> productType;
	public ArrayList<Object> products;

	public ProductsContainer(Class<?> productType) {
		this.productType = productType;
		products = new ArrayList<Object>();
	}

	// constructor with products
	public ProductsContainer(Class<?> productType, Object[] products) {
		this.productType = productType;
		this.products = new ArrayList<Object>();
		// casts products as their original objects, then adds them to ArrayList
		for (int i = 0; i < products.length; i++) {
			this.products.add(products[i]);
		}
	}

	public void addProduct(Object product) {
		// checks if the container has a designation
		if (productType != null) {
			this.products.add(product);
		} else {
			System.out.println("Product Type must be initialized");
		}
	}

	/***************************************************************************
	 * The sorting algorithms will sort the ArrayList according to the fieldName
	 * passed in the parameters. All 4 algorithms are Quicksort, categorized as
	 * high or low and number or string. The methods here return ArrayLists in a
	 * recursive method. There exists three parts to each quicksort: 
	 * 1. The main quicksorting method 
	 * 2. The partition 
	 * 3. The swap method (not special toquicksort)
	 * 
	 * I'll put each partition method with each quicksort, and leave the swap at
	 * the bottom
	 *************************************************************************/

	// stores what is lexicographically earliest to the first location
	public ArrayList<Object> quicksortLoToHiString(ArrayList<Object> array,
			int start, int end, String fieldName) {
		if (start >= end) {
			return array;
		}
		int pIndex = partitionLoToHiString(array, start, end, fieldName);

		quicksortLoToHiString(array, start, pIndex - 1, fieldName);
		quicksortLoToHiString(array, pIndex + 1, end, fieldName);

		return array;
	}

	private int partitionLoToHiString(ArrayList<Object> array, int start,
			int end, String fieldName) {

		String pivot = getStringFromField(fieldName, end);
		String parser;
		int pIndex = start;
		for (int i = start; i < end; i++) {
			parser = getStringFromField(fieldName, i);
			// if parser is lexicographically before or the same as the pivot
			if (parser.toLowerCase().compareTo(pivot.toLowerCase()) <= 0) {
				swap(i, pIndex);
				pIndex++;
			}
		}
		swap(pIndex, end);

		return pIndex;
	}

	// swaps arrayList objects
	private void swap(int location1, int location2) {
		Object temp = products.get(location1);
		products.set(location1, products.get(location2));
		products.set(location2, temp);
	}

	/***************************************************************************
	 * The following methods return values of different datatypes from an object
	 * 
	 * @param fieldName
	 * @param position
	 * @return
	 **************************************************************************/

	public String getStringFromField(String fieldName, int position) {
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
		System.out.println("Invaid Field: Field should be a String");
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
		System.out.println("Invaid Field: Field should be an int");
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
		System.out.println("Invaid Field: Field should be a double");
		return 0; // if the program fails to find the field
	}

}
