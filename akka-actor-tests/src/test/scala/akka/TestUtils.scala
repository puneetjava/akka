/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */

package akka

import scala.collection.immutable
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import akka.actor.{ Terminated, ActorSystem, ActorRef }
import akka.testkit.TestProbe

object TestUtils {

  def temporaryServerAddress(address: String = "127.0.0.1"): InetSocketAddress =
    temporaryServerAddresses(1, address).head

  def temporaryServerAddresses(numberOfAddresses: Int, hostname: String = "127.0.0.1"): immutable.IndexedSeq[InetSocketAddress] = {
    Vector.fill(numberOfAddresses) {
      val serverSocket = ServerSocketChannel.open()
      serverSocket.socket.bind(new InetSocketAddress(hostname, 0))
      (serverSocket, serverSocket.socket.getLocalPort)
    } collect { case (socket, port) ⇒ socket.close(); new InetSocketAddress(hostname, port) }
  }

  def verifyActorTermination(actor: ActorRef)(implicit system: ActorSystem): Unit = {
    val watcher = TestProbe()
    watcher.watch(actor)
    watcher.expectTerminated(actor)
  }

}
