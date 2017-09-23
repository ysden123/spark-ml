/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.regression

import com.stulsoft.sparkMl.util.Utils
import org.apache.spark.ml.regression.GeneralizedLinearRegression
import org.apache.spark.sql.SparkSession

/** Regression - Generalized linear regression example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#generalized-linear-regression Generalized linear regression]]
  * @author Yuriy Stul
  */
object GeneralizedLinearRegressionExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Regression - Generalized linear regression example")
      .getOrCreate()
    // Load training data
    val dataset = spark.read.format("libsvm")
      .load(Utils.getResourceFilePath("data/sample_linear_regression_data.txt"))

    val glr = new GeneralizedLinearRegression()
      .setFamily("gaussian")
      .setLink("identity")
      .setMaxIter(10)
      .setRegParam(0.3)

    // Fit the model
    val model = glr.fit(dataset)

    // Print the coefficients and intercept for generalized linear regression model
    println(s"Coefficients: ${model.coefficients}")
    println(s"Intercept: ${model.intercept}")

    // Summarize the model over the training set and print out some metrics
    val summary = model.summary
    println(s"Coefficient Standard Errors: ${summary.coefficientStandardErrors.mkString(",")}")
    println(s"T Values: ${summary.tValues.mkString(",")}")
    println(s"P Values: ${summary.pValues.mkString(",")}")
    println(s"Dispersion: ${summary.dispersion}")
    println(s"Null Deviance: ${summary.nullDeviance}")
    println(s"Residual Degree Of Freedom Null: ${summary.residualDegreeOfFreedomNull}")
    println(s"Deviance: ${summary.deviance}")
    println(s"Residual Degree Of Freedom: ${summary.residualDegreeOfFreedom}")
    println(s"AIC: ${summary.aic}")
    println("Deviance Residuals: ")
    summary.residuals().show()
    spark.stop()
    println("<==test")
  }
}
