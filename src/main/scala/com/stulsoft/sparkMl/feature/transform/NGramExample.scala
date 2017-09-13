/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.feature.transform

import org.apache.spark.ml.feature.NGram
import org.apache.spark.sql.SparkSession

/** Feature Transformers - NGram
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#n-gram n-gram]]
  * @author Yuriy Stul
  */
object NGramExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Feature Transformers - StopWordsRemover Example")
      .getOrCreate()
    val wordDataFrame = spark.createDataFrame(Seq(
      (0, Array("Hi", "I", "heard", "about", "Spark")),
      (1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
      (2, Array("Logistic", "regression", "models", "are", "neat"))
    )).toDF("id", "words")

    val ngram = new NGram().setN(2).setInputCol("words").setOutputCol("ngrams")

    val ngramDataFrame = ngram.transform(wordDataFrame)
    //    ngramDataFrame.select("ngrams").show(false)
    ngramDataFrame.show(false)

    spark.stop()
    println("<==test")
  }
}
