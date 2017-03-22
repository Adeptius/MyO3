package ua.freenet.cabinet.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import com.github.barteksc.pdfviewer.PDFView;

//import com.joanzapata.pdfview.PDFView;

import java.net.HttpURLConnection;
import java.net.URL;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.model.PdfDocument;

import static ua.freenet.cabinet.utils.Utilits.EXECUTOR;

public class PdfActivity extends AppCompatActivity {


    public static PdfDocument document;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
//        final PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
//
//
//        EXECUTOR.submit(new Runnable() {
//            @Override
//            public void run() {
//
//
//        try{
//            URL obj = new URL(document.getUrl());
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//
//            pdfView.fromAsset("pravila_bonusnoi_sistemi.pdf")
////                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
//                    .enableSwipe(true)
////                    .swipeHorizontal(false)
////                    .enableDoubletap(true)
////                    .defaultPage(0)
////                .onDraw(onDrawListener)
////                .onLoad(onLoadCompleteListener)
////                .onPageChange(onPageChangeListener)
////                .onPageScroll(onPageScrollListener)
////                .onError(onErrorListener)
////                    .enableAnnotationRendering(false)
////                    .password(null)
////                    .scrollHandle(null)
//                    .load();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//            }
//        });

    }
}
