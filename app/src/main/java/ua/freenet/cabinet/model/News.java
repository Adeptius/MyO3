package ua.freenet.cabinet.model;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.EXECUTOR;

public class News {
    private String title;
    private String comment;
    private String url;
    private String imgUrl;
    private String numberedDate;
    private String html = "";
    private String youTube = "";
    private Elements tables;

    public News(final String title, String comment, final String url, final String imgUrl, String numberedDate) {
        this.title = title;
        this.comment = comment;
        this.url = url;
        this.imgUrl = "https://o3.ua" + imgUrl;
        this.numberedDate = numberedDate;


    }


    public void runBackgroundLoad(){
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Element element = document.body().getElementsByClass("post").first();
                    try {
                        element.getElementsByTag("time").remove();
                    } catch (NullPointerException ignored) {
                    }
                    try {
                        element.getElementsByTag("h2").remove();
                    } catch (NullPointerException ignored) {
                    }
                    try {
                        element.getElementsByClass("back-link").remove();
                    } catch (NullPointerException ignored) {
                    }
                    try {
                        element.getElementsByTag("img").first().remove();
                    } catch (NullPointerException ignored) {
                    }

                    try {
                        Elements elements = element.getElementsByAttribute("img");
                        for (Element el : elements) {
                            try {
                                if (Integer.parseInt(el.attr("width")) < 200) {
                                    el.remove();
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        element.getElementsByTag("img").first().remove();
                    } catch (NullPointerException ignored) {
                    }

                    try{
                        tables =  element.getElementsByTag("table");
                        tables.remove();
                    }catch (NullPointerException ignored){}

                    String html = element.html();
                    html = html.replaceAll("/content/", "https://o3.ua/content/");
                    html = html.replaceAll("href=\"", "href=\"https://o3.ua");
                    html = html.replaceAll("https://o3.uahttps:", "https:");
                    if (html.contains("https://www.youtube.com")) {
                        String temp = html.substring(html.indexOf("https://www.youtube.com"));
                        temp = temp.substring(0, temp.indexOf("\""));
                        temp = temp.substring(temp.lastIndexOf("/") + 1);
                        News.this.youTube = temp;
                    } else if (html.contains("https://youtu.be")) {
                        String temp = html.substring(html.indexOf("https://youtu.be"));
                        temp = temp.substring(0, temp.indexOf("\""));
                        temp = temp.substring(temp.lastIndexOf("/") + 1);
                        News.this.youTube = temp;
                    }



                    News.this.html = html;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public boolean isHaveTables() {
        if (tables == null) return false;
        return tables.size()>0;
    }

    public String getNumberedDate() {
        return numberedDate;
    }

    public String getStringedDate() {
        String numbered = numberedDate;
        String result = "";
        try {
            int month = Integer.parseInt(numbered.substring(4, 6));
            int day = Integer.parseInt(numbered.substring(6, 8));
            result = day + " " + Utilits.getStrMonth(month - 1);
        } catch (Exception ignored) {
        }
        return result;
    }

    public String getTitle() {
        title = title.replaceAll("&nbsp;", "\"");
        return title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getHtml() {
        return html;
    }

    public String getYouTube() {
        return youTube;
    }


    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", html='" + html + '\'' +
                '}';
    }


}
