/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.classification.logistic.regression.binomial

import com.stulsoft.sparkMl.util.Utils
import org.apache.spark.ml.classification.{BinaryLogisticRegressionSummary, LogisticRegression}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.max

/** Classification Regression - ElasticNet example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#binomial-logistic-regression Binomial logistic regression]]
  * @author Yuriy Stul
  */
object LogisticRegressionSummaryExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Classification Logistic Regression - ElasticNet Example")
      .getOrCreate()
    import spark.implicits._

    // Load training data
    val training = spark.read.format("libsvm").load(Utils.getResourceFilePath("data/sample_libsvm_data.txt"))

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)

    // Extract the summary from the returned LogisticRegressionModel instance trained in the earlier
    // example
    val trainingSummary = lrModel.summary

    // Obtain the objective per iteration.
    val objectiveHistory = trainingSummary.objectiveHistory
    println("objectiveHistory:")
    objectiveHistory.foreach(loss => println(loss))

    // Obtain the metrics useful to judge performance on test data.
    // We cast the summary to a BinaryLogisticRegressionSummary since the problem is a
    // binary classification problem.
    val binarySummary = trainingSummary.asInstanceOf[BinaryLogisticRegressionSummary]

    // Obtain the receiver-operating characteristic as a dataframe and areaUnderROC.
    val roc = binarySummary.roc
    roc.show()
    println(s"areaUnderROC: ${binarySummary.areaUnderROC}")

    // Set the model threshold to maximize F-Measure
    val fMeasure = binarySummary.fMeasureByThreshold
    val maxFMeasure = fMeasure.select(max("F-Measure")).head().getDouble(0)
    val bestThreshold = fMeasure.where($"F-Measure" === maxFMeasure)
      .select("threshold").head().getDouble(0)
    lrModel.setThreshold(bestThreshold)

    spark.stop()
    println("<==test")
  }
}
