package ac.il.technion.amazon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexableField;

public class DocumentsBuilder {

	private Map<String, Document> documentsMap = new HashMap<String, Document>();
	
	public DocumentsBuilder(String dataSetDirPath) throws FileNotFoundException, IOException{
		BuildDocuments(dataSetDirPath);
	}

	private boolean isLongField(String fieldName){
		if(fieldName.startsWith("time")||fieldName.startsWith("FirstReviewTime")||fieldName.startsWith("LastReviewTime"))
			return true;
		return false;
	}
	private boolean isIntField(String fieldName){
		if(fieldName.startsWith("num"))
			return true;
		return false;
	}
	private boolean isDoubleField(String fieldName){
		if(fieldName.startsWith("price")||fieldName.startsWith("helpfulness")||fieldName.startsWith("score"))
			return true;
		return false;
	}
	private boolean isFieldShouldBeTokenized(String fieldName){
		if(fieldName.startsWith("price")|| fieldName.startsWith("users")||fieldName.startsWith("FirstReviewTime")||fieldName.startsWith("LastReviewTime")||
				fieldName.startsWith("helpfulness")||fieldName.startsWith("score")||fieldName.startsWith("time")||fieldName.startsWith("num"))
			return false;
		return true;
	}

	private String[] getNameAndValue(String line) {
		//System.out.println("the line is  " + line);
		String[] parts = line.split(": ");
		String fieldValue = "unknown";
		if(parts.length==2 && !parts[1].isEmpty() && !parts[1].equals(" "))
			fieldValue = parts[1];
		String fieldName = parts[0].split("/")[1];
		String[] list = { fieldName, fieldValue };
		return list;
	}
	
	private FieldType setFieldTypeOptions(String fieldName){
		FieldType type;
		type = new FieldType();
		type.setIndexed(true);
		type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		type.setStored(true);
		type.setTokenized(false);
		if(isFieldShouldBeTokenized(fieldName))
			type.setTokenized(true);
		if(isDoubleField(fieldName))
			type.setNumericType(FieldType.NumericType.DOUBLE);
		if(isIntField(fieldName))
			type.setNumericType(FieldType.NumericType.INT);
		if(isLongField(fieldName))
			type.setNumericType(FieldType.NumericType.LONG);
		type.setStored(true);
		return type;
	}
	
	private double getDoubleValueFromString(String fieldName,String fieldValue){
		if(fieldValue==null || fieldValue.isEmpty() || fieldValue.equals("unknown"))
			return -1.0;
		if(fieldName.startsWith("helpfulness")){
			String[] nums = fieldValue.split("/");
			int num1 = Integer.valueOf(nums[0]).intValue();
			int num2 = Integer.valueOf(nums[1]).intValue();
			if(num1==0)
				return 0;
			if(num2==0)
				return -1.0;
			return num1/num2;
		}
		//if(fieldName.startsWith("score")) 9.0
		//if(fieldName.startsWith("price")) 8.8
		double d = Double.valueOf(fieldValue);
		return d;
	}
	private Field  buildField(String fieldName,String fieldValue){
		FieldType type = setFieldTypeOptions(fieldName);
		if(isLongField(fieldName)){;
			Long lV = Long.valueOf(fieldValue);
			return new LongField(fieldName, lV,type);
		}else if(isDoubleField(fieldName)){
			double dV = getDoubleValueFromString(fieldName,fieldValue);
			return new DoubleField(fieldName,dV,type);
		}else if(isIntField(fieldName)){
			int iV = Integer.valueOf(fieldValue).intValue();
			return new IntField(fieldName,iV,type);
		}
		return new Field(fieldName, fieldValue, type);
	}
	//review line - from all.txt.gz
	private void addField(Document doc, String line) {
		String[] res = getNameAndValue(line);
		String fieldName = res[0];
		String fieldValue = res[1];
        Field f = buildField(fieldName,fieldValue);
        float boost_num = 1.5F;
        if(fieldName.equals("title")||fieldName.equals("category")||fieldName.equals("description")||fieldName.equals("brand")||fieldName.startsWith("summary"))
        	f.setBoost(boost_num);
		doc.add(f);
	}

	private void addField(Document doc, String fieldName, String fieldValue) {
		Field f = buildField(fieldName,fieldValue);
		doc.add(f);
	}
	/*
	//review line - from all.txt.gz
	private void addField(Document doc, String line) {
		String[] res = getNameAndValue(line);
		String fieldName = res[0];
		String fieldValue = res[1];

		FieldType type = setFieldTypeOptions(fieldName);
		doc.add(new Field(fieldName, fieldValue, type));
	}

	private void addField(Document doc, String fieldName, String fieldValue) {
		FieldType type = setFieldTypeOptions(fieldName);
		doc.add(new Field(fieldName, fieldValue, type));
	}*/

