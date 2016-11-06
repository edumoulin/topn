package com.edumoulin.topn;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for parsing.
 * @author etienne
 *
 */
public class TopNParserTest {
	
	private static Logger logger = Logger.getLogger(TopNParserTest.class);
	
	protected CommandLineParser parser = new DefaultParser();
	protected Options options = TopNParser.createOptions();


	@BeforeClass
	public static void init(){
		LoggerInit.init();
	}
	
	
	/**
	 * Parse File argument and test validity
	 * @param args File arguments such as "-f" "test.txt"
	 * @param notNull True if a not null answer is expected
	 * @throws ParseException
	 */
	public void testFileArgument(String[] args,boolean notNull) throws ParseException{
		CommandLine cmd = null;
		try{
			cmd = parser.parse(options, args);
		}catch(Exception e){
			assertFalse(notNull);
		}
		if(cmd != null){
			if(notNull){
				assertTrue("Test correct file arguments: "+Arrays.toString(args),
						TopNParser.getFileArgument(cmd) != null);
			}else{
				assertTrue("Test incorrect file arguments: "+Arrays.toString(args),
						TopNParser.getFileArgument(cmd) == null);
			}
		}
	}

	/**
	 * Test the file argument parsing.
	 */
	@Test
	public void testFileArgument(){
		try{
			new File("test.txt").createNewFile();
			testFileArgument(new String[]{"-f","test.txt"},true);
			testFileArgument(new String[]{"-f"},false);
			testFileArgument(new String[]{},false);
			
			new File("test.txt").delete();
			testFileArgument(new String[]{"-f","test.txt"},false);
		}catch(Exception e){
			logger.error(e,e);
			assertTrue("Unexpected error: "+e,false);
		}
	}	

	/**
	 * Test a full run.
	 * @param args Command line arguments
	 * @param ok True, if it is expected to run.
	 * @throws Exception
	 */
	public void testFullRun(String[] args,boolean ok) throws Exception{
		if(ok){
			assertTrue("Full run correct, arguments: "+Arrays.toString(args),
					TopNParser.parseAndRun(args));
		}else{
			assertFalse("Full run incorrect, arguments: "+Arrays.toString(args),
					TopNParser.parseAndRun(args));
		}
	}
	
	/**
	 * Test parser and run.
	 */
	@Test
	public void testParserFunction(){
		try{
			File f = new File("test.txt").getAbsoluteFile();
			f.delete();
			TopNReaderTest.writeTestFile(f,100,true);
			testFullRun(new String[]{"-f","test.txt","-n","0"},true);
			testFullRun(new String[]{"-f","test.txt","-n","3"},true);
			testFullRun(new String[]{"-f","test.txt","-n","10"},true);
			testFullRun(new String[]{"-f","test.txt","-n","40"},true);
			
			testFullRun(new String[]{"-f","test.txt"},false);
			
			f.delete();
			testFullRun(new String[]{"-f","test.txt","-n","40"},false);
		}catch(Exception e){
			logger.error(e,e);
			assertTrue("Unexpected error: "+e,false);
		}
	}
}
