package com.yuefei.library.widget.emoji;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author shao
 */
public class EmojiTextView extends TextView {
	private static final String TAG = "liweiping";
	private static final String START_CHAR = "[";
	private static final String END_CHAR = "]";
	private boolean mIsDynamic;
	private Context context;
	public EmojiTextView(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public EmojiTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		EmojiUtils.init(getContext());
		setText(getText());
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		SpannableString content = new SpannableString(text);
		emotifySpannable(content);
		super.setText(content, BufferType.SPANNABLE);
	}

	/**
	 * Work through the contents of the string, and replace any occurrences of
	 * [icon] with the imageSpan
	 * 
	 * @param spannable
	 */
	private void emotifySpannable(Spannable spannable) {
		int length = spannable.length();
		int position = 0;
		int tagStartPosition = 0;
		int tagLength = 0;
		StringBuilder buffer = new StringBuilder();
		boolean inTag = false;

		if (length <= 0)
			return;

		do {
			String c = spannable.subSequence(position, position + 1).toString();

			if (!inTag && c.equals(START_CHAR)) {
				buffer = new StringBuilder();
				tagStartPosition = position;
				// Log.d(TAG, "   Entering tag at " + tagStartPosition);

				inTag = true;
				tagLength = 0;
			}

			if (inTag) {
				buffer.append(c);
				tagLength++;

				// Have we reached end of the tag?
				if (c.equals(END_CHAR)) {
					inTag = false;

					String tag = buffer.toString();
					int tagEnd = tagStartPosition + tagLength;
					// start by liweiping for 去除首部有多个“[”符号
					int lastIndex = tag.lastIndexOf(START_CHAR);
					if (lastIndex > 0) {
						tagStartPosition = tagStartPosition + lastIndex;
						tag = tag.substring(lastIndex, tag.length());
					}
					// end by liweiping for
					// Log.d(TAG, "Tag: " + tag + ", started at: "
					// + tagStartPosition + ", finished at " + tagEnd
					// + ", length: " + tagLength);


					EmojiUtils.EmojiImageSpan imageSpan = getImageSpan(tag);
						if (imageSpan != null){	
							spannable.setSpan(imageSpan, tagStartPosition,
									tagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
									}
			}

			position++;
		} while (position < length);
	}

	private EmojiUtils.EmojiImageSpan getImageSpan(String content) {

		if (!TextUtils.isEmpty(content)) {
			try {
				EmojiUtils.EmojiImageSpan imageSpan = new EmojiUtils.EmojiImageSpan(content);
				return imageSpan;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	

}
