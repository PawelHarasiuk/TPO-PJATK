/**
 * @author Harasiuk Paweł S24628
 */

package zad1;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String host;
    private final int port;
    private final String id;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String host, int port, String id) {
        this.host = host;
        this.port = port;
        this.id = id;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String send(String req) {
        out.println(req);
        String res = null;
        try {
            res = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getId() {
        return id;
    }


    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}