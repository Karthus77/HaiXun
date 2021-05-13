package com.example.ouchaixun.Data;

import java.util.List;

public class CircleList {


    /**
     * code : 200
     * msg : 获取成功
     * data : [{"release_time":"2021-05-12T23:27:50.787","content":"lalal","id":48,"writer":1,"click_num":10,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[{"picture":"http://47.102.215.61:8888/media/Talk_pic/u41053966379407115fm11gp0.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-05_235919.662491.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-06_183506.668686.jpg"}],"is_like":0},{"release_time":"2021-05-12T20:47:31.454","content":"proident Duis ut ad","id":47,"writer":3,"click_num":0,"like_num":1,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":1},{"release_time":"2021-05-12T20:45:18.542","content":"proident Duis ut ad","id":46,"writer":3,"click_num":0,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-12T20:45:10.295","content":"proident Duis ut ad","id":45,"writer":3,"click_num":1,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-12T20:45:00.753","content":"proident Duis ut ad","id":44,"writer":3,"click_num":0,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-12T20:25:16.874","content":"aliqua consectetur cillum i","id":43,"writer":3,"click_num":0,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-12_201036.905650.jpg"}],"is_like":0},{"release_time":"2021-05-12T20:24:55.320","content":"lalal","id":42,"writer":1,"click_num":0,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-12_195153.093054.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-12_195410.115793.jpg"}],"is_like":0},{"release_time":"2021-05-12T20:24:44.028","content":"lalal","id":41,"writer":1,"click_num":0,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-12T20:22:20.670","content":"aliqua consectetur cillum i","id":40,"writer":3,"click_num":0,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-12T20:15:40.234","content":"aliqua consectetur cillum in","id":39,"writer":3,"click_num":1,"like_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","pic_list":[],"is_like":0}]
     * num_pages : 5
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
         * release_time : 2021-05-12T23:27:50.787
         * content : lalal
         * id : 48
         * writer : 1
         * click_num : 10
         * like_num : 0
         * comment_num : 0
         * writer_nickname : 江湖骗子
         * writer_avatar : http://47.102.215.61:8888/media/avatar/default.png
         * pic_list : [{"picture":"http://47.102.215.61:8888/media/Talk_pic/u41053966379407115fm11gp0.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-05_235919.662491.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-06_183506.668686.jpg"}]
         * is_like : 0
         */

        private String release_time;
        private String content;
        private int id;
        private int writer;
        private int click_num;
        private int like_num;
        private int comment_num;
        private String writer_nickname;
        private String writer_avatar;
        private int is_like;
        private List<PicListBean> pic_list;

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

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

        public int getClick_num() {
            return click_num;
        }

        public void setClick_num(int click_num) {
            this.click_num = click_num;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
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

        public int getIs_like() {
            return is_like;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public List<PicListBean> getPic_list() {
            return pic_list;
        }

        public void setPic_list(List<PicListBean> pic_list) {
            this.pic_list = pic_list;
        }

        public static class PicListBean {
            /**
             * picture : http://47.102.215.61:8888/media/Talk_pic/u41053966379407115fm11gp0.jpg
             */

            private String picture;

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }
        }
    }
}
