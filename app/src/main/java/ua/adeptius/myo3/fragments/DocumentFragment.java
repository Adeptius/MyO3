package ua.adeptius.myo3.fragments;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.GetInfo;
import ua.adeptius.myo3.model.Person;
import ua.adeptius.myo3.model.Zayavleniya;
import ua.adeptius.myo3.utils.Utilits;


public class DocumentFragment extends BaseFragment {

    private Person person;

    @Override
    void setAllSettings() {
        titleText = "Заяви";
        descriptionText = "";
        fragmentId = R.layout.fragment_base_scrolling;
        layoutId = R.id.base_scroll_view;
        titleImage = R.drawable.background_main1;
    }

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        person = GetInfo.getPersonInfo();
        String header = Zayavleniya.getHeader(person,"CH","55555", person.getPhones().get(0).getPhone(),person.getEmail());
        String message = Zayavleniya.realIP(Utilits.getUkrDateNow());
        String footer = Zayavleniya.getFooter();

        sendEmail("adeptius@gmail.com", "Надання реальної IP адреси.", header + message + footer);
    }

    @Override
    void processIfOk() {
        draw();
        hideAllViewsInMainScreen();
        animateScreen();
    }

    private void draw() {

    }

    @Override
    public void onClick(View v) {

    }

    private void sendEmail(String recipient, String subject, String message){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",recipient, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject + "Угода " + person.getCard());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
