package unicl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongodbConn {
	MongoClient mongoClient = new MongoClient();
	DB db = mongoClient.getDB( "unicl" );
	DBCollection col1 = db.getCollection("newswid");	
	
	
	public MongodbConn() {
		mongoClient = new MongoClient();
		db = mongoClient.getDB( "unicl" );
		col1 = db.getCollection("newswid");	
		/*   
		BasicDBObject doc = new BasicDBObject("name", "MongoDB").append("type", "database")
				   .append("count", 1)
				   .append("info", new BasicDBObject("x", 203).append("y", 102));
		   col1.insert(doc);
		  *
		   DBCursor cursor = col1.find();
		   
		   try {
			    while (cursor.hasNext()) {
			        System.out.println(cursor.next());
			    }
			} finally {
			    cursor.close();
			}	*/
	}
	
	public void Log(String code, String news) {
		mongoClient = new MongoClient();
		db = mongoClient.getDB( "unicl" );
		col1 = db.getCollection("newswid");	
		   
		BasicDBObject doc = new BasicDBObject("code", code).append("news", news);
		  
		   DBCursor cursor = col1.find(doc);
		   
		   try {
			    while (cursor.hasNext()) {
			        System.out.println(cursor.next());
			    }
			} finally {
			    cursor.close();
			}	
	}
	
	public void close() {
		mongoClient.close();
	}
}
