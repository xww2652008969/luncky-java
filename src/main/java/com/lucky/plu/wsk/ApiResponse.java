package com.lucky.plu.wsk;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {

    @JSONField(name = "success")
    private boolean success;

    @JSONField(name = "timestamp")
    private String timestamp; // 若需日期类型，可改为 LocalDateTime 并添加 @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @JSONField(name = "server_id")
    private Integer serverId;

    @JSONField(name = "data")
    private List<DataItem> data;

    @JSONField(name = "metadata")
    private Metadata metadata;
}

// data 数组中的元素类
@Data
class DataItem {

    @JSONField(name = "historyArray")
    private List<HistoryEntry> historyArray;

    @JSONField(name = "is_emergency")
    private boolean isEmergency;

    @JSONField(name = "weather_id")
    private Integer weatherId;

    @JSONField(name = "message")
    private String message; // 可能为 null

    @JSONField(name = "timestamp")
    private String timestamp; // 与外层 timestamp 同类型

    @JSONField(name = "progress")
    private String progress;

    @JSONField(name = "has_reported_25700")
    private boolean hasReported25700;
}

// historyArray 数组中的历史记录类
@Data
class HistoryEntry {

    @JSONField(name = "weather_id")
    private String weatherId;

    @JSONField(name = "timestamp")
    private String timestamp; // 时间戳（秒级）

    @JSONField(name = "message")
    private String message; // 可能为 null（注意 JSON 中换行符会被保留）
}

// 元信息类
@Data
class Metadata {

    @JSONField(name = "server_id")
    private String serverId;

    @JSONField(name = "updated_at")
    private String updatedAt; // 若需日期类型，同上处理
}