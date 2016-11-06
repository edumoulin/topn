package com.edumoulin.topn;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TopNParser {

	private static Logger logger = Logger.getLogger(TopNParser.class);

	/**
	 * Create the command line options.
	 * @return Command line options supported.
	 */
	public static Options createOptions(){
		Options options = new Options();
		options.addOption("h","help",false, MessageManager.getProperty("help.msg"));
		options.addOption("ll","log4j-level",true, MessageManager.getProperty("log4j.msg"));
		options.addOption("f","file",true, MessageManager.getProperty("file.msg"));
		options.addOption("n","topn",true, MessageManager.getProperty("topn.msg"));
		return options;
	}

	/**
	 * Print the help.
	 * @param options Command line options.
	 */
	public static void printHelp(Options options){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("topn", MessageManager.getProperty("help.desc"), options,"" );
	}

	/**
	 * Return a File from the command line argument.
	 * @param cmd The parsed command line arguments.
	 * @return null if there is an error.
	 */
	public static File getFileArgument(CommandLine cmd){
		File file = null;
		String filePath = null;
		try{
			filePath = cmd.getOptionValue("f");
		}catch(Exception e){}
		if(filePath == null){
			logger.warn(MessageManager.getProperty("filepath.missing.msg"));
		}else{
			file = new File(filePath).getAbsoluteFile();
			try{
				Paths.get(filePath);
				file.getCanonicalPath();
			}catch(Exception e){
				file = null;
				logger.error(MessageManager.getProperty("filepath.invalid.msg",new Object[]{filePath}));
			}
			if(file != null){
				if(!file.exists() || !file.isFile()){
					file = null;
					logger.error(MessageManager.getProperty("file.doesnotexist.msg",new Object[]{filePath}));
				}
			}
		}
		return file;
	}


	/**
	 * Parse command line arguments and run
	 * @param args Command line arguments
	 * @return True if the execution has been successful.
	 * @throws Exception
	 */
	public static boolean parseAndRun(String[] args) throws Exception {
		boolean ok = true;
		CommandLineParser parser = new DefaultParser();
		Options options = createOptions();
		CommandLine cmd = null;
		try{
			cmd = parser.parse(options, args);
		}catch(Exception e){
			logger.error(e,e);
			printHelp(options);
			return false;
		}
		String loggerLevel = cmd.getOptionValue("ll");
		if(loggerLevel != null){
			try{
				Logger.getRootLogger().setLevel(Level.toLevel(loggerLevel));
			}catch(Exception e){
				logger.error(MessageManager.getProperty("log4j.invalid.msg",new Object[]{loggerLevel}));
			}
		}else{
			Logger.getRootLogger().setLevel(Level.INFO);
		}
		if(cmd.hasOption("h")){
			printHelp(options);
		}else{

			File file = getFileArgument(cmd);
			String topnStr = null;
			Integer topn = null;
			try{
				topnStr = cmd.getOptionValue("n");
				topn= Integer.valueOf(topnStr);
			}catch(Exception e){
				logger.error("Integer "+topnStr+" unrecognized.");
			}
			if(file == null || topn == null){
				logger.info("File or topn value incorrect");
				printHelp(options);
				ok = false;
			}else{
				List<Double> l = new TopNReader().getTopN(file, topn);
				if(logger.isDebugEnabled()){
					logger.debug(l);
				}
				for(int i = l.size()-1;i >= 0;--i){
					System.out.println(l.get(i));
				}
			}
		}
		return ok;

	}

	public static void main(String[] args) {
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();
		try{
			if(!parseAndRun(args)){
				System.exit(1);
			}
		}catch(Exception e){
			logger.error(MessageManager.getProperty("unexpeced.error.msg",new Object[]{e}),e);
			System.exit(1);
		}
	}

}
