package net.khirr.android.bottombar.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.khirr.library.bottombar.BottomBar;

import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBottomBar = new BottomBar(this)
                .addItem(new BottomBar.Item(0,
                        "Home",
                        R.drawable.ic_home_white_24dp))
                .addItem(new BottomBar.Item(1,
                        "Chat",
                        R.drawable.ic_chat_bubble_white_24dp))
                .addItem(new BottomBar.Item(2,
                        "Notifications",
                        R.drawable.ic_notifications_white_24dp))
                .build();

        mBottomBar.setOnItemClickListener(new Function1<Integer, Boolean>() {
            @Override
            public Boolean invoke(Integer integer) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, FragmentExample.newInstance(integer))
                        .commitAllowingStateLoss();
                return true;
            }
        });

        mBottomBar.forcePressed(0);
    }
}
