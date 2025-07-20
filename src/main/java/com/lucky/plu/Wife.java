package com.lucky.plu;

import com.lucky.manager.ConfigManager;
import com.lucky.utils.StructUtils;
import com.lucky.utils.http.Httphelp;
import com.xww.core.BasePlugins;
import com.xww.core.LogUtils;
import com.xww.model.BotPlugin;
import com.xww.model.Message;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@BotPlugin
public class Wife extends BasePlugins {
    private boolean open = true;
    private final ConfigManager.PluginPath configPath;
    private final static String ApiUrl = "https://flat-dove-71.deno.dev/";
    private static final List<Path> ImgPath = new ArrayList<>();
    private static final Httphelp httphelp = new Httphelp("127.0.0.1", 7890);

    private CompletableFuture<Void> initTask;

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
        return "今日二次元老婆";
    }

    public Wife() {
        configPath = ConfigManager.CreatePluginPath("Wife");
        initTask = CompletableFuture.runAsync(this::initialize);
    }

    @Override
    public void groupHandle(Message message) {
        if (!initTask.isDone()) return;
        if (!message.getRawMessage().equalsIgnoreCase("今日老婆")) return;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(String.format("%s%s", message.getUserId(), LocalDate.now()).getBytes());
            byte[] bytes = messageDigest.digest();
            long index = Math.abs(StructUtils.BytestoLong(bytes)) % ImgPath.size();
            postApi.createGroupMessage(message.getGroupId());
            postApi.chatMessage.addReply(message.getMessageId()).addText("你今日二次元老婆是" + getFileName(ImgPath.get((int) index))).addImage(ImgPath.get((int) index).toString());
            postApi.sendGroupMsg();
        } catch (NoSuchAlgorithmException e) {
            return;
        }
    }


    private void initialize() {
        try {
            var l = httphelp.Get(ApiUrl);
            if (l.isSuccessful()) {
                String[] strings = l.body().string().split("\n");
                for (String jpg : strings) {
                    var imgpath = configPath.getDataPath().resolve(jpg);
                    if (!Files.exists(imgpath)) {
                        Response get = httphelp.Get(ApiUrl + jpg);
                        if (get.isSuccessful()) {
                            boolean b = configPath.WriteDataBytes(jpg, get.body().bytes());
                            if (b) {
                                ImgPath.add(imgpath);
                            }
                        }
                    } else {
                        ImgPath.add(imgpath);
                    }
                }
            }
        } catch (IOException e) {
            LogUtils.error(this.getClass(), e.toString());
        }
    }

    public static String getFileName(Path path) {
        String fileNameWithExt = path.getFileName().toString();

        int lastDotIndex = fileNameWithExt.lastIndexOf('.');

        if (lastDotIndex > 0 && lastDotIndex < fileNameWithExt.length() - 1) {
            return fileNameWithExt.substring(0, lastDotIndex);
        } else {
            return fileNameWithExt;
        }
    }
}
