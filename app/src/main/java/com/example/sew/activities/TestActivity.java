package com.example.sew.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyService;

public class TestActivity extends AppCompatActivity {
    TextView txtChangeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        txtChangeLanguage = findViewById(R.id.txtChangeLanguage);
//        findViewById(R.id.txtChangeLanguage).setOnClickListener(v -> {
//            Enums.LANGUAGE selectedLanguage = MyService.getSelectedLanguage();
//            if (selectedLanguage == Enums.LANGUAGE.HINDI)
//                MyService.setSelectedLanguage(Enums.LANGUAGE.ENGLISH);
//            else if (selectedLanguage == Enums.LANGUAGE.ENGLISH)
//                MyService.setSelectedLanguage(Enums.LANGUAGE.URDU);
//            else if (selectedLanguage == Enums.LANGUAGE.URDU)
//                MyService.setSelectedLanguage(Enums.LANGUAGE.HINDI);
//            updateText();
//        });
//        updateText();


        startActivity(RenderContentActivity.getInstance(this,"e6cda9b7-41e1-498f-88e2-9272943b6ba7"));
        finish();
    }

    private void updateText() {
        Enums.LANGUAGE selectedLanguage = MyService.getSelectedLanguage();
        if (selectedLanguage == Enums.LANGUAGE.HINDI)
            txtChangeLanguage.setText("Hindi");
        else if (selectedLanguage == Enums.LANGUAGE.ENGLISH)
            txtChangeLanguage.setText("English");
        else if (selectedLanguage == Enums.LANGUAGE.URDU)
            txtChangeLanguage.setText("Urdu");
    }
}
