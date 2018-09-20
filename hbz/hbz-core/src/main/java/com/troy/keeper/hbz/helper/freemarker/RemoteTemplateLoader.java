package com.troy.keeper.hbz.helper.freemarker;

import freemarker.cache.URLTemplateLoader;

import java.net.URL;
import java.net.URLConnection;

/**
 * @Author：YangJx
 * @Description：
 * @DateTime：2018/3/2 11:33
 */
public class RemoteTemplateLoader extends URLTemplateLoader {
    // 远程模板文件的存储路径（目录）
    private String remotePath;

    public RemoteTemplateLoader(String remotePath) {
        if (remotePath == null) {
            throw new IllegalArgumentException("remotePath is null");
        }
        this.remotePath = canonicalizePrefix(remotePath);
        if (this.remotePath.indexOf('/') == 0) {
            this.remotePath = this.remotePath.substring(this.remotePath.indexOf('/') + 1);
        }
    }

    @Override
    protected URL getURL(String name) {
        String fullPath = this.remotePath + name;
        if ((this.remotePath.equals("/")) && (!isSchemeless(fullPath))) {
            return null;
        }
        if (name.contains("WEB-INF/template/")) {
            fullPath = fullPath.replace("WEB-INF/template/", "");
        }
        URL url = null;
        try {
            url = new URL(fullPath);
            URLConnection con = url.openConnection();
            long lastModified = con.getLastModified();
            if (lastModified == 0) {
                url = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            url = null;
        }
        return url;
    }

    private static boolean isSchemeless(String fullPath) {
        int i = 0;
        int ln = fullPath.length();
        if ((i < ln) && (fullPath.charAt(i) == '/')) {
            i++;
        }
        while (i < ln) {
            char c = fullPath.charAt(i);
            if (c == '/') {
                return true;
            }
            if (c == ':') {
                return false;
            }
            i++;
        }
        return true;
    }
}
