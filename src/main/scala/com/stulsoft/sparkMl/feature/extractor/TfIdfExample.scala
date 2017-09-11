/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.feature.extractor

import com.stulsoft.sparkMl.util.TimeWatch
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.SparkSession

/** TF-IDF Feature Extractor Example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#tf-idf TF-IDF]]
  * @author Yuriy Stul
  */
object TfIdfExample extends App {
  test()

  def test(): Unit = {
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("TF-IDF Feature Extractor Example")
      .getOrCreate()

    val sentenceData = spark.createDataFrame(Seq(
      (0.0, "Hi I heard about Spark"),
      (0.0, "I wish Java could use case classes"),
      (1.0, "Logistic regression models are neat")
    )).toDF("label", "sentence")

    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    tw.start()
    val wordsData = tokenizer.transform(sentenceData)
    println(s"Tokenizer takes ${tw.duration}ms.")

    val hashingTF = new HashingTF()
      .setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(20)

    tw.start()
    val featurizedData = hashingTF.transform(wordsData)
    println(s"Hashing takes ${tw.duration}ms.")
    // alternatively, CountVectorizer can also be used to get term frequency vectors

    val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
    tw.start()
    val idfModel = idf.fit(featurizedData)
    println(s"Training takes ${tw.duration}ms.")

    tw.start()
    val rescaledData = idfModel.transform(featurizedData)
    println(s"Predicting takes ${tw.duration}ms.")
    rescaledData.select("label", "features").show()

    spark.stop()
  }
}
