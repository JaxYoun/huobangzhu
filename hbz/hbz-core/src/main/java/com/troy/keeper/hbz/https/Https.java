package com.troy.keeper.hbz.https;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by leecheng on 2017/10/12.
 */
public class Https {

    private String store;
    private String pass;

    public Https(String store, String pass) {
        this.store = store;
        this.pass = pass;
    }

    public Https() {

    }

    public String get(String url, Map<String, String> params, String charset) throws Exception {
        return new String(doConnect(url, "get", params, charset), charset);
    }

    public String post(String url, Map<String, String> params, String charset) throws Exception {
        return new String(doConnect(url, "post", params, charset), charset);
    }

    public String postData(String url, String data, String charset) throws Exception {
        return new String(doConnect(url, "post", data.getBytes(), charset), charset);
    }

    private byte[] doConnect(String url, String method, Map<String, String> params, String charset) throws Exception {
        TrustManager[] tm;
        try {
            tm = new TrustManager[]{this.store == null && this.pass == null ? new TrustAnyManager() : new RequestX509TrustManager(store, pass)};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            HttpsURLConnection httpsConn = null;

            switch (method.trim().toLowerCase()) {
                case "post": {
                    String posts = "";
                    if (params.size() > 0) {
                        int i = 0;
                        for (String key : params.keySet()) {
                            posts = posts.concat((i++ == 0 ? "" : "&") + key + "=" + URLEncoder.encode(params.get(key), charset));
                        }
                    }
                    byte[] data = posts.getBytes(charset);
                    URL myURL = new URL(url);
                    httpsConn = (HttpsURLConnection) myURL.openConnection();
                    httpsConn.setSSLSocketFactory(ssf);
                    httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                    httpsConn.setRequestMethod("POST");
                    httpsConn.setRequestProperty("accept", "*/*");
                    httpsConn.setRequestProperty("connection", "Keep-Alive");
                    httpsConn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    httpsConn.setRequestProperty("Content-Length", String.valueOf(data.length));
                    httpsConn.setDoInput(true);
                    httpsConn.setDoOutput(true);
                    httpsConn.connect();
                    OutputStream os = httpsConn.getOutputStream();
                    os.write(data);
                    os.flush();
                }
                break;
                case "get": {
                    if (url.indexOf("?") > 0) {
                        for (String key : params.keySet()) {
                            url = url.concat("&" + key + "=" + URLEncoder.encode(params.get(key), charset));
                        }
                    } else {
                        if (params.size() > 0) {
                            url = url.concat("?");
                            for (String key : params.keySet()) {
                                url = url.concat("&" + key + "=" + URLEncoder.encode(params.get(key), charset));
                            }
                        }
                    }
                    URL myURL = new URL(url);
                    httpsConn = (HttpsURLConnection) myURL.openConnection();
                    httpsConn.setSSLSocketFactory(ssf);
                    httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                    httpsConn.setRequestMethod("GET");
                    httpsConn.setRequestProperty("accept", "*/*");
                    httpsConn.setRequestProperty("connection", "Keep-Alive");
                    httpsConn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    httpsConn.connect();
                }
                break;
            }
            InputStream is = httpsConn.getInputStream();
            int readc = 0;
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            byte[] buff = new byte[122];
            while ((readc = is.read(buff)) != -1) {
                bo.write(buff, 0, readc);
                bo.flush();
            }
            try {
                is.close();
            } catch (Exception e) {
            }
            return bo.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    private byte[] doConnect(String url, String method, byte[] data, String charset) throws Exception {
        TrustManager[] tm;
        try {
            tm = new TrustManager[]{this.store == null && this.pass == null ? new TrustAnyManager() : new RequestX509TrustManager(store, pass)};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            HttpsURLConnection httpsConn = null;

            switch (method.trim().toLowerCase()) {
                case "post": {
                    URL myURL = new URL(url);
                    httpsConn = (HttpsURLConnection) myURL.openConnection();
                    httpsConn.setSSLSocketFactory(ssf);
                    httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                    httpsConn.setRequestMethod("POST");
                    httpsConn.setRequestProperty("accept", "*/*");
                    httpsConn.setRequestProperty("connection", "Keep-Alive");
                    httpsConn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    httpsConn.setRequestProperty("Content-Length", String.valueOf(data.length));
                    httpsConn.setDoInput(true);
                    httpsConn.setDoOutput(true);
                    httpsConn.connect();
                    OutputStream os = httpsConn.getOutputStream();
                    os.write(data);
                    os.flush();
                }
                break;
                case "get": {
                    URL myURL = new URL(url);
                    httpsConn = (HttpsURLConnection) myURL.openConnection();
                    httpsConn.setSSLSocketFactory(ssf);
                    httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                    httpsConn.setRequestMethod("GET");
                    httpsConn.setRequestProperty("accept", "*/*");
                    httpsConn.setRequestProperty("connection", "Keep-Alive");
                    httpsConn.setRequestProperty("user-agent",
                            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    httpsConn.connect();
                }
                break;
            }
            InputStream is = httpsConn.getInputStream();
            int readc = 0;
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            byte[] buff = new byte[122];
            while ((readc = is.read(buff)) != -1) {
                bo.write(buff, 0, readc);
                bo.flush();
            }
            try {
                is.close();
            } catch (Exception e) {
            }
            return bo.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
