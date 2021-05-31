package com.pchyyiyi.photoselect;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;


/**
 * @ClassName PhotoSectApplication
 * @Description TODO
 * @Author fayXxxx
 * @Date 2021/5/31 上午9:12
 * @Version 1.0
 */
public class PhotoSectApplication extends Application {

    public final static String TAG = "PhotoSectApplication";
    private static PhotoSectApplication instance = null;

    private static int displayX, displayY;

    /**
     * 设备唯一标识码
     */
    private static String deviceIdUUID;

    public static Context getAppContext() {
        return instance;
    }

    /**
     * 获取该应用唯一的实例
     * @return
     */
    public static PhotoSectApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        init();
    }

    /**
     * 对app数据进行初始化
     */
    private void init(){
        /**
         * Display X and Y
         */
        WindowManager wm = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
        displayX = wm.getDefaultDisplay().getWidth();
        displayY = wm.getDefaultDisplay().getHeight();
    }

    public int getDisplayX() {
        return displayX;
    }

    public void setDisplayX(int displayX) {
        this.displayX = displayX;
    }

    public int getDisplayY() {
        return displayY;
    }

    public void setDisplayY(int displayY) {
        this.displayY = displayY;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
