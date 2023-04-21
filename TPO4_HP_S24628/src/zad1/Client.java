/**
 * @author Harasiuk Paweł S24628
 */

package zad1;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private final String host;
    private final int port;
    private final String id;
    private SocketChannel socketChannel;
    private ByteBuffer buffer;

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
            buffer = ByteBuffer.allocate(1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String send(String req) {
        try {
            buffer.clear();
            buffer.put(req.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            buffer.clear();
            socketChannel.read(buffer);
            buffer.flip();

            byte[] resBytes = new byte[buffer.limit()];
            buffer.get(resBytes);
            return new String(resBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return id;
    }


    public void disconnect() throws IOException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }
}