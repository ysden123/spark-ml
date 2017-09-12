package com.stulsoft.sparkMl.feature.extractor
import com.stulsoft.sparkMl.util.TimeWatch
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.sql.SparkSession
/** CountVectorizerExample
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#countvectorizer CountVectorizer Example]]
  * @author Yuriy Stul.
  */
object CountVectorizerExample extends App {
  test1()
  def test1(): Unit ={
    println("==>test1")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("TF-IDF Feature Extractor Example")
      .getOrCreate()

    spark.stop()
    println("<==test1")
  }
}
