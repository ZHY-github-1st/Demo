package com.example.user.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {
    String getObjName(String fileName);

    String getSuffix(String fileName);

    String getcontentType(String filenameExtension);
}
