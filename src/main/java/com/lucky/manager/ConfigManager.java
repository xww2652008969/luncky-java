package com.lucky.manager;

import com.xww.core.LogUtils;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static String BasePath;
    private static Path DataPath;
    private static Path ConfigPath;

    public static void Init() {
        BasePath = System.getProperty("user.dir");
        DataPath = Paths.get(BasePath, "data", "plugindata");
        ConfigPath = Paths.get(BasePath, "data", "pluginconfig");
        try {
            Files.createDirectories(DataPath);
            Files.createDirectories(ConfigPath);
        } catch (IOException e) {
            throw new RuntimeException(e);  //直接抛出错误
        }
    }

    public static PluginPath CreatePluginPath(String pluginname) {
        var c = ConfigPath.resolve(pluginname);
        var d = DataPath.resolve(pluginname);
        try {
            Files.createDirectories(c);
            Files.createDirectories(d);
        } catch (IOException e) {
            LogUtils.error(ConfigManager.class, e.toString());
        }
        return new PluginPath(c, d);
    }


    public static class PluginPath {
        @Getter
        private Path configPath;
        @Getter
        private Path dataPath;

        public PluginPath(Path configPath, Path dataPath) {
            this.configPath = configPath;
            this.dataPath = dataPath;
        }

        public String ReadConfigString(String filename) {
            var p = configPath.resolve(filename);
            try {
                String s = Files.readString(p);
                return s;
            } catch (IOException e) {
                return null;
            }
        }

        public byte[] ReadConfigBytes(String filename) {
            var p = configPath.resolve(filename);
            try {
                byte[] bytes = Files.readAllBytes(p);
                return bytes;
            } catch (IOException e) {
                return null;
            }
        }

        public String ReadDataString(String filename) {
            var p = dataPath.resolve(filename);
            try {
                String s = Files.readString(p);
                return s;
            } catch (IOException e) {
                return null;
            }
        }

        public byte[] ReadDataBytes(String filename) {
            var p = dataPath.resolve(filename);
            try {
                byte[] bytes = Files.readAllBytes(p);
                return bytes;
            } catch (IOException e) {
                return null;
            }
        }

        public boolean WriteConfigBytes(String filename, byte[] data) {
            var p = configPath.resolve(filename);
            try {
                Files.write(p, data);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public boolean WriteDataBytes(String filename, byte[] data) {
            var p = dataPath.resolve(filename);
            try {
                Files.write(p, data);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public boolean WriteConfigString(String filename, String data) {
            var p = configPath.resolve(filename);
            try {
                Files.writeString(p, data);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public boolean WriteDataString(String filename, String data) {
            var p = dataPath.resolve(filename);
            try {
                Files.writeString(p, data);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
