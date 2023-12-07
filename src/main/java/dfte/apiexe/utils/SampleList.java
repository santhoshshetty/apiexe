package dfte.apiexe.utils;

import java.util.ArrayList;

public class SampleList {
	ArrayList<sample> listItems = new ArrayList<sample>();

	public ArrayList<sample> getItems() {
		return listItems;
	}

	public void setItems() {
		for (int i = 1; i <= 100; i++) {
			sample sample = new sample();
			sample.setCategory("category" + i);
			sample.setInventory("inventory" + i);
			sample.setMeasurementUnit("measurement" + i);
			sample.setProductId("product" + i);
			sample.setTotalStock("stock" + i);
			sample.setWarehouse("warehouse" + i);
			sample.setWarehouseStock("warehousestock" + i);
			listItems.add(sample);
		}
	}
}
