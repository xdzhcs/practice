package org.xdkitten.zhihudaily.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 最新消息
 * Created by sanders on 2016/7/12.
 */
public class LastestPost {

    private String title;
    private List<String> images = new ArrayList<String>();
    private String type;
    private String id;

    public LastestPost() {

    }

    public LastestPost(String id, String title, List<String> images, String type) {
        this.id = id;
        this.title = title;
        this.images = images;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "LastestPost{" +
                "title='" + title + '\'' +
                ", images=" + images +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
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
