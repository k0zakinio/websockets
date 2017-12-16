package com.company.websocket_learning

import java.net.{InetSocketAddress, URI}

import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.{ClientHandshake, ServerHandshake}
import org.java_websocket.server.WebSocketServer
import scala.collection.JavaConverters._

object Main extends App {
  private val port = 8080
  private val chatServer = new ChatServer(port)

  println(s"Chat server starting $port")
  chatServer.start()
}

class ChatServer(port: Int) extends WebSocketServer(new InetSocketAddress(port)) {

  private def publishToClients(msg: String) = connections().asScala.foreach(client => client.send(msg))

  override def onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) = println(s"server has has closed a connection with $conn! Current active connections: ${connections.size}")

  override def onMessage(conn: WebSocket, message: String) = {
    println(s"$conn: $message")
    publishToClients(message)
  }

  override def onOpen(conn: WebSocket, handshake: ClientHandshake) =
    println(s"server has opened a connection with ${handshake.getResourceDescriptor}! Current active connections: ${connections.size}")

  override def onError(conn: WebSocket, ex: Exception) = println(s"server has had an exception from $conn with message: ${ex.getMessage}")
}

class ChatClient(uri: URI) extends WebSocketClient(uri) {
  override def onClose(code: Int, reason: String, remote: Boolean) = println(s"client has closed the connection with reason: $reason!")

  override def onMessage(message: String) = println(s"client has received message $message")

  override def onOpen(handshakedata: ServerHandshake) = println(s"client has opened connection with ${handshakedata.getHttpStatusMessage}")

  override def onError(ex: Exception) = println(s"client has had an error with message: ${ex.getMessage}")
}
