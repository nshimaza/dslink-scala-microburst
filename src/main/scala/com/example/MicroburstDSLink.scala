/**
 * @license
 * Copyright (c) 2016,2018 Cisco and/or its affiliates.
 *
 * This software is licensed to you under the terms of the Cisco Sample
 * Code License, Version 1.0 (the "License"). You may obtain a copy of the
 * License at
 *
 *                https://developer.cisco.com/docs/licenses
 *
 * All use of the material herein must be in accordance with the terms of
 * the License. All rights not expressly granted by the License are
 * reserved. Unless required by applicable law or agreed to separately in
 * writing, software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 */

package com.example

import java.util.concurrent.{SynchronousQueue, TimeUnit}

import org.dsa.iot.dslink.node.value.{Value, ValueType}
import org.dsa.iot.dslink.util.Objects
import org.dsa.iot.dslink.{DSLink, DSLinkFactory, DSLinkHandler}
import org.slf4j.LoggerFactory

/**
  * Created by nshimaza on 2016/09/19.
  */
object MicroburstDSLink {

  private val log = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    log.info("Starting Microburst")

    DSLinkFactory.start(args.drop(1), MicroburstDSLinkHandler(args(0).toInt))
  }
}

case class MicroburstDSLinkHandler(burst: Int) extends DSLinkHandler {
  private val log = LoggerFactory.getLogger(getClass)
  override val isResponder = true

  var count: Int = 0

  override def onResponderInitialized(link: DSLink): Unit = {
    log.info("Microburst base initialized")

    val node = link.getNodeManager.getSuperRoot
      .createChild("Bursty", true)
      .setValueType(ValueType.NUMBER)
      .setValue(new Value(-1))
      .build()

    Objects.getDaemonThreadPool
      .scheduleAtFixedRate(() => {
        (1 to burst).foreach {_ =>
          node.setValue(new Value(count))
          count += 1
        }
      }, 1, 1, TimeUnit.SECONDS)

    log.info("Microburst initialization completed")
  }

  override def onResponderConnected(link: DSLink): Unit = {
    log.info("Microburst connected")
  }
}
