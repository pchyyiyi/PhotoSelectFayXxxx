package com.pchyyiyi.photoselect.model;

/**
 * @ClassName MerchandisePictureInfo
 * @Description TODO
 * @Author fayXxxx
 * @Date 2021/5/29 下午3:16
 * @Version 1.0
 */
public class MerdisePhotoInfo {
    /**
     * 默认是已经上传的图片
     */
    private PictureType type = PictureType.HTTP_URL_TYPE;

    private String picPath;

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    /**
     * 商品图片的类型，用户标记是已经上传过的图片，还是新的【或者添加的】需要上传的图片
     */
    public enum PictureType{
        HTTP_URL_TYPE,
        FILE_URI_TYPE
    }

    @Override
    public String toString() {
        return "MerdisePictureInfo{" +
                "type=" + type +
                ", picPath='" + picPath + '\'' +
                '}';
    }
}
