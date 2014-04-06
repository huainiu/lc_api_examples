import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 7/4/14
 * Time: 1:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActivatePlayerHandler extends HttpServlet implements Servlet {

    private static final Log log = LogFactory.getLog(ActivatePlayerHandler.class);

    private static final String URI = "/api/activate_player";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("activate_player.html");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String platformId = req.getParameter("platformid");
        log.info("platformid: "+platformId);

        final String time = PythonUtils.getNanoTime();
        final String params = "nonce="+time+"&platformid="+platformId;
        log.info("params: "+params);

        final String sign = HMacSHA512Utils.encrypt(params);
        log.info("sign: "+sign);

        final HttpClient httpClient = HttpClients.getInstance();
        final PostMethod postMethod = new PostMethod((
                Config.isLocalTest() ? "http://" : "https://"
        )+Config.getUrl()+URI);
        final NameValuePair nameValuePair = new NameValuePair("nonce", time);
        final NameValuePair platformIdPair = new NameValuePair("platformid", platformId);
        postMethod.setRequestBody(new NameValuePair[]{nameValuePair, platformIdPair});

        postMethod.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Key", Config.getApiKey());
        postMethod.setRequestHeader("Sign", sign);

        httpClient.executeMethod(postMethod);
//        System.out.println("------------------");
//        System.out.println(new String(postMethod.getResponseBody()));
//        System.out.println("++++++++++++++++++");

        resp.getWriter().print(new String(postMethod.getResponseBody()));
    }

    public static void main(String ...args) throws IOException {
        final ActivatePlayerHandler activatePlayerHandler = new ActivatePlayerHandler();
        activatePlayerHandler.doPost(new MockHttpServletRequest(){
            @Override
            public String getParameter(String name) {
                switch (name) {
                    case "platformid":
                        return "76561198006985967";
                    default:
                        return null;
                }
            } }, null);
    }

}
