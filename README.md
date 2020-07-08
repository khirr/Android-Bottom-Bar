# Android-Bottom-Bar
A very simple Android way to use Bottom Bar on Android

[![](https://jitpack.io/v/khirr/Android-Bottom-Bar.svg)](https://jitpack.io/#khirr/Android-Bottom-Bar)

Api-level 14+

Versions:
1.0.10 uses AppCompat libraries
2.0.0 uses AndroidX libraries

Usage:

Add the repository to your gradle app
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.khirr:Android-Bottom-Bar:2.0.0'
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
//  set custom colors
final BottomBarColors bottomBarColors = new BottomBarColors()
                .setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackground))
                .setUnselectedColor(ContextCompat.getColor(this, R.color.colorUnselected))
                .setSelectedColor(ContextCompat.getColor(this, R.color.colorSelected))
                .setBadgeColor(Color.parseColor("#00ab48"))
                .setDividerColor(Color.parseColor("#b23c09"));

//  addItem method needs id, name, icon 
mBottomBar = new BottomBar(this, bottomBarView)
        .setBottomBarColors(bottomBarColors)
        .addItem(new BottomBar.Item(0, "Home", R.drawable.ic_home_white_24dp))
        .addItem(new BottomBar.Item(1, "Chat", R.drawable.ic_chat_bubble_white_24dp))
        .addItem(new BottomBar.Item(2, "Notifications", R.drawable.ic_notifications_white_24dp))
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
