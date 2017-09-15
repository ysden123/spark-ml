/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.classification.logistic.regression.binomial

import com.stulsoft.sparkMl.util.Utils
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}

/** Classification Regression - ElasticNet example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#binomial-logistic-regression Binomial logistic regression]]
  * @author Yuriy Stul
  */
object LogisticRegressionWithElasticNetExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Classification Logistic Regression - ElasticNet Example")
      .getOrCreate()
    // Load training data
    val training = spark.read.format("libsvm").load(Utils.getResourceFilePath("data/sample_libsvm_data.txt"))

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)

    // Print the coefficients and intercept for logistic regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")

    // We can also use the multinomial family for binary classification
    val mlr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)
      .setFamily("multinomial")

    val mlrModel = mlr.fit(training)

    // Print the coefficients and intercepts for logistic regression with multinomial family
    println(s"Multinomial coefficients: ${mlrModel.coefficientMatrix}")
    println(s"Multinomial intercepts: ${mlrModel.interceptVector}")

    spark.stop()
    println("<==test")
  }
}
