package com.example.legend.lmusic.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.model.ImageLoader;
import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.model.Mp3Info;

/**
 *
 * Created by liuzh on 2017/2/19.
 */

public class NotificationHelper {
    public final static String PLAY="com.legend.BROADCAST_PLAY";
    public final static String NEXT="com.legend.BROADCAST_NEXT";
    public final static String PREVIOUS="com.legend.BROADCAST_PREVIOUS";
    public final static String PAUSE="com.legend.BROADCAST_PAUSE";




    private ImageLoader imageLoader;


    private final static String BROADCAST="com.legend.BROADCAST";
    private final static int INT_PLAY=0x1;
    private final static int INT_PREVIOUS=0x2;
    private final static int INT_NEXT=0x3;
    private final static int INT_PAUSE=0x4;

    public NotificationHelper() {
        imageLoader=ImageLoader.getImageLoader();
    }

    public void playingNotification(Mp3Info mp3Info, boolean can_pause){
        Intent intent=new Intent(LApplication.getContext(), HomeActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(LApplication.getContext(),0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(LApplication.getContext());
        builder
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.small)
                .setContentIntent(pendingIntent);

        /**
         * 大视图布局，尚未设置背景色以及专辑图片
         */
        RemoteViews remoteViewsBig=new RemoteViews(LApplication.getContext().getPackageName(),R.layout.notification_layout);
        remoteViewsBig.setTextViewText(R.id.notification_song_name,mp3Info.getSongName());
        remoteViewsBig.setTextViewText(R.id.notification_artist,mp3Info.getArtist()+" - "+mp3Info.getAlbumName());
        Bitmap bitmap=imageLoader.getBitmapFromFile(mp3Info);
        int w= (int) LApplication.getContext().getResources().getDimension(R.dimen.notification);










        /**
         * 小视图布局，尚未设置背景色以及专辑图片
         */
        RemoteViews remoteViewsSmall=new RemoteViews(LApplication.getContext().getPackageName(),R.layout.notification_small_layout);
        remoteViewsSmall.setTextViewText(R.id.small_notification_song_name,mp3Info.getSongName());
        remoteViewsSmall.setTextViewText(R.id.small_notification_artist,mp3Info.getArtist()+" - "+mp3Info.getAlbumName());
        remoteViewsSmall.setImageViewBitmap(R.id.small_notification_image,imageLoader.getBitmapFromFile(mp3Info));


        /**
         * 设置视图
         */
        if (bitmap==null){
            Drawable drawable=ContextCompat.getDrawable(LApplication.getContext(),R.drawable.notification);
            BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
            bitmap=Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(),w,w,false);
        }else {

//            Bitmap b=getBitmap(bitmap,25);
            bitmap=Bitmap.createScaledBitmap(bitmap,w,w,false);
        }

        ContextCompat.getDrawable(LApplication.getContext(),R.drawable.bg_default);

        switch (LApplication.getMusicTheme()){
            case DEFAULT:
                remoteViewsSmall.setInt(R.id.notification_small_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorCP));
                remoteViewsBig.setInt(R.id.notification_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorCP));
                break;
            case YELLOW:
                remoteViewsSmall.setInt(R.id.notification_small_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorOrange));
                remoteViewsBig.setInt(R.id.notification_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorOrange));
                break;
            case PINK:
                remoteViewsSmall.setInt(R.id.notification_small_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorPink));
                remoteViewsBig.setInt(R.id.notification_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorPink));
                break;
            case DIY:
                remoteViewsSmall.setInt(R.id.notification_small_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorMi));
                remoteViewsBig.setInt(R.id.notification_bg,"setBackgroundColor",ContextCompat.getColor(LApplication.getContext(),R.color.colorMi));
                break;

        }
        int h= (int) LApplication.getContext().getResources().getDimension(R.dimen.notification_small);

        Bitmap smallBitmap=imageLoader.getBitmapFromFile(mp3Info);
        if (smallBitmap==null){
            Drawable drawable=ContextCompat.getDrawable(LApplication.getContext(),R.drawable.notification);
            BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
            smallBitmap=bitmapDrawable.getBitmap();
        }
        smallBitmap=Bitmap.createScaledBitmap(smallBitmap,h,h,false);
        remoteViewsBig.setImageViewBitmap(R.id.notification_albums,bitmap);
        remoteViewsSmall.setImageViewBitmap(R.id.small_notification_image,smallBitmap);



        /**
         * 通知按钮点击事件
         */







        /**
         * 中间的播放按钮与暂停按钮点击事件
         */
        if (can_pause){
        Intent intent_play=new Intent(PAUSE);
        intent_play.putExtra(PAUSE,INT_PAUSE);//可以暂停，此时应该是暂停按钮在界面上，点击后切换为三角（播放）按钮

        PendingIntent play_pendingIntent=PendingIntent.getBroadcast(LApplication.getContext(),0,intent_play,0);
        remoteViewsBig.setOnClickPendingIntent(R.id.notification_play,play_pendingIntent);
            remoteViewsSmall.setOnClickPendingIntent(R.id.small_play,play_pendingIntent);
            remoteViewsBig.setImageViewResource(R.id.notification_play,R.drawable.pause);
            remoteViewsSmall.setImageViewResource(R.id.small_play,R.drawable.pause);
        }else {
            Intent intent_pause=new Intent(PLAY);
            intent_pause.putExtra(PLAY,INT_PLAY);
            PendingIntent pause_pendingIntent=PendingIntent.getBroadcast(LApplication.getContext(),0,intent_pause,0);
            remoteViewsBig.setOnClickPendingIntent(R.id.notification_play,pause_pendingIntent);
            remoteViewsSmall.setOnClickPendingIntent(R.id.small_play,pause_pendingIntent);
            remoteViewsBig.setImageViewResource(R.id.notification_play,R.drawable.play);
            remoteViewsSmall.setImageViewResource(R.id.small_play,R.drawable.play);
        }


        /**
         * 上一首按钮点击事件
         */

        Intent intent_previous=new Intent(PREVIOUS);
        intent_previous.putExtra(PREVIOUS,INT_PREVIOUS);
        PendingIntent previous_pendingIntent=PendingIntent.getBroadcast(LApplication.getContext(),0,intent_previous,0);

        remoteViewsBig.setOnClickPendingIntent(R.id.notification_previous,previous_pendingIntent);
        remoteViewsSmall.setOnClickPendingIntent(R.id.small_previous,previous_pendingIntent);

        /**
         * 下一首按钮点击事件
         */

        Intent intent_next=new Intent(NEXT);
        intent_next.putExtra(NEXT,INT_NEXT);

        PendingIntent next_pendingIntent=PendingIntent.getBroadcast(LApplication.getContext(),0,intent_next,0);
        remoteViewsBig.setOnClickPendingIntent(R.id.notification_next,next_pendingIntent);
        remoteViewsSmall.setOnClickPendingIntent(R.id.small_next,next_pendingIntent);



        builder.setCustomContentView(remoteViewsSmall);
        builder.setCustomBigContentView(remoteViewsBig);
        Notification notification;
        NotificationManager notificationManager= (NotificationManager) LApplication.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (can_pause){
            builder.setOngoing(true);
            notification=builder.build();
            notificationManager.notify(1,notification);
        }else {
            builder.setOngoing(false);
            notification=builder.build();
            notificationManager.notify(1,notification);

        }
    }

    /**
     * 图片高斯模糊 保留方法
     * @param source
     * @param radius
     * @return
     */
    private Bitmap getBitmap(Bitmap source,int radius){

        Bitmap bitmap=source;
        RenderScript renderScript=RenderScript.create(LApplication.getContext());

        final Allocation input=Allocation.createFromBitmap(renderScript,bitmap);

        final Allocation output=Allocation.createTyped(renderScript,input.getType());

        ScriptIntrinsicBlur scriptIntrinsicBlur=ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        scriptIntrinsicBlur.setInput(input);

        scriptIntrinsicBlur.setRadius(radius);

        scriptIntrinsicBlur.forEach(output);

        output.copyTo(bitmap);

        renderScript.destroy();

        return bitmap;
    }

}
