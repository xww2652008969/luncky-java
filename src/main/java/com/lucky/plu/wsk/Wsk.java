package com.lucky.plu.wsk;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.xww.core.BasePlugins;
import com.xww.core.LogUtils;
import com.xww.model.BotPlugin;
import com.xww.model.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.lucky.plu.wsk.Wsktool.*;
import static java.lang.Thread.sleep;

@BotPlugin
public class Wsk extends BasePlugins {
    private static boolean open = false;

    private static Path path = Paths.get(System.getProperty("user.dir")).resolve("Wskconfig.json");
    //通过服务器id推送给群的列表
    private static Map<Integer, List<Long>> PushList = new ConcurrentHashMap<>();
    //储存轮询后的数据的
    private static Map<Integer, ApiResponse> WskDataList = new ConcurrentHashMap<>();

    //用来判断是不是预告的
    private static final Set<Integer> EMERGENCY_IDS = new HashSet<>();

    static {
        EMERGENCY_IDS.add(149);
        EMERGENCY_IDS.add(194);
        EMERGENCY_IDS.add(195);
        EMERGENCY_IDS.add(196);
        EMERGENCY_IDS.add(197);
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
        return "1.0.0";
    }

    @Override
    public String getAuthor() {
        return "xww";
    }

    @Override
    public String getName() {
        return "月球天气预报";
    }


    @Override
    public void groupHandle(Message message) {
        LocalDateTime.now();
        String[] s = message.getRawMessage().split(" ");
        if (s.length == 1) {
            if (s[0].equalsIgnoreCase("wsk")) {
                //获取帮助的先不写
                postApi.createGroupMessage(message.getGroupId());
                postApi.chatMessage.addReply(message.getMessageId());
                postApi.chatMessage.addText("wsk 服务器名字 -获取服务器当前天气信息\n");
                postApi.chatMessage.addText("wsk add 服务器名字 -为本群订阅服务器的信息\n");
                postApi.chatMessage.addText("wsk remove 服务器名字 -为本群取消订阅服务器的信息\n");
                postApi.sendGroupMsg();
                return;
            }
        }
        if (s.length == 2) {
            if (s[0].equalsIgnoreCase("wsk")) {
                if (REVERSE_SERVERMAP.get(s[1]) != null) {
                    LogUtils.info(this.getClass(), String.format("获取服务器数据成功:%s", s[1]));
                    if (WskDataList.get(REVERSE_SERVERMAP.get(s[1])) != null) {
                        buildchat(message.getGroupId(), WskDataList.get(REVERSE_SERVERMAP.get(s[1])));
                        return;
                    } else {
                        LogUtils.info(this.getClass(), String.format("获取缓存数据为空,服务器信息:%s  缓存数据:%s", s[1], WskDataList));
                    }

                }
            }
        }

        if (s.length == 3) {


            if (s[0].equalsIgnoreCase("wsk")) {
                postApi.createGroupMessage(message.getGroupId());
                if (s[1].equalsIgnoreCase("add")) {
                    if (REVERSE_SERVERMAP.get(s[2]) != null) {
                        if (PushList.containsKey(REVERSE_SERVERMAP.get(s[2]))) {
                            var list = PushList.get(REVERSE_SERVERMAP.get(s[2]));
                            if (!list.contains(message.getGroupId())) {
                                list.add(message.getGroupId());
                                PushList.put(REVERSE_SERVERMAP.get(s[2]), list);
                            }
                        } else {
                            List<Long> list = new ArrayList<>();
                            list.add(message.getGroupId());
                            PushList.put(REVERSE_SERVERMAP.get(s[2]), list);
                        }
                        postApi.chatMessage.addReply(message.getMessageId());
                        postApi.chatMessage.addText("订阅成功");
                        postApi.sendGroupMsg();
                        //订阅成功
                        saveconfig();
                    }
                }
                if (s[1].equalsIgnoreCase("remove") && (message.getUserId() == 1271701079 || message.getUserId() == 273421673)) {
                    if (REVERSE_SERVERMAP.get(s[2]) != null) {
                        //检测订阅列表里有没有该服务器
                        if ((PushList.containsKey(REVERSE_SERVERMAP.get(s[2])))) {
                            var list = PushList.get(REVERSE_SERVERMAP.get(s[2]));
                            if (list.contains(message.getGroupId())) {
                                list.remove(message.getGroupId());
                            }
                            PushList.put(REVERSE_SERVERMAP.get(s[2]), list);
                            //取消成功
                            postApi.chatMessage.addReply(message.getMessageId());
                            postApi.chatMessage.addText("取消订阅成功");
                            postApi.sendGroupMsg();
                        }
                        saveconfig();
                    }
                }
            }
        }

    }

    @Override
    public void push() {

//        readconfig();
//        PushCd.init();
//        //每分钟执行  尝试推送数据
//        CronUtil.schedule("0 * * * * *", (Task) () -> pushData());
//        //每3分轮询
//        CronUtil.schedule("0 */3 * * * *", (Task) () -> getData());
//        CronUtil.setMatchSecond(true);
//        CronUtil.start();
//        for (; ; ) {
//
//        }
    }


    /**
     * 定时获取数据的 只获取中国区的数据
     */
    private void getData() {
        for (var DATACENTER : DATACENTER_MAP.values()) {
            try {
                for (var id : DATACENTER.getWorlds()) {
                    ApiResponse response = getdata(id);
                    if (response != null) {
                        WskDataList.put(id, response);
                    }
                }
            } catch (Exception e) {
                LogUtils.error(Wsk.class, e.toString());
            }
        }
    }

