package ua.adeptius.myo3.dao;


public class News {
    private String date;
    private String title;
    private String comment;
    private String url;
    private String imgUrl;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title.replaceAll("&nbsp;","\"");

        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = "http://o3.ua" + imgUrl;
    }


    @Override
    public String toString() {
        return "News{" +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
