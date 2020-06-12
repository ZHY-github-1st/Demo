package com.example.user.Controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.example.user.pojo.User;
import com.example.user.dao.UserMapper;
import com.example.user.repository.UserRepository;
import com.yx.lib.json.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello from User Controller";
    }

    @Autowired
    private UploadUser uploadUser;

    @RequestMapping("/test")
    public void getImgUrl(HttpServletRequest request) throws Exception {
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.apply("id = {0}","2")
//                .inSql("username","select username from user where username like '%龙'");
//        List<User> list = userMapper.selectList(wrapper);

//        userRepository.deleteById(4);
        List<User> list = userRepository.findAll();
        ArrayList<String> array = new ArrayList<String>();
        for (User user :
                list) {
            array.add(user.getUsername());
            System.out.println(array);
        }
        String obj = "d://u=410793110,2154823044&fm=15&gp=0.jpg";
        File file = new File(obj);
        JsonResult msg = uploadUser.uploadFile(file, obj);
        HashMap url = uploadUser.getUrl(file);
        System.out.println(msg);
        System.out.println(url);
    }

//    accountID: 1616591087457048 acs:ram::1616591087457048:role/cs
//    @RequestMapping("/getSTS")
//    public JsonResult getSTS(@Param("accountId") String accountId, @Param("roleSessinName")String roleSessinName, @Param("roleName")String roleName){
//        JsonResult sts = uploadUser.getSTS(accountId, roleName, roleSessinName);
//        return sts;
//    }

    @RequestMapping("/video")
    public void getVideoUrl(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String obj = "d://QQ空间视频_20200506163351.mp4";
        File file = new File(obj);
//        InputStream is = new FileInputStream(file);
        UploadUser uploadUser = new UploadUser();
        HashMap map = uploadUser.uploadVideo(file, obj, session);
        System.out.println(map);
    }

    @GetMapping("/getSTS")
    public AssumeRoleResponse getSTS() {
        AssumeRoleResponse response = (AssumeRoleResponse) uploadUser.getSTS("", "", "").getObject();
        String accessKeyId = response.getCredentials().getAccessKeyId();
        String accessKeySecret = response.getCredentials().getAccessKeySecret();
        String securityToken = response.getCredentials().getSecurityToken();
        System.out.println("accessKeyId:" + accessKeyId + "\n" + "accessKeySecret:" + accessKeySecret + "\n" + "securityToken:" + "\n" + securityToken);
        OSS oss = new OSSClientBuilder().build("http://oss-cn-beijing.aliyuncs.com", response.getCredentials().getAccessKeyId(), response.getCredentials().getAccessKeySecret(), response.getCredentials().getSecurityToken());
        Date expiration = new Date(new Date().getTime() + 3600 * 10000);
        URL url = oss.generatePresignedUrl("zlife-file", "test.jpg", expiration);
        System.out.println(url);
        return response;
    }

    @GetMapping(value = "/users")
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    /**
     * 增
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/users")
    public User addUser(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        User userEntity = new User();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        return userRepository.save(userEntity);
    }

    /**
     * 改
     *
     * @param id
     * @param username
     * @param password
     * @return
     */
    @PutMapping(value = "users/{id}")
    public User updUser(@PathVariable("id") Integer id,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password) {
        User userEntity = new User();
        userEntity.setId(id);
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        return userRepository.save(userEntity);

    }

    /**
     * 删
     *
     * @param id
     */
    @DeleteMapping(value = "users/{id}")
    public void delUser(@PathVariable("id") Integer id) {
        User userEntity = new User();
        userEntity.setId(id);
        userRepository.delete(userEntity);
    }
}
