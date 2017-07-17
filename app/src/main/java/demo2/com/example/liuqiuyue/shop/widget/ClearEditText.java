package demo2.com.example.liuqiuyue.shop.widget;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import demo2.com.example.liuqiuyue.shop.R;

/**
 * Created by liuqiuyue on 2017/4/30.
 * 自定义输入框
 */

public class ClearEditText extends AppCompatAutoCompleteTextView
        implements View.OnTouchListener, View.OnFocusChangeListener,TextWatcher  {
    //删除按钮
    private Drawable mClearDrawable;
    //焦点改变监听
    private OnFocusChangeListener mOnFocusChangeListener;
    //触摸监听
    private OnTouchListener mOnTouchListener;

    public ClearEditText(Context context) {
//        this(context, null);
        super(context);   init(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
        super(context, attrs);   init(context);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.icon_delete_32);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearDrawable = wrappedDrawable;
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicHeight(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);


    }

    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        this.mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    /**
     * 清楚图标是否显示
     *
     * @param b
     */
    private void setClearIconVisible(boolean b) {
        mClearDrawable.setVisible(b, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                b ? mClearDrawable : null,
                compoundDrawables[3]);
    }

    /**
     * 如果用户的手指是从 ‘x’ 所在的区域抬起来的。我们就要去清除文本。
     * 如果 ‘x’ 是不显示的或者 onTouch 事件不是在 right drawable 区域内的，我们就应该处理其他的对应的 touch 监听。
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();
        if (mClearDrawable.isVisible() && x > getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}
