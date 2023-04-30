/**
 *
 *  @author Harasiuk Paweł S24628
 *
 */
package zad1;


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.channels.*;
import java.time.*;


public class ChatServer implements Runnable {
    private ServerSocketChannel ssc = null;
    private Selector selector = null;
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final Lock lock = new ReentrantLock();
    Map<SocketChannel, String> cmap = new LinkedHashMap<>();
    StringBuilder serverLog = new StringBuilder();
    Charset charset = StandardCharsets.UTF_8;

    public ChatServer(String host, int port) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(new InetSocketAddress(host, port));
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void startServer() {
        exec.submit(this);
        System.out.println("Server started\n");
    }

    public void stopServer() {
        System.out.println("Server stopped");
        try {
            if (!cmap.isEmpty()) {
                for (SocketChannel sc : cmap.keySet()) {
                    if (sc.isOpen()) {
                        sc.close();
                        sc.socket().close();
                    }
                }
            }
            lock.lock();
            try {
                exec.shutdownNow();
            } finally {
                lock.unlock();
            }
            if (selector != null) selector.close();
            if (ssc != null) {
                ssc.close();
                ssc.socket().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getServerLog() {
        return serverLog.toString();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();

                if (Thread.interrupted()) {
                    break;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = this.ssc.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        this.serviceRequest(clientChannel);
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    private void serviceRequest(SocketChannel sc) throws IOException {
        if (!sc.isOpen()) return;
        if (sc.socket().isClosed()) return;
        ByteBuffer buf = ByteBuffer.allocate(2000);
        int n = sc.read(buf);
        if (n <= 0) return;
        buf.flip();
        CharBuffer cb = charset.decode(buf);
        String freq = cb.toString();
        String[] mreq = freq.split("@");
        for (String req : mreq) {
            String resp;
            if (req.startsWith("login")) {
                String[] split = req.split("\\s+");
                cmap.put(sc, split[1]);
                String info = split[1] + " logged in\n";
                writeResp(info);
            } else if (req.startsWith("bye")) {
                resp = cmap.get(sc) + " logged out\n";
                writeResp(resp);
                if (sc.isOpen()) {
                    sc.close();
                    sc.socket().close();
                }
                cmap.remove(sc);
            } else {
                String info = cmap.get(sc) + ": " + req + "\n";
                writeResp(info);
            }
        }
    }

    private void writeResp(String resp) throws IOException {
        serverLog.append(LocalTime.now()).append(" ").append(resp);
        ByteBuffer buf = charset.encode(CharBuffer.wrap(resp));
        for (SocketChannel c : cmap.keySet()) {
            if (c.isOpen() && !c.socket().isClosed()) {
                c.write(buf);
                buf.rewind();
            }
        }
    }
}
