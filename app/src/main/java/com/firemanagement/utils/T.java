package com.firemanagement.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firemanagement.AppClient;


/**
 * Toast 统一管理类
 */
public class T {
    private static boolean isShow = true;//默认显示
    private static Toast mToast = null;//全局唯一的Toast
    private static Context mContext = AppClient.mContext;

    /*private控制不应该被实例化*/
    private T() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     *
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, resId, Toast.LENGTH_LONG);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration 单位:毫秒
     */
    public static void show(CharSequence message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param resId    资源ID:getResources().getString(R.string.xxxxxx);
     * @param duration 单位:毫秒
     */
    public static void show(int resId, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, resId, duration);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的View
     *
     * @param message
     * @param duration 单位:毫秒
     * @param view     显示自己的View
     */
    public static void customToastView(CharSequence message, int duration, View view) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, duration);
            } else {
                mToast.setText(message);
            }
            if (view != null) {
                mToast.setView(view);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的位置
     *
     * @param message
     * @param duration 单位:毫秒
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void customToastGravity(CharSequence message, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     *
     * @param message
     * @param iconResId 图片的资源id,如:R.drawable.icon
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(CharSequence message, int iconResId, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型CharSequence
     *
     * @param message
     * @param duration
     * @param view
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(CharSequence message, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, message, duration);
            } else {
                mToast.setText(message);
            }
            if (view != null) {
                mToast.setView(view);
            }
            if (isMargin) {
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if (isGravity) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型resId
     *
     * @param resId
     * @param duration
     * @param view             :应该是一个布局，布局中包含了自己设置好的内容
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(int resId, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(mContext, resId, duration);
            } else {
                mToast.setText(resId);
            }
            if (view != null) {
                mToast.setView(view);
            }
            if (isMargin) {
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if (isGravity) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }


}
