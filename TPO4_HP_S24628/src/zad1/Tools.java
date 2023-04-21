/**
 *
 *  @author Harasiuk Paweł S24628
 *
 */

package zad1;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class Tools {
    static Options createOptionsFromYaml(String fileName) throws Exception {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(fileName);
        Map<String, Object> obj = yaml.load(inputStream);

        String host = (String) obj.get("host");
        int port = (int) obj.get("port");
        boolean concurMode = (boolean) obj.get("concurMode");
        boolean showSendRes = (boolean) obj.get("showSendRes");

        Map<String, List<String>> clientsMap = new LinkedHashMap<>();
        Map<String, List<String>> rawClientsMap = (Map<String, List<String>>) obj.get("clientsMap");
        for (Map.Entry<String, List<String>> entry : rawClientsMap.entrySet()) {
            String clientId = entry.getKey();
            List<String> clientData = entry.getValue();
            clientsMap.put(clientId, clientData);
        }

        return new Options(host, port, concurMode, showSendRes, clientsMap);
    }
}
