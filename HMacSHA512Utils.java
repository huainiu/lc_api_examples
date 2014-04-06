import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 6/4/14
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class HMacSHA512Utils {

    private static final byte[] SHARED_SECRET = Config.getSharedSecret().getBytes();

    private static String encrypt0(final String data) throws NoSuchAlgorithmException, InvalidKeyException {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(SHARED_SECRET, "HmacSHA512" );

        final Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);

        final byte[] macData = mac.doFinal(data.getBytes());
        return ByteArrayUtils.toHexString(macData);
    }

    public static String encrypt(final String data) {
        try {
            return encrypt0(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
