package com.lucky.plu.llf;

import com.lucky.manager.ConfigManager;
import com.xww.core.BasePlugins;
import com.xww.core.LogUtils;
import com.xww.model.BotPlugin;
import com.xww.model.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@BotPlugin
public class Llf extends BasePlugins {
    private static boolean open = true;
    private final ConfigManager.PluginPath configPath;
    private static final List<Path> imgpath = new ArrayList<>();

    public Llf() {
        configPath = ConfigManager.CreatePluginPath("llf");
        try {
            Stream<Path> list = Files.list(configPath.getConfigPath());
            list.filter(Files::isRegularFile)
                    .forEach(imgpath::add);
            list.close();
            System.out.println(imgpath.size());
        } catch (IOException e) {
            LogUtils.error(Llf.class, e.toString());
        }
    }

    @Override
    public boolean isOpen() {
        return open;
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
        return "母肥";
    }

    @Override
    public void groupHandle(Message message) {
        if (message.getRawMessage().equals("母肥")) {
            Random random = new Random();
            // 生成 a 到 b 的随机整数
            int i = random.nextInt(3 - 1 + 1) + 1; // 随机数范围包括 b
            postApi.createGroupMessage(message.getGroupId());
            postApi.chatMessage.addReply(message.getMessageId());
            for (int j = 0; j < i; j++) {
                int i1 = random.nextInt(imgpath.size());
                var img = imgpath.get(i1).toString();
                postApi.chatMessage.addImage(img);
            }
            postApi.sendGroupMsg();
        }
    }
}
