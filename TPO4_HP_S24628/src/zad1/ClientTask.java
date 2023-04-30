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
            String response;
            c.connect();

            String log = "login " + c.getId();
            response = c.send(log);
            if (showSendRes) System.out.println(response);
            for (String req : reqs) {
                response = c.send(req);
                if (showSendRes) System.out.println(response);
            }
            String byeRequest = "bye and log transfer";
            response = c.send(byeRequest);
            if (showSendRes) System.out.println(response);

            return response;
        });
    }
}
