package dev.msfjarvis.claw.android.network

import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory

/**
 * A [SocketFactory] that delegates calls. Sockets can be configured after creation by overriding
 * [configureSocket].
 */
open class DelegatingSocketFactory(private val delegate: SocketFactory) : SocketFactory() {
  override fun createSocket(): Socket {
    val socket = delegate.createSocket()
    return configureSocket(socket)
  }

  override fun createSocket(host: String, port: Int): Socket {
    val socket = delegate.createSocket(host, port)
    return configureSocket(socket)
  }

  override fun createSocket(
    host: String,
    port: Int,
    localAddress: InetAddress,
    localPort: Int
  ): Socket {
    val socket = delegate.createSocket(host, port, localAddress, localPort)
    return configureSocket(socket)
  }

  override fun createSocket(host: InetAddress, port: Int): Socket {
    val socket = delegate.createSocket(host, port)
    return configureSocket(socket)
  }

  override fun createSocket(
    host: InetAddress,
    port: Int,
    localAddress: InetAddress,
    localPort: Int
  ): Socket {
    val socket = delegate.createSocket(host, port, localAddress, localPort)
    return configureSocket(socket)
  }

  open fun configureSocket(socket: Socket): Socket {
    // No-op by default.
    return socket
  }
}
