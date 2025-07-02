package com.lucky.plu.wsk;

import com.alibaba.fastjson2.JSON;
import com.lucky.utils.http.Httphelp;
import com.xww.core.LogUtils;
import lombok.Getter;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class Wsktool {

    private static String urlapi = "https://wks.ff14.xin/api/data/";
    private static Httphelp httphelp = new Httphelp();
    public static final Map<Integer, String> WeatherConstants = Map.of(149, "磁暴", 194, "流星雨", 195, "流星雨", 196, "磁暴", 197, "孢子雾", 148, "月尘", 49, "灵风", 2, "晴朗");

    public static final Map<Integer, String> SERVERMAP = Map.ofEntries(Map.entry(21, "Ravana"), Map.entry(22, "Bismarck"), Map.entry(23, "Asura"), Map.entry(24, "Belias"), Map.entry(28, "Pandaemonium"), Map.entry(29, "Shinryu"), Map.entry(30, "Unicorn"), Map.entry(31, "Yojimbo"), Map.entry(32, "Zeromus"), Map.entry(33, "Twintania"), Map.entry(34, "Brynhildr"), Map.entry(35, "Famfrit"), Map.entry(36, "Lich"), Map.entry(37, "Mateus"), Map.entry(39, "Omega"), Map.entry(40, "Jenova"), Map.entry(41, "Zalera"), Map.entry(42, "Zodiark"), Map.entry(43, "Alexander"), Map.entry(44, "Anima"), Map.entry(45, "Carbuncle"), Map.entry(46, "Fenrir"), Map.entry(47, "Hades"), Map.entry(48, "Ixion"), Map.entry(49, "Kujata"), Map.entry(50, "Typhon"), Map.entry(51, "Ultima"), Map.entry(52, "Valefor"), Map.entry(53, "Exodus"), Map.entry(54, "Faerie"), Map.entry(55, "Lamia"), Map.entry(56, "Phoenix"), Map.entry(57, "Siren"), Map.entry(58, "Garuda"), Map.entry(59, "Ifrit"), Map.entry(60, "Ramuh"), Map.entry(61, "Titan"), Map.entry(62, "Diabolos"), Map.entry(63, "Gilgamesh"), Map.entry(64, "Leviathan"), Map.entry(65, "Midgardsormr"), Map.entry(66, "Odin"), Map.entry(67, "Shiva"), Map.entry(68, "Atomos"), Map.entry(69, "Bahamut"), Map.entry(70, "Chocobo"), Map.entry(71, "Moogle"), Map.entry(72, "Tonberry"), Map.entry(73, "Adamantoise"), Map.entry(74, "Coeurl"), Map.entry(75, "Malboro"), Map.entry(76, "Tiamat"), Map.entry(77, "Ultros"), Map.entry(78, "Behemoth"), Map.entry(79, "Cactuar"), Map.entry(80, "Cerberus"), Map.entry(81, "Goblin"), Map.entry(82, "Mandragora"), Map.entry(83, "Louisoix"), Map.entry(85, "Spriggan"), Map.entry(86, "Sephirot"), Map.entry(87, "Sophia"), Map.entry(88, "Zurvan"), Map.entry(90, "Aegis"), Map.entry(91, "Balmung"), Map.entry(92, "Durandal"), Map.entry(93, "Excalibur"), Map.entry(94, "Gungnir"), Map.entry(95, "Hyperion"), Map.entry(96, "Masamune"), Map.entry(97, "Ragnarok"), Map.entry(98, "Ridill"), Map.entry(99, "Sargatanas"), Map.entry(400, "Sagittarius"), Map.entry(401, "Phantom"), Map.entry(402, "Alpha"), Map.entry(403, "Raiden"), Map.entry(404, "Marilith"), Map.entry(405, "Seraph"), Map.entry(406, "Halicarnassus"), Map.entry(407, "Maduin"), Map.entry(408, "Cuchulainn"), Map.entry(409, "Kraken"), Map.entry(410, "Rafflesia"), Map.entry(411, "Golem"), Map.entry(3000, "Cloudtest01"), Map.entry(3001, "Cloudtest02"), Map.entry(1167, "红玉海"), Map.entry(1081, "神意之地"), Map.entry(1042, "拉诺西亚"), Map.entry(1044, "幻影群岛"), Map.entry(1060, "萌芽池"), Map.entry(1173, "宇宙和音"), Map.entry(1174, "沃仙曦染"), Map.entry(1175, "晨曦王座"), Map.entry(1172, "白银乡"), Map.entry(1076, "白金幻象"), Map.entry(1171, "神拳痕"), Map.entry(1170, "潮风亭"), Map.entry(1113, "旅人栈桥"), Map.entry(1121, "拂晓之间"), Map.entry(1166, "龙巢神殿"), Map.entry(1176, "梦羽宝境"), Map.entry(1043, "紫水栈桥"), Map.entry(1169, "延夏"), Map.entry(1106, "静语庄园"), Map.entry(1045, "摩杜纳"), Map.entry(1177, "海猫茶屋"), Map.entry(1178, "柔风海湾"), Map.entry(1179, "琥珀原"), Map.entry(1192, "水晶塔"), Map.entry(1183, "银泪湖"), Map.entry(1180, "太阳海岸"), Map.entry(1186, "伊修加德"), Map.entry(1201, "红茶川"), Map.entry(1068, "黄金谷"), Map.entry(1064, "月牙湾"), Map.entry(1187, "雪松原"), Map.entry(2075, "카벙클"),  // 韩文服务器名
            Map.entry(2076, "초코보"),
            Map.entry(2077, "모그리"),
            Map.entry(2078, "톤베리"),
            Map.entry(2080, "펜리르"));
    public static final Map<String, Integer> REVERSE_SERVERMAP = Map.ofEntries(
            Map.entry("Ravana", 21), Map.entry("Bismarck", 22), Map.entry("Asura", 23), Map.entry("Belias", 24), Map.entry("Pandaemonium", 28),
            Map.entry("Shinryu", 29), Map.entry("Unicorn", 30), Map.entry("Yojimbo", 31), Map.entry("Zeromus", 32), Map.entry("Twintania", 33),
            Map.entry("Brynhildr", 34), Map.entry("Famfrit", 35), Map.entry("Lich", 36), Map.entry("Mateus", 37), Map.entry("Omega", 39),
            Map.entry("Jenova", 40), Map.entry("Zalera", 41), Map.entry("Zodiark", 42), Map.entry("Alexander", 43), Map.entry("Anima", 44),
            Map.entry("Carbuncle", 45), Map.entry("Fenrir", 46), Map.entry("Hades", 47), Map.entry("Ixion", 48), Map.entry("Kujata", 49),
            Map.entry("Typhon", 50), Map.entry("Ultima", 51), Map.entry("Valefor", 52), Map.entry("Exodus", 53), Map.entry("Faerie", 54),
            Map.entry("Lamia", 55), Map.entry("Phoenix", 56), Map.entry("Siren", 57), Map.entry("Garuda", 58), Map.entry("Ifrit", 59),
            Map.entry("Ramuh", 60), Map.entry("Titan", 61), Map.entry("Diabolos", 62), Map.entry("Gilgamesh", 63), Map.entry("Leviathan", 64),
            Map.entry("Midgardsormr", 65), Map.entry("Odin", 66), Map.entry("Shiva", 67), Map.entry("Atomos", 68), Map.entry("Bahamut", 69),
            Map.entry("Chocobo", 70), Map.entry("Moogle", 71), Map.entry("Tonberry", 72), Map.entry("Adamantoise", 73), Map.entry("Coeurl", 74),
            Map.entry("Malboro", 75), Map.entry("Tiamat", 76), Map.entry("Ultros", 77), Map.entry("Behemoth", 78), Map.entry("Cactuar", 79),
            Map.entry("Cerberus", 80), Map.entry("Goblin", 81), Map.entry("Mandragora", 82), Map.entry("Louisoix", 83), Map.entry("Spriggan", 85),
            Map.entry("Sephirot", 86), Map.entry("Sophia", 87), Map.entry("Zurvan", 88), Map.entry("Aegis", 90), Map.entry("Balmung", 91),
            Map.entry("Durandal", 92), Map.entry("Excalibur", 93), Map.entry("Gungnir", 94), Map.entry("Hyperion", 95), Map.entry("Masamune", 96),
            Map.entry("Ragnarok", 97), Map.entry("Ridill", 98), Map.entry("Sargatanas", 99), Map.entry("Sagittarius", 400), Map.entry("Phantom", 401),
            Map.entry("Alpha", 402), Map.entry("Raiden", 403), Map.entry("Marilith", 404), Map.entry("Seraph", 405), Map.entry("Halicarnassus", 406),
            Map.entry("Maduin", 407), Map.entry("Cuchulainn", 408), Map.entry("Kraken", 409), Map.entry("Rafflesia", 410), Map.entry("Golem", 411),
            Map.entry("Cloudtest01", 3000), Map.entry("Cloudtest02", 3001), Map.entry("红玉海", 1167), Map.entry("神意之地", 1081), Map.entry("拉诺西亚", 1042),
            Map.entry("幻影群岛", 1044), Map.entry("萌芽池", 1060), Map.entry("宇宙和音", 1173), Map.entry("沃仙曦染", 1174), Map.entry("晨曦王座", 1175),
            Map.entry("白银乡", 1172), Map.entry("白金幻象", 1076), Map.entry("神拳痕", 1171), Map.entry("潮风亭", 1170), Map.entry("旅人栈桥", 1113),
            Map.entry("拂晓之间", 1121), Map.entry("龙巢神殿", 1166), Map.entry("梦羽宝境", 1176), Map.entry("紫水栈桥", 1043), Map.entry("延夏", 1169),
            Map.entry("静语庄园", 1106), Map.entry("摩杜纳", 1045), Map.entry("海猫茶屋", 1177), Map.entry("柔风海湾", 1178), Map.entry("琥珀原", 1179),
            Map.entry("水晶塔", 1192), Map.entry("银泪湖", 1183), Map.entry("太阳海岸", 1180), Map.entry("伊修加德", 1186), Map.entry("红茶川", 1201),
            Map.entry("黄金谷", 1068), Map.entry("月牙湾", 1064), Map.entry("雪松原", 1187), Map.entry("카벙클", 2075), Map.entry("초코보", 2076),
            Map.entry("모그리", 2077), Map.entry("톤베리", 2078), Map.entry("펜리르", 2080)
    );
    // 添加所有数据中心数据（按原数组顺序）
    public static final Map<String, Datacenter> DATACENTER_MAP = Map.of("陆行鸟", new Datacenter(
            "陆行鸟",
            "中国",
            List.of(1167, 1081, 1042, 1044, 1060, 1173, 1174, 1175)
    ), "莫古力", new Datacenter(
            "莫古力",
            "中国",
            List.of(1172, 1076, 1171, 1170, 1113, 1121, 1166, 1176)
    ), "猫小胖", new Datacenter(
            "猫小胖",
            "中国",
            List.of(1043, 1169, 1106, 1045, 1177, 1178, 1179)
    ), "豆豆柴", new Datacenter(
            "豆豆柴",
            "中国",
            List.of(1192, 1183, 1180, 1186, 1201)
    ));
    ;

    public static ApiResponse getdata(int serid) {
        try {
            Response get = httphelp.Get(urlapi + serid);
            if (get.isSuccessful()) {
                ApiResponse apiResponse = JSON.parseObject(get.body().string().getBytes(), ApiResponse.class);
                if (apiResponse.isSuccess()) {
                    return apiResponse;
                }
                return null;
            }
        } catch (IOException e) {
            LogUtils.error(Wsktool.class, e.toString());
        }
        return null;
    }

    static class Datacenter {
        @Getter
        private final String name;      // 数据中心名称（如 "陆行鸟"）
        @Getter
        private final String region;    // 所属区域（如 "中国"）
        @Getter
        private final List<Integer> worlds; // 包含的服务器 ID 列表

        // 构造方法（私有，仅在内部初始化时调用）
        private Datacenter(String name, String region, List<Integer> worlds) {
            this.name = name;
            this.region = region;
            this.worlds = Collections.unmodifiableList(worlds); // 不可变列表
        }

    }
}
