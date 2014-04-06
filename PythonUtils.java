/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 6/4/14
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class PythonUtils {

    public static String getNanoTime() {
        final long now = System.nanoTime();
        final long time = now / 1000000000;
        final long last = now % 1000000000;
        final String tail = Float.toString(last / 1000000000.0F).substring(1);
        return time+tail;
    }

}
