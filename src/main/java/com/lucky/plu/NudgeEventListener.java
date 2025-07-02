package com.lucky.plu;

import com.xww.core.BasePlugins;
import com.xww.model.BotPlugin;
import com.xww.model.Message;

import java.lang.annotation.Annotation;
import java.util.Random;

@BotPlugin
public class NudgeEventListener extends BasePlugins {

    private boolean open=true;

   private static String[] nudge ={
        "喂(#`O′)，戳我干什么",
                "不许戳！",
                "再这样我要叫警察叔叔啦",
                "讨厌没有边界感的人类",
                "戳牛魔戳",
                "再戳我就要戳回去啦",
                "呜......戳坏了",
                "放手啦，不给戳QAQ",
                "(。´・ω・)ん?",
                "请不要戳 >_<",
                "这里是hmmt(っ●ω●)っ",
                "啾咪~",
                "ん？",
                "hmmt不在",
                "厨房有煤气灶自己拧着玩",
                "操作太快了，等会再试试吧",
                "手指该充电啦~滴滴滴电量不足警告！",
                "这是hmmt的草莓奶油城堡~禁止投喂手指",
                "再戳的话...会触发害羞の隐藏剧情哦！",
                "操作太快像在打音游呢~要掉血啦！",
                "手指该做SPA啦~再动要变成胡萝卜啦！",
                "hmmt宝宝在睡觉~不许戳醒它！",
                "这是游戏存档点~乱戳会覆盖进度哦！",
    };
    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void setState(boolean flag) {
        this.open=flag;
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getAuthor() {
        return "xww";
    }

    @Override
    public String getName() {
        return "搓一搓";
    }

    @Override
    public void noticeHandle(Message message) {
        if (message.getTargetId()==message.getSelfId()&& message.getSubType().equals("poke")){
            postApi.createGroupMessage(message.getGroupId());
            Random random = new Random();
            String selectedString = nudge[random.nextInt(nudge.length)];
            postApi.chatMessage.addText(selectedString);
            postApi.sendGroupMsg();
        }
    }
}
