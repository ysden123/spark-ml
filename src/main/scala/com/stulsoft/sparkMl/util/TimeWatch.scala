/*
 * Copyright (c) 2017. Yuriy Stul
 */

package com.stulsoft.sparkMl.util

/**
  * Time watch
  *
  * @author Yuriy Stul
  */
class TimeWatch {
  private var startTime: Long = System.currentTimeMillis()
  private var endTime: Long = _

  /**
    * Start timer (sets start time to current time and end time to 0)
    */
  def start(): Unit = {
    startTime = System.currentTimeMillis()
    endTime = 0
  }

  /**
    * Returns duration in ms
    * <p>
    * Checks, if end time is 0, then sets end time to current time
    *
    * @return the duration in ms
    */
  def duration: Long = {
    if (endTime == 0)
      stop()

    endTime - startTime
  }

  /**
    * Sets end time to current time
    */
  def stop(): Unit = {
    endTime = System.currentTimeMillis()
  }

}

/**
  * Companion object for TimeWatch class
  */
object TimeWatch {
  def apply(): TimeWatch = new TimeWatch()
}
