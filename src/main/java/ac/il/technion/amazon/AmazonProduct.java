package ac.il.technion.amazon;

import java.util.*;

//public class AmazonProduct implements Comparable<AmazonProduct> {
public class AmazonProduct {

	private static final String UNDEFINED = "unknown";
	private static final int EQUAL = 0;
	private static final int BEFORE = 1;
	private static final int AFTER = -1;

	private final String productId;
	private final String title;
	private final String price;
	//private final String userId;
	private final String brand;
	private final String category;
	private final String description;
	private List<ProductReview> productReviews;

	private Date firstDate;
	private Date lastDate;

	/*
	 * public AmazonProduct(){ reviews = new LinkedList<Review>(); }
	 */
	public AmazonProduct(String productId, String title, String price,String brand,String category,String description,
						 List<ProductReview> productReviews) {
		// super();
		this.productId = productId;
		this.title = title;
		this.price = price;
		//this.userId = userId;
		this.brand=brand;
		this.category=category;
		this.description=description;
		firstDate = null;
		lastDate = null;

		this.productReviews = new LinkedList<ProductReview>();
		for (ProductReview r : productReviews)
			this.addReview(r);
	}

	public void addReview(ProductReview newReview) {
		productReviews.add(newReview);

		Date newDate = newReview.getTime();

		if (firstDate == null)
			firstDate = newReview.getTime();
		else {
			Date originalFirstDate = firstDate;
			if (newDate.before(originalFirstDate))
				firstDate = newReview.getTime();

		}
		if (lastDate == null)
			lastDate = newReview.getTime();
		else {
			Date originalLastDate = lastDate;
			if (newDate.after(originalLastDate))
				lastDate = newReview.getTime();
		}

	}

	public List<ProductReview> getReviews() {
		return productReviews;
	}

	public String getProductId() {
		return productId;
	}

	public String getTitle() {
		return title;
	}

	public String getPrice() {
		return price;
	}
	
	public String getBrand(){
		return brand;
	}
	public String getCategory(){
		return category;
	}
	public String getDescription(){
		return description;
	}
/*
	public String getUserId() {
		return userId;
	}
*/
	public Date getFirstDate() {
		return firstDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmazonProduct other = (AmazonProduct) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AmazonProduct [productId=" + productId + ", title=" + title
				+ ", price=" + price +  ", reviews="
				+ productReviews + "]";
	}

	public static Comparator<AmazonProduct> ByPriceComparator = new Comparator<AmazonProduct>() {

		public int compare(AmazonProduct p1, AmazonProduct p2) {

			int returnValue;
			String price1 = p1.getPrice();
			String price2 = p2.getPrice();

			if (price1.equals(price2))
				returnValue = EQUAL;

			if (price1.equals(UNDEFINED))
				returnValue = BEFORE;
			if (price2.equals(UNDEFINED))
				returnValue = AFTER;

			returnValue = (Integer.parseInt(price1) - Integer.parseInt(price2));

			return returnValue;
		}
	};

	public static Comparator<AmazonProduct> ByUserIdComparator = new Comparator<AmazonProduct>() {

		public int compare(AmazonProduct p1, AmazonProduct p2) {

			String id1 = p1.getProductId();
			String id2 = p2.getProductId();

			return id1.compareTo(id2);
		}
	};
	public static Comparator<AmazonProduct> ByNumberOfReviewsComparator = new Comparator<AmazonProduct>() {

		public int compare(AmazonProduct p1, AmazonProduct p2) {
			Integer n1 = p1.getReviews().size();
			Integer n2 = p2.getReviews().size();

			return n1.compareTo(n2);
		}
	};
	public static Comparator<AmazonProduct> ByFirstDateComparator = new Comparator<AmazonProduct>() {

		public int compare(AmazonProduct p1, AmazonProduct p2) {
			Date date1 = p1.getFirstDate();
			Date date2 = p2.getFirstDate();

			return date1.compareTo(date2);
		}
	};

	public static Comparator<AmazonProduct> ByLastDateComparator = new Comparator<AmazonProduct>() {

		public int compare(AmazonProduct p1, AmazonProduct p2) {
			Date date1 = p1.getLastDate();
			Date date2 = p2.getLastDate();

			return date1.compareTo(date2);
		}
	};

}
/*
 * @Override public int compareTo(AmazonProduct other) {
 * 
 * // TODO - change numbers final int EQUAL = 0; final int BEFORE = 0; final int
 * AFTER = 0;
 * 
 * if (this == other) return EQUAL;
 * 
 * if(this.id > other.getId()) return BEFORE;
 * 
 * return AFTER; }
 */

// compare by price
/*
 * class SortByPrice implements Comparator<AmazonProduct> {
 * 
 * @Override public int compare(AmazonProduct p1, AmazonProduct p2) { final int
 * BEFORE = 1; final int AFTER = -1; int i = 0; Integer j = 2; // final int
 * EQUAL = 0;
 * 
 * if (p1.getPrice().equals("undefined")) return BEFORE; if
 * (p2.getPrice().equals("undefined")) return AFTER;
 * 
 * int price1 = Integer.parseInt(p1.getPrice()); int price2 =
 * Integer.parseInt(p2.getPrice());
 * 
 * return (price1 - price2); } }
 */
