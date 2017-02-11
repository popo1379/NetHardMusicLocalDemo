package com.palmintelligence.administrator.nethardmusiclocaldemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.palmintelligence.administrator.nethardmusiclocaldemo.constant.MusicServiceCT;


/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class playerService extends Service {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String path;                        //音乐文件路径
    private boolean isPause;                    //暂停状态

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer.isPlaying()) {
            stop();
        }
        path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("MSG", 0);
        if (msg == MusicServiceCT.PlayerMsg.PLAY_MSG) {
            play(0);
        } else if (msg == MusicServiceCT.PlayerMsg.PAUSE_MSG) {
            pause();
        } else if (msg ==MusicServiceCT.PlayerMsg.STOP_MSG) {
            stop();
        }
        else if (msg ==MusicServiceCT.PlayerMsg.CONTINUE_MSG) {
            resume();
        }else if (msg == MusicServiceCT.PlayerMsg.NEXT_MSG){
            play(0);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 播放音乐
     * @param position
     */
    private void play(int position) {
        try {
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();  //进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐//&& mediaPlayer.isPlaying()
     */
    private void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPause = true;
        }
    }
    /**
     * 停止音乐
     */
    private void stop(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    private void resume() {
        if (isPause) {
            mediaPlayer.start();
        }
        }


    /**
     *
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     *
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if(positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
        }
    }
}