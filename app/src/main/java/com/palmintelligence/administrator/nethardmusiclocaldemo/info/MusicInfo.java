package com.palmintelligence.administrator.nethardmusiclocaldemo.info;

/**
 * Created by Administrator on 2016/9/17 0017.
 */
public class MusicInfo {
     private long id;
    private String title;
    private String artist;
    private long duration;
    private long size;
    private  String url;
    private int isMusic;
    private long albumId;

public void setId(long id){
    this.id=id;

}
public void setTitle(String title){
    this.title=title;

}
public void setArtist(String artist){
    this.artist=artist;

}

public void setDuration(long duration){
    this.duration=duration;

}
public void setSize(long size){
    this.size=size;
}

public void setUrl(String url){
    this.url=url;
}
public void setIsMusic(int isMusic){
    this.isMusic=isMusic;
}
    public void setAlbumId(long albumId){this.albumId=albumId;}

public long getId(){
    return id;
}
public String getTitle(){
    return title;
}
public String getArtist(){
    return artist;
}
    public long getDuration(){
        return duration;
    }
 public long getSize(){
     return size;
 }
  public String getUrl(){
      return url;
  }
 public int getIsMusic(){
     return isMusic;
 }
    public long getAlbumId(){return albumId;}
}
