package ua.adeptius.myo3.model;


public class News {
    private String title;
    private String comment;
    private String url;
    private String imgUrl;
    private String NumberedDate;

    public String getNumberedDate() {
        return NumberedDate;
    }

    public void setNumberedDate(String numberedDate) {
        NumberedDate = numberedDate;
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
}
