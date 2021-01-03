package mynimbus;

import java.util.HashMap;
import java.util.Map;

public class Receiver {

    private static Map<String,ClientHandler> clients = new HashMap<>();

    public static ClientHandler connect(String clientId, Sender sender) throws Exception
    {
        ClientHandler clientHandler = new ClientHandler();

        clientHandler.init(sender);
        clients.put(clientId,clientHandler);

        return clientHandler;
    }

}
