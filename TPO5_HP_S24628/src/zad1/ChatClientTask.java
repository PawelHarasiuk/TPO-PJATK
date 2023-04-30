/**
 *
 *  @author Harasiuk Paweł S24628
 *
 */

package zad1;

import java.util.List;
import java.util.concurrent.*;

public class ChatClientTask extends FutureTask<String> {

    private final ChatClient client;

    public ChatClientTask(ChatClient c, Callable<String> code) {
        super(code);
        client = c;
    }

    public static ChatClientTask create(ChatClient c, List<String> reqs, int wait) {
        Callable<String> code = () -> {
            String id = c.getClientId();
            c.login();
            if (wait != 0) Thread.sleep(wait);
            try {
                for (String req : reqs) {
                    if (Thread.interrupted()) return id + " task interrupted";
                    c.send(req);
                    if (wait != 0) Thread.sleep(wait);
                }
                c.logout();
                if (wait != 0) Thread.sleep(wait);
            } catch (InterruptedException e) {
                return id + " task interrupted";
            }
            return id + " task completed";
        };
        return new ChatClientTask(c, code);
    }


    public ChatClient getClient() {
        return client;
    }
}
