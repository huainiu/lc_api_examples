import com.alibaba.fastjson.JSONObject;
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

    private static final boolean LOCAL_TESTING = true;

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

        final JSONObject award = new JSONObject();
        award.put("player_key", playerKey);
        award.put("player_name", playerName);
        award.put("award_amount", awardAmount);
        award.put("award_title", awardTitle);

        final String awardStr = URLEncoder.encode(award.toJSONString(), "UTF8");
        final String time = PythonUtils.getNanoTime();

        final String params = "award="+awardStr+"&nonce="+time;
        log.info("params: "+params);

        final String sign = HMacSHA512Utils.encrypt(params);

        final HttpClient httpClient = HttpClients.getInstance();
        final PostMethod postMethod = new PostMethod((
                LOCAL_TESTING ? "http://" : "https://"
                )+Config.getUrl()+URI);
        final NameValuePair nameValuePairAward = new NameValuePair("award", awardStr);
        final NameValuePair nameValuePairNonce = new NameValuePair("nonce", time);
        postMethod.setRequestBody(new NameValuePair[]{nameValuePairAward, nameValuePairNonce});

        postMethod.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        postMethod.setRequestHeader("Key", Config.getApiKey());
        postMethod.setRequestHeader("Sign", sign);

        httpClient.executeMethod(postMethod);
//        System.out.println(new String(postMethod.getResponseBody()));

        resp.getWriter().print(new String(postMethod.getResponseBody()));
    }

}
