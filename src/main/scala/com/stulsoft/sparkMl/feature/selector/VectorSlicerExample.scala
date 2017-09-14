/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.feature.selector

import org.apache.spark.ml.attribute.{Attribute, AttributeGroup, NumericAttribute}
import org.apache.spark.ml.feature.VectorSlicer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Row, SparkSession}

/** Feature Selector - VectorSlicer
  *
  * @see [[https://spark.apache.org/docs/latest/ml-features.html#feature-selectors VectorSlicer]]
  * @author Yuriy Stul
  */
object VectorSlicerExample extends App {
  test1()
  test2()

  def test1(): Unit = {
    println("==>test1")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Feature Selectors - VectorSlicer Example")
      .getOrCreate()

    val data = java.util.Arrays.asList(
      Row(Vectors.sparse(3, Seq((0, -2.0), (1, 2.3)))),
      Row(Vectors.dense(-2.0, 2.3, 0.0))
    )

    val defaultAttr = NumericAttribute.defaultAttr
    val attrs = Array("f1", "f2", "f3").map(defaultAttr.withName)
    val attrGroup = new AttributeGroup("userFeatures", attrs.asInstanceOf[Array[Attribute]])

    val dataSet = spark.createDataFrame(data, StructType(Array(attrGroup.toStructField())))
    println("Original dataSet:")
    dataSet.show(false)

    val slicer = new VectorSlicer().setInputCol("userFeatures").setOutputCol("features")

    slicer.setIndices(Array(1)).setNames(Array("f3"))
    // or slicer.setIndices(Array(1, 2)), or slicer.setNames(Array("f2", "f3"))

    val output = slicer.transform(dataSet)
    output.show(false)

    spark.stop()
    println("<==test1")
  }

  def test2(): Unit = {
    println("==>test2")
    val spark: SparkSession = SparkSession.builder.
      master("local")
      .appName("Feature Selectors - VectorSlicer Example")
      .getOrCreate()

    val data = java.util.Arrays.asList(
      Row(Vectors.sparse(3, Seq((0, -2.0), (1, 2.3)))),
      Row(Vectors.dense(-2.0, 2.3, 0.0))
    )

    val defaultAttr = NumericAttribute.defaultAttr
    val attrs = Array("f1", "f2", "f3").map(defaultAttr.withName)
    val attrGroup = new AttributeGroup("userFeatures", attrs.asInstanceOf[Array[Attribute]])

    val dataSet = spark.createDataFrame(data, StructType(Array(attrGroup.toStructField())))
    println("Original dataSet:")
    dataSet.show(false)

    val slicer = new VectorSlicer().setInputCol("userFeatures").setOutputCol("features")

    slicer.setIndices(Array(0)).setNames(Array("f2"))
    // or slicer.setIndices(Array(0, 1)), or slicer.setNames(Array("f1", "f2"))

    val output = slicer.transform(dataSet)
    output.show(false)

    spark.stop()
    println("<==test2")
  }
}
