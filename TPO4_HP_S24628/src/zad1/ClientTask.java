/**
 * @author Harasiuk Paweł S24628
 */

package zad1;


import java.util.ArrayList;
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
            StringBuilder sb = new StringBuilder();
            if (showSendRes) {
                String loginRequest = "=== " + c.getId() + " log start ===";
                String response = c.send(loginRequest);
                sb.append(loginRequest).append("\n");
                sb.append(response).append("\n");

                for (String req : reqs) {
                    response = c.send(req);
                    sb.append("Request: ").append(req).append("\n");
                    sb.append("Result:\n").append(response).append("\n");
                }

                String byeRequest = "bye and log transfer";
                response = c.send(byeRequest);
                sb.append(response).append("\n");
                String end = "=== " + c.getId() + " log end ===";
                sb.append(end).append("\n");
            }

            c.disconnect();
            return sb.toString();
        });
    }
}
