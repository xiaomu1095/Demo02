package com.example.ll.demo02.bean;

/**
 * Created by Administrator on 2016/3/31.
 */
public class VersionInfo {
    private String version;
    private String url;
    private String description;
    private String Name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}