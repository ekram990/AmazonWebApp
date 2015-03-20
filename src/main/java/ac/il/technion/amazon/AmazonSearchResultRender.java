package ac.il.technion.amazon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/*
 * this class gets a set with N products and arrange them by specific fields
 */

public class AmazonSearchResultRender {

	// private static SortedSet<AmazonProduct> products;

	
	private static ArrayList<AmazonProduct> products;

	private static void init(Set<AmazonProduct> unsortedSet) {
		products = new ArrayList<AmazonProduct>();
		for (AmazonProduct p : unsortedSet)
			products.add(p);
	}

	/*
	 * sort the set by product price
	 */
	public static ArrayList<AmazonProduct> sortByPrice(
			Set<AmazonProduct> unsortedSet, final boolean isAscending) {

		init(unsortedSet);
		if (isAscending)
			Collections.sort(products, AmazonProduct.ByPriceComparator);
		else
			Collections.reverse(products);
		return products;

	}

	/*
	 * sort by user id
	 */
	public ArrayList<AmazonProduct> sortByUser(Set<AmazonProduct> unsortedSet,
			final boolean isAscending) {

		init(unsortedSet);
		if (isAscending)
			Collections.sort(products, AmazonProduct.ByUserIdComparator);
		else
			Collections.reverse(products);
		return products;

	}

	/*
	 * sort by number of reviews
	 */
	public ArrayList<AmazonProduct> sortByNumberOfReviews(
			Set<AmazonProduct> unsortedSet, final boolean isAscending) {

		init(unsortedSet);
		if (isAscending)
			Collections.sort(products, AmazonProduct.ByNumberOfReviewsComparator);
		else
			Collections.reverse(products);
		return products;
		
	}
	
					
					

	/*
	 * sort by first date
	 */
	public ArrayList<AmazonProduct> sortByFirstDate(
			Set<AmazonProduct> unsortedSet, final boolean isAscending) {

		init(unsortedSet);
		if (isAscending)
			Collections.sort(products, AmazonProduct.ByFirstDateComparator);
		else
			Collections.reverse(products);
		return products;
	}
		

	/*
	 * sort by last date
	 */
	public ArrayList<AmazonProduct> sortByLastDate(
			Set<AmazonProduct> unsortedSet, final boolean isAscending) {

		init(unsortedSet);
		if (isAscending)
			Collections.sort(products, AmazonProduct.ByLastDateComparator);
		else
			Collections.reverse(products);
		return products;
		
		
					
	}

	/*
	 * print the sorted set - for debugging
	 */
	public static void printSortedResults(ArrayList<AmazonProduct> products) {
		for (AmazonProduct p : products)
			System.out.println(p.toString());
	}

}
