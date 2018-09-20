package c;

import com.troy.keeper.hbz.https.Https;

import java.util.HashMap;

/**
 * Created by leecheng on 2017/10/12.
 */
public class HttpsTest {

    public static void main(String[] args) throws Exception {
        Https https = new Https("d:/keystore/hbz.keystore", "123456");
        String o = https.post("https://127.0.0.1", new HashMap<>(), "UTF-8");
        System.out.println(o);
    }

}
