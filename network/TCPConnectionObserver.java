package network;

/**
 * @author Anton Masiukevich
 * @version 1.0
 * @since   2020-05-29
 *
 * TCPConnectionObserver is an interface, which usage is to provide
 * needed abstraction to implement both server and client as Listeners,
 * who listen for messages.
 */

public interface TCPConnectionObserver {
    void onConnectionReady(TCPConnection connection);
    void onReceiveString(TCPConnection connection, String value);
    void onDisconnect(TCPConnection connection);
    void onException(TCPConnection connection, Exception exception);
}
