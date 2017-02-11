package com.palmintelligence.administrator.nethardmusiclocaldemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.palmintelligence.administrator.nethardmusiclocaldemo.adapter.LocalMusicAdapter;
import com.palmintelligence.administrator.nethardmusiclocaldemo.adapter.MainViewpagerAdapter;
import com.palmintelligence.administrator.nethardmusiclocaldemo.constant.MusicServiceCT;
import com.palmintelligence.administrator.nethardmusiclocaldemo.info.MusicInfo;
import com.palmintelligence.administrator.nethardmusiclocaldemo.service.playerService;
import com.palmintelligence.administrator.nethardmusiclocaldemo.utils.LocalMusicUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<View> mViewpages;
    //导航圆点图片
    private ImageView mImageView;
    //导航圆点数组
    private ImageView[] imageViews;
    // 包裹滑动图片LinearLayout
    private ViewGroup main;
    // 包裹小圆点的LinearLayout
    private LinearLayout mPoints;
    private int lastPosition;
    private Toolbar toolbar;
    private ListView mListView;
    private List<MusicInfo> musicInfos=new ArrayList<MusicInfo>();
    private ImageView play_Button, next_Button,bar_pic;
    private TextView singer,sing_name;
    private boolean isFirstTime = true;
    private boolean isPlaying; // 正在播放
    private boolean isPause; // 暂停
    private int listPosition;//列表数
   private MusicInfo musicInfo;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private RemoteViews contentViews;
    private BroadcastReceiver  mBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotification=new Notification(R.drawable.login_logo_netease,"网难云音乐通知栏启动",System.currentTimeMillis());
        //NotificationManager用来管理notification的显示和消失
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //常驻通知栏
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
      contentViews=new RemoteViews(getPackageName(),R.layout.notification_player);
        //设置通知栏notification播放键控制：
        Intent previousButtonIntent=new Intent("com.NHMLD.notification_playbutton");
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(this, 0, previousButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.notification_play,pendPreviousButtonIntent);
        mNotification.contentView=contentViews;
        mNotificationManager.notify(0,mNotification);


        mViewPager=(ViewPager)findViewById(R.id.banner_viewpager);
        mPoints=(LinearLayout)findViewById(R.id.banner_viewpager_points);



        //将图片装入ViewPager:
        int [] img=new int[]{R.drawable.main_viewpager_pic1,R.drawable.main_viewpager_pic2,R.drawable.main_viewpager_pic3,
                R.drawable.main_viewpager_pic4,R.drawable.main_viewpager_pic5};
        mViewpages=new ArrayList<View>();
        for(int i=0;i<img.length;i++) {
            LinearLayout linearLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams ltp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final ImageView imageView = new ImageView(MainActivity.this);
            //将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //动态设置图片显示
            imageView.setImageResource(img[i]);
            //绑定小圆点
            linearLayout.addView(imageView, ltp);
            //将资源加入到容器中
            mViewpages.add(linearLayout);
        }
        mViewPager.setAdapter(new MainViewpagerAdapter(mViewpages));
        //设置滑动事件
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
    // onPageSelected方法是页面跳转完后得到调用，arg0是你当前选中的页面的Position（位置编号）。
                @Override
                public void onPageSelected(int position) {
                    // 控制底部导航点的显示
                    position %= mViewpages.size();

                    mPoints.getChildAt(position).setEnabled(true);
                    mPoints.getChildAt(lastPosition).setEnabled(false);
                    lastPosition = position;
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

/**
 * 动态加载指示点
 */
        imageViews=new ImageView[mViewpages.size()];
        for (int i=0;i<mViewpages.size();i++) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mImageView = new ImageView(MainActivity.this);
            mImageView.setBackgroundResource(R.drawable.viewpager_point);
            imageViews[i] = mImageView;
            // 设置每个小圆点距离左边的间距
            margin.setMargins(10, 0, 0, 0);
            mImageView.setLayoutParams(margin);
            if (i == 0) {
                mImageView.setEnabled(true);
            } else {
                mImageView.setEnabled(false);
            }

            mPoints.addView(mImageView);
        }





        mListView=(ListView)findViewById(R.id.main_layout_listview);
        //设置配适器：
        musicInfos= LocalMusicUtils.getMusicInfos(MainActivity.this);//获得歌曲列表，可能有问题
        LocalMusicAdapter mAdapter=new LocalMusicAdapter(MainActivity.this,musicInfos);
        mListView.setAdapter(mAdapter);

        toolbar=(Toolbar)findViewById(R.id.main_layout_toolbar);
        toolbar.setTitle("网难云音乐本地版");
        toolbar.setNavigationIcon(R.drawable.toolbar_discover_normal);
        setSupportActionBar(toolbar);

        play_Button=(ImageView)findViewById(R.id.main_layout_play);
        play_Button.setImageResource(R.drawable.playbar_btn_play);
        next_Button=(ImageView)findViewById(R.id.main_layout_next);
        sing_name=(TextView)findViewById(R.id.main_layout_name);
        singer=(TextView)findViewById(R.id.main_layout_singer);
        bar_pic=(ImageView)findViewById(R.id.main_bar_pic);
