package com.example.family.furi.ProductObjects;

public class BatteryMeter {
	public String name;
	public double price;
	public String features;
	
	// for nonspecific initialization
	public BatteryMeter() {
	}
	
	public BatteryMeter(String name, double price, String features) {
		this.name = name;
		this.price = price;
		this.features = features;
	}
}
