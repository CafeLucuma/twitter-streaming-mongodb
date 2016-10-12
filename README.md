# twitter-streaming-mongodb

  Para cambiar la base de datos en donde se guardan los tweets:
	  MongoDatabase db = mongoClient.getDatabase("test1TBD");
  Cambiar test1TBD por nombre de preferencia.
  
  Para cambiar la collection donde se guardan los tweets:
    db.getCollection("tweets").insertOne(tweet);
  Cambiar "tweets" por nombre de preferencia. 
   
         