//底部播放开关设置：
       play_Button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isPlaying) {
                   isPlaying = false;
                   isPause = true;
                    pause();
               }else if(isPause){
                   isPlaying = true;
                   isPause =  false;
                   play();
               }
           }
       });

        /**
         * 列表监听：
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
               int listPosition=position;
                if(musicInfos != null) {
                    isFirstTime = false;
                    isPlaying = true;
                    isPause = false;
                    MusicInfo  musicInfo  = musicInfos.get(position);
                    Log.d("musicInfo-->",musicInfo .toString());
                    Intent intent = new Intent();
                    intent.putExtra("url", musicInfo .getUrl());
                    intent.putExtra("MSG", MusicServiceCT.PlayerMsg.PLAY_MSG);
                    intent.setClass(MainActivity.this, playerService.class);
                    startService(intent);       //启动服务
                    play_Button.setImageResource(R.drawable.playbar_btn_pause);
                    sing_name.setText(musicInfo.getTitle());
                    bar_pic.setImageBitmap(LocalMusicUtils.getArtwork(MainActivity.this,musicInfo.getId(),musicInfo.getAlbumId(),true, true));
                    contentViews.setTextViewText(R.id.notification_sing,musicInfo.getTitle());
                    contentViews.setTextViewText(R.id.notification_singer,musicInfo.getArtist());
                    contentViews.setImageViewResource(R.id.notification_play,R.drawable.playbar_btn_pause);
                    contentViews.setImageViewBitmap(R.id.notification_pic,LocalMusicUtils.getArtwork(MainActivity.this,musicInfo.getId(),musicInfo.getAlbumId(),true, true));
                    mNotification.contentView=contentViews;
                    mNotificationManager.notify(0,mNotification);
                    if (musicInfo.getArtist()!=null) {
                        singer.setText(musicInfo.getArtist());
                    }else {
                        singer.setText("无名");
                    }
                }

            }
            });
        next_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }

    public void pause(){
        Intent intent = new Intent();
        play_Button.setImageResource(R.drawable.playbar_btn_play);
        intent.setAction("com.wwj.media.MUSIC_SERVICE");
        intent.putExtra("MSG", MusicServiceCT.PlayerMsg.PAUSE_MSG);
        intent.setClass(MainActivity.this, playerService.class);
        startService(intent);
        contentViews.setImageViewResource(R.id.notification_play,R.drawable.playbar_btn_play);
        mNotification.contentView=contentViews;
        mNotificationManager.notify(0,mNotification);
    }
public void play(){
    Intent intent = new Intent();
    play_Button.setImageResource(R.drawable.playbar_btn_pause);
    intent.setAction("com.wwj.media.MUSIC_SERVICE");
    intent.putExtra("MSG", MusicServiceCT.PlayerMsg.CONTINUE_MSG);
    intent.setClass(MainActivity.this, playerService.class);
    startService(intent);
    contentViews.setImageViewResource(R.id.notification_play,R.drawable.playbar_btn_pause);
    mNotification.contentView=contentViews;
    mNotificationManager.notify(0,mNotification);
}
    public void next() {
        play_Button.setImageResource(R.drawable.playbar_btn_pause);
        listPosition = listPosition + 1;
        if(listPosition <= musicInfos.size() - 1) {
            musicInfo = musicInfos.get(listPosition);
          sing_name.setText(musicInfo.getTitle());
            bar_pic.setImageBitmap(LocalMusicUtils.getArtwork(MainActivity.this,musicInfo.getId(),musicInfo.getAlbumId(),true, true));
            Intent intent = new Intent();
            intent.setAction("com.wwj.media.MUSIC_SERVICE");
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("url", musicInfo.getUrl());
            intent.putExtra("MSG", MusicServiceCT.PlayerMsg.NEXT_MSG);
            intent.setClass(MainActivity.this, playerService.class);
            startService(intent);
            isFirstTime = false;
            isPlaying = true;
            isPause = false;
        } else {
            Toast.makeText(MainActivity.this, "没有下一首了", Toast.LENGTH_SHORT).show();
        }
    }
//设置Toolbar菜单
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.toolbar_menus, menu);
    SearchManager searchManager =
            (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView =
            (SearchView) menu.findItem(R.id.ab_search).getActionView();
    searchView.setSearchableInfo(
            searchManager.getSearchableInfo(getComponentName()));
    return true;

}
    //代码作废，仅作参考
private void initMyNotification(){
    mNotification=new Notification(R.drawable.login_logo_netease,"网难云音乐通知栏启动",System.currentTimeMillis());

  //NotificationManager用来管理notification的显示和消失
    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    //常驻通知栏
    mNotification.flags = Notification.FLAG_ONGOING_EVENT;

    RemoteViews contentViews=new RemoteViews(getPackageName(),R.layout.notification_player);

    mNotification.contentView=contentViews;
mNotificationManager.notify(0,mNotification);

}



}

