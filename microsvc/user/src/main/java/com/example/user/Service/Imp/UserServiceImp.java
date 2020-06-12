package com.example.user.Service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.Service.UserService;
import com.example.user.dao.UserMapper;
import com.example.user.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper,User> implements UserService {
    public String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
    public String getObjName(String fileName){
        return fileName.substring(fileName.lastIndexOf("/")+ 1 );
    }
    public String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        if (filenameExtension.equalsIgnoreCase(".mp4")){
            return "video/mpeg4";
        }
        return "image/jpg";
    }
}
