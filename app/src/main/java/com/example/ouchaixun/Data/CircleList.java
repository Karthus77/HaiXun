package com.example.ouchaixun.Data;

import java.util.List;

public class CircleList {


    /**
     * code : 200
     * msg : 获取成功
     * data : [{"release_time":"2021-05-09T10:22:21.370","content":"aliquip aliqua dolor","id":15,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[{"picture":"Talk_pic/u41053966379407115fm11gp0.jpg"}],"is_like":0},{"release_time":"2021-05-09T10:13:11.579","content":"aliquip aliqua dolor","id":14,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-09T10:13:00.019","content":"aliquip aliqua dolor","id":13,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-09T10:12:53.215","content":"aliquip aliqua dolor","id":12,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-08T21:48:07.400","content":"pic id list test","id":11,"writer":1,"click_num":1,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[{"picture":"Talk_pic/2021-05-08_214607.997289.jpg"}],"is_like":0},{"release_time":"2021-05-06T22:00:35.658","content":"dolor ex sed nisi Ut","id":9,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-06T21:54:26.232","content":"occaecat in non fugiat","id":8,"writer":3,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-05T23:56:08.925","content":"1","id":7,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-05T10:51:41.619","content":"lllll","id":5,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0},{"release_time":"2021-05-05T10:45:39.403","content":"test4","id":4,"writer":1,"click_num":0,"comment_num":0,"writer_nickname":"江湖骗子","writer_avatar":"/media/avatar/default.png","pic_list":[],"is_like":0}]
     * num_pages : 2
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
         * release_time : 2021-05-09T10:22:21.370
         * content : aliquip aliqua dolor
         * id : 15
         * writer : 1
         * click_num : 0
         * comment_num : 0
         * writer_nickname : 江湖骗子
         * writer_avatar : /media/avatar/default.png
         * pic_list : [{"picture":"Talk_pic/u41053966379407115fm11gp0.jpg"}]
         * is_like : 0
         */

        private String release_time;
        private String content;
        private int id;
        private int writer;
        private int click_num;
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
             * picture : Talk_pic/u41053966379407115fm11gp0.jpg
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
