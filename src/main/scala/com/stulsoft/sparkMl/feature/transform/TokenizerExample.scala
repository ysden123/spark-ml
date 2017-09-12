/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.feature.transform

import com.stulsoft.sparkMl.util.TimeWatch
import org.apache.spark.ml.feature.{RegexTokenizer, Tokenizer}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/** Feature Transformers - Tokenizer
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#tokenizer Tokenizer]]
  * @author Yuriy Stul
  */
object TokenizerExample extends App {
  test1()

  def test1(): Unit = {
    println("==>test1")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("TF-IDF Feature Extractor Example")
      .getOrCreate()

    val sentenceDataFrame = spark.createDataFrame(Seq(
      (0, "Hi I heard about Spark"),
      (1, "I wish Java could use case classes"),
      (2, "Logistic,regression,models,are,neat")
    )).toDF("id", "sentence")

    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    val regexTokenizer = new RegexTokenizer()
      .setInputCol("sentence")
      .setOutputCol("words")
      .setPattern("\\W") // alternatively .setPattern("\\w+").setGaps(false)

    val countTokens = udf { (words: Seq[String]) => words.length }

    tw.start()
    val tokenized = tokenizer.transform(sentenceDataFrame)
    println(s"Tokenizer transform takes ${tw.duration} ms.")
    tokenized.select("sentence", "words")
      .withColumn("tokens", countTokens(col("words"))).show(false)

    tw.start()
    val regexTokenized = regexTokenizer.transform(sentenceDataFrame)
    println(s"Regex Tokenizer transform takes ${tw.duration} ms.")
    regexTokenized.select("sentence", "words")
      .withColumn("tokens", countTokens(col("words"))).show(false)

    spark.stop()
    println("<==test1")
  }
}
