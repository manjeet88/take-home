package es.nitaur;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;

public class HttpPostRunnable implements Runnable {

    public static final String LOCALHOST = "localhost";
    public static final String HTTP = "http";
    public static final Integer MAX_RETRIES = 10;

    private final int port;
    private final int idx;
    private final String path;
    private final String body;

    public HttpPostRunnable(int port, int idx, String path, String body) {
        this.port = port;
        this.idx = idx;
        this.path = path;
        this.body = body;
    }

    @Override
    public void run() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(HTTP)
                    .setHost(LOCALHOST)
                    .setPort(port)
                    .setPath(path)
                    .build();

            HttpPost post = new HttpPost(uri);
            post.setHeader("Content-type", "application/json");
            post.setEntity(new StringEntity(body.replaceAll("@idx@", Integer.toString(idx))));

            System.out.println("Executing request # " + idx + post.getRequestLine());

            executeRequest(post, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void executeRequest(HttpPost post, int retryIdx) throws IOException, InterruptedException {
        if (retryIdx > MAX_RETRIES) {
            return;
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);
        if (HttpStatus.INTERNAL_SERVER_ERROR.value() == response.getStatusLine().getStatusCode()) {
            System.out.println("Call failed, re-executing request # " + idx + post.getRequestLine());
            Thread.sleep(100);
            executeRequest(post, retryIdx++);
        } else {
            System.out.println("Request executed # " + idx + post.getRequestLine() + " with status " + response.getStatusLine().getStatusCode());
        }
    }

}
