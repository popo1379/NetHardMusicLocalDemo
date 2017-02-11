package com.palmintelligence.administrator.nethardmusiclocaldemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.palmintelligence.administrator.nethardmusiclocaldemo.R;
import com.palmintelligence.administrator.nethardmusiclocaldemo.info.MusicInfo;
import com.palmintelligence.administrator.nethardmusiclocaldemo.utils.LocalMusicUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class LocalMusicAdapter extends BaseAdapter {
    private Context context;
    private List<MusicInfo> musicInfos;
    private MusicInfo musicInfo;
    private int pos = -1;
    public LocalMusicAdapter(Context context, List<MusicInfo> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
    }

    @Override
    public int getCount() {
        return musicInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        if (convertView == null) {
            holder=new ViewHolder();
            //绑定控件
            convertView = LayoutInflater.from(context).inflate(R.layout.local_music_item, null);
            holder.musicTitle=(TextView)convertView.findViewById(R.id.local_music_name);
           holder.musicArtist=(TextView)convertView.findViewById(R.id.local_music_singer);
            holder.albumImage=(ImageView)convertView.findViewById(R.id.local_music_pic);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
}
        musicInfo = musicInfos.get(position);
            //设置内容
        if(position == pos) {
            holder.albumImage.setImageResource(R.drawable.login_logo_netease);
        } else {
            Bitmap bitmap = LocalMusicUtils.getArtwork(context, musicInfo.getId(),musicInfo.getAlbumId(), true, true);
            holder.albumImage.setImageBitmap(bitmap);
        }
        holder.musicTitle.setText(musicInfo.getTitle());
        holder.musicArtist.setText(musicInfo.getArtist());

return convertView;
        }
    public final class ViewHolder{
        public TextView musicTitle;
        public TextView musicArtist;
        public ImageView albumImage;
    }
}