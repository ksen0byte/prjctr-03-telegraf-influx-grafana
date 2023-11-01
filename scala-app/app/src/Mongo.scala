package app

import org.bson.Document
import com.mongodb.client.{MongoClient, MongoClients, MongoCollection, MongoDatabase}

object Mongo {
  // mongodb://root:example@mongodb:27017/mydatabase?authSource=admin
  private val mongoClient = MongoClients.create("mongodb://root:example@mongodb:27017/mydatabase?authSource=admin")
  private val database    = mongoClient.getDatabase("mydatabase")
  private val collection  = database.getCollection("mycollection")

  def findReviews(limit: Int): Seq[Document] = {
    import scala.jdk.CollectionConverters._
    collection.find().limit(limit).asScala.toSeq
  }
}
