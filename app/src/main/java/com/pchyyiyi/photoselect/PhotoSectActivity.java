package com.pchyyiyi.photoselect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pchyyiyi.photoselect.adapter.PhotoSelectAdapter;
import com.pchyyiyi.photoselect.model.GifSizeFilter;
import com.pchyyiyi.photoselect.model.MerdisePhotoInfo;
import com.pchyyiyi.photoselect.model.util.AndroidUtil;
import com.pchyyiyi.photoselect.model.util.PermissionUtil;
import com.pchyyiyi.photoselect.widget.ShowBigPictureDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PhotoSectApplication
 * @Description TODO
 * @Author fayXxxx
 * @Date 2021/5/31 上午9:22
 * @Version 1.0
 */
public class PhotoSectActivity extends AppCompatActivity implements PhotoSelectAdapter.OnItemClickListener{

    Activity mContext;

    /**
     * 读取相册权限
     */
    private static String[] PERMISSIONS_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    /**
     * 拍照片权限
     */
    private static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 权限申请回调
     */
    private final static int REQUEST_EXTERNAL_STORAGE = 0x01;
    private final static int REQUEST_CAMERA  = 0x02;

    private final int REQUEST_CODE_CHOOSE = 300; //照片选择回调
    private final int REQUEST_CODE_MODIFY = 301; //修改照片回调

    private List<MerdisePhotoInfo> mSelectedObtainPathResult = new ArrayList<>();

    //最大能上传的照片数
    private final int maxNum = 6;
    /**
     * 长按修改图片的位置
     */
    private int mModifyPosition = 0;

    private PhotoSelectAdapter myCommonAdapter;

