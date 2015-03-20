package ac.il.technion.amazon;

import java.util.Comparator;
import java.util.Date;

//import java.util.Comparator;
//implements Comparable<ProductReview>
public class ProductReview {

	private static final String UNDEFINED = "uknown";
	private static final int EQUAL = 0;
	private static final int BEFORE = 1;
	private static final int AFTER = -1;

	// TODO - string or int
	private int id;
	private String userID;
	private final String profileName;
	private final String helpfulness;
	private final String score;
	private final Date time;
	private final String summary;
	private final String text;

	public ProductReview(String userId,int id, String profileName, String helpfulness,
			String score, String time, String summary, String text) {
		// super();
		this.userID = userId;
		this.id = id;
		this.profileName = profileName;
		this.helpfulness = helpfulness;
		this.score = score;
		this.time = new Date((Long.parseLong(time) * 1000));

		this.summary = summary;
		this.text = text;
	}

	public String getUserID(){
		return userID;
	}
	
	public int getId() {
		return id;
	}

	public String getProfileName() {
		return profileName;
	}

	public String getHelpfulness() {
		return helpfulness;
	}

	public String getScore() {
		return score;
	}

	public Date getTime() {
		return time;
	}

	public String getSummary() {
		return summary;
	}

	public String getText() {
		return text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		ProductReview other = (ProductReview) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProductReview [id=" + id + ", profileName=" + profileName
				+ ", helpfulness=" + helpfulness + ", score=" + score
				+ ", time=" + time + ", summary=" + summary + ", text=" + text
				+ "]";
	}
	
	
	// default acsending
	public static Comparator<ProductReview> ByHelpfulnessComparator = new Comparator<ProductReview>() {

		public int compare(ProductReview r1, ProductReview r2) {

			int returnValue;
			String help1 = r1.getHelpfulness();
			String help2 = r2.getHelpfulness();

			if (help1.equals(help2))
				returnValue = EQUAL;

			if (help1.equals(UNDEFINED))
				returnValue = BEFORE;
			if (help2.equals(UNDEFINED))
				returnValue = AFTER;

			returnValue = (int) (Double.parseDouble(help1) - Double
					.parseDouble(help2));

			return returnValue;
			// return (isAscending == true) ? returnValue : returnValue * (-1);
		}
	};

	public static Comparator<ProductReview> ByScoreComparator = new Comparator<ProductReview>() {

		public int compare(ProductReview r1, ProductReview r2) {

			int returnValue;
			String score1 = r1.getScore();
			String score2 = r2.getScore();

			if (score1.equals(score2))
				returnValue = EQUAL;

			if (score1.equals(UNDEFINED))
				returnValue = BEFORE;
			if (score2.equals(UNDEFINED))
				returnValue = AFTER;

			returnValue = (int) (Double.parseDouble(score1) - Double
					.parseDouble(score2));

			return returnValue;
		}

	};

	public static Comparator<ProductReview> ByTimeComparator = new Comparator<ProductReview>() {

		public int compare(ProductReview r1, ProductReview r2) {
			Date time1 = r1.getTime();
			Date time2 = r2.getTime();

			return (time1.compareTo(time2));
		}
	};

	public static Comparator<ProductReview> ByProfileNameComparator = new Comparator<ProductReview>() {

		public int compare(ProductReview r1, ProductReview r2) {
			String name1 = r1.getProfileName();
			String name2 = r2.getProfileName();

			return name1.compareTo(name2);
		}
	};

}