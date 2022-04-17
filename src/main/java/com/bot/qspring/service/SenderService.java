package com.bot.qspring.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;

@Service
public class SenderService {

    String host = "http://127.0.0.1:6700";

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
        return send(url);
    }

    public Integer sendGroup(Long user_id, Long group_id, String msg){
        msg = toUrl("[CQ:at,qq=" + user_id + "]\n" + msg);
        String url = host + "/send_group_msg?group_id=" + group_id.toString() + "&message=" + msg;
        return send(url);
    }

    private Integer send(String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response;
        System.out.println(url);
        HttpPost httpPost = new HttpPost(url);
        try{
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            byte[] body = new byte[(int) entity.getContentLength()];
            entity.getContent().read(body);
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < body.length; i++){
                builder.append((char)body[i]);
            }
            String str = builder.toString();
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(str, JsonElement.class);
            JsonElement msg_id = element.getAsJsonObject().get("data").getAsJsonObject().get("message_id");
            return msg_id.getAsInt();
        }
        catch (Exception e){
            System.err.println(e);
        }
        return 0;
    }
}
