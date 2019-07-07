package com.pinyougou.shop.controller;

import com.pinyougou.common.util.FastDFSClient;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @RequestMapping("/uploadFile.do")
    public Result uploadFile(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();

        String extName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String url = client.uploadFile(file.getBytes(), extName);
//            url += "http://192.168.25.133/";
            System.out.println(url);
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
}
