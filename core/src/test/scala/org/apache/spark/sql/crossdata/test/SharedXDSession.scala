/*
 * Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.crossdata.test

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.scalatest.BeforeAndAfterAll


/**
  * Helper trait for SQL test suites where all tests share a single [[TestXDSession]].
  */
trait SharedXDSession extends XDTestUtils with BeforeAndAfterAll{

  protected val sparkConf = new SparkConf()

  /**
    * The [[TestXDSession]] to use for all tests in this suite.
    *
    * By default, the underlying [[org.apache.spark.SparkContext]] will be run in local
    * mode with the default test configurations.
    */
  private var _spark: TestXDSession = null

  /**
    * The [[TestXDSession]] to use for all tests in this suite.
    */
  protected implicit def spark: SparkSession = _spark

  /**
    * The [[TestXDSession]] to use for all tests in this suite.
    */
  protected implicit def sqlContext: SQLContext = _spark.sqlContext

  /**
    * Initialize the [[TestXDSession]].
    */
  protected override def beforeAll(): Unit = {
    SparkSession.sqlListener.set(null)
    if (_spark == null) {
      _spark = new TestXDSession(sparkConf)
    }
    // Ensure we have initialized the context before calling parent code
    super.beforeAll()
  }

  /**
    * Stop the underlying [[org.apache.spark.SparkContext]], if any.
    */
  protected override def afterAll(): Unit = {
    try {
      if (_spark != null) {
        _spark.stop()
        _spark = null
      }
    } finally {
      super.afterAll()
    }
  }
}