package ua.com.mcgray.utils;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @author orezchykov
 * @since 04.12.14
 */
public class RestCaller {

    private final int port;
    private final String contextPath;

    public RestCaller(int port, String contextPath) {
        this.port = port;
        this.contextPath = contextPath;
    }

    public ResponseEntity<String> queryApi(HttpMethod method, String path ) {
        return queryApi(method,path,"");
    }
    public ResponseEntity<String> queryApi(HttpMethod method, String path, String query) {
        return new TestRestTemplate().exchange("http://localhost:" + this.port + contextPath +  path + "/" + query,method,null,String.class);
    }


}
