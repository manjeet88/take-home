package es.nitaur;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractRestTest {

    @Autowired
    private TestRestTemplate restTemplate;

    protected ResponseEntity httpGetList(String url, ParameterizedTypeReference responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    protected ResponseEntity httpGetOne(String url, Class responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    protected <T> ResponseEntity<T> httpPostOne(String url, T requestObject, Class responseType) {
        return restTemplate.postForEntity(url, requestObject, responseType);
    }

    protected ResponseEntity httpDelete(String url, Class responseType) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, responseType);
    }

    //TODO: return response for verifying
    protected <T> void httpPutOne(String url, T requestObject) {
        restTemplate.put(url, requestObject);
    }

    protected <T> void httpPutList(String url, List<T> requestObject) {
        restTemplate.put(url, requestObject);
    }
}