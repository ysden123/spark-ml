package com.stulsoft.sparkMl.feature.extractor

import com.stulsoft.sparkMl.util.TimeWatch
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.Row

/** Word2Vec Example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#word2vec Word2Vec]]
  * @author Yuriy Stul.
  */
object Word2VecExample extends App {
  test1()

  def test1(): Unit = {
    println("==>test1")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("TF-IDF Feature Extractor Example")
      .getOrCreate()

    // Input data: Each row is a bag of words from a sentence or document.
    val documentDF = spark.createDataFrame(Seq(
      "Hi I heard about Spark".split(" "),
      "I wish Java could use case classes".split(" "),
      "Logistic regression models are neat".split(" ")
    ).map(Tuple1.apply)).toDF("text")

    // Learn a mapping from words to Vectors.
    val word2Vec = new Word2Vec()
      .setInputCol("text")
      .setOutputCol("result")
      .setVectorSize(3)
      .setMinCount(0)

    tw.start()
    val model = word2Vec.fit(documentDF)
    println(s"Training takes ${tw.duration}ms.")

    tw.start()
    val result = model.transform(documentDF)
    println(s"Predicting takes ${tw.duration}ms.")
    result.collect().foreach { case Row(text: Seq[_], features: Vector) =>
      println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n")
    }
    spark.stop()
    println("<==test1")
  }
}
