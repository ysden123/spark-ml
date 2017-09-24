/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.regression

import com.stulsoft.sparkMl.util.Utils
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.SparkSession

/** Regression - Linear regression example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#linear-regression Linear regression]]
  * @author Yuriy Stul
  */
object LinearRegressionWithElasticNetExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Regression - Linear regression example")
      .getOrCreate()

    val maxNumberOfFeatures = Utils.countNumberOfFeatures("data/sample_linear_regression_data.txt")
    println(s"maxNumberOfFeatures = $maxNumberOfFeatures")

    val maxDataIndex = Utils.countMaxDataIndex("data/sample_linear_regression_data.txt")
    println(s"maxDataIndex = $maxDataIndex")

    // Load training data
    val training = spark.read.format("libsvm")
      .load(Utils.getResourceFilePath("data/sample_linear_regression_data.txt"))
    println("Input data:")
    training.select("features").show()

    val lr = new LinearRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)

    // Print the coefficients and intercept for linear regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
    println(s"Number of non-zero coefficients = ${lrModel.coefficients.numNonzeros}")

    // Summarize the model over the training set and print out some metrics
    val trainingSummary = lrModel.summary
    println(s"numIterations: ${trainingSummary.totalIterations}")
    println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
    trainingSummary.residuals.show()
    println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
    println(s"r2: ${trainingSummary.r2}")

    spark.stop()
    println("<==test")
  }
}
