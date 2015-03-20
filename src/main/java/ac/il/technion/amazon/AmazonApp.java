package ac.il.technion.amazon;

import java.io.IOException;



public class AmazonApp {

	private AmazonSearch amazonSearchObj ;
	
	public AmazonApp(){
		amazonSearchObj = new AmazonSearch();
	}
	
    /*
     * initialize the search app . 
     * builds the index in targetDirPath(reads the data from sourcePath  directory ). 
     * and initialize the iSearcher . 
     * @arguments : 
     * 		sourcePath - string specifying the full path of the data directory .
     * 		targetPath - string specifying the full path of the directory to contain the index .
     * @output : no out put .
     * @Exceptions :  throws IDException .
     *  
     */
	public void initApp(String sourceDirPath, String targetDirPath){
		try {
			amazonSearchObj.Index(sourceDirPath,targetDirPath);
			amazonSearchObj.runBeforeSearch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * closes the application resources and iSearcher.
	 * @arguments : no arguments
	 * @output : no out put .
	 * @Exceptions :  throws IDException .
	 * 
	 */
	public void closeApp(){
		try {
			amazonSearchObj.runAfterSearch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
