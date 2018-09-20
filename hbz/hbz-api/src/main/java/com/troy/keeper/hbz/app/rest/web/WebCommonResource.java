package com.troy.keeper.hbz.app.rest.web;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.hbz.consts.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * created by yangjx 2017/11/23
 * web端公共接口
 */
@Slf4j
@RestController
@RequestMapping("/api/web/common")
public class WebCommonResource extends BaseResource {

    /**
     * 图片大小限制初始为3 * 1024 kb
     */
    private static final long maxImageSize = 3072L;

    /**
     * 图片后缀限制
     */
    private static final Set<String> POSTIX_SET = new HashSet<String>() {{
        add("jpg");
        add("jpeg");
        add("png");
        add("gif");
    }};


    /**
     * 将上传的图片转换为base64编码并返回
     *
     * @return
     */
    @PostMapping("/transImageToBase64")
    public ResponseDTO transImageToBase64(@RequestParam("image") MultipartFile image) {
        System.out.println(image.getContentType());

//        File image = new File("D:\\image.png");
        InputStream inputStream = null;
        byte[] byteArr = null;

//        long imageSize = image.length();
        if (image != null) {
            String fileName = image.getOriginalFilename();
            String prefix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            if (POSTIX_SET.contains(prefix)) {
                long imageSize = image.getSize();
                if (imageSize / 1024 < maxImageSize) {
                    try {
//                        inputStream = new FileInputStream(image);
                        inputStream = image.getInputStream();

                        byteArr = new byte[inputStream.available()];
                        inputStream.read(byteArr);

                        Map<String, String> codeMap = new HashMap<>();
                        codeMap.put("base64Code", new String(Base64.encodeBase64(byteArr)));
                        return new ResponseDTO("200", "转换成功", codeMap);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        return new ResponseDTO(Const.STATUS_ERROR, "转换失败", null);
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                    }
                } else {
                    return new ResponseDTO(Const.STATUS_ERROR, "文件体积超出3M", null);
                }
            } else {
                return new ResponseDTO(Const.STATUS_ERROR, "文件格式不正确，只能上传：jpg、jpeg、png、gif", null);
            }
        } else {
            return new ResponseDTO(Const.STATUS_ERROR, "请上传图片", null);
        }
    }

}
