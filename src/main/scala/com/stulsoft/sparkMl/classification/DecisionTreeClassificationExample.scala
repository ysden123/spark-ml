/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.classification

import com.stulsoft.sparkMl.util.{TimeWatch, Utils}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}
import org.apache.spark.sql.SparkSession

/** Classification - Decision Tree Classifier example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-classification-regression.html#decision-tree-classifier Decision tree classifier]]
  * @author Yuriy Stul
  */
object DecisionTreeClassificationExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val tw = TimeWatch()
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Classification - Decision Tree Classifier example")
      .getOrCreate()

    // Load the data stored in LIBSVM format as a DataFrame.
    tw.start()
    val data = spark.read.format("libsvm").load(Utils.getResourceFilePath("data/sample_libsvm_data.txt"))
    println(s"Reading data takes ${tw.duration} ms.")

    // Index labels, adding metadata to the label column.
    // Fit on whole dataset to include all labels in index.
    tw.start()
    val labelIndexer = new StringIndexer()
      .setInputCol("label")
      .setOutputCol("indexedLabel")
      .fit(data)
    println(s"Fit on whole dataset to include all labels in index takes ${tw.duration} ms.")

    // Automatically identify categorical features, and index them.
    tw.start()
    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4) // features with > 4 distinct values are treated as continuous.
      .fit(data)
    println(s"Automatically identify categorical features, and index them takes ${tw.duration} ms.")

    // Split the data into training and test sets (30% held out for testing).
    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    // Train a DecisionTree model.
    val dt = new DecisionTreeClassifier()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("indexedFeatures")

    // Convert indexed labels back to original labels.
    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    // Chain indexers and tree in a Pipeline.
    val pipeline = new Pipeline()
      .setStages(Array(labelIndexer, featureIndexer, dt, labelConverter))

    // Train model. This also runs the indexers.
    tw.start()
    val model = pipeline.fit(trainingData)
    println(s"Training model takes ${tw.duration} ms.")

    // Make predictions.
    tw.start()
    val predictions = model.transform(testData)
    println(s"Making predictions takes ${tw.duration} ms.")

    // Select example rows to display.
    predictions.select("predictedLabel", "label", "features").show(5)

    // Select (prediction, true label) and compute test error.
    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("accuracy")
    val accuracy = evaluator.evaluate(predictions)
    println("Test Error = " + (1.0 - accuracy))

    val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
    println("Learned classification tree model:\n" + treeModel.toDebugString)
    spark.stop()
    println("<==test")
  }
}