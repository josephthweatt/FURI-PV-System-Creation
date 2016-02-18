import java.util.Objects;

// made for storing multiple products of the same type
// used for ranking individual objects and narrowing down the options
public class ProductsContainer {
	public Object[] products;
	
	public ProductsContainer(Class<?> productType, Objects... products) {
		this.products = new Object[products.length];
		for (int i = 0; i < products.length; i++) {
			this.products[i] = productType.cast(products[i]);
		}
	}
	
	//sorting methods coming soon...

}
