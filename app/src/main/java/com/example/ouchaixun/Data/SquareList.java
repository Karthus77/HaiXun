package com.example.ouchaixun.Data;

import java.util.List;

public class SquareList {

    /**
     * code : 200
     * msg : 获取成功
     * data : [{"id":1,"writer":1,"title":"test","click_num":46,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","tag":"表白墙","first_pic":"","is_star":1},{"id":6,"writer":3,"title":"离与总使温局","click_num":0,"comment_num":0,"writer_nickname":"该内容由匿名用户发布","writer_avatar":"media/avatar/default.png","tag":"表白墙","first_pic":"media/Post_pic/2021-05-04_203009.967077.jpg","is_star":0}]
     * num_pages : 1
     */

    private int code;
    private String msg;
    private int num_pages;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getNum_pages() {
        return num_pages;
    }

    public void setNum_pages(int num_pages) {
        this.num_pages = num_pages;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * writer : 1
         * title : test
         * click_num : 46
         * comment_num : 0
         * writer_nickname : 江湖骗子
         * writer_avatar : /media/avatar/default.png
         * tag : 表白墙
         * first_pic :
         * is_star : 1
         */

        private int id;
        private int writer;
        private String title;
        private int click_num;
        private int comment_num;
        private String writer_nickname;
        private String writer_avatar;
        private String tag;
        private String first_pic;
        private int is_star;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWriter() {
            return writer;
        }

        public void setWriter(int writer) {
            this.writer = writer;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getClick_num() {
            return click_num;
        }

        public void setClick_num(int click_num) {
            this.click_num = click_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public String getWriter_nickname() {
            return writer_nickname;
        }

        public void setWriter_nickname(String writer_nickname) {
            this.writer_nickname = writer_nickname;
        }

        public String getWriter_avatar() {
            return writer_avatar;
        }

        public void setWriter_avatar(String writer_avatar) {
            this.writer_avatar = writer_avatar;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getFirst_pic() {
            return first_pic;
        }

        public void setFirst_pic(String first_pic) {
            this.first_pic = first_pic;
        }

        public int getIs_star() {
            return is_star;
        }

        public void setIs_star(int is_star) {
            this.is_star = is_star;
        }
    }
}
