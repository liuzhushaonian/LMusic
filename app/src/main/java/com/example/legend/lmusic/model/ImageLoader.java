package com.example.legend.lmusic.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.view.CircleImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于加载图片以及缓存图片，三级缓存
 * Created by liuzhushaonian on 2017/3/29.
 */

public class ImageLoader {

    private final int TAG_KEY_URL= R.id.albums_image;
    private static final int CPU_COUNT=Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE=CPU_COUNT+1;
    private static final int MAXNUM_POOL_SIZE=CPU_COUNT*2+1;
    private static final long KEEP_ALIVE=10L;
    Bitmap bitmap;


    private LruCache<String,Bitmap> lruCache;

    private static final String TAG="ImageLoader";
    private final static ImageLoader imageLoader=new ImageLoader();

//    private final String CACHE_PATH= Environment.getExternalStorageDirectory().
//            getAbsolutePath()+"/CMusicCache";

    private final String CACHE_PATH= String.valueOf(LApplication.getContext().getCacheDir());

    private final String IMAGE_PATH= String.valueOf(LApplication.getContext().getFilesDir());

    private ImageLoader(){

        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize=maxMemory/8;

        lruCache=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };


    }



    /**
     * 使用单例模式
     * @return
     */
    public static ImageLoader getImageLoader(){
        return imageLoader;
    }


    /**
     * 获取音乐文件里内置的封面
     * @param album_id
     * @return
     */
    private String getAlbumArt(int album_id){
        String mUriAlbums="content://media/external/audio/albums";
        String[] projection=new String[]{"album_art"};
        Cursor cursor=LApplication.getContext().getContentResolver().query(
                Uri.parse(mUriAlbums+"/"+Integer.toString(album_id)),projection,null,null,null);
        String albums_art=null;
        if (cursor.getCount()>0&&cursor.getColumnCount()>0){
            cursor.moveToNext();
            albums_art=cursor.getString(0);
        }
        cursor.close();
        cursor=null;
        return albums_art;
    }

    /**
     * 此处为真正获取封面的方法，返回一个Bitmap对象
     * @param mp3Info
     * @return
     */
    public Bitmap getBitmapFromFile(Mp3Info mp3Info) {

        int albums_id=mp3Info.getAlbumId();
        Bitmap bitmap = null;
        String albumsArt = getAlbumArt(albums_id);
        if (albumsArt != null) {
            bitmap = BitmapFactory.decodeFile(albumsArt);
        }

        if (bitmap!=null){
            setInDisk(bitmap, String.valueOf(mp3Info.getAlbumId()));
//            bitmap=Bitmap.createScaledBitmap(bitmap,MyApplication.getStatusWidth(),MyApplication.getStatusHeight(),false);
        }
        return bitmap;
    }

    /**
     * 将Bitmap写入磁盘
     * @param bitmap
     * @param albums_id
     */
    private void setInDisk(Bitmap bitmap,String albums_id){
        try {
            String fileName= String.valueOf(albums_id);
            File file=new File(CACHE_PATH,fileName);
            File parentFile=file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘获取Bitmap
     * @param url
     * @return
     */
    private Bitmap getBitmapFromDisk(String url,int reqWidth,int reqHeight){
        String fileName=null;
        Bitmap bitmap=null;
        try {
            fileName= String.valueOf(url);
            File file=new File(CACHE_PATH,fileName);
            FileInputStream fileInputStream=new FileInputStream(file);
//            FileDescriptor fileDescriptor=fileInputStream.getFD();
            bitmap=BitmapFactory.decodeStream(new FileInputStream(file));
            bitmap=resizeBitmap(bitmap,reqWidth,reqHeight);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (bitmap!=null){
            setInMemory(bitmap,url);
        }

        return bitmap;
    }

    /**
     * 将Bitmap写入内存
     * @param bitmap
     * @param albums_id
     */
    private void setInMemory(Bitmap bitmap,String albums_id){
            lruCache.put(String.valueOf(albums_id),bitmap);
    }

    /**
     * 在内存读取Bitmap
     * @param albums_id
     * @return
     */
    private Bitmap getBitmapFromMemory(String albums_id){
        String url=String.valueOf(albums_id);

        return lruCache.get(url);
    }


    /**
     * 提供接口获取Bitmap
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmap(Mp3Info mp3Info,int reqWidth,int reqHeight){


        Bitmap bitmap=getBitmapFromMemory(String.valueOf(mp3Info.getAlbumId()));
        if (bitmap!=null){

            return bitmap;
        }

        bitmap=getBitmapFromDisk(String.valueOf(mp3Info.getAlbumId()),reqWidth,reqHeight);
        if (bitmap!=null){

            return bitmap;
        }

        bitmap=getBitmapFromFile(mp3Info);
        if (bitmap!=null){
            bitmap=resizeBitmap(bitmap,reqWidth,reqHeight);
            return bitmap;
        }
        return null;

    }

    /**
     * 使用handler设置图片
     */
    private Handler handler=new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            LoaderResult loaderResult= (LoaderResult) msg.obj;
            CircleImage imageView=loaderResult.imageView;
//            String url= (String) loaderResult.imageView.getTag(TAG_KEY_URL);
//            if (url.equals(loaderResult.url)){
            if (loaderResult.bitmap!=null) {
                imageView.setImageBitmap(loaderResult.bitmap);
            }else {
                imageView.setImageResource(R.mipmap.ic);
            }
//            }else {
//                Log.w(TAG,"set image bitmap,but url has changed,ignored!");
//            }
        }
    };

    public void bindBitmap(final Mp3Info mp3Info, final CircleImage imageView, final int reqWidth, final int reqHeight){

        final String url= String.valueOf(mp3Info.getAlbumId());
        imageView.setTag(TAG_KEY_URL,url);
        final Bitmap bitmap=getBitmapFromMemory(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }else {
            imageView.setImageResource(R.mipmap.ic);
        }

        Runnable loadBitmapTask=new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=loadBitmap(mp3Info,reqWidth,reqHeight);
                if (bitmap!=null){
                    LoaderResult loaderResult=new LoaderResult(imageView,url,bitmap);
                    handler.obtainMessage(1,loaderResult).sendToTarget();
                }
            }
        };
        ThreadPool.execute(loadBitmapTask);

    }

    public void bindBitmap(final Mp3Info mp3Info, final ImageView imageView, final int reqWidth, final int reqHeight){

        final String url= String.valueOf(mp3Info.getAlbumId());
        imageView.setTag(TAG_KEY_URL,url);
        final Bitmap bitmap=getBitmapFromMemory(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask=new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=loadBitmap(mp3Info,reqWidth,reqHeight);
                if (bitmap!=null){
                    LoaderResult loaderResult=new LoaderResult(imageView,url,bitmap);
                    handler.obtainMessage(1,loaderResult).sendToTarget();
                }
            }
        };
        ThreadPool.execute(loadBitmapTask);

    }



    /**
     * 获取专辑大图封面，高清无码
     * @param mp3Info
     * @param imageView
     */
