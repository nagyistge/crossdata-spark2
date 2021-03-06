package org.apache.spark.sql.crossdata.catalyst.catalog

import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.catalog.{ExternalCatalog, FunctionResourceLoader, GlobalTempViewManager, SessionCatalog}

import org.apache.spark.sql.crossdata.catalyst.catalog.temporary.XDTemporaryCatalog

class XDSessionCatalog(
                        val temporaryCatalog: XDTemporaryCatalog,
                        externalCatalog: ExternalCatalog,
                        globalTempViewManager: GlobalTempViewManager,
                        functionResourceLoader: FunctionResourceLoader,
                        functionRegistry: FunctionRegistry,
                        conf: CatalystConf,
                        hadoopConf: Configuration) extends SessionCatalog(
  externalCatalog,
  globalTempViewManager,
  functionResourceLoader,
  functionRegistry,
  conf,
  hadoopConf
)