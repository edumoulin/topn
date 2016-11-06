package com.edumoulin.topn;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit Test for writting a file.
 * @author etienne
 *
 */
public class TopNReaderTest {

	private static Logger logger = Logger.getLogger(TopNReaderTest.class);
	

	@BeforeClass
	public static void init(){
		LoggerInit.init();
	}
	
	public static void writeTestFile( File f,int nbLines,boolean deterministic) throws IOException{
		if(!f.exists()){
			Random r = new Random(); 
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = null;
			bw = new BufferedWriter(fw);
			for(int i = 0; i < nbLines;++i){
				if(deterministic){
					bw.write(String.valueOf(i));
				}else{
					bw.write(String.valueOf(r.nextDouble()));
				}
				bw.newLine();
			}
			bw.close();
		}
	}
	
	/**
	 * Check if a file can be written.
	 */
	@Test
	public void testWriteFile(){
		File f = new File("test.txt");
		f.delete();
		try{
			writeTestFile(f,100,false);
			List<Double> top5 = new TopNReader().getTopN(f, 5);
			logger.info("Top 5: "+top5);
			assertTrue("Should have 5 value",top5.size()== 5);
			
			top5 = new TopNReader().getTopN(f, 0);
			logger.info("Empty list: "+top5);
			assertTrue("Should have 0 value",top5.size()== 0);
			
			f.delete();
			writeTestFile(f,4,false);
			top5 = new TopNReader().getTopN(f, 5);
			logger.info("Top 4: "+top5);
			assertTrue("Should have 4 value",top5.size()== 4);
			
			f.delete();
			writeTestFile(f,100,true);
			top5 = new TopNReader().getTopN(f, 5);
			assertTrue("Should have 5 value",top5.size()== 5);
			assertTrue("Min value: 95",top5.get(0)== 95);
			assertTrue("Max value: 99",top5.get(4)== 99);
			
			f.delete();
		}catch(Exception e){
			logger.error(e,e);
			assertTrue("Unexpected error: "+e,false);
		}

	}


}
