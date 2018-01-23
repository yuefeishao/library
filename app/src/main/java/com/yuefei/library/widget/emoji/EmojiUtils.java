package com.yuefei.library.widget.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.util.DisplayMetrics;
import android.widget.TextView;
import com.yuefei.library.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Agent on 2017/6/16.
 */

public class EmojiUtils {
    private static float density;
    private static Context mContext;
    private static Map<String, EmojiInfo> sEmojiMap;
    private static List<EmojiInfo> sEmojiList;

    public EmojiUtils() {

    }
    public static void init(Context context) {
        sEmojiMap = new HashMap();
        sEmojiList = new ArrayList();
        mContext = context.getApplicationContext();
        String[] names = context.getResources().getStringArray(context.getResources().getIdentifier("emoji_name", "array", context.getPackageName()));
        TypedArray array = context.getResources().obtainTypedArray(context.getResources().getIdentifier("emoji_drawable", "array", context.getPackageName()));
        if(names.length != array.length()) {
            throw new RuntimeException("Emoji resource init fail.");
        } else {
            int i = -1;

            while(true) {
                ++i;
                if(i >= names.length) {
                    DisplayMetrics var5 = context.getResources().getDisplayMetrics();
                    density = var5.density;
                    array.recycle();
                    return;
                }

                EmojiInfo dm = new EmojiInfo(names[i], array.getResourceId(i, -1));
                sEmojiMap.put(names[i], dm);
                sEmojiList.add(dm);
            }
        }
    }

    public static List<EmojiInfo> getEmojiList() {
        return sEmojiList;
    }

    public static int getEmojiCount(String input) {
        if(input == null) {
            return 0;
        } else {
            int count = 0;
            char[] chars = input.toCharArray();
            new SpannableStringBuilder(input);

            for(int i = 0; i < chars.length; ++i) {
                if(!Character.isHighSurrogate(chars[i])) {
                    int codePoint;
                    boolean isSurrogatePair;
                    if(Character.isLowSurrogate(chars[i])) {
                        if(i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                            continue;
                        }

                        codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                        isSurrogatePair = true;
                    } else {
                        codePoint = chars[i];
                        isSurrogatePair = false;
                    }

                    if(sEmojiMap.containsKey(codePoint)) {
                        ++count;
                    }
                }
            }

            return count;
        }
    }


    public static boolean isEmoji(String input) {
        if(input == null) {
            return false;
        } else {
            char[] chars = input.toCharArray();
            boolean codePoint = false;
            int length = chars.length;

            for(int i = 0; i < length; ++i) {
                if(!Character.isHighSurrogate(chars[i])) {
                    int var5;
                    if(Character.isLowSurrogate(chars[i])) {
                        if(i <= 0 || !Character.isSurrogatePair(chars[i - 1], chars[i])) {
                            continue;
                        }

                        var5 = Character.toCodePoint(chars[i - 1], chars[i]);
                    } else {
                        var5 = chars[i];
                    }

                    if(sEmojiMap.containsKey(Integer.valueOf(var5))) {
                        return true;
                    }
                }
            }

            return false;
        }
    }


    public static int getEmojiSize() {
        return sEmojiMap.size();
    }

    public static String getEmojiName(int index) {
        EmojiInfo info = sEmojiList.get(index);
        return info.name;
    }

    public static int getEmojiDrawable(Context context, int index) {
        if(index >= 0 && index < sEmojiList.size()) {
            EmojiInfo emoji = (EmojiInfo)sEmojiList.get(index);
             return emoji.resId;
        }
        return R.mipmap.f_static_000;
    }

    private static class EmojiInfo {
        String name;
        int resId;
        public EmojiInfo(String name,int resId) {
           this.resId=resId;
            this.name=name;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }
    }

    public static class EmojiImageSpan extends ReplacementSpan {
        Drawable mDrawable;
        private static final String TAG = "DynamicDrawableSpan";
        public static final int ALIGN_BOTTOM = 0;
        private WeakReference<Drawable> mDrawableRef;

        public EmojiImageSpan(String codePoint) {
            if(sEmojiMap.containsKey(codePoint)) {
                this.mDrawable = mContext.getResources().getDrawable(sEmojiMap.get(codePoint).resId);
                int width = this.mDrawable.getIntrinsicWidth() - (int)(4.0F * density);
                int height = this.mDrawable.getIntrinsicHeight() - (int)(4.0F * density);
                this.mDrawable.setBounds(0, 0, width > 0?width:0, height > 0?height:0);
            }
        }

        public Drawable getDrawable() {
            return this.mDrawable;
        }

        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = this.getCachedDrawable();
            Rect rect = d.getBounds();
            if(fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;
                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            return rect.right;
        }

        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = this.getCachedDrawable();
            canvas.save();
            int transY = bottom - b.getBounds().bottom;
            transY = (int)((float)transY - density);
            canvas.translate(x, (float)transY);
            b.draw(canvas);
            canvas.restore();
        }

        private Drawable getCachedDrawable() {
            WeakReference wr = this.mDrawableRef;
            Drawable d = null;
            if(wr != null) {
                d = (Drawable)wr.get();
            }

            if(d == null) {
                d = this.getDrawable();
                this.mDrawableRef = new WeakReference(d);
            }

            return d;
        }
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,

                                            int reqWidth, int reqHeight) {

        // 源图片的高度和宽度

        final int height = options.outHeight;

        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // 计算出实际宽高和目标宽高的比率

            final int heightRatio = Math.round((float) height / (float) reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高

            // 一定都会大于等于目标的宽和高。

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;

    }



    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,

                                                         int reqWidth, int reqHeight) {

        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, resId, options);

        // 调用上面定义的方法计算inSampleSize值

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 使用获取到的inSampleSize值再次解析图片

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);

    }





    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);

    }



    public static void handlerEmojiText(TextView comment, String content, Context context){

        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<EmojiInfo> iterator;
        EmojiInfo emoji = null;
        while (m.find()) {
            iterator = sEmojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getName())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context,
                            decodeSampledBitmapFromResource(context.getResources(), emoji.getResId()
                            , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);

    }

      public String getFaceId(String faceStr) {
        if (sEmojiMap.containsKey(faceStr)) {
            return sEmojiMap.get(faceStr).name;
        }
        return "";
    }



}
