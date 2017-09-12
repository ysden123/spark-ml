package com.stulsoft.sparkMl.feature.extractor

import com.stulsoft.sparkMl.util.TimeWatch
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.sql.SparkSession

/** CountVectorizerExample
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#countvectorizer CountVectorizer Example]]
  * @author Yuriy Stul.
  */
object CountVectorizerExample extends App {
  test1()

  def test1(): Unit = {
    println("==>test1")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("TF-IDF Feature Extractor Example")
      .getOrCreate()

    val df = spark.createDataFrame(Seq(
      (0, Array("a", "b", "c")),
      (1, Array("a", "b", "b", "c", "a"))
    )).toDF("id", "words")

    println("Input:")
    df.show()

    // fit a CountVectorizerModel from the corpus
    tw.start()
    val cvModel: CountVectorizerModel = new CountVectorizer()
      .setInputCol("words")
      .setOutputCol("features")
      .setVocabSize(3)
      .setMinDF(2)
      .fit(df)
    println(s"Training takes ${tw.duration} ms.")

    // alternatively, define CountVectorizerModel with a-priori vocabulary
    val cvm = new CountVectorizerModel(Array("a", "b", "c"))
      .setInputCol("words")
      .setOutputCol("features")

    println("Features:")
    tw.start()
    cvModel.transform(df).show(false)
    println(s"Featuring takes ${tw.duration} ms.")
    spark.stop()
    println("<==test1")
  }
}