    /**
     * 推送数据的
     */
    private void pushData() {
        for (var p : PushList.keySet()) {
            if (WskDataList.get(p) != null) {
                //构建数据
                for (var guid : PushList.get(p)) {
                    if (PushCd.isCd(guid, WskDataList.get(p).getServerId())) {
                        continue;
                        //如果在cd中就不推送了
                    }
                    Boolean b = buildpushchat(guid, WskDataList.get(p));
                    if (b) {
                        //如果发送信息就加入cd
                        //5分钟cd
                        PushCd.addcd(guid, WskDataList.get(p).getServerId(), 300);
                    }
                }
            }
        }

    }

    private Boolean buildchat(Long groupId, ApiResponse chatdata) {
        String s = getTask(chatdata);
        postApi.createGroupMessage(groupId);
        postApi.chatMessage.addText(String.format("%s 宇宙合建信息\n", SERVERMAP.get(chatdata.getServerId())));
        postApi.chatMessage.addText(String.format("当前天气:%s\n", WeatherConstants.get(chatdata.getData().get(0).getWeatherId())));
        if (!s.equals("没有任务")) {
            postApi.chatMessage.addText(String.format("任务类型:无"));
        }
        postApi.chatMessage.addText(String.format("任务类型:%s\n", s));
        postApi.chatMessage.addText(String.format("任务信息:%s", chatdata.getData().get(0).getMessage()));
        postApi.sendGroupMsg();
        return true;
    }

    private Boolean buildpushchat(Long groupId, ApiResponse chatdata) {
        String s = getTask(chatdata);
        if (!s.equals("紧急任务")) {
            return false;
        }
        postApi.createGroupMessage(groupId);
        postApi.chatMessage.addText(String.format("%s 宇宙合建信息\n", SERVERMAP.get(chatdata.getServerId())));
        postApi.chatMessage.addText(String.format("当前天气:%s\n", WeatherConstants.get(chatdata.getData().get(0).getWeatherId())));
        postApi.chatMessage.addText(String.format("任务类型:%s\n", s));
        postApi.chatMessage.addText(String.format("任务信息:%s\n", chatdata.getData().get(0).getMessage()));
        postApi.sendGroupMsg();
        return true;
    }

    private void readconfig() {
        if (!Files.exists(path)) {
            //不存在
            try {
                Files.createFile(path);
                String s = JSON.toJSONString(PushList);
                Files.write(path, s.getBytes());
            } catch (IOException e) {
                LogUtils.error(Wsk.class, e.toString());
            }
        } else {
            //存在
            try {
                String s = Files.readString(path);
                Map<Integer, List<Long>> list = JSON.parseObject(s, new TypeReference<Map<Integer, List<Long>>>() {
                });
                if (list != null) {
                    PushList = list;
                }
            } catch (IOException e) {
                LogUtils.error(Wsk.class, e.toString());
            }
        }
    }

    private void saveconfig() {
        if (!Files.exists(path)) {
            //不存在
            try {
                Files.createFile(path);
                String s = JSON.toJSONString(PushList);
                Files.write(path, s.getBytes());
            } catch (IOException e) {
                LogUtils.error(Wsk.class, e.toString());
            }
        } else {
            //存在
            try {
                String s = JSON.toJSONString(PushList);
                Files.write(path, s.getBytes());
//                String s = Files.readString(path);
//                PushList = JSON.parseObject(s, new TypeReference<Map<Integer, List<Long>>>() {
//                });
            } catch (IOException e) {
                LogUtils.error(Wsk.class, e.toString());
            }
        }
    }

    private static String getTask(ApiResponse chatdata) {
        if (chatdata.getData().get(0).getMessage() == null || chatdata.getData().get(0).getMessage().equals("null")) {
            return "没有任务";
        } else if (chatdata.getData().get(0).getMessage().contains("机甲") && chatdata.getData().get(0).getMessage().contains("工程")) {
            return "联合工程";
        } else if (chatdata.getData().get(0).getMessage().contains("机甲")) {
            return "机甲任务";
        } else if (chatdata.getData().get(0).getMessage().contains("工程")) {
            return "联合工程";

        } else if (chatdata.getData().get(0).isEmergency()) {
            return "紧急任务";
        } else if (isEmergencyPreview(chatdata.getData().get(0).getMessage(), chatdata.getData().get(0).getWeatherId())) {
            return "紧急任务预告中";
        }
        return "其他任务";
    }

    //任务预告
    private static boolean isEmergencyPreview(String mes, Integer wei) {
        var b = mes.contains("陨石") || mes.contains("孢子") || mes.contains("磁暴");
        var c = EMERGENCY_IDS.contains(wei);
        return b & c;
    }

    static class PushCd {
        private static final List<pushdata> cddata = new CopyOnWriteArrayList<>();

        public static boolean isCd(Long groupId, Integer serverId) {
            for (var d : cddata) {
                if (d.serverId.equals(serverId) && d.groupId.equals(groupId)) {
                    return true;
                }
            }
            return false;
        }

        public static void addcd(Long groupId, Integer serverId, int removetime) {
            var d = new pushdata(serverId, groupId, LocalDateTime.now(), removetime);
            cddata.add(d);
        }

        public static void init() {
            Thread thread = new Thread(PushCd::clean);
            thread.start();
        }

        private static void clean() {
            for (; ; ) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    continue;
                }
                var t = LocalDateTime.now();
                cddata.removeIf(d -> (Math.abs(Duration.between(t, d.Addtime).toSeconds()) > d.removetime));
            }
        }


        private static class pushdata {
            private Integer serverId;
            private Long groupId;
            private LocalDateTime Addtime;
            private int removetime;

            public pushdata(Integer serverId, Long groupId, LocalDateTime Addtime, int removetime) {
                this.serverId = serverId;
                this.groupId = groupId;
                this.Addtime = Addtime;
                this.removetime = removetime;

            }
        }
    }
}
