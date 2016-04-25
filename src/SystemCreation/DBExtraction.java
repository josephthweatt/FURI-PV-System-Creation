package com.example.family.furi.SystemCreation;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.family.furi.ProductObjects.*;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

// This class extracts data from the sqlite DB and stores the information
// into objects. !!!This is a rough draft, I'll make changes when necessary!!!
public class DBExtraction extends SQLiteOpenHelper {
	public String dbName = null;
	private Context context;
	private DBHelper db;
	private Cursor resultSet;


	// not exactly sure if these should be private or public
	private static HashMap<String, Object> productMap;
	private String duplicateName; // used when names are duplicates

	// construct dbExtraction by opening the db
	public DBExtraction(Context context, String dbName) {
		super(context, dbName, null, 1);
		this.context = context;
		this.dbName = dbName;
		productMap = new HashMap<String, Object>();
		db = new DBHelper(context);
		try {
			db.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			db.openDataBase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// returns the product to the class using it (does not save the product)
	public Object getProductByName(String productName) {
		return productMap.get(productName);
	}

	// stores all products in the database into their respective objects
	public void loadAllProducts() {
		int i = 0;
		Object obj;
		try {
			for (i = 0; i < FullSystem.productType.length; i++) {
				resultSet = db.query(FullSystem.productType[i]);
				resultSet.moveToFirst();
				for (int j = 0; j < resultSet.getCount(); j++) {
					obj = setObjectToMap(i);
					productMap.put(resultSet.getString(0) + duplicateName, obj);
					// sets the duplicate name to null if it was used
					if (duplicateName != null) {
						duplicateName = null;
					}
					resultSet.moveToNext();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Problem at " + FullSystem.productType[i]
					+ " in database " + dbName);
		}
		db.close();
	}

	// used to set product objects into the productMap
	private Object setObjectToMap(int i) throws SQLException {
		// all product objects will have a name and price...
		String name = resultSet.getString(0);
		double price = resultSet.getDouble(1);

		// now we need to find the which object will store the data
		switch (i) {
		case 0:
			return new Panel(name, price, resultSet.getInt(2),
					resultSet.getDouble(3), resultSet.getDouble(4),
					resultSet.getInt(5), resultSet.getInt(6),
					resultSet.getString(7));
		case 1:
			return new Inverter(name, price, resultSet.getInt(2),
					resultSet.getInt(3), resultSet.getInt(4),
					resultSet.getInt(5));
		case 2:
			duplicateName = " " + resultSet.getString(2);
			return new Racking(name, price, resultSet.getString(2),
					resultSet.getInt(3));
		case 3:
			return new Battery(name, price, resultSet.getInt(2),
					resultSet.getInt(3));
		case 4:
			return new BatteryController(name, price,
					resultSet.getString(2),
					resultSet.getString(3));
		case 5:
			return new BatteryMeter(name, price, resultSet.getString(2));
		case 6:
			return new DCACDisconnect(name, price, resultSet.getInt(2),
					resultSet.getInt(3));
		case 7:
			duplicateName = " " + Integer.toString(resultSet.getInt(2));
			return new BatteryWire(name, price, resultSet.getInt(2),
					resultSet.getString(3));
		case 8:
			duplicateName = " " + Integer.toString(resultSet.getInt(2));
			return new PVWire(name, price, resultSet.getInt(2));
		default:
			return null;
		}
	}

	// returns all products
	public static HashMap<String, Object> getProducts() {
		return productMap;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
