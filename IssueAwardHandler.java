import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 6/4/14
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class IssueAwardHandler extends HttpServlet implements Servlet {

    private static final Log log = LogFactory.getLog("IssueAwardHandler");

    private static final String URI = "/api/issue_award";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("issue_award.html");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String playerKey = req.getParameter("player_key");
        log.info("playerKey: "+playerKey);

        final String playerName = req.getParameter("player_name");
        log.info("playerName: "+playerName);

        final String awardAmount = req.getParameter("award_amount");
        log.info("awardAmount: "+awardAmount);

        final String awardTitle = req.getParameter("award_title");
        log.info("awardTitle: "+awardTitle);

        final Award award = new Award();
        award.setPlayerName(playerName);
        award.setAmount(Integer.parseInt(awardAmount));
        award.setPlayerKey(playerKey);
        award.setTitle(awardTitle);

        final String awardStrBeforeURLEncode = JsonUtils.toJSONString(award);
        final String awardStrAfterURLEncode = URLEncoder.encode(awardStrBeforeURLEncode, "UTF8");
        final String time = PythonUtils.getNanoTime();

        final String params = "award="+awardStrAfterURLEncode+"&nonce="+time;
        log.info("params: "+params);

        final String sign = HMacSHA512Utils.encrypt(params);

        final HttpClient httpClient = HttpClients.getInstance();
        final PostMethod postMethod = new PostMethod((
                Config.isLocalTest() ? "http://" : "https://"
                )+Config.getUrl()+URI);
        final NameValuePair nameValuePairAward = new NameValuePair("award", awardStrBeforeURLEncode);
        final NameValuePair nameValuePairNonce = new NameValuePair("nonce", time);
        postMethod.setRequestBody(new NameValuePair[]{nameValuePairAward, nameValuePairNonce});

        postMethod.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Key", Config.getApiKey());
        postMethod.setRequestHeader("Sign", sign);

        log.info("sign: "+sign);

        httpClient.executeMethod(postMethod);

//        System.out.println("------------");
//        System.out.println(new String(postMethod.getResponseBody()));
//        System.out.println("++++++++++++");

        resp.getWriter().print(new String(postMethod.getResponseBody()));
    }

    public static void main(String ...args) throws IOException {
        final IssueAwardHandler issueAwardHandler = new IssueAwardHandler();
        issueAwardHandler.doPost(new MockHttpServletRequest(){
            @Override
            public String getParameter(String name) {
                switch (name) {
                    case "player_key":
                        return "agpzfjEzMzdjb2luchMLEgZQbGF5ZXIYgICAgNfi3QoM";
                    case "player_name":
                        return "qing";
                    case "award_amount":
                        return "123";
                    case "award_title":
                        return "award-title-test";
                    default:
                        return null;
                }
            }
        }, null);
    }

}
