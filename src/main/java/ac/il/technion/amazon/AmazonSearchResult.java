package ac.il.technion.amazon;

import java.util.ArrayList;

public class AmazonSearchResult {
	
	private ArrayList<AmazonProduct> products;
	
	public AmazonSearchResult(){
		products = new ArrayList<AmazonProduct>();
	}

	public void addProduct(AmazonProduct p){
		products.add(p);
	}
	public void removeProduct(AmazonProduct p){
		products.remove(p);
	}
	public ArrayList<AmazonProduct> getProductsList(){
		return products;
	}
	
	/*
	 * 
	 * Methods For Testing 
	 * 
	 */
	public boolean hasProduct(AmazonProduct p){
		return products.contains(p);
	}
	
	public AmazonProduct getProductWithID(String proID){
		for(AmazonProduct p : products){
			if(p.getProductId().equals(proID)){
				return p;
			}
		}
		return null;
	}
	
	public AmazonProduct getIthProduct(int i){
		if(i>products.size()){
			return null;
		}
		return products.get(i);
	}
}
