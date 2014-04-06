import com.google.protobuf.MessageLite;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 6/4/14
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Base64Utils {

    private static final Class<?> base64Class;
    private static final MethodType byteArrayToBase64MethodType = MethodType.methodType(byte[].class);
    private static final MethodType base64ToByteArrayMethodType = MethodType.methodType(String.class);
    private static final Method byteArrayToBase64Method;
    private static final Method base64ToByteArrayMethod;
    private static final MethodHandle byteArrayToBase64MethodHandle;
    private static final MethodHandle base64ToByteArrayMethodHandle;

    static {
        try {
            base64Class = Class.forName("java.util.prefs.Base64");
            byteArrayToBase64Method = base64Class.getDeclaredMethod("byteArrayToBase64", byte[].class);
            base64ToByteArrayMethod = base64Class.getDeclaredMethod("base64ToByteArray", String.class);
            byteArrayToBase64Method.setAccessible(true);
            base64ToByteArrayMethod.setAccessible(true);
            byteArrayToBase64MethodHandle = MethodHandles.lookup().unreflect(byteArrayToBase64Method);
            base64ToByteArrayMethodHandle = MethodHandles.lookup().unreflect(base64ToByteArrayMethod);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String byteArrayToBase64(final byte[] a) {
        try {
            return (String) byteArrayToBase64MethodHandle.invokeExact(a);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String messageToBase64(final MessageLite messageLite) {
        return byteArrayToBase64(messageLite.toByteArray());
    }

    public static byte[] base64ToByteArray(String s) {
        int len = s.length();
        int append = 4 - (len % 4);
        if(append < 4) {
            for(int i = 0; i < append; i++) {
                s += "=";
            }
        }

        try {
            return (byte[])base64ToByteArrayMethodHandle.invokeExact(s);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
