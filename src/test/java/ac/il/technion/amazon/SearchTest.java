package ac.il.technion.amazon;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.amazon.AmazonProduct;

public class SearchTest {

	AmazonSearch sObj = new AmazonSearch();
		
	String productId1 = "B000GKXY4S" ;
	String title1 = "Crazy Shape Scissor Set";
	String price1 = "unknown";
	String brand1 = "brandA";
	String category1 = "reviews";
	String description1 = "dsgsdgsdgsdgds dsf sedg  sdg 98 et wekjgd ' styuwet";
	List<ProductReview> productReviews = new ArrayList<ProductReview>();
	AmazonProduct p1 = new AmazonProduct(productId1,title1,price1,brand1,category1,description1,productReviews);
	
	
	String productId2 = "B000G6HRZE" ;
	String title2 = "Dritz(R) Extra Large Safety Pins-Size 3 65/Pkg";
	String price2 = "4.43";
	String brand2 = "brandC";
	String category2 = "reviews";
	String description2 = "Just add water!	The Nano Cube is a 24-gallon glass aquarium with specially rounded corners for a sleek, ";
	int reviewsNumber2 = 10;
	AmazonProduct p2 = new AmazonProduct(productId2,title2,price2,brand2,category2,description2,productReviews);

	String productId3 = "12345" ;
	String title3 = "unknown";
	String price3 = "24.0";
	String brand3 = "unknown";
	String category3 = "reviews";
	String description3 = "unknown";
	AmazonProduct p3 = new AmazonProduct(productId3,title3,price3,brand3,category3,description3,productReviews);

	String productId4 = "B000140KIW" ;
	String title4 = "Fiskars Softouch Multi-Purpose Scissors 10\"";
	String price4 = "unknown";
	String brand4 = "brandB";
	String category4 = "reviews";
	String description4 = "Whether you're hoping to obtain a raise from your boss, convince an insurance claim representative to reimburse your";
	AmazonProduct p4 = new AmazonProduct(productId4,title4,price4,brand4,category4,description4,productReviews);

	
	@Before
	public void runBeforeTests(){
		String sourceDirPath = "C:/Users/2012/Desktop/AmazonWebAppProject/TestingDataSet/DataSet_1";
		String targetDirPath= "C:/Users/2012/Desktop/AmazonWebAppProject/TestingDataSet/DataSet_1/Index";
		try {
			File file = new File(targetDirPath);
			if(file.isDirectory()){
				if(file.list().length>0){
					sObj.runBeforeSearchWitBuiltIndex(targetDirPath);
					return;
				}
			}
			sObj.Index(sourceDirPath,targetDirPath);
			sObj.runBeforeSearch();
		} catch (IOException e1) {
			System.out.println("initializing  FAILED ,IOException, BasicSearchTest/test1 ");
		}
	}
	
	@Test
	public void testSearchWithNoTextMethod() {	
		try {
			AmazonSearchResult res = sObj.searchWithNoText();
			assertEquals(res.getProductsList().size(),9);
			
			//check for p1  
			assertTrue(res.hasProduct(p1));
			AmazonProduct p = res.getProductWithID(productId1);
			assertNotNull(p);
			assertEquals(p.getProductId(),productId1);
			assertEquals(p.getBrand(),brand1);
			assertEquals(p.getCategory(),category1);
			assertEquals(p.getTitle(),title1);
			assertEquals(p.getPrice(),price1);
			assertEquals(p.getDescription(),description1);
			assertEquals(p.getReviews().size(),2);
			assertEquals(p.getFirstDate(),new Date((Long.parseLong("1314057600") * 1000)));
			assertEquals(p.getLastDate(),new Date((Long.parseLong("1328659200") * 1000)));

			//check for p2
			assertTrue(res.hasProduct(p2));
			p = res.getProductWithID(productId2);
			assertNotNull(p);
			assertEquals(p.getProductId(),productId2);
			assertEquals(p.getBrand(),brand2);
			assertEquals(p.getCategory(),category2);
			assertEquals(p.getTitle(),title2);
			assertEquals(p.getPrice(),price2);
			assertTrue(p.getDescription().startsWith(description2));
			assertEquals(p.getReviews().size(),10);
			
			//check for p3 
			assertTrue(res.hasProduct(p3));
			p = res.getProductWithID(productId3);
			assertNotNull(p);
			assertEquals(p.getProductId(),productId3);
			assertEquals(p.getBrand(),brand3);
			assertEquals(p.getCategory(),category3);
			assertEquals(p.getTitle(),title3);
			assertEquals(p.getPrice(),price3);
			assertTrue(p.getDescription().startsWith(description3));
			assertEquals(p.getDescription(),description3);
			assertEquals(p.getReviews().size(),1);
			
			
		} catch (IOException e) {
			System.out.println("searchWithNoText() FAILED ,IOException, BasicSearchTest/test1 ");	
		}
	}

