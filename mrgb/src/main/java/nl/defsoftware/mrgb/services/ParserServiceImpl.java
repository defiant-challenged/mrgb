/**
 * 
 */
package nl.defsoftware.mrgb.services;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import nl.defsoftware.mrgb.view.controllers.MrgbController;

/**
 *
 *
 * @author D.L. Ettema
 * @date 26 May 2016
 */
public class ParserServiceImpl implements ParserService {

	GraphGFAParser graphParser = new GraphGFAParser();
	@Override
	public void loadGraphData() {
		try {
			graphParser.loadResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
		graphParser.parseData();
	}
	
	public HashMap<Short, short[]> getParsedEdges() {
		return graphParser.getParsedEdges();
	}
}