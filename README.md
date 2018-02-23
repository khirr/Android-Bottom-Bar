# Android-Bottom-Bar
A very simple Android way to use Bottom Bar on Android

Api-level 14+

Usage:

Add the repository to your gradle app
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.khirr:Android-Bottom-Bar:1.0.5'
}
```

Add this view to your code
```
<net.khirr.library.bottombar.BottomBarView
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:id="@+id/bottomBarView"
        android:layout_alignParentBottom="true"/>
```

Add items

Java
```
final BottomBarView bottomBarView = (BottomBarView) findViewById(R.id.bottomBarView);
//  addItem method needs id, name, icon 
mBottomBar = new BottomBar(this, bottomBarView)
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
```

Java
```
//  Click listener
//  Returns true|false is used to select or not item, 
//  for example if is restricted area and needs login
mBottomBar.setOnItemClickListener(new Function1<Integer, Boolean>() {
    @Override
    public Boolean invoke(Integer id) {

        return true;
    }
});
```

## License
MIT
