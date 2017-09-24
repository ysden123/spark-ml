/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.clustering

import com.stulsoft.sparkMl.util.{TimeWatch, Utils}
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.SparkSession

/** Clustering - K-means example
  *
  * @see [[https://spark.apache.org/docs/latest/ml-clustering.html#k-means K-means]]
  * @author Yuriy Stul
  */
object KMeansExample extends App {
  test()

  def test(): Unit = {
    println("==>test")
    val tw = TimeWatch()

    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Clustering - K-means example")
      .getOrCreate()

    val maxNumberOfFeatures = Utils.countNumberOfFeatures("data/sample_kmeans_data.txt")
    println(s"maxNumberOfFeatures = $maxNumberOfFeatures")

    val maxDataIndex = Utils.countMaxDataIndex("data/sample_kmeans_data.txt")
    println(s"maxDataIndex = $maxDataIndex")

    // Loads data.
    tw.start()
    val dataSet = spark.read.format("libsvm").load(Utils.getResourceFilePath("data/sample_kmeans_data.txt"))
    println(s"Loading data takes ${tw.duration} ms.")

    // Trains a k-means model.
    val kMeans = new KMeans().setK(2).setSeed(1L)
    tw.start()
    val model = kMeans.fit(dataSet)
    println(s"Training takes ${tw.duration} ms.")

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    tw.start()
    val WSSSE = model.computeCost(dataSet)
    println(s"Computing cost takes ${tw.duration} ms.")
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)

    println("<==test")
  }
}
