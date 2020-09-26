package com.example.sew.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.sew.R;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.views.TitleTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {


    @BindView(R.id.txtAboutUsTitle)
    TitleTextView txtAboutUsTitle;
    @BindView(R.id.webAboutUs)
    WebView webAboutUs;

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, AboutUsActivity.class);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        updateUI();
    }

    private void updateUI() {
        setHeaderTitle(MyHelper.getString(R.string.about));
        txtAboutUsTitle.setText(MyHelper.getString(R.string.about));
        webAboutUs.setBackgroundColor(Color.parseColor("#00000000"));
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                setAboutEng();
                break;
            case HINDI:
                setAboutHin();
                break;
            case URDU:
                setAboutUrdu();
                break;
        }
    }


    public void setAboutEng() {
        String aboutEng =
                "<div class=\"contentListBody\">\n" +
                        "<div style=\"direction: ltr; clear: both; position: relative; text-align: justify;\">" +
                        "<p><strong>Rekhta Foundation</strong> has established a free website Rekhta.org with the objective of promoting and disseminating Urdu literature, especially Urdu Poetry to an audience beyond those conversant with the Urdu script. The content is available in Devanagari and Roman scripts in addition to the Urdu script.</p>\n" +
                        "<p>The website has a global readership from over 160 countries across the globe. In less than a year, it has attracted wide acclaim and has been highly appreciated not only by laypersons and enthusiasts but also by Urdu literatures from India, Pakistan and other parts of the globe.</p>\n" +
                        "<p>This resource provides the works of the highest number of poets compared to any other site in this genre. The site is the largest online repository of Urdu poetry anywhere, with over 30000 Ghazals and Nazms of over 2500 Urdu poets of over the last three centuries.</p>\n" +
                        "<p>It has a large number of features that make it extremely user-friendly and provides the reader unparalleled convenience in the ability to browse, search and find relevant content with its customized powerful search facility. It also has the unique feature of an in-built glossary that provides the meaning of every word at just a click. In order to give the uninitiated reader a flavour of the diction and pronunciation, a large number of compositions have been recorded in the voice of poets and professionals and have been provided along with the text.</p>\n" +
                        "<p>It has started a section of Urdu Shayari for beginners wherein newcomers to the world of Urdu poetry are introduced to relatively easy poetry as well as popular Ghazals and Nazms along with videos of these compositions sung and recited by well known artists and poets.</p>\n" +
                        "<p>It has also started an initiative to digitize Urdu literature, both poetry and prose, in order to preserve rare and popular Urdu books. &nbsp;Apart from sourcing books from personal collections of various people, it has also associated with reputed libraries and organizations to digitize their collections.&nbsp;</p>\n" +
                        "<p>It has further started a program to provide young and upcoming poets a platform to showcase their compositions. Towards this end, videos of these young poets are recorded and uploaded by Rekhta on it website as well as on Youtube and made available for public viewing.</p>\n" +
                        "<p>In addition to these young poets, established literary figures from India, Pakistan, Canada and Middle east, and other nations are also invited and recorded at Rekhta and are available to be heard and seen by all interested viewers</p>\n" +
                        "<p>The site has recently been made responsive which would enable users across all platforms including desktop and laptop computers, tablets as well as mobile phones to view and navigate the site with equal ease which would enable users even in remote locations to access the website.</p>\n" +
                        "<p>Rekhta now enjoys the unique honour of presenting the complete works of two great Urdu Poets, Mir Taqi Mir and Mirza Ghalib and all the short stories by the legendary Urdu fictions writer Saadat Hasan Manto. One more new addition is Rekhti, which comprises Urdu Shayari composed by men employing women&rsquo;s language. A lecture series on Urdu prosody by Mr. Bhatnagar Shadab forms another attraction on it.</p>\n" +
                        "</div>";
        String text;
        int textColors= getPrimaryTextColor();
        String int2string = Integer.toHexString(textColors);
        String HtmlColor = "#"+ int2string.substring(int2string.length() - 6, int2string.length());
        text = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Lato-Medium.ttf\")}body {font-family: MyFont;color:"+HtmlColor+";font-size: medium;text-align: justify;}</style></head><body>";
       // text = "<html><body style=\"text-align:justify;\">";
        text += aboutEng;
        text += "</body></html>";
        webAboutUs.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }

    public void setAboutHin() {
        String aboutHin =
                "<div class=\"contentListBody\">\n" +
                        "<div style=\"direction: ltr; clear: both; position: relative; text-align: justify;\">" +
                        "<p> <strong>रेख़्ता</strong> उर्दू शायरी की वेबसाइट है जिसका मक़सद उर्दू शायरी को ऐसे लोगों तक आन लाइन उपलब्ध कराना है जो इससे गहरा लगाव रखते है और इसका आनन्द लेना चाहते हैं | वेबसाइट पर इस समय लगभग 1200 उर्दू शायरों की 12000 ग़ज़लें और नज़्में उपलब्ध हैं जिनमें बढ़ोतरी जारी है। इस वेबसाइट का सबसे विशिष्ट पहलू ये है कि इसमें उर्दू शायरी को देवनागरी और रोमन लिपियों में भी पेश किया गया है ताकि उर्दू लिपि न जानने वाले उर्दू शायरी के प्रेमी भी अपने पसंदीदा शायरों का कलाम पढ़ सकें।</p>\n" +
                        "<p>ऐसे पाठकों की आसानी के लिए सरल और लोकप्रिय शायरी को अलग से जमा किया है जिसमें सारे बड़े शायरों का आसान कलाम शामिल है। रेख़्ता पर शायरी को पढ़ने के साथ ही सुना भी जा सकता है । इसके लिए शायरी को, शायरों और गायकों की आवाज़ में ऑडियो और वीडियो की शक्ल में भी पेश किया गया है। उर्दू लिपि न जानने वालों के लिए, ग़ज़लों और नज़्मों के हर शब्द का अर्थ भी इस वेबसाइट पर उपलब्ध है।</p>\n" +
                        "<p>रेख़्ता’ को इस समय 160 देशों में देखा जा रहा है और पिछले एक साल में इसे लगभग ढाई लाख लोग देख चुके हैं। इस वेबसाइट को अधिक से अधिक फैलाने के लिए, अब नई टेक्नालोजी का इस्तेमाल करके डेस्कटॉप और लैपटॉप कंपुयटरों के साथ साथ टैबलेट  और मोबाइल फ़ोन पर भी उपलब्ध कराया जा रहा है।</p>\n" +
                        "<p>रेख़्ता’ का एक बड़ा कारनामा उसका ई-पुस्तक भाग है जहाँ उर्दू की पुरानी किताबों के साथ साथ उर्दू शायरी और साहित्य से संबंधित हिंदी और अंग्रेज़ी पुस्तकों को भी डिजिटाइज़ करके संरक्षित किया जा रहा है। यह पहली वेबसाइट है जिस पर मीर तक़ी मीर और मिर्ज़ा ग़ालिब की सारी ग़ज़ले और विख्यात कहानीकार सआदत हसन मंटो की सारी कहानियाँ पेश की गई हैं।\n</p>\n" +
                        "<p>‘रेख़्ता’ पर उर्दू के छंद-विधान जिसे अरूज़ कहा जाता है, पर एक लेक्चर सीरीज़ भी शुरू की गई है जो अरूज़ के विख्यात माहिर श्री भटनागर ‘शादाब’ पेश कर रहे हैं। इन लेक्चरों से ऐसे हिंदी-भाषी नौजवानों को लाभ होगा जो उर्दू में शायरी करने का प्रयास कर रहें है और इसके छंद-विधान से पूरी तरह परिचित नहीं है।</p>\n" +
                        "</div>";
        String text;
        int textColors= getPrimaryTextColor();
        String int2string = Integer.toHexString(textColors);
        String HtmlColor = "#"+ int2string.substring(int2string.length() - 6, int2string.length());
        text = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/NotoSansDevanagari-Regular.ttf\")}body {font-family: MyFont;color:"+HtmlColor+";font-size: medium;text-align: justify;}</style></head><body>";
        text += aboutHin;
        text += "</body></html>";
        webAboutUs.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }

    public void setAboutUrdu() {
        String aboutUrdu =
                "<div class=\"contentListBody\">\n" +
                        "<div style=\"direction: rtl; clear: both; position: relative; text-align: justify;\">" +
                        "<p>اردو شاعری برصغیر ہند کی تہذیب کا ایک ایسا خوبصورت آئنہ ہے جس میں عشق کے ہجرووصال کی صورتیں اپنے تمام تر پہلوؤں سمیت منعکس ہیں۔ انٹرنیٹ کی ایجاد سے اب تک، اردو شاعری کا حصول ہرکسی کے لیے آسان نہیں تھا۔حالانکہ انٹرنیت پر شاعری کا بہت سارا حصہ پہلے سے موجود ہے لیکن وہ اتنا منتشر، نامکمل اور غیر مستند ہے کہ قاری کا ذوق جس شاعری کے مطالعے کا تقاضا کرتا ہے ، اس سے اس کی سیرابی ممکن نہیں ہوپاتی، ساتھ ہی ساتھ اردو شاعری کا بیشتر حصہ انٹرنیٹ پر چونکہ میں موجود ہے اور اس کی تلاش کا کوئی اہم وسیلہ بھی وہاں موجود نہیں اس لیے قاری کی دشواری مزید بڑھ جاتی ہے۔بہت سے ایسے لوگ بھی ہیں جو اردو شاعری کو پڑھنا اور جاننا چاہتے ہیں لیکن رسم الخط سے نا واقفیت مانع ہوتی ہے۔یہاں تک کہ اردو جاننے والے حضرات کا بھی بہت سا وقت اپنے ذوق کے مطابق کلام ڈھونڈنے میں بے جا صرف ہوتا ہے۔خاص طور پر نئے نئے قارئین کو اردو شاعری کا مطالعہ کرتے وقت کے معنی سمجھنے میں خاصی دشواری کا سامنا کرنا پڑتا ہے۔</p>\n" +
                        "<p>ریختہ نے اس فاصلے کو کم کرنے کے لیے اردو شاعری کو اردو ، دیوناگری اور رومن رسم الخط میں عام کرنے کا بیڑہ اٹھا یا ہے۔اردو شاعری کے معیار کو برقرار رکھتے ہوئے شعرا اور ان کے کلام کا پوری ذمہ داری اور احتیاط کے ساتھ انتخاب کیا گیا ہے تاکہ یہاں موجود متن کے استناد میں کسی طرح کے شک کی گنجائش نہ ہو۔علاوہ ازیں اس ویب سائٹ پر ایک کلک کے ذریعے کسی بھی لفظ یا ترکیب کے معنی کو حاصل کرنے کے لیے لغات ان بلٹ کی گئی ہے۔بیشتر اردو شاعری کو شعرا کی اپنی آواز یا کسی دوسری اہم ادبی شخصیت کے ذریعے پڑھوایا بھی گیا ہے تاکہ قاری کو الفاظ کے تلفظ اور قرأت کو سمجھنے میں بھی آسانی ہو۔ہم نے خاص طور پر اردو شاعروں کے دواوین ،علم شعر اور نقد شعر کے متعلق کتابوں کو برقی صورت میں ویب سائٹ کے ذریعے آسانی سے فراہم کرانے کا التزام بھی کیا ہے۔</p>\n" +
                        "<p>ہم تمام اردو شاعری کا احاطہ کرنے کا دعویٰ تو نہیں کرتے تاہم مستقلاً اردو شاعری کی اس ویب سائٹ پرشعرا کے کلام کا اضافہ کیا جاتا رہے گا۔اگر آپ ہمیں کسی قسم کی رائے سے نوازنا ، کسی غلطی کی اصلاح کرنا اور کسی کتاب کو فراہم کرانا چاہتے ہیں تو ہم سے رابطہ کریں ہم آپ کا تہہِ دل سے استقبال کرتے ہیں۔ہماری ویب سائٹ سے کچھ ممتاز اور معتبر ادیب وابستہ ہیں جو آپ کی رایوں، درخواستوں اور کتابوں کومنظور کریں گے تاکہ انہیں ویب سائٹ پر اپلوڈ کیا جاسکے۔ </p>\n" +
                        "<p>مجھے پوری امید ہے کہ یہ ویب سائٹ آپ کے ادبی ذوق پر پوری اترے گی۔ \n" +
                        "اردو شاعری کی شاندار دنیا میں آپ کاخیر مقدم ہے۔</p>\n" +
                        "</div>";
        String text;
        int textColors= getPrimaryTextColor();
        String int2string = Integer.toHexString(textColors);
        String HtmlColor = "#"+ int2string.substring(int2string.length() - 6, int2string.length());
        text = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/NotoNastaliqUrdu-Regular.ttf\")}body {font-family: MyFont;color:"+HtmlColor+";font-size: medium;text-align: justify;}</style></head><body>";
        text += aboutUrdu;
        text += "</body></html>";
        webAboutUs.loadDataWithBaseURL(null, text, "text/html", "UTF-8", null);
    }
}
