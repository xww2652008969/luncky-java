package com.lucky.plu;

import com.lucky.manager.ConfigManager;
import com.lucky.utils.StructUtils;
import com.lucky.utils.utils;
import com.xww.core.BasePlugins;
import com.xww.core.LogUtils;
import com.xww.model.BotPlugin;
import com.xww.model.Message;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@BotPlugin
public class JRRP extends BasePlugins {
    private static boolean open = true;

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void setState(boolean flag) {
        open = flag;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "xww";
    }

    @Override
    public String getName() {
        return "今日人品";
    }

    @Override
    public void groupHandle(Message message) {

        if (!message.getRawMessage().equalsIgnoreCase("jrrp")) return;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            //笑死有人转化为byte
            messageDigest.update(String.format("%s%s", message.getUserId(), LocalDate.now()).getBytes());
            byte[] bytes = messageDigest.digest();
            long luckValue = Math.abs(StructUtils.BytestoLong(bytes)) % 101;
            var mchat = "";
            var username = utils.getUserName(message.getSender().getCard(), message.getSender().getNickname());
            if (luckValue == 0) {
                mchat = (username + " 的今日人品是：" + luckValue + "。怎，怎么会这样……");
            } else if (luckValue > 0 && luckValue <= 20) {
                mchat = (username + " 的今日人品是：" + luckValue + "。推荐闷头睡大觉。");
            } else if (luckValue > 20 && luckValue <= 40) {
                mchat = (username + " 的今日人品是：" + luckValue + "。也许今天适合摆烂。");
            } else if (luckValue > 40 && luckValue <= 60 && luckValue != 42) {
                mchat = (username + " 的今日人品是：" + luckValue + "。又是平凡的一天。");
            } else if (luckValue > 60 && luckValue <= 80 && luckValue != 77) {
                mchat = (username + " 的今日人品是：" + luckValue + "。太阳当空照，花儿对你笑。");
            } else if (luckValue > 80 && luckValue < 100) {
                mchat = (username + " 的今日人品是：" + luckValue + "。出门可能捡到" + luckValue + "块钱。");
            } else if (luckValue == 42) {
                mchat = (username + " 的今日人品是：" + luckValue + "。感觉可以参透宇宙的真理。");
            } else if (luckValue == 77) {
                mchat = (username + " 的今日人品是：" + luckValue + "。要不要去抽一发卡试试呢……");
            } else if (luckValue == 100) {
                mchat = (username + " 的今日人品是：" + luckValue + "。买彩票可能会中大奖哦！");
            }
            if (!mchat.isEmpty()) {
                postApi.createGroupMessage(message.getGroupId());
                postApi.chatMessage.addReply(message.getMessageId()).addText(mchat);
                postApi.sendGroupMsg();
            }
        } catch (NoSuchAlgorithmException e) {
            LogUtils.error(ConfigManager.class, e.toString());
        }
    }
}
