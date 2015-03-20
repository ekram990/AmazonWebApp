package ac.il.technion.amazon;

public class AmazonSortOptions {
	// options to sort AmazonProducts
	// price , popularity , time
	// 0- off 1-on
	// descending/ascending order
	private boolean ascendingPriceOrder;
	private boolean descendingPriceOrder;

	private boolean ascendingPopularityOrder;
	private boolean descendingPopularityOrder;

	private boolean ascendingTimeOrder;
	private boolean descendingTimeOrder;

	public AmazonSortOptions() {
		ascendingPriceOrder = false;
		descendingPriceOrder =false;
		ascendingPopularityOrder = false;
		descendingPopularityOrder = false;
		ascendingTimeOrder = false;
		descendingTimeOrder = false;
	}

	public static final AmazonSortOptions ASCENDING_PRICE_ORDER = new AmazonSortOptions()
			.setOnAscendingPriceOrder();
	public static final AmazonSortOptions ASCENDING_POPULARITY_ORDER = new AmazonSortOptions()
			.setOnAscendingPopularityOrder();
	public static final AmazonSortOptions ASCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnAscendingTimeOrder();

	public static final AmazonSortOptions DESCENDING_PRICE_ORDER = new AmazonSortOptions()
			.setOnDescendingPriceOrder();
	public static final AmazonSortOptions DESCENDING_POPULARITY_ORDER = new AmazonSortOptions()
			.setOnDescendingPopularityOrder();
	public static final AmazonSortOptions DESCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnDescendingTimeOrder();

	public static final AmazonSortOptions ASCENDING_PRICE_ORDER_AND_ASCENDING_POPULARITY_ORDER = new AmazonSortOptions()
			.setOnAscendingPriceOrder().setOnAscendingPopularityOrder();
	public static final AmazonSortOptions ASCENDING_PRICE_ORDER_AND_DESCENDING_POPULARITY_ORDER = new AmazonSortOptions()
			.setOnAscendingPriceOrder().setOnDescendingPopularityOrder();
	public static final AmazonSortOptions ASCENDING_PRICE_ORDER_AND_ASCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnAscendingPriceOrder().setOnAscendingTimeOrder();
	public static final AmazonSortOptions ASCENDING_PRICE_ORDER_AND_DESCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnAscendingPriceOrder().setOnDescendingTimeOrder();

	public static final AmazonSortOptions DESCENDING_PRICE_ORDER_AND_ASCENDING_POPULARITY_ORDER = new AmazonSortOptions()
			.setOnDescendingPriceOrder().setOnAscendingPopularityOrder();
	public static final AmazonSortOptions DESCENDING_PRICE_ORDER_AND_DESCENDING_POPULARITY_ORDER = new AmazonSortOptions()
			.setOnDescendingPriceOrder().setOnDescendingPopularityOrder();
	public static final AmazonSortOptions DESCENDING_PRICE_ORDER_AND_ASCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnDescendingPriceOrder().setOnAscendingTimeOrder();
	public static final AmazonSortOptions DESCENDING_PRICE_ORDER_AND_DESCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnDescendingPriceOrder().setOnDescendingTimeOrder();

	public static final AmazonSortOptions ASCENDING_POPULARITY_ORDER_AND_ASCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnAscendingPopularityOrder().setOnAscendingTimeOrder();
	public static final AmazonSortOptions ASCENDING_POPULARITY_ORDER_AND_DESCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnAscendingPopularityOrder().setOnDescendingTimeOrder();

	public static final AmazonSortOptions DESCENDING_POPULARITY_ORDER_AND_ASCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnDescendingPopularityOrder().setOnAscendingTimeOrder();
	public static final AmazonSortOptions DESCENDING_POPULARITY_ORDER_AND_DESCENDING_TIME_ORDER = new AmazonSortOptions()
			.setOnDescendingPopularityOrder().setOnDescendingTimeOrder();

	private AmazonSortOptions setOnAscendingPriceOrder() {
		ascendingPriceOrder = true;
		return this;
	}

	private AmazonSortOptions setOnDescendingPriceOrder() {
		descendingPriceOrder = true;
		return this;
	}

	private AmazonSortOptions setOnAscendingPopularityOrder() {
		ascendingPopularityOrder = true;
		return this;
	}

	private AmazonSortOptions setOnDescendingPopularityOrder() {
		descendingPopularityOrder = true;
		return this;
	}

	private AmazonSortOptions setOnAscendingTimeOrder() {
		ascendingTimeOrder = true;
		return this;
	}

	private AmazonSortOptions setOnDescendingTimeOrder() {
		descendingTimeOrder = true;
		return this;
	}
/*
	private AmazonSortOptions setOffAscendingTimeOrder() {
		ascendingTimeOrder = false;
		return this;
	}

	private AmazonSortOptions setOffDescendingTimeOrder() {
		descendingTimeOrder = false;
		return this;
	}
	
	private AmazonSortOptions setOffAscendingPopularityOrder() {
		ascendingPopularityOrder = false;
		return this;
	}

	private AmazonSortOptions setOffDescendingPopularityOrder() {
		descendingPopularityOrder = false;
		return this;
	}
	
	private AmazonSortOptions setOffAscendingPriceOrder() {
		ascendingPriceOrder = false;
		return this;
	}

	private AmazonSortOptions setOffDescendingPriceOrder() {
		descendingPriceOrder = false;
		return this;
	}
*/
	public boolean ascendingPriceOrder() {
		return ascendingPriceOrder;
	}

	public boolean descendingPriceOrder() {
		return descendingPriceOrder;
	}

	public boolean ascendingPopularityOrder() {
		return ascendingPopularityOrder;
	}

	public boolean descendingPopularityOrder() {
		return descendingPopularityOrder;
	}

	public boolean ascendingTimeOrder() {
		return ascendingTimeOrder;
	}

	public boolean descendingTimeOrder() {
		return descendingTimeOrder;
	}

}
