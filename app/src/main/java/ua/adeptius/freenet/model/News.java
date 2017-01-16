package ua.adeptius.freenet.model;


public class News {
    private String title;
    private String comment;
    private String url;
    private String imgUrl;
    private String numberedDate;

    public News(String title, String comment, String url, String imgUrl, String numberedDate) {
        this.title = title;
        this.comment = comment;
        this.url = url;
        this.imgUrl = imgUrl;
        this.numberedDate = numberedDate;
    }

    public String getNumberedDate() {
        return numberedDate;
    }

    public String getTitle() {
        title = title.replaceAll("&nbsp;","\"");
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getUrl() {
        return url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

}
