/**
 * @author Harasiuk Paweł S24628
 */

package zad1;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {
    private final String host;
    private final int port;
    private final String id;
    private SocketChannel socketChannel;
    private final StringBuilder log = new StringBuilder();

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));
            int n = 10;
            while (!socketChannel.finishConnect()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                n--;
                if (n <= 0) try {
                    throw new Exception("connection timeout");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String send(String req) {
        Charset charset = StandardCharsets.UTF_8;
        ByteBuffer bufferOut = charset.encode(CharBuffer.wrap(req + "@"));
        ByteBuffer bufferIn = ByteBuffer.allocate(1800);

        try {
            socketChannel.write(bufferOut);
            bufferIn.clear();
            int n = socketChannel.read(bufferIn);
            while (n == 0) {
                Thread.sleep(10);
                n = socketChannel.read(bufferIn);
            }
            bufferIn.flip();
            CharBuffer charBuffer = charset.decode(bufferIn);
            String res = charBuffer.toString();

            if (req.contains("login")) {
                log.append("=== ").append(getId()).append(" log start ===").append("\n");
                log.append(res).append("\n");
            } else if (req.contains("bye")) {
                log.append(res).append("\n");
                log.append("=== ").append(getId()).append(" log end ===").append("\n");
                if (socketChannel != null) {
                    socketChannel.close();
                }
                return log.toString();
            } else {
                log.append("Request: ").append(req).append("\n");
                log.append("Result:\n").append(res).append("\n");
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public String getId() {
        return id;
    }

    public String getLog() {
        return log.toString();
    }
}