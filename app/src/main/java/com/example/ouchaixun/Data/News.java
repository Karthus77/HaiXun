package com.example.ouchaixun.Data;

import java.util.List;

public class News {

    private int type;
    private int news_id;
    private List<ViewPagerData> pager;
    private String title,nickName,img;
    private Boolean visibility,intop;


    public void setIntop(Boolean intop) {
        this.intop = intop;
    }

    public Boolean getIntop() {
        return intop;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setPager(List<ViewPagerData> pager) {
        this.pager = pager;
    }

    public List<ViewPagerData> getPager() {
        return pager;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public int getNews_id() {
        return news_id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Boolean getVisibility() {
        return visibility;
    }
}
