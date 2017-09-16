/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.classification

import com.stulsoft.sparkMl.util.{TimeWatch, Utils}
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

/** Classification - Multilayer Perceptron Classifier example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#multilayer-perceptron-classifier Multilayer perceptron classifier]]
  * @author Yuriy Stul
  */
object MultilayerPerceptronClassifierExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Classification - Multilayer Perceptron Classifier example")
      .getOrCreate()

    // Load the data stored in LIBSVM format as a DataFrame.
    val data = spark.read.format("libsvm").load(Utils.getResourceFilePath("data/sample_multiclass_classification_data.txt"))

    // Split the data into train and test
    val splits = data.randomSplit(Array(0.6, 0.4), seed = 1234L)
    val train = splits(0)
    val test = splits(1)

    // specify layers for the neural network:
    // input layer of size 4 (features), two intermediate of size 5 and 4
    // and output of size 3 (classes)
    val layers = Array[Int](4, 5, 4, 3)

    // create the trainer and set its parameters
    val trainer = new MultilayerPerceptronClassifier()
      .setLayers(layers)
      .setBlockSize(128)
      .setSeed(1234L)
      .setMaxIter(100)

    // train the model
    tw.start()
    val model = trainer.fit(train)
    println(s"Training model takes ${tw.duration} ms.")

    // compute accuracy on the test set
    tw.start()
    val result = model.transform(test)
    println(s"Making prediction takes ${tw.duration} ms.")

    val predictionAndLabels = result.select("prediction", "label")
    val evaluator = new MulticlassClassificationEvaluator()
      .setMetricName("accuracy")

    println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels))

    spark.stop()
    println("<==test")
  }
}
