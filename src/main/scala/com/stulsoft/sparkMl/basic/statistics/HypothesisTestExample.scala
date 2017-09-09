/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.basic.statistics

import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.ChiSquareTest
import org.apache.spark.sql.SparkSession

/** Hypothesis
  * <p>Hypothesis testing is a powerful tool in statistics to determine whether a result is statistically significant, whether this result occurred by chance or not
  *
  * @see [[https://spark.apache.org/docs/latest/ml-statistics.html https://spark.apache.org/docs/latest/ml-statistics.html#hypothesis-testing]]
  * @author Yuriy Stul
  */
object HypothesisTestExample extends App {
  val spark = SparkSession
    .builder
    .master("local")
    .appName("HypothesisTestExample")
    .getOrCreate()

  import spark.implicits._

  val data = Seq(
    (0.0, Vectors.dense(0.5, 10.0)),
    (0.0, Vectors.dense(1.5, 20.0)),
    (1.0, Vectors.dense(1.5, 30.0)),
    (0.0, Vectors.dense(3.5, 30.0)),
    (0.0, Vectors.dense(3.5, 40.0)),
    (1.0, Vectors.dense(3.5, 40.0))
  )

  println("data = " + data)

  val df = data.toDF("label", "features")
  val chi = ChiSquareTest.test(df, "features", "label").head
  println("pValues = " + chi.getAs[Vector](0))
  println("degreesOfFreedom = " + chi.getSeq[Int](1).mkString("[", ",", "]"))
  println("statistics = " + chi.getAs[Vector](2))

  spark.stop()
}
