/**
 * @author Harasiuk Paweł S24628
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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
        this.chatView = new StringBuilder("\n=== " + id + " chat view\n");
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
            this.chatView.append(id).append(" logged in\n");

            this.start();
        } catch (Exception e) {
            this.chatView.append("*** ").append(e);
        }
    }

    public void logout() {
        try {
            this.request("bye");
            this.chatView.append(id).append(" logged out");

            lock.lock();
            this.interrupt();
        } catch (Exception e) {
            this.chatView.append("*** ").append(e);
        } finally {
            lock.unlock();
        }
    }

    public void send(String request) {
        this.request(request);
    }

    private void request(String message) {
        try {
            this.sc.write(StandardCharsets.UTF_8.encode(message + '@'));
        } catch (Exception e) {
            this.chatView.append("*** ").append(e);
        }
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        int bytesRead;

        while (!this.isInterrupted()) {
            try {
                lock.lock();
                bytesRead = sc.read(buffer);
                if (bytesRead > 0) {
                    buffer.flip();
                    Charset charset = StandardCharsets.UTF_8;
                    CharBuffer cbuf = charset.decode(buffer);
                    StringBuilder stringBuffer = new StringBuilder();
                    boolean hasReadFullLine = false;

                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();

                        if (c == '@') {
                            hasReadFullLine = true;
                            break;
                        } else {
                            stringBuffer.append(c);
                        }
                    }

                    String response = stringBuffer.toString().trim();
                    if (!(response.contains(id) && response.contains("logged"))) {
                        if (response.trim().startsWith(id + ":")) {
                            this.chatView.append(response.replace(":", "")).append("\n");
                        } else {
                            this.chatView.append(response).append("\n");
                        }
                    }


                    if (hasReadFullLine) {
                        break;
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                lock.unlock();
            }

            buffer.clear();
        }
    }


    public String getChatView() {
        return chatView.toString();
    }

    public String getClientId() {
        return id;
    }
}