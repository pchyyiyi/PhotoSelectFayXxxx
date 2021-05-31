package com.pchyyiyi.photoselect.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pchyyiyi.photoselect.PhotoSectApplication;
import com.pchyyiyi.photoselect.R;


/**
 * @ClassName ShowBigPictureDialog
 * @Description TODO
 * @Author fayxx
 * @Date 2021/5/19 下午1:35
 * @Version 1.0
 */
public class ShowBigPictureDialog extends AlertDialog {
    private static final String TAG = "ShowBigPictureDialog";
    private Context mContext;

    private View contentView;
    private AppCompatImageView mBigImageView;

    private static final int RES_TYPE_URI = 1;
    private static final int RES_TYPE_URL = 2;
    private static final int RES_TYPE_RESID = 3;

    private int mresType = RES_TYPE_RESID;
    private String mpathurl;
    private Uri mpathUri;
    private int mpathResID;

    public ShowBigPictureDialog(@NonNull Context context, String pathurl) {
        super(context, R.style.Theme_dialog);
        this.mContext = context;
        this.mpathurl = pathurl;
        mresType = RES_TYPE_URL;
    }

    public ShowBigPictureDialog(@NonNull Context context, Uri pathURI) {
        super(context, R.style.Theme_dialog);
        this.mContext = context;
        this.mpathUri = pathURI;
        mresType = RES_TYPE_URI;
    }

    public ShowBigPictureDialog(@NonNull Context context, int pathResID) {
        super(context, R.style.Theme_dialog);
        this.mContext = context;
        this.mpathResID = pathResID;
        mresType = RES_TYPE_RESID;
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int displayX = wm.getDefaultDisplay().getWidth();
        int displayY = wm.getDefaultDisplay().getHeight();

        lp.width = (int) (displayX * 0.9); // 宽度设置为屏幕的0.9
        lp.height = (int) (displayY * 0.9); // 高度设置为屏幕的0.9
        getWindow().setAttributes(lp);
        getWindow().setGravity(Gravity.CENTER);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        layoutInflaterView();

        setContentView(contentView);
    }
    /**
     * 获取布局
     * @return
     */
    private View layoutInflaterView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.layout_showbigpicture_dialog, null);

        mBigImageView = (AppCompatImageView)contentView.findViewById(R.id.BigImageView);

        if(mresType == RES_TYPE_URL){
            Glide.with(PhotoSectApplication.getAppContext())
                    .load(mpathurl)
                    .centerInside()
                    .apply(RequestOptions.bitmapTransform(
                            new GlideRoundCornerTransform()))
                    .into(mBigImageView);
        }else if(mresType == RES_TYPE_URI){
            Glide.with(PhotoSectApplication.getAppContext())
                    .load(mpathUri)
                    .centerInside()
                    .apply(RequestOptions.bitmapTransform(
                            new GlideRoundCornerTransform()))
                    .into(mBigImageView);
        }else if(mresType == RES_TYPE_RESID){
            Glide.with(PhotoSectApplication.getAppContext())
                    .load(mpathResID)
                    .centerInside()
                    .apply(RequestOptions.bitmapTransform(
                            new GlideRoundCornerTransform()))
                    .into(mBigImageView);
        }

        mBigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return contentView;
    }

}
