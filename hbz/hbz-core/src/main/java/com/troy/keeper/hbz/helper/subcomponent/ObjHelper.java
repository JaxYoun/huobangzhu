package com.troy.keeper.hbz.helper.subcomponent;

import com.troy.keeper.hbz.helper.Base64Encrypter;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by leecheng on 2017/11/10.
 */
public class ObjHelper {

    @SneakyThrows
    public static String obj2str(Object obj) {
        if (obj == null) return null;
        @Cleanup ByteArrayOutputStream baos = new ByteArrayOutputStream();
        @Cleanup ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        return Base64Encrypter.base64Encode(baos.toByteArray());
    }

    @SneakyThrows
    public static Object str2obj(String st) {
        if (st == null) return null;
        @Cleanup ByteArrayInputStream bais = new ByteArrayInputStream(Base64Encrypter.base64Decode(st));
        @Cleanup ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

}
