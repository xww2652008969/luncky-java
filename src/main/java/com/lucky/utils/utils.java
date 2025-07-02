package com.lucky.utils;

import com.xww.model.Message;

import java.util.Objects;

public class utils {
    public static AtResult isAtMe(Message message){
        for (var a:message.getMessage()){
            if(Objects.equals(a.getType(), "at")){
                return new AtResult(Long.parseLong(a.getData().getQq()),true);
            }
        }
        return new AtResult(0,false);
    }
    public static String getUserName(String Card, String Nickname){
        if(Card.equals("")||Card==null){
            return Nickname;
        }
        return Card;
    }








    public static class AtResult{
        public long qq;
        public boolean isat;
        public AtResult(long qq,Boolean flag){
            this.qq=qq;
            this.isat=flag;
        }
    }
}
