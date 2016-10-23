package cl.citiaps.twitter.streaming;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.UserMentionEntity;  
import twitter4j.json.DataObjectFactory;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.mongodb.DBObject;
import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.BasicDBObject;  
import com.mongodb.DB;  
import com.mongodb.DBCollection;  
import com.mongodb.DBCursor;
import com.mongodb.Mongo;  
import com.mongodb.MongoException; 

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.util.Arrays.asList;

public class Main {
	
    public final static String OAUTH_CONSUMER_KEY = "eq0A0AkiDawkgXOzjPvkMjWiA";
    public final static String OAUTH_CONSUMER_SECRET = "oohRopdtBqcWhEKIQOsHZDTmILz8egFLAO38io1MOepdWaKQTO";
    public final static String OAUTH_ACCESS_TOKEN = "18440720-5O9nJXSVknPqxmpHusDYJ55eYocQmFlaxRVpfBA1E";
    public final static String OAUTH_ACCESS_TOKEN_SECRET = "MxwZ4oVA1Qr3EUn47kH2uQWfb78aMVXG6C7pKH3Q13Cc0";
	
    public static void main(String[] args) {

    	new Main().doMain(args);      
	//conección con base de datos test

	//buscar documentos en base de datos
	//FindIterable<Document> iterable = db.getCollection("restaurants").find(new Document("name", "Morris Park Bake Shop"));
	/*iterable.forEach(new Block<Document>() {
	    @Override
	    public void apply(final Document document) {
		System.out.println(document);
		w.close();
	    }
	});*/
    }
    
   public void doMain(String[] args){
    	ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setJSONStoreEnabled(true);

    	cb.setDebugEnabled(true);
    	cb.setOAuthConsumerKey(OAUTH_CONSUMER_KEY);
    	cb.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET);
    	cb.setOAuthAccessToken(OAUTH_ACCESS_TOKEN);
    	cb.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
	MongoClient mongoClient = new MongoClient();
	MongoDatabase db = mongoClient.getDatabase("TBD1");
    	

    	TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
    	StatusListener listener = new StatusListener() {  		

	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	        }

	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	            System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
	        }

	        public void onScrubGeo(long userId, long upToStatusId) {
	            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	        }

	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }

			@Override
			public void onStallWarning(StallWarning arg0) {
				
			}

			@Override
			public void onStatus(Status status){
				System.out.println(status.getText());
			        Document tweet = (Document)Document.parse(DataObjectFactory.getRawJSON(status));
				db.getCollection("tweets").insertOne(tweet);

				

				
		//guardar essta wea en mongo
		//docs.mongodb.com/getting-started/java
		//java -cp build/libs/...jar cl/citiaps/twitter/streaming/Main
			}
	    };

	    FilterQuery fq = new FilterQuery();
	    String keywords[] = {"FaunaPrimavera", "PrimaveraFauna", "FaunaPrimaveraFest", "pf2016", "cumbre del rock chileno", "LaCumbreDelRockChileno", "LaCumbreDelRockCL", "LaCumbreDelRockConTRONIC", "LaCumbreDelRockChile", "cumbredelrockcl", "LollaCL", "lollapaloozacl", "PreguntaLollaCL", "RPLollaWeekend", "Lollapalooza versión #Chile2017", "lollapaloozachile2017", "RPLolla", "Frontera2016", "FronteraFestival", "festivalfrontera", "CreamfieldsCL2016", "CreamfieldsChile", "creamfields", "creamfieldsCL", "fiiS2016", "UnaNuevaRealidad", "fiiSAntofagasta", "Defqon1Chile", "DEFQON1", "Defqon1CL", "defqon1chile2016", "DefqonChile", "DQ1CL"};

	    fq.track(keywords);

	    twitterStream.addListener(listener);
	    twitterStream.filter(fq);      
    }
}
