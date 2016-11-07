package com.edumoulin.topn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.collections4.list.TreeList;
import org.apache.log4j.Logger;

/**
 * TopN File Handling Class
 * 
 * @author etienne
 *
 */
public class TopNReader {

	private static Logger logger = Logger.getLogger(TopNReader.class);
	
	/**
	 * Dichotomic Search in an array.
	 * 
	 * Note that there is an ambiguity for the last index.
	 * 
	 * @param l list
	 * @param min minimum index
	 * @param max maximum index
	 * @param value the value to search
	 * @return index on which the value has to inserted
	 */
	public int search(List<Double> l, int min, int max, double value){
		if(min == max){
			logger.trace("Final result: "+min);
			return min;
		}
		
		logger.trace("Dichotomic search (min,max,value): "+min+","+max+","+value);
		int mid = min + (max - min)/2;
		double eval = l.get(mid);
		logger.trace("Eval mid "+mid+": "+eval);
		if(eval < value){
			min = mid+1;
		}else{
			max = mid;
		}
		
		return search(l,min,max,value);
	}
	
	/**
	 * Insert into a sorted list
	 * @param l The list
	 * @param curVal The value to insert
	 */
	protected void insertIntoArray(List<Double> l, double curVal){
		int index = search(l,0,l.size()-1,curVal);
		if(index == l.size()-1 && l.get(index) < curVal  ){
			l.add(index+1,curVal);
		}else{
			l.add(index,curVal);
		}
	}
	
	/**
	 * Get the topn value of a file.
	 * @param f The file to read
	 * @param topN The number of value to return
	 * @return The list of value in ascending order
	 * @throws Exception
	 */
	public List<Double> getTopN(File f, int topN) throws Exception{
		List<Double> lAns = new TreeList<Double>();
		if(topN > 0){
			FileReader fr = new FileReader(f);
			BufferedReader br = null;
			try{
				br = new BufferedReader(fr);
				String line = null;
				Double minValue = null;
				while( (line = br.readLine()) != null){
					try{
						double curVal = Double.valueOf(line);
						if(minValue == null){
							if(lAns.isEmpty()){
								lAns.add(curVal);
							}else{
								insertIntoArray(lAns,curVal);
							}
							if(lAns.size() == topN){
								minValue = lAns.get(0);
								if(logger.isDebugEnabled()){
									logger.debug("Init list (min "+minValue+"): "+lAns);
								}
							}
						}else if(minValue < curVal){
							insertIntoArray(lAns,curVal);
							lAns.remove(0);
							minValue = lAns.get(0);
							if(logger.isDebugEnabled()){
								logger.debug("Reset list (min"+minValue+"): "+lAns);
							}
						}
						
					}catch(Exception e){
						if(logger.isDebugEnabled()){
							logger.debug(e,e);
							logger.debug(line+" not a double, value ignored");
						}
					}
				}
			}catch(Exception e){
				logger.debug(e,e);
				br.close();
				throw e;
			}
			br.close();
		}
		return lAns;
	}
}
