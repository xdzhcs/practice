package xyz.xdzhcs.zhihudaily.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 最新消息
 * Created by sanders on 2016/7/12.
 */
public class NewsItem implements Serializable{

    private String title;
    private List<String> images = new ArrayList<String>();
    private String type;
    private String id;
    private String date;

    public NewsItem() {

    }

    public String getId(){
        return id;
    }

    public NewsItem(String id, String title, List<String> images, String type, String date) {
        this.id = id;
        this.title = title;
        this.images = images;
        this.type = type;
        this.date=date;
    }
    public String getDate(){
        return date;
    }
    public String getType(){
        return type;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\'' +
                ", images=" + images +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    //获取第一张图片的地址
    public String getFirstImg(){
        if(images==null||images.size()==0){
            return null;
        }
        return images.get(0);
    }

}
