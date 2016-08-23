package xyz.xdzhcs.zhihudaily.entity;

import java.io.Serializable;

/**
 * 主题
 * Created by sanders on 2016/8/21.
 */
public class Theme implements Serializable {

    private String color;        //颜色
    private String thumbnail;   //图片
    private String description; //描述
    private String id;           //id
    private String name;         //主题名称

    public Theme(String color, String thumbnail, String description, String id, String name) {
        this.color = color;
        this.thumbnail = thumbnail;
        this.description = description;
        this.id = id;
        this.name = name;
    }

    public Theme() {
    }

    @Override
    public String toString() {
        return "Theme{" +
                "color='" + color + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
