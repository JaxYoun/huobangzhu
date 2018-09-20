package com.troy.keeper.hbz.helper;

import com.troy.keeper.hbz.helper.subcomponent.BZX509TrustManager;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

/**
 * Created by licheng on 2017/8/9.
 */
@Service
public class LinkaiSmsClient {

    public String handle(String url, String method, Map<String, Object> parameter) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
        // 请求结果
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URLConnection conn = null;
        //查询地址
        String queryString = url;
        //请求参数获取
        String postpar = "";

        // map格式的请求参数
        if (parameter != null) {
            StringBuffer mstr = new StringBuffer();
            for (String str : parameter.keySet()) {
                String val = (String) parameter.get(str);
                try {
                    val = URLEncoder.encode(val, "GB2312");
                } catch (UnsupportedEncodingException e) {
                }
                mstr.append(str + "=" + val + "&");
            }
            // 最终参数
            postpar = mstr.toString();
            int lasts = postpar.lastIndexOf("&");
            postpar = postpar.substring(0, lasts);
        }
        if (method.toUpperCase().equals("GET")) {
            queryString += "?" + postpar;
        }
        SSLSocketFactory ssf = BZX509TrustManager.getSSFactory();
        try {
            conn = new URL(queryString).openConnection();
            if (url.toUpperCase().startsWith("HTTPS://")) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(ssf);
            }
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (method.toUpperCase().equals("POST")) {
                conn.setRequestProperty("Content-Length", String.valueOf(postpar.length()));
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                out = new PrintWriter(conn.getOutputStream());
                out.print(postpar);
                out.flush();
            } else {
                conn.connect();
            }
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result.replace("\r", "").replace("\n", "");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    if (url.toUpperCase().startsWith("HTTPS://")) {
                        ((HttpsURLConnection) conn).disconnect();
                    } else {
                        ((HttpURLConnection) conn).disconnect();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
