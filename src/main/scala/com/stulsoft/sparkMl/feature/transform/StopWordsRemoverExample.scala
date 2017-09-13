/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.feature.transform

import org.apache.spark.ml.feature.StopWordsRemover
import org.apache.spark.sql.SparkSession

/** Feature Transformers - StopWordsRemover
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#stopwordsremover StopWordsRemover]]
  * @author Yuriy Stul
  */
object StopWordsRemoverExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Feature Transformers - StopWordsRemover Example")
      .getOrCreate()

    val remover = new StopWordsRemover()
      .setInputCol("raw")
      .setOutputCol("filtered")

    val dataSet = spark.createDataFrame(Seq(
      (0, Seq("I", "saw", "the", "red", "balloon")),
      (1, Seq("Mary", "had", "a", "little", "lamb"))
    )).toDF("id", "raw")

    println("Input data:")
    dataSet.show(false)

    println("After removed stop words:")
    remover.transform(dataSet).show(false)

    spark.stop()

    println("<==test")
  }
}
