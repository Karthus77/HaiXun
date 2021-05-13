package com.example.ouchaixun.Data;

import java.util.List;

public class CircleDetail {

    /**
     * data : {"id":1,"writer":1,"content":"lllll","release_time":"2021-05-05 10:43:08","like_num":1,"click_num":50,"writer_avatar":"http://47.102.215.61:8888/media/avatar/default.png","writer_nickname":"江湖骗子","pic_list":[{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-09_154000.530189.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-09_171746.353669.jpg"}],"is_like":1,"comments":[{"id":1,"content":"test","like_num":0,"sender_id":1,"is_reply":0,"sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-05 10:50:00.997362","is_like":0,"original_comment":{"id":1,"time":"2021-05-05 10:50:00","content":"test","sender":1,"sender_nickname":"江湖骗子","sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png"}},{"id":2,"content":"this is a comment","like_num":0,"sender_id":1,"is_reply":0,"sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-07 12:54:37.950672","is_like":0},{"id":3,"content":"comment test","like_num":0,"sender_id":1,"is_reply":1,"sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-10 23:22:39.290258","is_like":0,"original_comment":{"id":1,"time":"2021-05-05 10:50:00","content":"test","sender":1,"sender_nickname":"江湖骗子","sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png"}}],"num_pages":1}
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
         * content : lllll
         * release_time : 2021-05-05 10:43:08
         * like_num : 1
         * click_num : 50
         * writer_avatar : http://47.102.215.61:8888/media/avatar/default.png
         * writer_nickname : 江湖骗子
         * pic_list : [{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-09_154000.530189.jpg"},{"picture":"http://47.102.215.61:8888/media/Talk_pic/2021-05-09_171746.353669.jpg"}]
         * is_like : 1
         * comments : [{"id":1,"content":"test","like_num":0,"sender_id":1,"is_reply":0,"sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-05 10:50:00.997362","is_like":0},{"id":2,"content":"this is a comment","like_num":0,"sender_id":1,"is_reply":0,"sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-07 12:54:37.950672","is_like":0},{"id":3,"content":"comment test","like_num":0,"sender_id":1,"is_reply":1,"sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png","sender_nickname":"江湖骗子","time":"2021-05-10 23:22:39.290258","is_like":0,"original_comment":{"id":1,"time":"2021-05-05 10:50:00","content":"test","sender":1,"sender_nickname":"江湖骗子","sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png"}}]
         * num_pages : 1
         */

        private int id;
        private int writer;
        private String content;
        private String release_time;
        private int like_num;
        private int click_num;
        private String writer_avatar;
        private String writer_nickname;
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

        public String getWriter_avatar() {
            return writer_avatar;
        }

        public void setWriter_avatar(String writer_avatar) {
            this.writer_avatar = writer_avatar;
        }

        public String getWriter_nickname() {
            return writer_nickname;
        }

        public void setWriter_nickname(String writer_nickname) {
            this.writer_nickname = writer_nickname;
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
             * picture : http://47.102.215.61:8888/media/Talk_pic/2021-05-09_154000.530189.jpg
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
             * is_reply : 0
             * sender_avatar : http://47.102.215.61:8888/media/avatar/default.png
             * sender_nickname : 江湖骗子
             * time : 2021-05-05 10:50:00.997362
             * is_like : 0
             * original_comment : {"id":1,"time":"2021-05-05 10:50:00","content":"test","sender":1,"sender_nickname":"江湖骗子","sender_avatar":"http://47.102.215.61:8888/media/avatar/default.png"}
             */

            private int id;
            private String content;
            private int like_num;
            private int sender_id;
            private int is_reply;
            private String sender_avatar;
            private String sender_nickname;
            private String time;
            private int is_like;
            private OriginalCommentBean original_comment;

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

            public int getIs_reply() {
                return is_reply;
            }

            public void setIs_reply(int is_reply) {
                this.is_reply = is_reply;
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

            public OriginalCommentBean getOriginal_comment() {
                return original_comment;
            }

            public void setOriginal_comment(OriginalCommentBean original_comment) {
                this.original_comment = original_comment;
            }

            public static class OriginalCommentBean {
                /**
                 * id : 1
                 * time : 2021-05-05 10:50:00
                 * content : test
                 * sender : 1
                 * sender_nickname : 江湖骗子
                 * sender_avatar : http://47.102.215.61:8888/media/avatar/default.png
                 */

                private int id;
                private String time;
                private String content;
                private int sender;
                private String sender_nickname;
                private String sender_avatar;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getSender() {
                    return sender;
                }

                public void setSender(int sender) {
                    this.sender = sender;
                }

                public String getSender_nickname() {
                    return sender_nickname;
                }

                public void setSender_nickname(String sender_nickname) {
                    this.sender_nickname = sender_nickname;
                }

                public String getSender_avatar() {
                    return sender_avatar;
                }

                public void setSender_avatar(String sender_avatar) {
                    this.sender_avatar = sender_avatar;
                }
            }
        }
    }
}
