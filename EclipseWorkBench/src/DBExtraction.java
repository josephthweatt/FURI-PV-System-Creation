import java.sql.*;

import java.util.HashMap;

// This class extracts data from the sqlite DB and stores the information
// into objects. !!!This is a rough draft, I'll make changes when necessary!!!
public class DBExtraction {
	Connection c;
	Satement stmt;

	// not exactly sure if these should be private or public
	private HashMap<String, Object> productMap;
	private Hashmap<String, FullSystem> systemMap;

	// construct dbExtraction by opening the db
	public DBExtraction(String dbName) {
		productMap = new HashMap<String, Obejct>;
		systemMap = new HashMap<String, FullSystem>;
	}

	// export product objects to serializable objects
	public void serializeProduct() {

	}

	// returns the product to the class using it (does not save the product)
	public Object getProductObject(String productName) {

	}

	// grabs products and inserts them into a full System (products must 
	// already be made as objects)
	public void commitProductsToSystem(String... productName) {

	}
}