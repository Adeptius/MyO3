package ua.adeptius.myo3.activities.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.News;

public class NewsFragment extends BaseFragment {

    List<News> newses;
    List<ImageView> imageViews = new ArrayList<>();
    LinearLayout mainLayout;

    @Override
    void init() {
        titleText = "Акції";
        descriptionText = "";
        mainLayout = (LinearLayout) baseView.findViewById(R.id.scroll_view_news);
        startBackgroundTask();
    }

    @Override
    void doInBackground() throws Exception {
        newses = getAllNews();
    }

    private void showNews(List<News> newses) {
        LinearLayout.LayoutParams layoutParamsMargin10 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsMargin10.setMargins(15, 20, 15, 20);
        for (int i = 0; i < newses.size(); i++) {
            final News news = newses.get(i);
            LinearLayout layoutWithNews = createNewsLayout(news);
            mainLayout.addView(layoutWithNews, layoutParamsMargin10);
            layoutWithNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = news.getUrl();
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
    }

    private LinearLayout createNewsLayout(final News news){

        LinearLayout layoutForNews = new LinearLayout(context);
        layoutForNews.setOrientation(LinearLayout.VERTICAL);
        layoutForNews.setBackgroundResource(R.drawable.roundrect_for_news);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutForNews.setElevation(8);
        }

        final LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final ImageView imageView = new ImageView(context);
        imageViews.add(imageView);
        layoutForNews.addView(imageView, imageParams);


        TextView title = new TextView(context);
        title.setText(news.getTitle());
        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);
        layoutForNews.addView(title);

        TextView textView = new TextView(context);
        textView.setText(news.getComment());
        layoutForNews.addView(textView);

        return layoutForNews;
    }

    @Override
    void processIfOk() {
        showNews(newses);
        addAllImages();
    }

    private void addAllImages() {

        for (int i = 0; i <imageViews.size(); i++) {
            final ImageView imageView = imageViews.get(i);
            final News news = newses.get(i);

            EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = getHiResImg(news.getUrl());


                        URL newurl = new URL(url);
                        final Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.getLayoutParams().height = 500;
                                imageView.setImageBitmap(mIcon_val);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



        }
    }

    @Override
    void processIfFail() {

    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_news;
    }

    @Override
    int setLayoutId() {
        return R.id.scroll_view_news;
    }

    @Override
    public void onClick(View view) {

    }

    private String getHiResImg(String url)throws Exception{
        System.out.println(url);
        Document doc2 = Jsoup.connect(url).get();
        Elements images = doc2.getElementsByTag("img");
        for (Element image : images) {
            if (image.toString().contains("/content/images/")){
                return "http://o3.ua" + image.attributes().get("src");
            }
        }
        return null;
    }

    private List<News> getAllNews() throws Exception {
        String url = "http://o3.ua/ua/about/akzii_programma_loyalnosti/";
        Document doc = Jsoup.connect(url).get();
        Elements post = doc.body().getElementsByClass("post");

        List<News> newss = new ArrayList<>();
        for (Element element : post) {
            String href = element.getElementsByTag("figure").first()
                    .getElementsByTag("a").first()
                    .attributes().get("href");
            String imageUrl = element.getElementsByTag("figure").first()
                    .getElementsByTag("a").first()
                    .getElementsByTag("img").first()
                    .attributes().get("src");
            String date = element.getElementsByTag("div").first()
                    .getElementsByTag("time").first().html();
            String title2 = element.getElementsByTag("div").first()
                    .getElementsByTag("h3").first()
                    .getElementsByTag("a").first().html();
            String text = element.getElementsByTag("div").first()
                    .getElementsByTag("p").first().html();


//            System.out.println(href);
//            try{
//                Document doc2 = Jsoup.connect(href).get();
//                imageUrl = doc2.body()
//                        .getElementsByClass("text-wrap").first()
//                        .getElementsByTag("h3").first()
//                        .getElementsByTag("img").first()
//                        .attributes().get("src");
//            }catch (Exception e){
//
//            }

            News news = new News();
            news.setComment(text);
            news.setTitle(title2);
            news.setDate(date);
            news.setImgUrl(imageUrl);
            news.setUrl(href);
            newss.add(news);
        }
        return newss;
    }
}

