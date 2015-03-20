package ac.il.technion.amazon;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.junit.Test;

public class DocumentsBuilderTest {

	@Test
	public void test() {
		try {
			DocumentsBuilder d = new DocumentsBuilder("C:/Users/2012/Desktop/AmazonWebAppProject/TestingDataSet/DataSet_1");
			assertEquals(d.getNumOfDocuments(),9);
			assertTrue(d.hasProduct("B000GKXY4S"));
			assertEquals(d.getProductReviewsNumber("B000GKXY4S"),2);
			assertTrue(d.isValidProductDetails("B000GKXY4S", "Crazy Shape Scissor Set", "unknown"));
			String desc = "dsgsdgsdgsdgds dsf sedg  sdg 98 et wekjgd ' styuwet";
			assertTrue(d.isProductDescription("B000GKXY4S", desc));
			
			String[] reviewDetails = {"A3JDQGJSSPHZBU","Michael","0/0","4.0","1325894400","Spectra Artkraft Just What I Needed",
										"I was looking for black kraft paper to use as dustcovers on the back of my paintings and they were all " +
										"very expensive especially by the time you paid to have it shipped. Amazon had this one and it works great! " +
										"I would also like to find it in the 24\" width/roll. If you need a heavy weight kraft paper, this one will not disappoint!"};
			assertFalse(d.hasReview("B000GKXY4S", reviewDetails));
			assertTrue(d.hasReview("B000J0B1OA", reviewDetails));
			
			assertTrue(d.hasProduct("12345"));
			assertEquals(d.getProductReviewsNumber("12345"),1);
			assertTrue(d.isValidProductDetails("12345", "unknown", "24.0"));
			assertTrue(d.isProductDescription("12345", "unknown"));
			assertTrue(d.isProductBrand("12345", "unknown"));
			
			assertTrue(d.isProductBrand("B000G6HRZE" ,"brandC"));
			assertFalse(d.isProductBrand("B000140KIW", "necessary objects"));
			
			assertTrue(d.isProductBrand("B000J0B1OA" ,"unknown"));
			assertFalse(d.isProductBrand("B000J0B1OA", "Canyon Ridge"));
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printing_test_2() {
		
		DocumentsBuilder d =null;
		try {
			d = new DocumentsBuilder("C:/Users/2012/Desktop/AmazonWebAppProject/TestingDataSet/DataSet");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Map<String, Document> documents = d.getDocuments();
		
		System.out.println("Number of documents in DocumentsMap "+documents.size());
		assertEquals(documents.size(),2);
		
		for (Map.Entry<String, Document> entry : documents.entrySet()){
			String productKey=entry.getKey();
			Document productDocument = entry.getValue();
			System.out.println("ProductID(Key In Map) is : "+productKey +" ** "+ "Fields are :");
			for(IndexableField field : productDocument.getFields()){
				System.out.println(field.name()+" : "+field.stringValue());
			}
			System.out.println("  ");
			System.out.println("  ");
		}
		
	}

}
