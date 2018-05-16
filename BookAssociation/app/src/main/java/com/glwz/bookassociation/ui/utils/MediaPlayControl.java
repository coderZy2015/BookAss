package com.glwz.bookassociation.ui.utils;

import android.media.MediaPlayer;

import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 音频播放控制
 * Created by zy on 2018/4/27.
 */

public class MediaPlayControl implements MediaPlayer.OnBufferingUpdateListener{

    private MediaPlayer mediaPlayer = null;

    private MediaEventListener eventListener = null;
    private List<BookMenuInfo.MessageBean.CatalogBean> adtaList;  //music的相关数据
    private List<BookMenuChinaBean.MessageBean.CatalogBean> chinaDataList;  //
    private ArrayList<String> songArrayList = new ArrayList<String>();  // 播放声音列表

    public String play_url = "";//记录当前播放的 play_url
    private int playType;	//播放类型  顺序  单曲  随机
    private int playIndex = -1;	//播放列表songArrayList里的id
    private boolean mp3_prepare;//判断音频文件是否准备完成
    private boolean bookIsOpen = false;

    public boolean back_to_menu = false;

    /**
     * 异步加载 准备完毕  是否可以播放
     */
    private boolean isCanPlay = true;

    private static MediaPlayControl instance = new MediaPlayControl();

    public static MediaPlayControl getInstance() {
       return instance;
    }

    public MediaPlayControl()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mp3_prepare = false;

    }
    /**
     * 播放相关资源music
     */
    public void play(String resource)
    {
        if (mediaPlayer == null) {
            return;
        }
        mp3_prepare = false;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(resource);
            play_url = resource;
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (isCanPlay) {
                        mp3_prepare = true;
                        mp.start();
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 播放第几首music
     * @param num  arrayList的index
     */
    public void setPlayIndex(int num)
    {
        if (songArrayList == null) {
            return;
        }

        if (num < songArrayList.size()) {
            playIndex = num;
            String url = songArrayList.get(playIndex);
            play(url);
        }
    }

    public int getPlayIndex()
    {
        return playIndex;
    }
    /**
     * 书籍是否开放  if not only read 5
     */
    public void setBookState(boolean _isOpen){
        bookIsOpen = _isOpen;
    }
    public boolean getBookState(){
        return bookIsOpen;
    }
    /**
     * 暂停播放
     */
    public void pause()
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return;
        }
        mediaPlayer.pause();
    }

    /**
     * 继续播放
     */
    public void continuePlay()
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return;
        }
        mediaPlayer.start();
    }

    /**
     * 获得总共时长
     */
    public int getDuration()
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return 1;
        }
        return mediaPlayer.getDuration();
    }

    /**
     * 上一首
     */
    public void setPrevious() {
        if (songArrayList == null) {
            return;
        }
        if (playIndex == 0) {
            return;
            //playIndex = songArrayList.size() - 1;
        } else {
            playIndex = playIndex - 1;
        }
        play(songArrayList.get(playIndex));
    }

    /**
     * 下一首
     */
    public void setNext() {
        if (songArrayList == null) {
            return;
        }
        if (playIndex == songArrayList.size() - 1) {
            return;
            //playIndex = 0;
        } else {
            playIndex = playIndex + 1;
        }
        play(songArrayList.get(playIndex));
    }

    public Boolean isPlaying()
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return false;
        }
        return mediaPlayer.isPlaying();
    }

    /**
     * 获得当前播放位置
     */
    public int getCurrentPostion()
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 设置播放进度
     * @param postion
     */
    public void setCurrentPostion(int postion)
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return;
        }
        int time = mediaPlayer.getDuration();
        if (postion <= time) {
            mediaPlayer.seekTo(postion);
        }

    }

    public void stop()
    {
        if (mediaPlayer == null || !mp3_prepare) {
            return;
        }
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    /**
     * 异步加载完    如果切换界面   关闭播放
     * @param bool
     */
    public void setPreparePlay(boolean bool)
    {
        isCanPlay = bool;
    }

    /**
     * 设置播放歌曲列表
     */
    public void setSongList(ArrayList<String> songList) {
        songArrayList.clear();
        songArrayList.addAll(songList);
    }

    public ArrayList<String> getSongList()
    {
        return songArrayList;
    }


    /**
     * 设置播放歌曲相关数据
     */
    public void setSongDataList(List<BookMenuInfo.MessageBean.CatalogBean> _adtaList) {
        adtaList = _adtaList;
    }

    public List<BookMenuInfo.MessageBean.CatalogBean> getSongDataList()
    {
        return adtaList;
    }

    public void setSongChinaDataList(List<BookMenuChinaBean.MessageBean.CatalogBean> _list){
        chinaDataList = _list;
    }

    public List<BookMenuChinaBean.MessageBean.CatalogBean> getSongChinaDataList(){
        return chinaDataList;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    /**
     * 播放完成的回调  接口
     * @author Administrator
     *
     */
    public interface MediaEventListener
    {
        void onCompleteExcute();
    }
}
