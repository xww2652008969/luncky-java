package com.lucky;

import com.lucky.manager.ConfigManager;
import com.xww.core.BootConfig;
import com.xww.core.Bot;

import java.io.IOException;


public class App {
    public static void main(String[] args) throws IOException {
        System.out.println(5 % 4);
        ConfigManager.Init();
        var config = new BootConfig();
        config.setPluginDir("com/lucky/plu");
        config.setWsUrl("ws://192.168.123.3:3001");
        config.setHttpUrl("http://192.168.123.3:4000");
        config.setDebug(true);
        var bot = new Bot(config);
        System.out.println(bot.getPluginControl().getPlugins());
        bot.run();
    }

}
