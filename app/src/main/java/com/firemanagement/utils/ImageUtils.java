package com.firemanagement.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firemanagement.AppClient;
import com.firemanagement.R;


/**
 * @ClassName ImageUtils
 * @Author AiSHan Feng
 * @Date 2019/1/18 10:58
 * @Version 1.0
 * @Description 图片有关操作工具类
 */
public class ImageUtils {

    /**
     * 根据resId给ImageView设置Bitmap图片
     *
     * @param resId
     * @param imageView
     */
    public static void setBitmap(int resId, ImageView imageView) {
        Glide.with(AppClient.mContext).load(resId).into(imageView);
    }

    /**
     * 裁剪填充
     *
     * @param resId
     * @param imageView
     */
    public static void setBitmapCenterCrop(int resId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(AppClient.mContext).load(resId).apply(requestOptions).into(imageView);
    }

    public static void setBitmapCenterCrop(String resId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(AppClient.mContext).load(resId).apply(requestOptions).into(imageView);
    }

    /**
     * 设置圆形头像
     *
     * @param resId
     * @param imageView
     */
    public static void setCircleBitmap(int resId, ImageView imageView) {
        // Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(AppClient.mContext).load(resId).apply(requestOptions).into(imageView);
    }

    public static void setCircleBitmap(String path, ImageView imageView) {
        // Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(AppClient.mContext).load(path).apply(requestOptions).into(imageView);
    }

    /**
     * 设置DrawableLeft等的大小
     *
     * @param textView 需要设置的控件(TextView or EditText)
     */
    public static void setDrawableSize(TextView textView) {
        Context context = AppClient.mContext;
        if (textView == null) {
            return;
        }
        //左，上，右，下
        Drawable[] compoundDrawables = textView.getCompoundDrawables();
//        for (int i = 0; i < compoundDrawables.length; i++) {
//            if (compoundDrawables[i] != null) {
//                L.i("-------compoundDrawables-----right-----" + compoundDrawables[i].getBounds().right);
//                L.i("-------compoundDrawables-----bottom-----" + compoundDrawables[i].getBounds().bottom);
//                L.i("-------compoundDrawables-----top-----" + compoundDrawables[i].getBounds().top);
//                L.i("-------compoundDrawables-----left-----" + compoundDrawables[i].getBounds().left);
//            }
//        }

        for (int i = 0; i < compoundDrawables.length; i++) {
            if (compoundDrawables[i] != null) {
                int size = (int) context.getResources().getDimension(R.dimen.dp_15);
                L.i("------Drawable---size---------" + size);
                compoundDrawables[i].setBounds(0, 0, size, size);
            }
        }
        textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }


}