//    public void bindBitmap(final Mp3Info mp3Info, final ImageView imageView){
//        final String url= String.valueOf(mp3Info.getAlbums_id());
//        if (imageView==null){
//            System.out.println("空了；返回！");
//            return;
//        }
////        imageView.setTag(TAG_KEY_URL,url);
////        final Bitmap bitmap=getBitmapFromMemory(url);
////        if (bitmap!=null){
////            imageView.setImageBitmap(bitmap);
////            return;
////        }
//
//        Runnable loadBitmapTask=new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap=resizeBitmap(getBitmapFromFile(mp3Info),MyApplication.getWidth()/2,MyApplication.getWidth()/2);
//                if (bitmap!=null){
//                    System.out.println(bitmap.getByteCount()/1024/1024);
//                    LoaderResult loaderResult=new LoaderResult(imageView,url,bitmap);
//                    handler.obtainMessage(1,loaderResult).sendToTarget();
//                }
//            }
//        };
//        ThreadPool.execute(loadBitmapTask);
//
//    }



    private static final ThreadFactory mThreadFactory=new ThreadFactory() {
        private final AtomicInteger count=new AtomicInteger(1);
        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable,"ImageLoader#"+count.getAndIncrement());
        }
    };


    private static final Executor ThreadPool=new ThreadPoolExecutor(
            CORE_POOL_SIZE,MAXNUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(),mThreadFactory);

    private static class LoaderResult{
        private CircleImage imageView;
        private ImageView imageView2;
        private String url;
        private Bitmap bitmap;

        private LoaderResult(CircleImage imageView,String url,Bitmap bitmap){
            this.imageView=imageView;
            this.url=url;
            this.bitmap=bitmap;
        }

        private LoaderResult(ImageView imageView,String url,Bitmap bitmap){
            this.imageView2=imageView;
            this.url=url;
            this.bitmap=bitmap;
        }


    }



    private Bitmap resizeBitmap(Bitmap bitmap,int reqWidth,int reqHeight){
        if (bitmap!=null){
            return Bitmap.createScaledBitmap(bitmap,reqWidth,reqHeight,false);
        }
        return null;
    }


    public boolean getImage(Mp3Info mp3Info){
        Bitmap bitmap=getBitmapFromFile(mp3Info);
        return bitmap!=null;
    }

    public Bitmap getBitmap() {

        try {
            File file=new File(IMAGE_PATH+"/bg");
            bitmap=BitmapFactory.decodeFile(String.valueOf(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
