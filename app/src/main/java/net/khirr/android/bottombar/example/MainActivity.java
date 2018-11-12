package net.khirr.android.bottombar.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.khirr.library.bottombar.BottomBar;
import net.khirr.library.bottombar.BottomBarView;
import net.khirr.library.bottombar.MultipleFragmentsManager;

import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    //  Multiple Fragments Manager keeps Fragment status when change tabs
    //  You can use directly user:
    //  getSupportFragmentManager()
    //      .beginTransaction()
    //      .replace(R.id.frameLayout, FragmentExample.newInstance(integer))
    //      .commitAllowingStateLoss();
    private MultipleFragmentsManager mMultipleFragmentsManager;

    private static final String TAG_HOME = "home";
    private static final String TAG_CHAT = "chat";
    private static final String TAG_NOTIFICATIONS = "notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final BottomBarView bottomBarView = (BottomBarView) findViewById(R.id.bottomBarView);

        mBottomBar = new BottomBar(this, bottomBarView)
                .setBackgroundColor(R.color.colorBackground)
                .setUnselectedColor(R.color.colorUnselected)
                .setSelectedColor(R.color.colorSelected)
                .setDividerColor(R.color.colorDivider)
                .addItem(new BottomBar.Item(0,
                        R.drawable.ic_home_white_24dp))
                .addItem(new BottomBar.Item(1,
                        "Chat",
                        R.drawable.ic_chat_bubble_white_24dp))
                .addItem(new BottomBar.Item(2,
                        "Notifications",
                        R.drawable.ic_notifications_white_24dp))
                .build();


        //  Multiple Fragments Manager helps to keep fragment status when
        //  load and back to already loaded fragment
        mMultipleFragmentsManager = new MultipleFragmentsManager(this, R.id.frameLayout)
                .addItem(FragmentExample.newInstance(0), TAG_HOME)
                .addItem(FragmentExample.newInstance(1), TAG_CHAT)
                .addItem(FragmentExample.newInstance(2), TAG_NOTIFICATIONS);

        mBottomBar.setBadgeCount(2, 5);

        mBottomBar.setOnItemClickListener(new Function1<Integer, Boolean>() {
            @Override
            public Boolean invoke(Integer integer) {
                switch (integer) {
                    case 0:
                        mMultipleFragmentsManager.select(TAG_HOME);
                        break;
                    case 1:
                        mMultipleFragmentsManager.select(TAG_CHAT);
                        break;
                    case 2:
                        mMultipleFragmentsManager.select(TAG_NOTIFICATIONS);
                        break;
                }
                return true;
            }
        });

        mBottomBar.forcePressed(0);
    }
}
