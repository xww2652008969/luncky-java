package com.lucky.plu;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.lucky.manager.ConfigManager;
import com.lucky.utils.http.Httphelp;
import com.xww.core.BasePlugins;
import com.xww.core.LogUtils;
import com.xww.model.BotPlugin;
import okhttp3.Response;

import java.io.IOException;

@BotPlugin
public class Love extends BasePlugins {
    private static boolean open = true;
    private static Httphelp httphelp = new Httphelp();
    private static ConfigManager.PluginPath config;

    public Love() {
        config = ConfigManager.CreatePluginPath("love");
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
        return "土味情话";
    }

    @Override
    public void push() {
        CronUtil.schedule("0 0 * * * *", (Task) this::miao);
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        for (; ; ) {

        }
    }

    private void miao() {
        var s = getlove();
        if (s != null) {
            postApi.createGroupMessage(628483968);
            postApi.chatMessage.addAt(827370816).addText(" 喵喵你知道吗\n").addText(s);
            postApi.sendGroupMsg();
        }
    }

    private String getlove() {
        try {
            Response get = httphelp.Get("https://api.zxki.cn/api/twqh");
            if (get.isSuccessful()) {
                return get.body().string();
            }
        } catch (IOException e) {
            LogUtils.error(Love.class, e.toString());
        }
        return null;
    }
}
