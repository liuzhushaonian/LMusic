package com.example.legend.lmusic.model;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

/**
 *选择手机图片作为显示
 * Created by legend on 2017/6/7.
 */

public class ImageSelector {



    @TargetApi(19)
    public Uri handlerImageOnKitKat(Intent intent){
        String imagePath=null;
        Uri uri=intent.getData();
        if (DocumentsContract.isDocumentUri(LApplication.getContext(),uri)){

            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){

                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){

                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){

            imagePath=getImagePath(uri,null);

        }else if ("file".equalsIgnoreCase(uri.getScheme())){

            imagePath=uri.getPath();
        }


//        displayImage(imagePath,imageView);
        return Uri.parse(imagePath);

    }



    public Uri handlerImageBeforeKitKat(Intent intent){
        Uri uri=intent.getData();
        String imagePath=getImagePath(uri,null);
//        displayImage(imagePath,imageView);
        return Uri.parse(imagePath);
    }


    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=LApplication.getContext().getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath, ImageView imageView){
        if (imagePath!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(LApplication.getContext(),"无法获取图片，请重试",Toast.LENGTH_SHORT).show();
        }
    }
}
