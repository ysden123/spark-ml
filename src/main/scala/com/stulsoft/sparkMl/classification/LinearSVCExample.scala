/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.classification

import com.stulsoft.sparkMl.util.{TimeWatch, Utils}
import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.sql.SparkSession

/** Classification - Linear Support Vector Machine example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#linear-support-vector-machine Linear Support Vector Machine]]
  * @author Yuriy Stul
  */
object LinearSVCExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Classification - Multilayer Perceptron Classifier example")
      .getOrCreate()

    // Load training data
    tw.start()
    val training = spark.read.format("libsvm").load(Utils.getResourceFilePath("data/sample_libsvm_data.txt"))
    println(s"Loading takes ${tw.duration} ms")

    val lsvc = new LinearSVC()
      .setMaxIter(10)
      .setRegParam(0.1)

    // Fit the model
    tw.start()
    val lsvcModel = lsvc.fit(training)
    println(s"Training takes ${tw.duration} ms")

    println(s"lsvcModel.numClasses = ${lsvcModel.numClasses}, lsvcModel.numFeatures = ${lsvcModel.numFeatures}")

    // Print the coefficients and intercept for linear svc
    //    println(s"Coefficients: ${lsvcModel.coefficients} Intercept: ${lsvcModel.intercept}")
    println(s"Coefficients: number of non-zeros ${lsvcModel.coefficients.numNonzeros}, number of actives ${lsvcModel.coefficients.numActives} Intercept: ${lsvcModel.intercept}")

    spark.stop()
    println("<==test")
  }
}