	@Test
	public void testSearchUserFreeText(){
		AmazonSearchResult res;
		try {
			res = sObj.searchUserFreeText("Scissor");
			//System.out.println("search for Scissor yields :: "+ res.getProductsList().size());
			assertEquals(res.getProductsList().size(),2);
			assertTrue(res.hasProduct(p1));
			
			
			res = sObj.searchUserFreeText("sciSsors");
			//System.out.println("search for Scissor yields :: "+ res.getProductsList().size());
			assertEquals(res.getProductsList().size(),2);
			assertTrue(res.hasProduct(p1));
			assertTrue(res.hasProduct(p4));
						
			res = sObj.searchUserFreeText("THISDOESNOTAPPEARANYWHERE");
			assertEquals(res.getProductsList().size(),0);
			
			res = sObj.searchUserFreeText("Fiskars Softouch Multi Purpose");
			//System.out.println("search for Scissor yields :: "+ res.getProductsList().size());
			/*for(AmazonProduct pp : res.getProductsList()){
				System.out.println(pp.getTitle());
			}*/
			assertEquals(res.getProductsList().size(),3);

			res = sObj.searchUserFreeText("Fiskars Softouch Multi-Purpose");
			//System.out.println("search for Scissor yields :: "+ res.getProductsList().size());
			assertEquals(res.getProductsList().size(),3);

			res = sObj.searchUserFreeText("      ");
			//System.out.println("search for Scissor yields :: "+ res.getProductsList().size());
			assertEquals(res.getProductsList().size(),0);
			
			res = sObj.searchUserFreeText("");
			assertEquals(res.getProductsList().size(),0);
			
		} catch (IOException e) {
			System.out.println("searchUserFreeText(String s) FAILED ,IOException, BasicSearchTest/test1 ");
		}
	}
	
