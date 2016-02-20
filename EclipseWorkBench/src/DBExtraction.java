import java.sql.*;
import java.util.HashMap;

import ProductObjects.*;

// This class extracts data from the sqlite DB and stores the information
// into objects. !!!This is a rough draft, I'll make changes when necessary!!!
public class DBExtraction {
	private static Connection c;
	private static ResultSet rs;
	private static Statement stmt;
	public final String dbName;

	// not exactly sure if these should be private or public
	private HashMap<String, Object> productMap;
	private HashMap<String, FullSystem> systemMap;

	// construct dbExtraction by opening the db
	public DBExtraction(String dbName) {
		this.dbName = dbName;
		openDB();
		productMap = new HashMap<String, Object>();
		systemMap = new HashMap<String, FullSystem>();

	}

	// gets access to PVModels.db
	private void openDB() {
		try {
			c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			stmt = c.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// export product objects to serializable objects
	public void serializeProduct() {
		
	}

	// returns the product to the class using it (does not save the product)
	public Object getProductByName(String productName) {
		return productMap.get(productName);
	}

	public FullSystem getSystemByName(String systemName) {
		return systemMap.get(systemName);
	}

	// grabs products and inserts them into a full System (products must
	// already be made as objects)
	public FullSystem commitProductsToSystem (String systemName, Object... product) {
		systemMap.put(systemName, new FullSystem());
		for (int i = 0; i < product.length; i++) {
			systemMap.get(systemName).addProduct(product);
		}
		return systemMap.get(systemName);
	}

	// stores all products in the database into their respective objects
	public void loadAllProducts() {
		int i = 0;
		try {
			for (i = 0; i < FullSystem.productType.length; i++) {
				rs = stmt.executeQuery("select * from "
						+ FullSystem.productType[i] + ";");
				while (rs.next()) {
					productMap.put(rs.getString("Name"), setObject(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Problem at " + FullSystem.productType[i]
					+ " in database " + dbName);
		}
		i++;
	}

	public Object setObject(int i) throws SQLException {
		// all product objects will have a name and price...
		String name = rs.getString("Name");
		double price = rs.getDouble("Price");

		// now we need to find the which object will store the data
		switch (i) {
		case 0:
			return new Panel(name, price, rs.getInt("SystemCap"),
					rs.getDouble("Amps"), rs.getDouble("Volts"),
					rs.getInt("PowerTolerance"), rs.getInt("module_type"));
		case 1:
			return new Inverter(name, price, rs.getInt("Efficiency"),
					rs.getInt("Watts"), rs.getInt("InputV"),
					rs.getInt("OutputV"));
		case 2:
			return new Racking(name, price, rs.getString("SizePerModule"),
					rs.getInt("RoofMounted"));
		case 3:
			return new Battery(name, price, rs.getInt("Voltage"),
					rs.getInt("AmpHours"));
		case 4:
			return new BatteryController(name, price,
					rs.getString("BankVoltage"),
					rs.getString("MaxAmpPerController"));
		case 5:
			return new BatteryMeter(name, price, rs.getString("Features"));
		case 6:
			return new DCACDisconnect(name, price, rs.getInt("Amps"),
					rs.getInt("Volts"));
		case 7:
			return new BatteryWire(name, price, rs.getInt("LengthInInches"),
					rs.getString("Gauge"));
		case 8:
			return new PVWires(name, price, rs.getInt("LengthInFeet"));
		default:
			return null;
		}
	}
}