	private String[] getIDAndBrand(String line) {
		//System.out.println("the line is  " + line);
		String[] parts = line.split(" ");
		String productID = parts[0];
		String productBrand = parts[1] ; 
		for(int i=2;i<parts.length;i++)
			productBrand = productBrand + parts[i];
		String[] list = { productID, productBrand };
		return list;
	}
	//input : path - the path of the brands.txt.gz file 
	private void updateProductBrands(String path) throws FileNotFoundException, IOException{
		GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(path));
		BufferedReader reader = new BufferedReader(new InputStreamReader(gzip));
		String line = reader.readLine();
		while (line != null) {
			String[] res = getIDAndBrand(line);
			String productId = res[0];
			String productBrand = res[1];
			if (!documentsMap.containsKey(productId)){ 
				line = reader.readLine();
				continue;
			}
			Document doc = documentsMap.get(productId);
			doc.removeField("brand");
			addField(doc,"brand", productBrand);
			line = reader.readLine();
			if(line==null || line.isEmpty()){
				break;
			}
		}
      reader.close();
	}
	//input : path - the path of the descriptions.txt.gz file 
	private void updateProductdescriptions(String path) throws FileNotFoundException, IOException{
		GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(path));
		BufferedReader reader = new BufferedReader(new InputStreamReader(gzip));
		String line = reader.readLine();
		while (line != null) {
			String productId = line.split(": ")[1];
			line = reader.readLine();
			String productDesc = line.split(": ")[1];
			line = reader.readLine();
			if (!documentsMap.containsKey(productId)) {
				line = reader.readLine();
				continue;
			}
			Document doc = documentsMap.get(productId);
			doc.removeField("description");
			addField(doc,"description", productDesc);
			line = reader.readLine();
			if(line==null || line.isEmpty()){
				break;
			}	
		}
		reader.close();
	}
	
	private boolean isReviewsFile(String fileName){
    	if(fileName.startsWith("brands.txt")||fileName.startsWith("categories.txt")||fileName.startsWith("descriptions.txt")
    			||fileName.startsWith("related.txt")||fileName.startsWith("titles.txt"))
    		return false;
    	return true;
    		 
	}
	
	//input:fileName should be {Name}.txt.gz 
	private String getCtegoryFromFileName(String fileName){
		String[] parts = fileName.split(".txt.gz");
		return parts[0];
	}
	private void readAllGzipReviewFiles(String DirPath) throws FileNotFoundException, IOException{
		File dirFile = new File(DirPath);
		String[] extensions = new String[1];extensions[0]="gz";
		Iterator<File> it = FileUtils.iterateFiles(dirFile,extensions, false);

        while(it.hasNext()){
        	String fileName = it.next().getName();
        	assert(fileName.endsWith(".gz"));
        	String path = DirPath + "/" + fileName ;
        	if(!isReviewsFile(fileName)){
        		continue;
        	}
    		GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(path));
        	readReviews(gzip,getCtegoryFromFileName(fileName));	
        }
	}

	private void readReviews(InputStream gzip,String category) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(gzip));
		String line = reader.readLine();
        
		while (line != null) {	
			Document doc = new Document();
			String productId = (getNameAndValue(line))[1];
			// if product doesn't exists
			if (!documentsMap.containsKey(productId)) {
				documentsMap.put(productId, doc);
				addField(doc,"category",category);
				for (int i = 0; i < 3; i++) {
					addField(doc, line);
					line = reader.readLine();
				}
				// add a new field for number of reviews
				addField(doc, "num", "1");
				// Add a Numeric Field : FirstReviewTime 
				doc.add(buildField("FirstReviewTime","0"));
				// Add a NumericField : lastReviewTime 
				doc.add(buildField("LastReviewTime","0"));

			} else {
				doc = documentsMap.get(productId);
				int oldNum = Integer.parseInt(doc.getField("num").stringValue());
				String newNum = String.valueOf((oldNum+1));
				doc.removeField("num");
				addField(doc, "num", newNum);
				for (int i = 0; i < 3; i++) {
					line = reader.readLine();
				}
			}
			// add a new review
			for (int i = 3; i < 10; i++) {
				String[] res = getNameAndValue(line);
				addField(doc, res[0] + (doc.get("num")), res[1]);
				if(i==9){
					addField(doc,"reviewsText",res[1]);
				}
				if(i==3){
					addField(doc,"users",res[1]);
				}
				if(i==7){
					long t = Long.valueOf(res[1]);
					//Update Numeric Field : FirstReviewTime 
					Long first_t = Long.valueOf(doc.getField("FirstReviewTime").stringValue());
					if(t<first_t ||(first_t<=0 && t>=0)){
						doc.removeField("FirstReviewTime");
						doc.add(buildField("FirstReviewTime",res[1]));
					}
					//Update NumericField : lastReviewTime 
					//Long last_t = doc.getField("LastReviewTime").numericValue().longValue();
					Long last_t = Long.valueOf(doc.getField("LastReviewTime").stringValue());
					if(t>last_t){
						doc.removeField("LastReviewTime");
						doc.add(buildField("LastReviewTime",res[1]));
					}
				}
				line = reader.readLine();
			}
			// get rid of the empty line
			line = reader.readLine();
			if(line==null )//then we have reached end of file 
				break;
			else if(line.isEmpty())
				throw new UnsupportedOperationException("Invalid Input File");
		}
	}
	private double getDoubleValueOfHelpfulnessString(String h){
		if(h==null || h.isEmpty() || h.equals("unknown"))
			return -1.0;
			String[] nums = h.split("/");
			int num1 = Integer.valueOf(nums[0]).intValue();
			int num2 = Integer.valueOf(nums[1]).intValue();
			if(num1==0)
				return 0;
			if(num2==0)
				return -1.0;
			return num1/num2;
	}
	private boolean isValidReviewDetails(int i,Document pDoc,String[] reviewDetails){
		String helpfulness = String.valueOf(getDoubleValueOfHelpfulnessString(reviewDetails[2]));
		return (pDoc.getField("userId"+String.valueOf(i)).stringValue().equals(reviewDetails[0]) && 
				pDoc.getField("profileName"+String.valueOf(i)).stringValue().equals(reviewDetails[1]) && 
				pDoc.getField("helpfulness"+String.valueOf(i)).stringValue().equals(helpfulness) &&
				pDoc.getField("score"+String.valueOf(i)).stringValue().equals(reviewDetails[3]) &&
				pDoc.getField("summary"+String.valueOf(i)).stringValue().equals(reviewDetails[5]) &&
				pDoc.getField("text"+String.valueOf(i)).stringValue().equals(reviewDetails[6]));
	}
	
	private void setProductsBrands(){
		for (Map.Entry<String, Document> entry : documentsMap.entrySet()){
			Document pDocument = entry.getValue();
			addField(pDocument,"brand","unknown");
		}
	}
	private void setProductsDescription(){
		for (Map.Entry<String, Document> entry : documentsMap.entrySet()){
			Document pDocument = entry.getValue();
			addField(pDocument,"description","unknown");
		}
	}
	private void BuildDocuments(String dataSetDirPath) throws FileNotFoundException, IOException{
		readAllGzipReviewFiles(dataSetDirPath);
		String brandsPath = dataSetDirPath +"/brands.txt.gz"; 
		String descriptionsPath = dataSetDirPath +"/descriptions.txt.gz";	
		setProductsBrands();
		setProductsDescription();
		updateProductBrands(brandsPath);
		updateProductdescriptions(descriptionsPath);
		
	}
	
	public Map<String, Document> getDocuments(){
		return documentsMap;
	}
	
	public long getNumOfDocuments(){
		return documentsMap.size();
	}
	
	public boolean hasProduct(String productID){
		return (documentsMap.get(productID)!=null);
	}
	
	public boolean isValidProductDetails(String productID,String title,String price){
		String p=price;
		if(price!=null && price.equals("unknown")){
			p = "-1.0";
		}
		Document doc = documentsMap.get(productID);
		return (doc!=null && doc.getField("productId").stringValue().equals(productID) &&
					doc.getField("title").stringValue().equals(title) &&
					doc.getField("price").stringValue().equals(p));				
	}

	public boolean hasReview(String productID,String[] reviewDetails){
		int reviewsNumber =  getProductReviewsNumber(productID);
		for(int i=1;i<=reviewsNumber;i++){
			Document productDoc = documentsMap.get(productID);
			if(productDoc.getField("time"+String.valueOf(i)).stringValue().equals(reviewDetails[4])){
				return isValidReviewDetails(i,productDoc,reviewDetails);
			}	
		}
		return false;
	}
	
	public int getProductReviewsNumber(String productID){
		return Integer.parseInt((documentsMap.get(productID).getField("num").stringValue()));
	}
	
	public boolean isProductDescription(String pID,String pDesc){
		IndexableField field = documentsMap.get(pID).getField("description");
	    return (field.stringValue().equals(pDesc));
	}
	
	public boolean isProductBrand(String pID,String pBrand){
		IndexableField field = documentsMap.get(pID).getField("brand");
		return (field.stringValue().equals(pBrand));	
	}
}


