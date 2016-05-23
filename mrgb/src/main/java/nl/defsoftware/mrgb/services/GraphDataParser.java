package nl.defsoftware.mrgb.services;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import nl.defsoftware.mrgb.models.Sequence;

public class GraphDataParser implements Parser {
	
	/* Local Main only */
	private Properties properties = new Properties();
	private static String PREFIX_PATH = "src/main/resources/";
	private static String GRAPH_DATA = "data.graph.aligned.gfa";
	
	/* Reader for the datasource */
	private BufferedReader reader = null;
	private String SEQUENCE = "S";
	private String LINK = "L";
	
	private static int GFA_LINE_INDICATOR = 0;
	private static int GFA_FROM_NODE = 1;
	private static int GFA_SEQUENCE = 2;
	private static int GFA_TO_NODE = 3;
	private static int GFA_ORI = 4;
	private static int GFA_CRD = 5;
	private static int GFA_CRDCTG = 6;
	private static int GFA_CTG = 7;
	private static int GFA_START = 8;
	
	private HashMap<Short, short[]> links;
	private HashMap<Short, Sequence> sequences;
	
	public static void main(String [] args) throws Exception {
		GraphDataParser p = new GraphDataParser();
		p.loadProperties();
		p.loadResource();
		p.parseData();
		
	}
	
	/* Local Main only */
	private void loadProperties() throws IOException {
		properties.load(new FileInputStream(PREFIX_PATH.concat("application.properties")));
	}
	
	@Override
	public void loadResource() throws UnsupportedEncodingException, FileNotFoundException {
		String dataPath = PREFIX_PATH.concat(properties.getProperty(GRAPH_DATA));
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataPath), "UTF-8"));
	}
	
	public void closeResources() throws IOException {
		reader.close();
	}
	
	public void parseData() {
		Scanner scanner = new Scanner(reader);
		Pattern pattern = Pattern.compile("\t");
		while( scanner.hasNextLine() ) {
			String [] aLine = pattern.split(scanner.nextLine(), 0);
			if (StringUtils.equals(SEQUENCE, aLine[GFA_LINE_INDICATOR])) {
				processSequence(aLine);
			} else if (StringUtils.equals(LINK, aLine[GFA_LINE_INDICATOR])) {
				processLink(aLine);
			}
		}
		scanner.close();
	}


	
	private void processSequence(String [] aLine) {
		for (int i = 0; i < aLine.length; i++) {
			Sequence aSequence = new Sequence(Integer.parseInt(aLine[GFA_FROM_NODE]), aLine[GFA_SEQUENCE] , null, null, null, null);
			sequences.put(Short.valueOf(aLine[GFA_FROM_NODE]), aSequence);
		}
	}

	
	private void processLink(String [] aLine) {
		for (int i = 0; i < aLine.length; i++) {
			if (links.containsKey(aLine[GFA_FROM_NODE])) {
				short [] l = links.get(aLine[1]);
				l[l.length] = Short.valueOf(aLine[GFA_TO_NODE]);
			} else {
				links.put(Short.valueOf(aLine[GFA_FROM_NODE]), new short[]{ Short.valueOf(aLine[GFA_TO_NODE]) });
			}
		}
	}

}