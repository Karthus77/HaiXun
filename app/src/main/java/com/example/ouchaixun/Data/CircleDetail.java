package com.example.ouchaixun.Data;

import java.util.List;

public class CircleDetail {
    /**
     * data : {"id":1,"writer":1,"title":"test","content":"lllll","release_time":"2021-05-05 10:43:08","like_num":0,"click_num":5,"pic_list":[{"picture":"media/Talk_pic/u41053966379407115fm11gp0.jpg"},{"picture":"media/Talk_pic/2021-05-05_235919.662491.jpg"},{"picture":"media/Talk_pic/2021-05-06_183506.668686.jpg"}],"is_like":0,"comments":[{"id":1,"content":"test","like_num":0,"sender_id":1,"sender_avatar":"/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-05 10:50:00.997362","is_like":0}],"num_pages":1}
     * code : 200
     * msg : 获取成功
     */

    private DataBean data;
    private int code;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * id : 1
         * writer : 1
         * title : test
         * content : lllll
         * release_time : 2021-05-05 10:43:08
         * like_num : 0
         * click_num : 5
         * pic_list : [{"picture":"media/Talk_pic/u41053966379407115fm11gp0.jpg"},{"picture":"media/Talk_pic/2021-05-05_235919.662491.jpg"},{"picture":"media/Talk_pic/2021-05-06_183506.668686.jpg"}]
         * is_like : 0
         * comments : [{"id":1,"content":"test","like_num":0,"sender_id":1,"sender_avatar":"/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-05 10:50:00.997362","is_like":0}]
         * num_pages : 1
         */

        private int id;
        private int writer;
        private String title;
        private String content;
        private String release_time;
        private int like_num;
        private int click_num;
        private int is_like;
        private int num_pages;
        private List<PicListBean> pic_list;
        private List<CommentsBean> comments;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRelease_time() {
            return release_time;
        }

        public void setRelease_time(String release_time) {
            this.release_time = release_time;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getClick_num() {
            return click_num;
        }

        public void setClick_num(int click_num) {
            this.click_num = click_num;
        }

        public int getIs_like() {
            return is_like;
        }

        public void setIs_like(int is_like) {
            this.is_like = is_like;
        }

        public int getNum_pages() {
            return num_pages;
        }

        public void setNum_pages(int num_pages) {
            this.num_pages = num_pages;
        }

        public List<PicListBean> getPic_list() {
            return pic_list;
        }

        public void setPic_list(List<PicListBean> pic_list) {
            this.pic_list = pic_list;
        }

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class PicListBean {
            /**
             * picture : media/Talk_pic/u41053966379407115fm11gp0.jpg
             */

            private String picture;

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }
        }

        public static class CommentsBean {
            /**
             * id : 1
             * content : test
             * like_num : 0
             * sender_id : 1
             * sender_avatar : /media/avatar/default.png
             * sender_nickname : 江湖骗子
             * time : 2021-05-05 10:50:00.997362
             * is_like : 0
             */

            private int id;
            private String content;
            private int like_num;
            private int sender_id;
            private String sender_avatar;
            private String sender_nickname;
            private String time;
            private int is_like;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLike_num() {
                return like_num;
            }

            public void setLike_num(int like_num) {
                this.like_num = like_num;
            }

            public int getSender_id() {
                return sender_id;
            }

            public void setSender_id(int sender_id) {
                this.sender_id = sender_id;
            }

            public String getSender_avatar() {
                return sender_avatar;
            }

            public void setSender_avatar(String sender_avatar) {
                this.sender_avatar = sender_avatar;
            }

            public String getSender_nickname() {
                return sender_nickname;
            }

            public void setSender_nickname(String sender_nickname) {
                this.sender_nickname = sender_nickname;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getIs_like() {
                return is_like;
            }

            public void setIs_like(int is_like) {
                this.is_like = is_like;
            }
        }
    }
}