	@Test
	public void testBasicSearch(){
		try {
			AmazonSearchResult res;
			
			res = sObj.basicSearch("",null);
			assertEquals(res.getProductsList().size(),0);
			
			res = sObj.basicSearch("  ",null);
			assertEquals(res.getProductsList().size(),0);
			
			res = sObj.basicSearch("sciSsors Softouch Multi ",null);
			assertEquals(res.getProductsList().size(),3);
			
			res = sObj.basicSearch("sciSsors Softouch Multi ","reviews");
			assertEquals(res.getProductsList().size(),9);
			
			res = sObj.basicSearch("  ","");
			assertEquals(res.getProductsList().size(),0);

			res = sObj.basicSearch("  "," ");
			assertEquals(res.getProductsList().size(),0);

			res = sObj.basicSearch("sciSsors Softouch Multi ","NoCt");
			assertEquals(res.getProductsList().size(),0);
			res = sObj.basicSearch("     ","NoCt");
			assertEquals(res.getProductsList().size(),0);
			res = sObj.basicSearch(null,"NoCt");
			assertEquals(res.getProductsList().size(),0);
			
			res = sObj.basicSearch("sciSsors","reviews");
			assertEquals(res.getProductsList().size(),9);
			
			res = sObj.basicSearch("    ","reviews");
			assertEquals(res.getProductsList().size(),9);
			res = sObj.basicSearch(null,"reviews");
			assertEquals(res.getProductsList().size(),9);

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSearchSpecificProductID(){
		AmazonSearchResult res;
		try {
			res = sObj.searchSpecificProductID("b000g6hrze");
			assertEquals(res.getProductsList().size(),1);
			
			res = sObj.searchSpecificProductID("B000GKXY4S");
			assertEquals(res.getProductsList().size(),1);
			
			res = sObj.searchSpecificProductID("B010GKXY4S");
			assertEquals(res.getProductsList().size(),1);
						
			res = sObj.searchSpecificProductID("1234");
			assertEquals(res.getProductsList().size(),1);
			
			res = sObj.searchSpecificProductID("14556");
			assertEquals(res.getProductsList().size(),0);

			
			
		} catch (IOException e) {
			System.out.println("SearchSpecificProductID(String s) FAILED ,IOException, BasicSearchTest/test1 ");
		}
		
	}
	
	@Test
	public void testSearchSpecificTitle(){
		AmazonSearchResult res;
		try {
			res= sObj.searchSpecificTitle("Crazy Shape Scissor Set");
		    /*for(AmazonProduct p1 : res.getProductsList()){
					System.out.println(p1.getTitle());
			}*/
			assertEquals(res.getProductsList().size(),4);
			//checks for p1  
			assertTrue(res.hasProduct(p1));
			AmazonProduct p = res.getProductWithID(productId1);
			assertNotNull(p);
			assertEquals(p.getProductId(),productId1);
			assertEquals(p.getBrand(),brand1);
			assertEquals(p.getCategory(),category1);
			assertEquals(p.getTitle(),title1);
			assertEquals(p.getPrice(),price1);
			assertEquals(p.getDescription(),description1);
			assertEquals(p.getReviews().size(),2);
			
			res= sObj.searchSpecificTitle("Scissor");
		  /*for(AmazonProduct p1 : res.getProductsList()){
				System.out.println(p1.getTitle());
			}*/
			assertEquals(res.getProductsList().size(),2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void searchProductWithReviewsInTimeRangeTest(){
		AmazonSearchResult res;
		//assertEquals(minDate.getTime(),(Long.parseLong("1314057600") * 1000));
		//assertEquals(minDate.getTime(),(Long.valueOf("1314057600") * 1000));
		//assertEquals((minDate.getTime())/1000,(Long.valueOf("1314057600") * 1));
		try {
			Date minDate = new Date((Long.parseLong("0") * 1000));
			Date maxDate = new Date((Long.parseLong("1328659200") * 1000));
			res= sObj.searchProductWithReviewsInTimeRange(minDate, maxDate);
			assertTrue(res.getProductsList().size()!=0);
			assertEquals(res.getProductsList().size(),5);
			//check for p1 in res
			assertTrue(res.hasProduct(p1));
			AmazonProduct p = res.getProductWithID(productId1);
			assertNotNull(p);
			assertEquals(p.getProductId(),productId1);
			assertEquals(p.getBrand(),brand1);
			assertEquals(p.getCategory(),category1);
			assertEquals(p.getTitle(),title1);
			assertEquals(p.getPrice(),price1);
			assertEquals(p.getDescription(),description1);
			assertEquals(p.getReviews().size(),2);
			assertEquals(p.getFirstDate(),new Date((Long.parseLong("1314057600") * 1000)));
			assertEquals(p.getLastDate(),new Date((Long.parseLong("1328659200") * 1000)));
			//assert that each product is valid result : 
			for(AmazonProduct pro : res.getProductsList()){
				assertTrue(pro.getFirstDate().getTime()>=minDate.getTime() && pro.getFirstDate().getTime()<=maxDate.getTime());
		        assertTrue(pro.getLastDate().getTime()<=maxDate.getTime() &&  pro.getLastDate().getTime()>=minDate.getTime());
			}
			
			
			Date minDate2 = new Date((Long.parseLong("1314057600") * 1000));
			Date maxDate2 = new Date((Long.parseLong("1328659200") * 1000));
			res= sObj.searchProductWithReviewsInTimeRange(minDate2, maxDate2);
			assertEquals(res.getProductsList().size(),2);
			//assert that each product is valid result : 
			for(AmazonProduct pro : res.getProductsList()){
				assertTrue(pro.getFirstDate().getTime()>=minDate.getTime() && pro.getFirstDate().getTime()<=maxDate.getTime());
		        assertTrue(pro.getLastDate().getTime()<=maxDate.getTime() &&  pro.getLastDate().getTime()>=minDate.getTime());
			}
			/*for(AmazonProduct pro : res.getProductsList()){
				System.out.println("ProductID : "+ pro.getProductId());
				System.out.println("FirstDate : "+pro.getFirstDate());
				System.out.println("LastDate : "+pro.getLastDate());
			}*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void searchProductsAccordingToPriceTest(){
		AmazonSearchResult res;
		try {
			res= sObj.searchProductsAccordingToPrice(null,null,0,44);
			assertEquals(res.getProductsList().size(),6);
			
			res= sObj.searchProductsAccordingToPrice(null,null,0,30);
			assertEquals(res.getProductsList().size(),5);
			
			res= sObj.searchProductsAccordingToPrice("scissor","UnknownCategory",0,30);
			assertEquals(res.getProductsList().size(),0);
			
			res= sObj.searchProductsAccordingToPrice("scissor","reviews",0,30);
			assertEquals(res.getProductsList().size(),5);
			
			res= sObj.searchProductsAccordingToPrice(null,"reviews",0,30);
			assertEquals(res.getProductsList().size(),5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*	
	@Test
	public void searchProductsFromBrandTest(){
		AmazonSearchResult res;
		try {
			res= sObj.searchProductsFromBrand(null, "brand A");
			assertEquals(res.getProductsList().size(),0);
			
			res= sObj.searchProductsFromBrand(null, "brandA");
			assertEquals(res.getProductsList().size(),2);
			
			res= sObj.searchProductsFromBrand("supply books", "brandA");
			assertEquals(res.getProductsList().size(),2);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/	
	@Test
	public void searchProductsReviewedByUserTest(){
		AmazonSearchResult res;
		try {
			res= sObj.searchProductsReviewedByUser("A1QA985ULVCQOB",null);
			assertEquals(res.getProductsList().size(),1);
			
			res= sObj.searchProductsReviewedByUser("NOSUCHUSERid",null);
			assertEquals(res.getProductsList().size(),0); 
			
			res= sObj.searchProductsReviewedByUser("A2M2M4R1KG5WOL",null);
			assertEquals(res.getProductsList().size(),1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void runAfterTests(){
		try {
			sObj.runAfterSearch();
		} catch (IOException e) {
			System.out.println("runAfterSearch() FAILED ,IOException, BasicSearchTest/test1 ");
		}
	}
}
