package com.bot.qspring.service.stopped;

import com.bot.qspring.entity.po.Customreply;
import com.bot.qspring.mapper.CustomreplyMapper;
import com.bot.qspring.service.dbauto.CustomreplyService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CustomReply {


    @Autowired
    private CustomreplyService customreplyService;

    @Autowired
    private CustomreplyMapper customreplyMapper;

    JsonElement rules;

    //@PostConstruct
    private void loadRules() {
        String jsonStr = "";
        Gson gson = new Gson();
        try {
            File jsonFile = ResourceUtils.getFile("classpath:content/rules.json");
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.rules = gson.fromJson(jsonStr, JsonElement.class);
    }

    String customReplyHelp = "" +
            "《自定义问答指南》\n" +
            "注意：除查询外均需要管理员审核通过，审核结果会第一时间通知！\n" +
            "【输入格式】: " +
            "学习[换行]  命令[换行]  参数\n" +
            "【可用命令】: " +
            "查询 添加 修改 删除\n" +
            "【示例】: \n" +
            "学习\n" +
            "添加\n" +
            "宫廷玉液酒\n" +
            "一百八一杯\n";


    private String checkCustomReply(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("question", msg);
        List<Customreply> replyList = customreplyMapper.selectByMap(map);
        if (replyList.isEmpty()) {
            return "NULL";
        } else if (replyList.size() != 1) {
            //有多个回复
            Random random = new Random();
            int pick_num = random.nextInt(replyList.size());
            Customreply pick = replyList.get(pick_num);
            String reply = pick.getAnswer();
            return reply;
        } else {
            return replyList.get(0).getAnswer();
        }
    }
//
//    String msg = messageVo.getMessage();
//    //senderService.sendPrivate(messageVo.getUser_id(), receive);
//        if(msg.startsWith("学习")){
//
//            /*String receive = this.Study(messageVo.getUser_id(),msg);
//            if(receive.equals("ERROR")){
//                receive = "无法识别语句，请重试\n" + customReplyHelp;
//            }
//            senderService.sendPrivate(messageVo.getUser_id(), receive);*/
//    }
//        else{
//        String receive = checkCustomReply(msg);
//        if(receive.equals("NULL")){
//            //ignore
//        }
//        else{
//            senderService.sendPrivate(messageVo.getUser_id(), receive);
//        }
//    }

    private String Study(Long from, String msg) {
        String[] rawList = msg.split("\r\n");
        if (rawList.length < 3) {
            return "ERROR";
        }
        String type = rawList[1];

        String question = rawList[2];

        if (type.equals("查询")) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("question", question);
            List<Customreply> getList = customreplyMapper.selectByMap(queryMap);
            if (getList.size() == 0) {
                return "未查到相关记录！";
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("查询到以下回复：\n");
                int number = 1;
                for (var each : getList) {
                    builder.append(number).append(". ");
                    builder.append(each.getAnswer());
                    builder.append("\n");
                    number++;
                }
                return builder.toString();
            }

        }

        String answer = rawList[3];

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("question", question);
        queryMap.put("answer", answer);
        List<Customreply> getList = customreplyMapper.selectByMap(queryMap);


        switch (type) {
            case "添加":
                if (getList.size() == 0) {
                    Customreply customreply = new Customreply();
                    customreply.setQuestion(question);
                    customreply.setAnswer(answer);
                    customreply.setCreator(from);
                    customreply.setLatestEditor(from);
                    customreplyService.save(customreply);
                    return "保存成功！";
                } else {
                    return "此规则已存在！";
                }
            case "修改":
                if (rawList.length < 5) {
                    return "ERROR";
                }
                String changeTo = rawList[4];
                if (getList.size() == 0) {
                    return "未查到此规则！";
                } else {
                    queryMap.put("answer", changeTo);
                    List<Customreply> getList2 = customreplyMapper.selectByMap(queryMap);
                    if (getList2.size() == 0) {
                        Customreply customreply = getList.get(0);
                        customreply.setAnswer(changeTo);
                        customreply.setLatestEditor(from);
                        customreplyService.updateById(customreply);
                        return "修改成功！";
                    } else {
                        return "修改撞车，请重试！";
                    }
                }
            case "删除":
                if (getList.size() == 0) {
                    return "未查到此规则！";
                } else {
                    customreplyService.removeById(getList.get(0));
                    return "删除成功！";
                }
            default:
                return "ERROR";
        }
    }

    //group
    /*else{
            String receive = checkCustomReply(msg);
            if(receive.equals("NULL")){
                //ignore
            }
            else{
                StringBuilder builder = new StringBuilder();
                //builder.append("[CQ:at,qq="+messageVo.getUser_id().toString()+"]\n");
                builder.append(receive);
                senderService.sendGroup(messageVo.getGroup_id(), builder.toString());
                int check_num = 0;
                while(!checkCustomReply(receive).equals("NULL") && check_num < 20){
                    receive = checkCustomReply(receive);
                    senderService.sendGroup(messageVo.getGroup_id(), builder.toString());
                    check_num++;
                }
            }
        }*/
}
