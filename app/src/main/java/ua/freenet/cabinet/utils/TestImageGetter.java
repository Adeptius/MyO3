package ua.freenet.cabinet.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ua.freenet.cabinet.R;

import static android.content.ContentValues.TAG;

public class TestImageGetter implements Html.ImageGetter {

    private TextView textView;
    private Context context;


    public TestImageGetter(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = context.getResources().getDrawable(R.drawable.some_logo);
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenwidth = size.x;
        double y = 349;
        double x = 1152;
        double ratio = y / x;
        int needBitMapWidht = (int) (screenwidth * 0.94);
        int needBitMapHeigh = (int) (needBitMapWidht * ratio);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, needBitMapWidht, needBitMapHeigh);
        new LoadImage().execute(source, d);
        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;
        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
//            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//            Log.d(TAG, "onPostExecute drawable " + mDrawable);
//            Log.d(TAG, "onPostExecute bitmap " + bitmap);

            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenwidth = size.x;



            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                double y = bitmap.getHeight();
                double x = bitmap.getWidth();
                double ratio = y / x;
                int needBitMapWidht = (int) (screenwidth * 0.94);
                int needBitMapHeigh = (int) (needBitMapWidht * ratio);

                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, needBitMapWidht, needBitMapHeigh);
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = textView.getText();
                textView.setText(t);
            }
        }
    }
}