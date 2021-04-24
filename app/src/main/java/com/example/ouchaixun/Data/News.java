package com.example.ouchaixun.Data;

import java.util.List;

public class News {

    private int type;
//    private String error;
//    private String no;
    private List<String> pics;
    private String title;
    private Boolean visibility;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }


    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getPics() {
        return pics;
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
