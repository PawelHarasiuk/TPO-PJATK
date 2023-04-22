/**
 * @author Harasiuk Paweł S24628
 */

package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private Selector selector;
    private boolean isRunning;
    private final StringBuilder serverLog = new StringBuilder();
    private Thread serverThread;

    public Server(String host, int port) {
        try {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        isRunning = true;
        serverThread = new Thread(() -> {
            while (isRunning) {
                try {
                    selector.select();
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectedKeys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isAcceptable()) {
                            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                            SocketChannel clientChannel = serverChannel.accept();
                            clientChannel.configureBlocking(false);
                            clientChannel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            StringBuilder request = new StringBuilder();
                            int bytesRead = clientChannel.read(buffer);
                            while (bytesRead > 0) {
                                buffer.flip();
                                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                                while (charBuffer.hasRemaining()) {
                                    char c = charBuffer.get();
                                    if (c == '@') {
                                        processRequest(clientChannel, request.toString().trim());
                                        request = new StringBuilder();
                                    } else {
                                        request.append(c);
                                    }
                                }
                                buffer.clear();
                                bytesRead = clientChannel.read(buffer);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getServerLog() {
        return serverLog.toString();
    }


    private void processRequest(SocketChannel clientChannel, String request) throws IOException {
        LocalTime time = LocalTime.now();
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String response = "";

        if (request.contains("bye")) {
            String clientName = (String) clientChannel.keyFor(selector).attachment();
            serverLog.append(clientName).append(" logged out at ").append(formattedTime).append("\n");
            response = "logged out";
        } else if (request.contains("login")) {
            String[] parts = request.split(" ");
            String clientName = parts[1];
            clientChannel.keyFor(selector).attach(clientName); // Attach the client name to the SelectionKey object
            serverLog.append(clientName).append(" logged in at ").append(formattedTime).append("\n");
            response = "logged in";
        } else {
            String clientName = (String) clientChannel.keyFor(selector).attachment();
            if (!request.equals("") && !request.contains("===")) {
                serverLog.append(clientName).append(" request ").append(formattedTime).append(": \"").append(request).append("\"\n");
                String[] dates = request.split(" +");
                response = Time.passed(dates[0], dates[1]);
            }
        }

        ByteBuffer buffer = StandardCharsets.UTF_8.encode(response);
        clientChannel.write(buffer);
    }
}