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


            String loginRequest = "=== " + c.getId() + " log start ===";
            String response = c.send(loginRequest);
            sb.append(loginRequest);
            if (showSendRes) {
                //System.out.println(loginRequest);
                //System.out.println(response);
            }

            for (String req : reqs) {
                response = c.send(req);
                if (showSendRes) {
                    //System.out.println("SENT: " + req);
                    //System.out.println("RECEIVED: " + response);
                }
            }

            String byeRequest = "bye and log transfer";
            response = c.send(byeRequest);
            if (showSendRes) {
                //System.out.println("SENT: " + byeRequest);
                //System.out.println("RECEIVED: " + response);
            }

            //String log = c.receiveLog();
            String log = "";
            //System.out.println("LOG for client " + c.getId() + ":");
            //System.out.println(log);

            c.disconnect();
            return sb.toString();
        });
    }
}
