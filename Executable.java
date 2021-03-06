package org.neo4j.abcd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.HighlyAvailableGraphDatabaseFactory;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.turtle.TurtleParser;

public class Executable {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if(args.length!=1){
			throw new Exception("Provide a file path as a parameter!");
		}
		
		File file = new File(args[0]);
		File fp=new File("db.local");
		
		if(!file.canRead())
			throw new Exception("Can't read the file.");
				
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put("org.neo4j.server.database.mode", "HA");

		GraphDatabaseService db = new GraphDatabaseFactory()
			    .newEmbeddedDatabaseBuilder(fp).loadPropertiesFromFile("neo4j.properties").setConfig(settings)
				.newGraphDatabase();
		
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		
		RDFParser rdfParser = new TurtleParser();
		
		Neo4jHandler handler = new Neo4jHandler(db);
		rdfParser.setRDFHandler(handler);
		
		rdfParser.parse(bis, file.toURI().toString());
		
		System.out.println(handler.getCountedStatements());
	}

	

}

