package com.wanshan.reggie.controller;

import com.wanshan.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author: Li JinHua
 * @date: 2025/9/25 下午4:58
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile  file) {
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info("文件上传：{}",file);
        String originalFilename = file.getOriginalFilename();
//        String[] split = filename.split("\\.");
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void donwnloda(String name, HttpServletResponse  response) {
        try {
            //输入流 通过输入流读取文件内容
            FileInputStream fis = new FileInputStream(basePath + name);

            //输出流,通过输出流将文件写回浏览器,在浏览器展示图片
            ServletOutputStream sos = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fis.read(bytes)) != -1){
                sos.write(bytes,0,len);
                sos.flush();
            }

            sos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
