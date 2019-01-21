package com.example.zxf.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class NavigationBean implements Parcelable {

        private String cid;
        private String name;
        private List<ArticlesBean> articles;

    protected NavigationBean(Parcel in) {
        cid = in.readString();
        name = in.readString();
        articles = in.createTypedArrayList(ArticlesBean.CREATOR);
    }

    public static final Creator<NavigationBean> CREATOR = new Creator<NavigationBean>() {
        @Override
        public NavigationBean createFromParcel(Parcel in) {
            return new NavigationBean(in);
        }

        @Override
        public NavigationBean[] newArray(int size) {
            return new NavigationBean[size];
        }
    };

    public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ArticlesBean> getArticles() {
            return articles;
        }

        public void setArticles(List<ArticlesBean> articles) {
            this.articles = articles;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(cid);
            parcel.writeString(name);
        }

        public static class ArticlesBean implements Parcelable{
            /**
             * apkLink :
             * author : 小编
             * chapterId : 272
             * chapterName : 常用网站
             * collect : false
             * courseId : 13
             * desc :
             * envelopePic :
             * fresh : false
             * id : 1849
             * link : http://www.github.com/
             * niceDate : 2018-01-07
             * origin :
             * projectLink :
             * publishTime : 1515322817000
             * superChapterId : 0
             * superChapterName :
             * tags : []
             * title : Github
             * type : 0
             * userId : -1
             * visible : 0
             * zan : 0
             */

            private String apkLink;
            private String author;
            private int chapterId;
            private String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String envelopePic;
            private boolean fresh;
            private int id;
            private String link;
            private String niceDate;
            private String origin;
            private String projectLink;
            private long publishTime;
            private int superChapterId;
            private String superChapterName;
            private String title;
            private int type;
            private int userId;
            private int visible;
            private int zan;
            private List<?> tags;

            protected ArticlesBean(Parcel in) {
                apkLink = in.readString();
                author = in.readString();
                chapterId = in.readInt();
                chapterName = in.readString();
                collect = in.readByte() != 0;
                courseId = in.readInt();
                desc = in.readString();
                envelopePic = in.readString();
                fresh = in.readByte() != 0;
                id = in.readInt();
                link = in.readString();
                niceDate = in.readString();
                origin = in.readString();
                projectLink = in.readString();
                publishTime = in.readLong();
                superChapterId = in.readInt();
                superChapterName = in.readString();
                title = in.readString();
                type = in.readInt();
                userId = in.readInt();
                visible = in.readInt();
                zan = in.readInt();
            }

            public static final Creator<ArticlesBean> CREATOR = new Creator<ArticlesBean>() {
                @Override
                public ArticlesBean createFromParcel(Parcel in) {
                    return new ArticlesBean(in);
                }

                @Override
                public ArticlesBean[] newArray(int size) {
                    return new ArticlesBean[size];
                }
            };

            public String getApkLink() {
                return apkLink;
            }

            public void setApkLink(String apkLink) {
                this.apkLink = apkLink;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public boolean isCollect() {
                return collect;
            }

            public void setCollect(boolean collect) {
                this.collect = collect;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(String envelopePic) {
                this.envelopePic = envelopePic;
            }

            public boolean isFresh() {
                return fresh;
            }

            public void setFresh(boolean fresh) {
                this.fresh = fresh;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public void setNiceDate(String niceDate) {
                this.niceDate = niceDate;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getProjectLink() {
                return projectLink;
            }

            public void setProjectLink(String projectLink) {
                this.projectLink = projectLink;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public int getSuperChapterId() {
                return superChapterId;
            }

            public void setSuperChapterId(int superChapterId) {
                this.superChapterId = superChapterId;
            }

            public String getSuperChapterName() {
                return superChapterName;
            }

            public void setSuperChapterName(String superChapterName) {
                this.superChapterName = superChapterName;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public int getZan() {
                return zan;
            }

            public void setZan(int zan) {
                this.zan = zan;
            }

            public List<?> getTags() {
                return tags;
            }

            public void setTags(List<?> tags) {
                this.tags = tags;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(apkLink);
                parcel.writeString(author);
                parcel.writeInt(chapterId);
                parcel.writeString(chapterName);
                parcel.writeByte((byte) (collect ? 1 : 0));
                parcel.writeInt(courseId);
                parcel.writeString(desc);
                parcel.writeString(envelopePic);
                parcel.writeByte((byte) (fresh ? 1 : 0));
                parcel.writeInt(id);
                parcel.writeString(link);
                parcel.writeString(niceDate);
                parcel.writeString(origin);
                parcel.writeString(projectLink);
                parcel.writeLong(publishTime);
                parcel.writeInt(superChapterId);
                parcel.writeString(superChapterName);
                parcel.writeString(title);
                parcel.writeInt(type);
                parcel.writeInt(userId);
                parcel.writeInt(visible);
                parcel.writeInt(zan);
            }
        }
}