    private AppCompatButton photoPermissionBtn;
    private AppCompatButton cameraPermissionBtn;
    private RecyclerView mRecyclerview;
    private AppCompatButton submitBtn;
    private AppCompatTextView showpicinfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_photo_select);
        assignViews();
        initData();
    }


    protected void assignViews() {
        photoPermissionBtn = findViewById(R.id.photoPermissionBtn);
        cameraPermissionBtn = findViewById(R.id.cameraPermissionBtn);
        mRecyclerview = findViewById(R.id.mRecyclerview);
        submitBtn = findViewById(R.id.submitBtn);
        showpicinfo = findViewById(R.id.showpicinfo);
    }


    protected void initData() {
        photoPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCheckExternalStorage();
            }
        });

        cameraPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCheckCamera();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureInfo();
            }
        });

        initRecyclerview();
    }

    private void showPictureInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("图片信息如下：\n");
        if(mSelectedObtainPathResult!=null && mSelectedObtainPathResult.size()>0){
            for(int pos = 0; pos <mSelectedObtainPathResult.size(); pos++){
                stringBuffer.append("第"+pos+"张 - "+mSelectedObtainPathResult.get(pos).toString()+"\n");
            }
        }
        showpicinfo.setText(stringBuffer.toString());
    }

    /**
     * Recyclerview相关配置
     */
    private void initRecyclerview() {
        myCommonAdapter = new PhotoSelectAdapter(this, mSelectedObtainPathResult, maxNum);

        mRecyclerview.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerview.setAdapter(myCommonAdapter);
        myCommonAdapter.setOnClickListener(this);
    }

    /**
     * 选择图片
     */
    private void selectPhoto(int num){
        Matisse.from(PhotoSectActivity.this)
                .choose(MimeType.ofImage(), false)//  .choose(MimeType.ofAll(),false)  //false表示不能同时选照片和视频
                .showSingleMediaType(true)
                .countable(true)//选择时是否计数
                .capture(false)//拍照需要的两个（写完这个就会有照相那个图标了）
                .captureStrategy(
                        new CaptureStrategy(true,
                                "com.pchyyiyi.photoselect.fileprovider",
                                "test"))//最后文件存储地址：Pictures/test
                .maxSelectable(num)//最大可选择数
                .addFilter(new GifSizeFilter(120, 120, 2 * Filter.K * Filter.K)) //过滤器   2M大小
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//每个图片方格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //选择方向
                .thumbnailScale(0.85f) //刚进入图片选择页面后图片的清晰度
                .imageEngine(new GlideEngine())//图片引擎
                .theme(R.style.Matisse_Zhihu)  //R.style.Matisse_Zhihu 或者 R.style.Matisse_Dracula
                .originalEnable(false)//原图按钮
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(REQUEST_CODE_CHOOSE); //请求码
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK){//选择照片后的回调
            //获取到的地址只是相对地址，但可以给图片加载，若要上传图片，需获取真实本地地址
            // Matisse.obtainResult(data)
            // Matisse.obtainPathResult(data));

            List<String> datatruePath = Matisse.obtainPathResult(data);//真实地址
            if(datatruePath!=null && datatruePath.size()>0) {
                for (String path : datatruePath) {
                    if (!TextUtils.isEmpty(path)) {
                        MerdisePhotoInfo info = new MerdisePhotoInfo();
                        info.setType(MerdisePhotoInfo.PictureType.FILE_URI_TYPE);
                        info.setPicPath(path);
                        mSelectedObtainPathResult.add(info);
                    }
                }
                myCommonAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == REQUEST_CODE_MODIFY && resultCode == RESULT_OK){//修改照片回调
            //获取到的地址只是相对地址，但可以给图片加载，若要上传图片，需获取真实本地地址
            // Matisse.obtainResult(data)
            // Matisse.obtainPathResult(data));

            List<String> datatruePath = Matisse.obtainPathResult(data);//真实地址
            if(datatruePath!=null && datatruePath.size()==1){
                if(!TextUtils.isEmpty(datatruePath.get(0))){
                    mSelectedObtainPathResult.get(mModifyPosition).setPicPath(datatruePath.get(0));
                    mSelectedObtainPathResult.get(mModifyPosition).setType(
                            MerdisePhotoInfo.PictureType.FILE_URI_TYPE);

                    myCommonAdapter.notifyDataSetChanged();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemAddClick(int position) {
        selectPhoto(maxNum - mSelectedObtainPathResult.size());
    }

    @Override
    public void onItemDelClick(int position) {
        mSelectedObtainPathResult.remove(position);
        myCommonAdapter.notifyDataSetChanged();
    }

    //TODO 日后可增加图片放大缩小查看的功能
    @Override
    public void onItemPicClick(int position) {
        showToast("点击图片"+position);
        ShowBigPictureDialog dialog = new ShowBigPictureDialog(mContext,
                mSelectedObtainPathResult.get(position).getPicPath());
        dialog.show();
    }

    @Override
    public void onItemPicLongClick(int position) {
        showToast("长按了图片"+position);
        mModifyPosition = position;
        modifyPhoto();
    }

    /**
     * 长按修改图片
     */
    private void modifyPhoto(){
        Matisse.from(PhotoSectActivity.this)
                .choose(MimeType.ofImage(), false)//  .choose(MimeType.ofAll(),false)  //false表示不能同时选照片和视频
                .showSingleMediaType(true)
                .countable(true)//选择时是否计数
                .capture(true)//拍照需要的两个（写完这个就会有照相那个图标了）
                .captureStrategy(
                        new CaptureStrategy(true,
                                "com.pchyyiyi.photoselect.fileprovider",
                                "test"))//最后文件存储地址：Pictures/test
                .maxSelectable(1)//最大可选择数
                .addFilter(new GifSizeFilter(120, 120, 2 * Filter.K * Filter.K)) //过滤器   2M大小
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//每个图片方格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) //选择方向
                .thumbnailScale(0.85f) //刚进入图片选择页面后图片的清晰度
                .imageEngine(new GlideEngine())//图片引擎
                .theme(R.style.Matisse_Zhihu)
                .originalEnable(false)//原图按钮
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(REQUEST_CODE_MODIFY); //请求码
    }


    public void showToast(String str){
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setText(str);
        toast.show();
    }

    public void showToast(int redId){
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setText(getString(redId));
        toast.show();
    }


    /**
     * 相册权限
     */
    private void toCheckExternalStorage() {
        if (AndroidUtil.isMarshmallowOrLater() &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)){
            requestExternalStorage();
        } else {
            showToast("相册权限已开通");
        }
    }

    /**
     * 相册权限
     */
    private void requestExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("需要访问相册文件系统权限")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            ActivityCompat.requestPermissions(PhotoSectActivity.this,
                                    PERMISSIONS_EXTERNAL_STORAGE,
                                    REQUEST_EXTERNAL_STORAGE);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            showToast("未授权将不能使用！");
                        }
                    }).cancelable(false).show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_EXTERNAL_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 照相机权限
     */
    private void toCheckCamera() {
        if (AndroidUtil.isMarshmallowOrLater() &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)){
            requestCamera();
        } else {
            showToast("照相机权限已开通");
        }
    }
    /**
     * 照相机权限
     */
    private void requestCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new MaterialDialog.Builder(this)
                    .title("提示")
                    .content("需要访问照相机权限")
                    .positiveText("确定")
                    .negativeText("取消")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            ActivityCompat.requestPermissions(PhotoSectActivity.this,
                                    PERMISSIONS_CAMERA,
                                    REQUEST_CAMERA);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            showToast("未授权将不能使用！");
                        }
                    }).cancelable(false).show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA,
                    REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                showToast("相册权限已开通");
            } else {
                showToast("未授权将不能使用！");
            }
        }else if(requestCode == REQUEST_CAMERA){
            if (PermissionUtil.verifyPermissions(grantResults)) {
                showToast("照相机权限已开通");
            } else {
                showToast("未授权将不能使用！");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}