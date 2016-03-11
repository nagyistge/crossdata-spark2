/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.crossdata.common

import java.util.UUID

import com.stratio.crossdata.common.result.SQLResult
import com.stratio.crossdata.common.security.Session

import scala.concurrent.duration.FiniteDuration

// Driver -> Server messages
private[crossdata] trait Command {
  val requestId: UUID
}

private[crossdata] case class SQLCommand private(sql: String,
                                                 override val requestId: UUID = UUID.randomUUID(),
                                                 queryId: UUID = UUID.randomUUID(),
                                                 flattenResults: Boolean = false,
                                                 timeout: Option[FiniteDuration] = None
                                                  ) extends Command {

  def this(query: String,
           retrieveColNames: Boolean,
           timeoutDuration: FiniteDuration
            ) = this(sql = query, flattenResults = retrieveColNames, timeout = Option(timeoutDuration))

  def this(query: String,
           retrieveColNames: Boolean
            ) = this(sql = query, flattenResults = retrieveColNames, timeout = None)

}

case class AddJARCommand(path: String,
                          override val requestId: UUID = UUID.randomUUID(),
                          timeout: Option[FiniteDuration] = None
                        ) extends Command {
  def this(
            jarpath: String,
            timeout: FiniteDuration
          ) = this(path=jarpath,  timeout=Option(timeout))

  def this(
            jarpath: String
          ) = this(path=jarpath)

}


trait ControlCommand extends Command

private[crossdata] case class CancelQueryExecution(override val requestId: UUID) extends ControlCommand

private[crossdata] case class GetJobStatus(override val requestId: UUID) extends ControlCommand

private[crossdata] case class CancelCommand(override val requestId: UUID) extends ControlCommand

private[crossdata] case class CommandEnvelope(cmd: Command, session: Session)


// Server -> Driver messages
private[crossdata] trait ServerReply {
  def requestId: UUID
}

private[crossdata] case class QueryCancelledReply(requestId: UUID) extends ServerReply

private[crossdata] case class SQLReply(requestId: UUID, sqlResult: SQLResult) extends ServerReply