package com.libang.tms.fileStore;

import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 七牛云文件存储
 * @author libang
 * @date 2018/9/1 8:45
 */
@Component
public class QiniuStore {


    //私钥
    @Value("${qiniu.ak}")

    private String accessKey;


    //秘钥
    @Value("${qiniu.sk}")

    private String secretKey;

    //bucket名称

    @Value("${qiniu.bucket")
    private String bucket;


    public String getUpLoadToken(){

        Auth auth = Auth.create(accessKey,secretKey);
        return auth.uploadToken(bucket);

    }


}
