/**
 *
 *  @author Harasiuk Paweł S24628
 *
 */

package zad1;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.*;

public class ChatClient extends Thread {
    private SocketChannel sc = null;
    private final String id;
    private final StringBuilder chatView;
    private final Lock lock = new ReentrantLock();
    private final String host;
    private final int port;

    public ChatClient(String host, int port, String id) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.chatView = new StringBuilder(String.format("\n=== %s chat view\n", id));
    }

    public void login() {
        try {
            this.sc = SocketChannel.open(new InetSocketAddress(this.host, this.port));
            sc.configureBlocking(false);
            int n = 20;
            while (!sc.finishConnect()) {
                Thread.sleep(50);
                n--;
                if (n <= 0) throw new Exception("connection timeout");
            }
            this.request("login " + this.id);
            this.chatView.append(String.format("%s logged in\n", id));

            this.start();
        } catch (Exception e) {
            this.logException(e);
        }
    }

    public void logout() {
        try {
            this.request("bye");
            this.chatView.append(String.format("%s logged out", id));

            lock.lock();
            this.interrupt();
        } catch (Exception e) {
            this.logException(e);
        } finally {
            lock.unlock();
        }
    }

    public void send(String request) {
        this.request(request);
        this.chatView.append(id).append(": ").append(request).append("\n");
    }

    private void request(String message) {
        try {
            this.sc.write(StandardCharsets.UTF_8.encode(message + '@'));
        } catch (Exception e) {
            this.logException(e);
        }
    }

    public String getChatView() {
        return chatView.toString();
    }

    private void logException(Exception exception) {
        this.chatView.append(String.format("*** %s", exception.toString()));
    }

    public String getClientId() {
        return id;
    }
}