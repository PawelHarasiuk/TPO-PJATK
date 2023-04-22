/**
 *
 *  @author Harasiuk Paweł S24628
 *
 */
package zad1;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ClientTask extends FutureTask<String> {

    public ClientTask(Callable<String> callable) {
        super(callable);
    }

    public static ClientTask create(Client c, List<String> reqs, boolean showSendRes) {
        return new ClientTask(() -> {
            c.connect();

            String log = "login " + c.getId();
            c.send(log);

            for (String req : reqs) {
                c.send(req);
            }

            String byeRequest = "bye and log transfer";
            if (!showSendRes) {
                byeRequest = "bye";
                c.send(byeRequest);
                return "";
            }

            return c.send(byeRequest);
        });
    }
}
