package com.itl.purple_gold.controller;


import com.itl.purple_gold.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${purple-gold.path}")
    private String basePath;

    /**
     * 文件上传到服务器
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        //file是临时文件，请求完成后删除

        //原始文件名
        String originalFilename = file.getOriginalFilename();

        //文件后缀
        String fileType=originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重构文件名
        String fileName = UUID.randomUUID().toString()+fileType;

        log.info("文件上传,文件名{}",fileName);

        //创建目录对象
        File dir=new File(basePath);
        if (!dir.exists()){
            //目录不存在，创建目录
            dir.mkdirs();
        }

        try {
            //临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }

    /**
     * 文件下载到浏览器
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //通过输入流读取文件内容
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));

            //通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            //响应回去的文件类型
            response.setContentType("image/jpeg");

            int len;
            byte[] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
