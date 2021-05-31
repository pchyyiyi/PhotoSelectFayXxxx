/*****************************************************************************
 * AndroidUtil.java
 * ****************************************************************************
 * Copyright © 2015 VLC authors, VideoLAN and VideoLabs
 * <p>
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/
package com.pchyyiyi.photoselect.model.util;

import android.os.Build;

public class AndroidUtil {
    private static final String TAG="AndroidUtil";

    public static boolean isFroyoOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean isGingerbreadOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean isHoneycombOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isHoneycombMr1OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean isICSOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isJellyBeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isJellyBeanMR1OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isJellyBeanMR2OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLolliPopOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLolliPopMR1OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean isMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isNougatOrLater() {//24
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isOreoOrLater() { //26
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean isPieOrLater() { //28
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean isQqqOrLater() {//29
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}
