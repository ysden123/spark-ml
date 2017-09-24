/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.util

import java.io.File

import scala.io.Source

/**
  * @author Yuriy Stul
  */
object Utils {
  /**
    * Returns maximum number of features
    *
    * @param path input data file
    * @return the maximum number of features
    */
  def countNumberOfFeatures(path: String): Int = {
    val src = Source.fromFile(Utils.getResourceFilePath(path))
    val maxLength = src.getLines()
      .map(_.split(" ").tail.length)
      .max
    src.close()
    maxLength
  }

  /**
    * Returns an absolute path to file
    *
    * @param name specifies resource file; may include subdirectory
    * @return the absolute path to file
    */
  def getResourceFilePath(name: String): String = new File(getClass.getClassLoader.getResource(name).toURI)
    .getAbsolutePath

  /**
    * Returns maximum index if input data (libsvm format)
    *
    * @param path input data file
    * @return the maximum index if input data (libsvm format)
    */
  def countMaxDataIndex(path: String): Int = {
    val src = Source.fromFile(Utils.getResourceFilePath(path))
    val maxIndex = src.getLines()
      .map(_.split(" ").tail.map(_.split(":").head.toInt).max)
      .max
    src.close()
    maxIndex
  }
}
