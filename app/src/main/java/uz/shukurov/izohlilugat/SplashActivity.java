package uz.shukurov.izohlilugat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 4000;


    TextView author,quote;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        Random rand = new Random();

        int n = rand.nextInt(4)+1;

        author = (TextView) findViewById(R.id.author);
        quote = (TextView) findViewById(R.id.quote);



        if(n ==1){
            quote.setText("«Yoshlar – kelajak bunyodkori».");
            author.setText("Shavkat Mirziyoyev");
        }
        else if(n==2){
            quote.setText("«Biz kelajagimizni o’z qo’limiz bilan quramiz.»");
            author.setText("Islom Karimov");
        }
        else if(n==3){
            quote.setText("«Yangicha fikrlash va ishlash — davr talabi».");
            author.setText("Islom Karimov");
        }
        else if(n==4){
            quote.setText("«Har kimning uch quroli bor: so'z, giyoh va tig'».");
            author.setText("Ibn Sino");
        }



    }

}
