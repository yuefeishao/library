package com.yuefei.library.widget.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuefei.library.R;


/**
 * Created by shao on 2017/6/16.
 */

public class EmojiTab {
    private LayoutInflater mLayoutInflater;
    private LinearLayout mIndicator;
    private int selected = 0;
    private IEmojiItemClickListener mOnItemClickListener;
    private int mEmojiCountPerPage;
    private Context mContext;
    public EmojiTab() {
    }

    public void setOnItemClickListener(IEmojiItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public Drawable obtainTabDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.rc_tab_emoji);
    }

    public View obtainTabPager(Context context) {
        return this.initView(context);
    }

    public void onTableSelected(int position) {
    }

    private View initView(final Context context) {
        this.mContext=context;
        EmojiUtils.init(context);
        int count = EmojiUtils.getEmojiSize();
        this.mEmojiCountPerPage = 20;
        final int pages = count / this.mEmojiCountPerPage + (count % this.mEmojiCountPerPage != 0?1:0);
        View view = LayoutInflater.from(context).inflate(R.layout.emoji_pager, null);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.rc_view_pager);
        this.mIndicator = (LinearLayout)view.findViewById(R.id.rc_indicator);
        this.mLayoutInflater = LayoutInflater.from(context);
        viewPager.setAdapter(new EmojiPagerAdapter(pages));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                onIndicatorChanged(selected,position);
                selected=position;
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setOffscreenPageLimit(1);
        this.initIndicator(pages, this.mIndicator);
        int position=0;
        viewPager.setCurrentItem(position);
        this.onIndicatorChanged(-1, position);
        return view;
    }

    private void initIndicator(int pages, LinearLayout indicator) {
        for(int i = 0; i < pages; ++i) {
            ImageView imageView = (ImageView)this.mLayoutInflater.inflate(R.layout.rc_ext_indicator, (ViewGroup)null);
            imageView.setImageResource(R.mipmap.rc_ext_indicator);
            indicator.addView(imageView);
        }

    }

    private void onIndicatorChanged(int pre, int cur) {
        int count = this.mIndicator.getChildCount();
        if(count > 0 && pre < count && cur < count) {
            ImageView curView;
            if(pre >= 0) {
                curView = (ImageView)this.mIndicator.getChildAt(pre);
                curView.setImageResource(R.mipmap.rc_ext_indicator);
            }

            if(cur >= 0) {
                curView = (ImageView)this.mIndicator.getChildAt(cur);
                curView.setImageResource(R.mipmap.rc_ext_indicator_hover);
            }
        }

    }

    private class ViewHolder {
        ImageView emojiIV;
        private ViewHolder() {
        }
    }

    private class EmojiAdapter extends BaseAdapter {
        int count;
        int index;

        public EmojiAdapter(int index, int count) {
            this.count = Math.min(mEmojiCountPerPage, count - index);
            this.index = index;
        }

        public int getCount() {
            return this.count + 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0L;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
              ViewHolder viewHolder;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.emoji_item, (ViewGroup)null);
                viewHolder.emojiIV = (ImageView)convertView.findViewById(R.id.rc_ext_emoji_item);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder)convertView.getTag();
            if(position != mEmojiCountPerPage && position + this.index != EmojiUtils.getEmojiSize()) {
                int mPosition=position+index;
                viewHolder.emojiIV.setImageResource(EmojiUtils.getEmojiDrawable(mContext,mPosition));
            } else {
                viewHolder.emojiIV.setImageResource(R.mipmap.rc_icon_emoji_delete);
            }

            return convertView;
        }
    }

    private class EmojiPagerAdapter extends PagerAdapter {
        int count;

        public EmojiPagerAdapter(int count) {
            this.count = count;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = (GridView)mLayoutInflater.inflate(R.layout.emoji_grid_view, (ViewGroup)null);
            gridView.setAdapter(new EmojiAdapter(position * mEmojiCountPerPage, EmojiUtils.getEmojiSize()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(mOnItemClickListener != null) {
                        int index = position + selected * mEmojiCountPerPage;
                        if(position ==mEmojiCountPerPage) {
                         mOnItemClickListener.onDeleteClick();
                        } else if(index >= EmojiUtils.getEmojiSize()) {
                           mOnItemClickListener.onDeleteClick();
                        } else {
                            int index1=position+selected*mEmojiCountPerPage;
                            String code = EmojiUtils.getEmojiName(index1);
                            mOnItemClickListener.onEmojiClick(code);
                        }
                    }
                }
            });
            container.addView(gridView);
            return gridView;
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public int getCount() {
            return this.count;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            View layout = (View)object;
            container.removeView(layout);
        }
    }
}
