package com.example.user.Controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.example.user.Service.Imp.UserServiceImp;
import com.example.user.Service.UserService;
import com.yx.lib.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;

@Service
public class UploadUser {
    @Autowired
    private UserService userService;
    private Logger log = LoggerFactory.getLogger(UploadUser.class);
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId = "LTAI4GHMWXQVpgoBJkngGUAT";
    private String accessKeySecret = "eou5zy7oorQfTNMywLNZjfNqzl1XTr";
    // 创建PutObjectRequest对象。
    String content;
    private String bucketName = "zlife-file";
    private String objectName = "";
    private String dir = "file/";

    private OSS initClient(String endpoint, String accessKeyId, String accessKeySecret) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        this.endpoint = endpoint;
        this.accessKeySecret = accessKeySecret;
        this.accessKeyId = accessKeyId;
        return ossClient;
    }
//    acs:ram::1616591087457048:role/cs
    private String getRoleArn(){
        return "acs:ram::1616591087457048:role/cs";
    }
//    private String getRoleArn(String accountId,String roleName){
//        return new String("asc:ram::"+accountId+":role/"+roleName);
//    }
    /**
     * * 上传到OSS服务器 如果同名文件会覆盖服务器上的
     * *
     * * @param instream
     * *            文件流
     * * @param fileName
     * *            文件名称 包括后缀名
     * * @return 出错返回"" ,唯一MD5数字签名
     */
    public JsonResult uploadFile(File uploadFile, String fileName) throws FileNotFoundException {
        UserService userService = new UserServiceImp();
        OSS oss = initClient(endpoint, accessKeyId, accessKeySecret);
        String suffix = userService.getSuffix(fileName);
        String type = userService.getcontentType(suffix);
        InputStream inputStream = new FileInputStream(uploadFile);
        this.objectName = userService.getObjName(fileName);
        ObjectMetadata omd = new ObjectMetadata();
        try {
            omd.setContentLength(inputStream.available());
            omd.setContentType(type);
            omd.setCacheControl("no-catch");
            omd.setHeader("Pragma", "no-cache");
            omd.setContentDisposition("inline;filename=\""+ fileName+"\"");
//            oss.putObject(bucketName, uploadFile.getName(), inputStream);
            PutObjectResult putresult = oss.putObject(bucketName,uploadFile.getName() , inputStream, omd);
            return new JsonResult(true,"上传成功",getUrl(uploadFile));
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult(false,"上传失败");
        }
    }

    public void download(String filePath) {
        OSS oss = initClient(endpoint, accessKeyId, accessKeySecret);
        this.objectName = userService.getObjName(objectName);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        oss.getObject(new GetObjectRequest(bucketName, objectName), new File(filePath));
        oss.shutdown();
    }

    public HashMap getUrl(File uploadFile) {
        OSS oss = initClient(endpoint, accessKeyId, accessKeySecret);
        //设置过期时间 -- 两小时
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 2);
        //取出文件上传路径
        String url = oss.generatePresignedUrl(bucketName, uploadFile.getName(), expiration).toString();
        HashMap<Object, Object> hashMap = new HashMap<>();
//        String url = oss.generatePresignedUrl(bucketName, videoPath + uploadFile.getOriginalFilename(), expiration).toString();
        HashMap map = new HashMap<String,Object>();
        map.put("key",uploadFile.getName());
//        map.put("key", videoPath + uploadFile.getOriginalFilename());
        map.put("url", url);
        return map;
    }

    public HashMap uploadVideo(File uploadFile, String videoPath, HttpSession session) {
        OSS oss = initClient(endpoint, accessKeyId, accessKeySecret);
        /**
         * 这里用带进度条的OSS上传
         * 将session传入PutObjectProgressListener的构造中!官网例子是没有这个操作的
         * 注意new PutObjectRequest()的第三个参数是File而不是官网介绍的FileInputStream,否则获取不到进度. 一定切记！！！
         * MultipartFile转File
         */
//        File f = null;
//        try {
//            f = File.createTempFile("tmp", null);
//            uploadFile.transferTo(f);
//            f.deleteOnExit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        // 上传  --> 不带进度条上传
//        ossClient.putObject(bucketNamePrivate, videoPath + uploadFile.getOriginalFilename(), new ByteArrayInputStream(uploadFile.getBytes()));
        // 上传 --> 带进度条上传
        oss.putObject(new PutObjectRequest(bucketName,uploadFile.getName(),uploadFile).withProgressListener(new PutObjectProgressListener(session)));
//        oss.putObject(new PutObjectRequest(bucketName, videoPath + uploadFile.getOriginalFilename(),f).withProgressListener(new PutObjectProgressListener(session)));
        // 关闭client
        oss.shutdown();
        //设置过期时间 -- 两小时
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 2);
        //取出文件上传路径
        String url = oss.generatePresignedUrl(bucketName, uploadFile.getName(), expiration).toString();
//        String url = oss.generatePresignedUrl(bucketName, videoPath + uploadFile.getOriginalFilename(), expiration).toString();
        HashMap map = new HashMap<String,Object>();
        map.put("key",uploadFile.getName());
//        map.put("key", videoPath + uploadFile.getOriginalFilename());
        map.put("url", url);
        return map;
    }

    public  class PutObjectProgressListener implements ProgressListener {
        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;
        private HttpSession session;
        private int percent = 0;

        //构造方法中加入session
        public PutObjectProgressListener() {
        }

        public PutObjectProgressListener(HttpSession mSession) {
            this.session = mSession;
            session.setAttribute("upload_percent", percent);
        }

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    log.debug("Start to upload......");
                    break;
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    log.debug(this.totalBytes + " bytes in total will be uploaded to OSS");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                        //将进度percent放入session中 官网demo中没有放入session这一步
                        session.setAttribute("upload_percent", percent);
                        log.debug(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    } else {
                        log.debug(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    log.debug("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                    break;
                case TRANSFER_FAILED_EVENT:
                    log.debug("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                    break;
                default:
                    break;
            }
        }

        public boolean isSucceed() {
            return succeed;
        }
    }





    public JsonResult getSTS(String accountId, String roleName, String roleSessionName) {
        try {
            IClientProfile icprofile = DefaultProfile.getProfile("", "LTAI4G5WQrHHVKAhczoa29rk", "Xe9clWf19MDfieJzyPQBItj8wkymki");
            // 用 profile 构造 client
            DefaultAcsClient client = new DefaultAcsClient(icprofile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            String roleArn = getRoleArn();
//            String roleArn = getRoleArn(accountId,roleName);
            request.setSysEndpoint("sts.cn-beijing.aliyuncs.com");
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName("alices");
            request.setPolicy("{\n" +
                    "    \"Statement\": [\n" +
                    "        {\n" +
                    "            \"Action\": \"oss:*\",\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Resource\": \"*\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Version\": \"1\"\n" +
                    "}"); // Optional
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return new JsonResult(200, "获取sts临时凭证成功",  response);
        } catch (Exception e) {
            log.info("Error Message: " + e.getMessage());
            return new JsonResult(500, "获取sts临时凭证失败", null);
        }
    }

}
