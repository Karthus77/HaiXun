package com.example.ouchaixun.Data;

import java.util.List;

public class SquareComment {

    private int types;
    private int passage_id,is_star;
    private String title,nickName,content,tag ;
    /**
     * like_num : 0
     * sender_nickname : 江湖骗子
     * is_like : 0
     * time : 2021-05-05 23:17:44.192291
     * sender_avatar : /media/avatar/default.png
     * content : 111
     * sender_id : 1
     */
    private int like_num;
    private String sender_nickname;
    private int is_like;
    private String time;
    private String sender_avatar;
    private int sender_id;
    /**
     * writer_nickname : 江湖骗子
     * writer_avatar : /media/avatar/default.png
     * pic_list : []
     */


    private String writer_nickname;
    private String writer_avatar;
    private List<String> pic_list;
    /**
     * id : 2
     * is_reply : 0
     */
    private int id;
    private int is_reply;
    /**
     * reply_time : 2021-05-11 22:46:03
     * reply_nickname : 江湖
     * reply_avatar : /media/avatar/default.png
     * reply_content : 个哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈
     * reply_sender : 7
     */
    private String reply_time;
    private String reply_nickname;
    private String reply_avatar;
    private String reply_content;
    private int reply_sender;


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


    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public void setSender_nickname(String sender_nickname) {
        this.sender_nickname = sender_nickname;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSender_avatar(String sender_avatar) {
        this.sender_avatar = sender_avatar;
    }


    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getLike_num() {
        return like_num;
    }

    public String getSender_nickname() {
        return sender_nickname;
    }

    public int getIs_like() {
        return is_like;
    }

    public String getTime() {
        return time;
    }

    public String getSender_avatar() {
        return sender_avatar;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setWriter_nickname(String writer_nickname) {
        this.writer_nickname = writer_nickname;
    }

    public void setWriter_avatar(String writer_avatar) {
        this.writer_avatar = writer_avatar;
    }

    public void setPic_list(List<String> pic_list) {
        this.pic_list = pic_list;
    }

    public String getWriter_nickname() {
        return writer_nickname;
    }

    public String getWriter_avatar() {
        return writer_avatar;
    }

    public List<String> getPic_list() {
        return pic_list;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIs_reply(int is_reply) {
        this.is_reply = is_reply;
    }

    public int getId() {
        return id;
    }

    public int getIs_reply() {
        return is_reply;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public void setReply_nickname(String reply_nickname) {
        this.reply_nickname = reply_nickname;
    }

    public void setReply_avatar(String reply_avatar) {
        this.reply_avatar = reply_avatar;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public void setReply_sender(int reply_sender) {
        this.reply_sender = reply_sender;
    }

    public String getReply_time() {
        return reply_time;
    }

    public String getReply_nickname() {
        return reply_nickname;
    }

    public String getReply_avatar() {
        return reply_avatar;
    }

    public String getReply_content() {
        return reply_content;
    }

    public int getReply_sender() {
        return reply_sender;
    }
}
