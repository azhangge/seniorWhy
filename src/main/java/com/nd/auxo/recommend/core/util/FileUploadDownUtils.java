package com.nd.auxo.recommend.core.util;

/**
 * Created by way on 2016/10/28.
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 *
 */

/**
 * @author zhzh 2015-3-31
 */
@Slf4j
public class FileUploadDownUtils {


    /**
     * 上传文件
     *
     * @throws ParseException
     * @throws IOException
     */
    public static Integer postFileToCloud(String url, String destFilePath) throws ParseException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            // 要上传的文件的路径
            HttpPost httpPost = new HttpPost(
                    url);
            httpPost.setHeader("Connection", "keep-alive");
            // 把文件转换成流对象FileBody
            File file = new File(destFilePath);
            FileBody bin = new FileBody(file);
            /*StringBody uploadFileName = new StringBody("my.png",
                    ContentType.create("text/plain", Consts.UTF_8));*/
            // 以浏览器兼容模式运行，防止文件名乱码。
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addPart("uploadFile", bin) // uploadFile对应服务端类的同名属性<File类型>
                    //.addPart("uploadFileName", uploadFileName)// uploadFileName对应服务端类的同名属性<String类型>
                    .setCharset(CharsetUtils.get("UTF-8")).build();

            httpPost.setEntity(reqEntity);
            // 发起请求 并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                // 打印响应状态
//                System.out.println(response.getStatusLine());
                // 获取响应对象
                HttpEntity resEntity = response.getEntity();
                Integer storeObjectId =null ;
                String errorMessage = "upload result:"+response.getStatusLine()+"";
                if (resEntity != null) {
                    // 打印响应长度
//                    System.out.println("Response content length: "
//                            + resEntity.getContentLength());
                    // 打印响应内容
//                    System.out.println(EntityUtils.toString(resEntity,
//                            Charset.forName("UTF-8")));

                    String content = EntityUtils.toString(resEntity,
                            Charset.forName("UTF-8"));
                    errorMessage += content;
                    JsonNode body = new ObjectMapper().readTree(content);
                    JsonNode data = body.get("Data");
                    if (!data.isNull()) {
                        storeObjectId =  data.get(0).get("StoreObjectId").getIntValue();
                    }
                }
                // 销毁
                EntityUtils.consume(resEntity);
                if(storeObjectId == null){
                    log.error(errorMessage);
                }
                return storeObjectId;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }

    /**
     * 下载文件
     *
     * @param url          http://www.xxx.com/img/333.jpg
     * @param destFileName xxx.jpg/xxx.png/xxx.txt
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static void getFile(String url, String destFileName)
            throws IOException {
        // 生成一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        File file = new File(destFileName);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp, 0, l);
                // 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
            }
            fout.flush();
            fout.close();
        } finally {
            // 关闭低层流。
            in.close();
            httpclient.close();
        }
    }

}

