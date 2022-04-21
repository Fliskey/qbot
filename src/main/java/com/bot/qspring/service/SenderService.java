package com.bot.qspring.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

@Service
public class SenderService {

    String host = "http://127.0.0.1:6700";

    public String getGroupName(Long groupId){
        String url = host + "/get_group_info?group_id=" + groupId.toString();
        JsonElement getData = send(url);
        if(getData == null){
            return "";
        }
        String getName = getData.getAsJsonObject().get("group_name").getAsString();
        return getName;
    }

    private String toUrl(String msg){
        return msg
                .trim()
                .replace("\r\n","%0A")
                .replace("\n","%0A")
                .replace("\r","%0A")
                .replace(" ","%20")
                .replace("+","%2B");
    }

    public Integer sendPrivate(Long to, String msg){
        msg = toUrl(msg);
        String url = host + "/send_private_msg?user_id=" + to.toString() + "&message=" + msg;
        JsonElement getData = send(url);
        if(getData == null){
            return 0;
        }
        return getData.getAsJsonObject().get("message_id").getAsInt();
    }

    public Integer sendGroup(Long user_id, Long group_id, String msg){
        msg = toUrl("[CQ:at,qq=" + user_id + "]\n" + msg);
        String url = host + "/send_group_msg?group_id=" + group_id.toString() + "&message=" + msg;
        JsonElement getData = send(url);
        if(getData == null){
            return 0;
        }
        return getData.getAsJsonObject().get("message_id").getAsInt();
    }

    private JsonElement send(String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response;
        System.out.println(url);
        HttpPost httpPost = new HttpPost(url);
        try{
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            byte[] body = new byte[(int) entity.getContentLength()];
            InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder builder = new StringBuilder();
            while(bufferedReader.ready()){
                builder.append((char) bufferedReader.read());
            }
            String str = builder.toString();
            System.out.println(str);
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(str, JsonElement.class);
            return element.getAsJsonObject().get("data");
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }
}
