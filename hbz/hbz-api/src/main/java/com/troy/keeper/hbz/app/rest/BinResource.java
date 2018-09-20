package com.troy.keeper.hbz.app.rest;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.hbz.app.dto.ValDTO;
import com.troy.keeper.hbz.consts.Const;
import com.troy.keeper.hbz.service.HbzBinaryService;
import com.troy.keeper.hbz.sys.annotation.Label;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Created by leecheng on 2017/12/18.
 */
@RestController
public class BinResource {

    @Autowired
    private HbzBinaryService hbzBinaryService;

    @Label("App端 - 文件上传接口")
    @SneakyThrows
    @RequestMapping("/api/binary/up")
    public ResponseDTO up(@RequestParam(name = "file") MultipartFile multipartFile) {
        byte[] bytes = multipartFile.getBytes();
        return new ResponseDTO(Const.STATUS_OK, "操作成功", hbzBinaryService.fastSave(Base64.getEncoder().encodeToString(bytes)));
    }

    @Label("App端 - 大文本上传接口")
    @SneakyThrows
    @RequestMapping("/api/binary/s/up")
    public ResponseDTO sup(@RequestBody ValDTO daval) {
        return new ResponseDTO(Const.STATUS_OK, "操作成功", hbzBinaryService.fastSave(daval.getVal()));
    }

    @Label("App端 - 大文本下载接口")
    @SneakyThrows
    @RequestMapping(value = "/api/binary/s/{key}")
    public ResponseDTO downBase64(@PathVariable(name = "key") String keyPath) {
        Object key = keyPath;
        return new ResponseDTO(Const.STATUS_OK, "操作成功", hbzBinaryService.findData((String) key));
    }

    @Label("App端 - 二进制下载")
    @SneakyThrows
    @RequestMapping(value = "/api/binary/bin/{key}")
    public void down(@PathVariable(name = "key") String keyPath, HttpServletResponse response) {
        Object key = keyPath;
        String data = hbzBinaryService.findData((String) key);
        OutputStream os = response.getOutputStream();
        os.write(Base64.getDecoder().decode(data));
        os.flush();
    }

}
