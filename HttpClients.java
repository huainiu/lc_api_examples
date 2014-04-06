import org.apache.commons.httpclient.HttpClient;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 6/4/14
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpClients {

    private static final ThreadLocal<HttpClient> HTTP_CLIENT = new ThreadLocal<HttpClient>(){
        protected HttpClient initialValue() {
            return new HttpClient();
        }
    };

    public static HttpClient getInstance() {
        return HTTP_CLIENT.get();
    }

}
