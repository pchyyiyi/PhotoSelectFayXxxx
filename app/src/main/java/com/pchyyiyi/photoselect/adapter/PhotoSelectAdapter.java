package com.pchyyiyi.photoselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pchyyiyi.photoselect.R;
import com.pchyyiyi.photoselect.model.MerdisePhotoInfo;
import com.pchyyiyi.photoselect.widget.GlideRoundCornerTransform;

import java.util.List;

/**
 * @ClassName PhotoSelectAdapter
 * @Description 普通Adapter实现多布局，像微信朋友圈一样添加图片
 * @Author fayXxxx
 * @Date 2021/5/29 下午1:50
 * @Version 1.0
 */
public class PhotoSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mMaxNum;
    private List<MerdisePhotoInfo> mData;
    private Context mContext;

    private final int ADD_ITEM = 1;
    private final int PIC_ITEM = 2;


    public PhotoSelectAdapter(Context context, List<MerdisePhotoInfo> data, int maxnum){
        this.mData = data;
        this.mMaxNum = maxnum;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ADD_ITEM){
            View mer_view = LayoutInflater.from(mContext).inflate(R.layout.item_addphoto_add, parent, false);
            AddViewHolder ddViewHolder = new AddViewHolder(mer_view);
            return ddViewHolder;
        }else {
            View mer_view = LayoutInflater.from(mContext).inflate(R.layout.item_addphoto_image, parent, false);
            PicViewHolder ddViewHolder = new PicViewHolder(mer_view);
            return ddViewHolder;
        }
    }

    //设置控件数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AddViewHolder){//加号的布局
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemAddClick(position);
                    }
                }
            });
        } else {//加载图片的布局
            PicViewHolder viewHolder = (PicViewHolder)holder;
            Glide.with(holder.itemView.getContext())
                    .load(mData.get(position).getPicPath())
                    .centerInside()
                    .apply(RequestOptions.bitmapTransform(
                            new GlideRoundCornerTransform()))
                    .into(viewHolder.pic);
            viewHolder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemPicClick(position);
                    }
                }
            });
            viewHolder.pic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemPicLongClick(position);
                    }
                    return false;
                }
            });

            viewHolder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemDelClick(position);
                    }
                }
            });
        }
    }

    /**
     * 当data.size()达到最大值时，返回data.size()(不给加号布局提供位置)
     * 否则，返回的数量+1，为了给加号布局添加位置
     */
    @Override
    public int getItemCount() {
        if(mData!=null) {
            if (mData.size() < mMaxNum) {
                return mData.size() + 1;
            } else {
                return mData.size();
            }
        }else {
            return 1;
        }
    }

    /**
     * 当data.size()达到最大值时，返回图片布局，
     * 否则按照逻辑：如果position + 1 == itemCount返回添加布局，不等于返回图片布局
     * (因为如果当前位置+1=itemCount，则代表它是最后一个，因为位置是从0计数的，而itemCount是从1计数)
     */
    @Override
    public int getItemViewType(int position) {
        if(mData!=null){
            if(mData.size() == mMaxNum){
                return PIC_ITEM;
            }else{
                if(position + 1 == getItemCount()){
                    return ADD_ITEM;
                }else{
                    return PIC_ITEM;
                }
            }
        }else{
            return ADD_ITEM;
        }
    }

    //加号布局
    static class AddViewHolder extends RecyclerView.ViewHolder{
        public AddViewHolder(@NonNull View view) {
            super(view);
        }
    }

    //普通布局
    static class PicViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;
        private ImageView del;
        public PicViewHolder(@NonNull View view) {
            super(view);
            pic = view.findViewById(R.id.ivImage);
            del = view.findViewById(R.id.ivDelete);
        }
    }

    public interface OnItemClickListener{
        //点击增加按键
        void onItemAddClick(int position);
        //点击删除按键
        void onItemDelClick(int position);
        //点击图片
        void onItemPicClick(int position);
        //点击图片长按
        void onItemPicLongClick(int position);
    }

    public void setOnClickListener(OnItemClickListener mitemClickListener) {
        this.onItemClickListener = mitemClickListener;
    }

    private OnItemClickListener onItemClickListener;



}
