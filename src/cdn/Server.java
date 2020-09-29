package cdn;

import java.util.concurrent.CompletableFuture;

public interface Server {

     CompletableFuture<String> getFromServer(String url, String urlExtension);

}
