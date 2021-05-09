package com.example.ouchaixun.Data;

import java.util.List;

public class SquareComment {

    private int types;
    private int passage_id,is_star;
    private String title,nickName,content,tag ;


    public void setIs_star(int is_star) {
        this.is_star = is_star;
    }

    public int getIs_star() {
        return is_star;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setContents(String content) {
        this.content = content;
    }

    public String getContents() {
        return content;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getTypes() {
        return types;
    }

    public void setPassage_id(int passage_id) {
        this.passage_id = passage_id;
    }

    public int getPassage_id() {
        return passage_id;
    }

    public void setTitles(String title) {
        this.title = title;
    }

    public String getTitles() {
        return title;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

}
