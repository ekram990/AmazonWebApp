package ac.il.technion.amazon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class AmazonSearch {
   
	AnalyzingSuggester suggester;
	 
	Directory directory;
	Analyzer analyzer;
	IndexSearcher iSearcher;
	DirectoryReader ireader;
	String indexDirPath;

	public AmazonSearch() {
		directory = null;
		analyzer = new StandardAnalyzer(Version.LUCENE_47);
		indexDirPath = null;
		iSearcher = null;
		ireader = null;
	}

	public void runBeforeSearchWitBuiltIndex(String indexDirPath_param)
			throws IOException {
		File path = new File(indexDirPath_param);
		directory = FSDirectory.open(path);
		DirectoryReader ireader = DirectoryReader.open(directory);
		iSearcher = new IndexSearcher(ireader);
	}

	// only after running the Index method .
	public void runBeforeSearch() throws IOException {
		File path = new File(indexDirPath);
		directory = FSDirectory.open(path);
		DirectoryReader ireader = DirectoryReader.open(directory);
		iSearcher = new IndexSearcher(ireader);
	}

	public void runAfterSearch() throws IOException {
		ireader.close();
		directory.close();
	}

	/****************************************************
	 *************** Index Methods **********************
	 ****************************************************/

	public void Index(String sourceDirPath, String targetDirPath)
			throws IOException {
		indexDirPath = targetDirPath;
		// To store an index on disk:
		File path = new File(targetDirPath);
		directory = FSDirectory.open(path);

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,
				analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		DocumentsBuilder builder = new DocumentsBuilder(sourceDirPath);
		Map<String, Document> documents = builder.getDocuments();
		Document doc = null;
		for (Map.Entry<String, Document> entry : documents.entrySet()) {
			doc = entry.getValue();
			iwriter.addDocument(doc);
		}
		iwriter.close();
		directory.close();
	}

	/****************************************************************************/
	/****************** Helping Functions For Search Methods ********************/
	/****************************************************************************/
	private String[] divideTextIntoWords(String text) {
		return text.split(" |-|\\+|_");
	}

	private boolean AllNonNullArg(String s1, String s2, String s3, String s4,
			String s5, String s6) {
		return (s1 != null && s2 != null && s3 != null && s4 != null
				&& s5 != null && s6 != null);
	}

	private List<ProductReview> getReviewsListFromDocument(Document doc)
			throws IOException {
		String rNumAsString = doc.get("num");
		int rNumber = 0;
		if (rNumAsString != null) {
			rNumber = Integer.parseInt(rNumAsString);
		}
		ArrayList<ProductReview> pReviews = new ArrayList<ProductReview>();
		for (int i = 1; i <= rNumber; i++) {
			int id = i;
			String fieldExt = String.valueOf(i);
			String userID = doc.get(("userId" + fieldExt));
			String profileName = doc.get(("profileName" + fieldExt));
			String helpfulness = doc.get(("helpfulness" + fieldExt));
			String score = doc.get(("score" + fieldExt));
			String time = doc.get(("time" + fieldExt));
			String summary = doc.get(("summary" + fieldExt));
			String text = doc.get(("text" + fieldExt));
			if (userID == null
					|| !AllNonNullArg(profileName, helpfulness, score, time,
							summary, text)) {
				throw new IOException();
			}
			ProductReview r = new ProductReview(userID, id, profileName,
					helpfulness, score, time, summary, text);
			pReviews.add(r);
		}
		return pReviews;
	}

	private AmazonSearchResult manipulateMatches(TopDocs matches)
			throws IOException {
		AmazonSearchResult searchResults = new AmazonSearchResult();
		for (ScoreDoc d : matches.scoreDocs) {
			Document doc = iSearcher.doc(d.doc);
			String productId = doc.get("productId");
			String title = doc.get("title");
			String price = doc.get("price");
			if (price.equals("-1.0")) {
				price = "unknown";
			}
			String brand = doc.get("brand");
			String category = doc.get("category");
			String description = doc.get("description");
			if (!AllNonNullArg(productId, title, price, brand, category,
					description)) {
				throw new IOException();
			}
			List<ProductReview> productReviews = getReviewsListFromDocument(doc);
			AmazonProduct product = new AmazonProduct(productId, title, price,
					brand, category, description, productReviews);
			searchResults.addProduct(product);
		}
		return searchResults;
	}

	private BooleanQuery buildQuery(String[] words) {
		MultiPhraseQuery query1 = new MultiPhraseQuery();
		MultiPhraseQuery query2 = new MultiPhraseQuery();
		MultiPhraseQuery query3 = new MultiPhraseQuery();
		MultiPhraseQuery query4 = new MultiPhraseQuery();
		MultiPhraseQuery query5 = new MultiPhraseQuery();
		for (String word : words) {
			query1.add(new Term("title", word));
			query2.add(new Term("description", word));
			query3.add(new Term("reviewsText", word));
			query4.add(new Term("brand", word));
			query5.add(new Term("category", word));
		}
		BooleanQuery bQuery = new BooleanQuery();
		bQuery.add(query1, BooleanClause.Occur.SHOULD);
		bQuery.add(query2, BooleanClause.Occur.SHOULD);
		bQuery.add(query3, BooleanClause.Occur.SHOULD);
		bQuery.add(query4, BooleanClause.Occur.SHOULD);
		bQuery.add(query5, BooleanClause.Occur.SHOULD);
		for (int i = 0; i < words.length; i++) {
			MultiPhraseQuery query11 = new MultiPhraseQuery();
			MultiPhraseQuery query22 = new MultiPhraseQuery();
			MultiPhraseQuery query33 = new MultiPhraseQuery();
			MultiPhraseQuery query44 = new MultiPhraseQuery();
			MultiPhraseQuery query55 = new MultiPhraseQuery();
			query11.add(new Term("title", words[i]));
			query22.add(new Term("description", words[i]));
			query33.add(new Term("reviewsText", words[i]));
			query44.add(new Term("brand", words[i]));
			query55.add(new Term("category", words[i]));
			for (int j = i; j < words.length; j++) {
				query11.add(new Term("title", words[j]));
				query22.add(new Term("description", words[j]));
				query33.add(new Term("reviewsText", words[j]));
				query44.add(new Term("brand", words[j]));
				query55.add(new Term("category", words[j]));
			}
			bQuery.add(query11, BooleanClause.Occur.SHOULD);
			bQuery.add(query22, BooleanClause.Occur.SHOULD);
			bQuery.add(query33, BooleanClause.Occur.SHOULD);
			bQuery.add(query44, BooleanClause.Occur.SHOULD);
			bQuery.add(query55, BooleanClause.Occur.SHOULD);
		}
		for (String word : words) {
			bQuery.add(new FuzzyQuery(new Term("title", word)),BooleanClause.Occur.SHOULD);
			bQuery.add(new FuzzyQuery(new Term("description", word)),BooleanClause.Occur.SHOULD);
			bQuery.add(new FuzzyQuery(new Term("reviewsText", word)),BooleanClause.Occur.SHOULD);
			bQuery.add(new FuzzyQuery(new Term("brand", word)),BooleanClause.Occur.SHOULD);
			bQuery.add(new FuzzyQuery(new Term("category", word)),BooleanClause.Occur.SHOULD);
		}
		return bQuery;
	}

	/****************************************************
	 *************** Basic Search Methods ***************
	 **************************************************** 
	 * @throws IOException
	 *             **************************
	 ****************************************************/

	public AmazonSearchResult searchWithNoText() throws IOException {
		Query query = new MatchAllDocsQuery();
		TopDocs docs = iSearcher.search(query, null, 100);
		return manipulateMatches(docs);
	}

	public AmazonSearchResult searchUserFreeText(String text)
			throws IOException {
		if (text == null) {
			return searchWithNoText();
		}
		String[] words = divideTextIntoWords(text.toLowerCase());
		TopDocs docs = iSearcher.search(buildQuery(words), null, 100);
		return manipulateMatches(docs);
	}

	public AmazonSearchResult basicSearch(String text, String category)
			throws IOException {
		if (category == null || category.length()<=0) {
			return searchUserFreeText(text);
		}
		BooleanQuery bQuery = new BooleanQuery();
		TermQuery query = new TermQuery(new Term("category", category));
		if (text == null || text.length()<=0) {
			bQuery.add(query, BooleanClause.Occur.MUST);
			TopDocs docs = iSearcher.search(bQuery, null, 100);
			return manipulateMatches(docs);
		}
		String[] words = divideTextIntoWords(text.toLowerCase());
		bQuery = buildQuery(words);
		bQuery.add(query, BooleanClause.Occur.MUST);
		TopDocs docs = iSearcher.search(bQuery, null, 100);
		return manipulateMatches(docs);
	}

	public AmazonSearchResult searchSpecificProductID(String productID)
			throws IOException {
		if (productID == null) {
			return searchWithNoText();
		}
		Query fQuery = new FuzzyQuery(new Term("productId",productID.toLowerCase()));
		TopDocs docs = iSearcher.search(fQuery, null, 100);
		return manipulateMatches(docs);
	}

	public AmazonSearchResult searchSpecificTitle(String productTitle)
			throws IOException {
		if (productTitle == null) {
			return searchWithNoText();
		}
		String[] words = divideTextIntoWords(productTitle.toLowerCase());
		BooleanQuery bQuery = new BooleanQuery();
        for(String word : words){
    		Query fQuery = new FuzzyQuery(new Term("title",word));
    		bQuery.add(fQuery,BooleanClause.Occur.MUST);
        }
        BooleanQuery mainQuery = new BooleanQuery();
        mainQuery.add(bQuery,BooleanClause.Occur.SHOULD);
        for(int i=0;i<words.length;i++){
        	BooleanQuery bq = new BooleanQuery();
        	bq.add(new FuzzyQuery(new Term("title",words[i])),BooleanClause.Occur.MUST);
        	mainQuery.add(bq,BooleanClause.Occur.SHOULD);
        }
		TopDocs docs = iSearcher.search(mainQuery, null, 100);
		return manipulateMatches(docs);
	}

	/****************************************************
	 *************** Advanced Search Methods
	 * 
	 * @throws IOException
	 *             ************
	 ****************************************************/

	public AmazonSearchResult searchProductsAccordingToPrice(String text,
			String category, double fromPrice, double toPrice)
			throws IOException {
		BooleanQuery bQuery = new BooleanQuery();
		if(text!=null){
			String[] words = divideTextIntoWords(text.toLowerCase());
			MultiPhraseQuery query1 = new MultiPhraseQuery();
			MultiPhraseQuery query2 = new MultiPhraseQuery();
			MultiPhraseQuery query3 = new MultiPhraseQuery();
			for (String word : words) {
				query1.add(new Term("title", word));
				query2.add(new Term("description", word));
				query3.add(new Term("reviewsText", word));
			}
			bQuery.add(query1, BooleanClause.Occur.SHOULD);
			bQuery.add(query2, BooleanClause.Occur.SHOULD);
			bQuery.add(query3, BooleanClause.Occur.SHOULD);
		}
		
		if(category!=null){
			TermQuery query4 = new TermQuery(new Term("category", category));
			bQuery.add(query4, BooleanClause.Occur.MUST);
		}
		NumericRangeQuery<Double> query5 = NumericRangeQuery.newDoubleRange(
				"price", fromPrice, toPrice, true, true);		
		bQuery.add(query5, BooleanClause.Occur.MUST);
		TopDocs docs = iSearcher.search(bQuery, null, 100);
		return manipulateMatches(docs);
	}
/*
	public AmazonSearchResult searchProductsFromBrand(String text, String brand)
			throws IOException {
		if (brand == null) {
			return searchUserFreeText(text);
		}
		BooleanQuery bQuery = new BooleanQuery();
		TermQuery query = new TermQuery(new Term("brand", brand));
		bQuery.add(query, BooleanClause.Occur.MUST);
		if (text == null) {
			TopDocs docs = iSearcher.search(bQuery, null, 100);
			return manipulateMatches(docs);
		}
		String[] words = divideTextIntoWords(text.toLowerCase());
		MultiPhraseQuery query1 = new MultiPhraseQuery();
		MultiPhraseQuery query2 = new MultiPhraseQuery();
		MultiPhraseQuery query3 = new MultiPhraseQuery();
		for (String word : words) {
			query1.add(new Term("title", word));
			query2.add(new Term("description", word));
			query3.add(new Term("reviewsText", word));
		}
		bQuery.add(query1, BooleanClause.Occur.SHOULD);
		bQuery.add(query2, BooleanClause.Occur.SHOULD);
		bQuery.add(query3, BooleanClause.Occur.SHOULD);
		TopDocs docs = iSearcher.search(bQuery, null, 100);
		return manipulateMatches(docs);
	}*/

	public AmazonSearchResult searchProductsReviewedByUser(String userID,
			String category) throws IOException {
		if (userID == null) {
			throw new IOException();
		}
		BooleanQuery bQuery = new BooleanQuery();
		TermQuery query = new TermQuery(new Term("users", userID));
		bQuery.add(query, BooleanClause.Occur.MUST);
		if (category != null) {
			TermQuery query2 = new TermQuery(new Term("category", category));
			bQuery.add(query2, BooleanClause.Occur.MUST);
		}
		TopDocs docs = iSearcher.search(bQuery, null, 100);
		return manipulateMatches(docs);
	}

	public AmazonSearchResult searchProductWithReviewsInTimeRange(Date l_limit,Date h_limit) throws IOException{
		Long t1 = (l_limit.getTime())/1000;
		Long t2 = (h_limit.getTime())/1000;
			
		BooleanQuery mainQuery = new BooleanQuery();
		NumericRangeQuery<Long> query1 = NumericRangeQuery.newLongRange("FirstReviewTime", t1, t2, true, true);
		NumericRangeQuery<Long> query2 = NumericRangeQuery.newLongRange("LastReviewTime", t1, t2, true, true);
		mainQuery.add(query1, BooleanClause.Occur.MUST);
		mainQuery.add(query2, BooleanClause.Occur.MUST);
	
		TopDocs docs = iSearcher.search(mainQuery, null, 100);
		return manipulateMatches(docs);
	}

	/*******************************************************
	 *************** Methods for Testing ************
	 *******************************************************/


}
