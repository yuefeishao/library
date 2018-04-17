package com.yuefei.library.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.yuefei.library.R;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shao on 2018/1/31.
 */

public class CommonUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 拍照
     * */
    protected void requestCamera(Activity context, Uri mTakePictureUri) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(!path.exists()) {
            path.mkdirs();
        }
        String name = System.currentTimeMillis() + ".jpg";
        File file = new File(path, name);
        mTakePictureUri = Uri.fromFile(file);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        List resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        Uri uri = null;

        try {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + context.getString(R.string.authorities_fileprovider), file);
        } catch (Exception var10) {
            var10.printStackTrace();
            throw new RuntimeException("Please check  Manifest FileProvider config.");
        }
        Iterator e = resInfoList.iterator();
        while(e.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo)e.next();
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.grantUriPermission(packageName, uri,  Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra("output", uri);
        context.startActivityForResult(intent, 1);
    }

}
