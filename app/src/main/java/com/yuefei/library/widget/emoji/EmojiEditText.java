package com.yuefei.library.widget.emoji;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;


public class EmojiEditText extends AppCompatEditText {

	private static final String TAG =EmojiEditText.class.getSimpleName();
	private static final String START_CHAR = "[";
	private static final String END_CHAR = "]";
	private int lastPosition;
	private String tempsp;
	public EmojiEditText(Context context) {
		super(context);
		init();
	}

	public EmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EmojiEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		SpannableString content = new SpannableString(text);
		emotifySpannable(content);
		super.setText(content, BufferType.SPANNABLE);
	}

	private void init() {
		EmojiUtils.init(getContext());
		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				emotifySpannable(s);
			}
		});
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

		if (length <= 0){
			tempsp = null;
			lastPosition =0;
			return;
		}
			

		if(tempsp!=null && tempsp.length()<spannable.length()){	
			position = lastPosition;
			if(!spannable.toString().contains(tempsp)){	
				position = 0;
			}
		}else if(tempsp!=null && tempsp.length()>spannable.length()){	
			lastPosition = lastPosition - (tempsp.length() - spannable.length());
			tempsp = spannable.toString();
			return;
		}
		tempsp = spannable.toString();
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
//					DynamicDrawableSpan imageSpan = getDynamicImageSpan(tag);
					if (imageSpan != null){	
						spannable.setSpan(imageSpan, tagStartPosition, tagEnd,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						//spanMap.put(tagStartPosition+","+tagEnd, imageSpan);
					}
						
				}
			}
			position++;
			
		} while (position < length);
		lastPosition = position;
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


}
