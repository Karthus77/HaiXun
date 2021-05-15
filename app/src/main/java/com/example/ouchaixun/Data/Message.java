package com.example.ouchaixun.Data;

public class Message {


    /**
     * post_title : this is test123
     * msg_type_word : 发布帖子评论有人回复
     * post_id : 3
     * sender_nickname : 江湖
     * msg_type : 6
     * comment_content : 广告广告
     * id : 46
     * original_comment_content : 111
     */
    private int type;
    private String post_title;
    private String msg_type_word;
    private int post_id;
    private String sender_nickname;
    private int msg_type;
    private String content;
    private int id;
    private String original_comment_content;
    private String time,avatar;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimes() {
        return time;
    }

    public void setTimes(String time) {
        this.time = time;
    }

    public void setTypes(int type) {
        this.type = type;
    }

    public int getTypes() {
        return type;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public void setMsg_type_word(String msg_type_word) {
        this.msg_type_word = msg_type_word;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public void setSender_nickname(String sender_nickname) {
        this.sender_nickname = sender_nickname;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public void setCcontent(String content) {
        this.content = content;
    }

    public void setIds(int id) {
        this.id = id;
    }

    public void setOriginal_comment_content(String original_comment_content) {
        this.original_comment_content = original_comment_content;
    }

    public String getPost_title() {
        return post_title;
    }

    public String getMsg_type_word() {
        return msg_type_word;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getSender_nickname() {
        return sender_nickname;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public String getCcontent() {
        return content;
    }

    public int getIds() {
        return id;
    }

    public String getOriginal_comment_content() {
        return original_comment_content;
    }
}
