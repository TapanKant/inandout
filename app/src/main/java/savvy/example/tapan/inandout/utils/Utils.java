
package savvy.example.tapan.inandout.utils;

import java.util.ArrayList;
import java.util.List;


public final class Utils {
	public static List<String> generateDataSet(int size) {
		List<String> dataSet = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			dataSet.add("Category " + (i+1));
		}
		return dataSet;
	}
	
	public static List<String> generateDataSetItem(int size) {
		List<String> dataSet = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			dataSet.add("Item " + (i+1));
		}
		return dataSet;
	}
	
	public static List<String> generateDataProducts(int size){
		List<String> dataSet = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			dataSet.add("Product " + (i+1));
		}
		return dataSet;
	}
}
