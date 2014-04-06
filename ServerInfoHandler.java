import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.*;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 6/4/14
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ServerInfoHandler extends HttpServlet implements Servlet {

    private static final String URI = "/api/get_server_info";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String time = PythonUtils.getNanoTime();
        final String params = "nonce="+time;

        final String sign = HMacSHA512Utils.encrypt(params);

        final HttpClient httpClient = HttpClients.getInstance();
        final PostMethod postMethod = new PostMethod("http://"+Config.getUrl()+URI);
        final NameValuePair nameValuePair = new NameValuePair("nonce", time);
        postMethod.setRequestBody(new NameValuePair[]{nameValuePair});

        postMethod.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Key", Config.getApiKey());
        postMethod.setRequestHeader("Sign", sign);

        httpClient.executeMethod(postMethod);
//        System.out.println(new String(postMethod.getResponseBody()));

        resp.getWriter().print(new String(postMethod.getResponseBody()));
    }

    public static void main(String ...args) throws IOException {
        final ServerInfoHandler serverInfoHandler = new ServerInfoHandler();
        serverInfoHandler.doGet(null, null);
    }

}
