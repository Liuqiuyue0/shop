package demo2.com.example.liuqiuyue.shop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import demo2.com.example.liuqiuyue.shop.R;

/**
 * Created by liuqiuyue on 2017/4/19.
 */

public class AddSubView extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;
    private Button bt_add;
    private Button bt_sub;
    private LayoutInflater mInflater;
    private View mView;

    private int value;
    private int minValue;
    private int maxValue;
    private onButtonClickListener mListener;

    public void setOnButtonClickListener(onButtonClickListener listener) {
        mListener = listener;
    }

    public AddSubView(Context context) {
        this(context, null);
    }


    public AddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);

        initView();

        //获取属性
        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.AddSubView, defStyleAttr, 0);
            final int val = a.getInt(R.styleable.AddSubView_value, 0);
            final int min_val = a.getInt(R.styleable.AddSubView_minValue, 0);
            final int max_val = a.getInt(R.styleable.AddSubView_maxValue, 0);
            setValue(val);
            setMinValue(min_val);
            setMaxValue(max_val);

            final Drawable addDrawable = a.getDrawable(R.styleable.AddSubView_btAddBackground);
            final Drawable subDrawable = a.getDrawable(R.styleable.AddSubView_btSubBackground);
            final Drawable textDrawable = a.getDrawable(R.styleable.AddSubView_textBackground);
            setAddBackground(addDrawable);
            setSubBackground(subDrawable);
            setTextBackground(textDrawable);
            a.recycle();
        }
    }

    private void initView() {
        //用this因为lineaLayout继承与ViewGroup
        mView = mInflater.inflate(R.layout.add_sub_view, this, true);
        bt_add = (Button) mView.findViewById(R.id.tb_add);
        bt_sub = (Button) mView.findViewById(R.id.bt_sub);
        mTextView = (TextView) mView.findViewById(R.id.mun_text);
        bt_add.setOnClickListener(this);
        bt_sub.setOnClickListener(this);

    }

    private void setAddBackground(Drawable icon) {
        if (bt_add != null) {
            bt_add.setBackground(icon);
        }

    }

    private void setSubBackground(Drawable icon) {
        if (bt_sub != null) {
            bt_sub.setBackground(icon);
        }

    }

    private void setTextBackground(Drawable icon) {
        if (mTextView != null) {
            mTextView.setBackground(icon);
        }

    }

    public int getValue() {
        String val = mTextView.getText().toString();
        //如果textView内不为空且不为字符，则取出值给value
        if (val != null && !"".equals(val))
            this.value = Integer.parseInt(val);
        return value;
    }

    public void setValue(int value) {

        mTextView.setText("" + value);
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tb_add) {
            add();
            if (mListener != null) {
                mListener.onAddClick(v, value);
            }
        }
        if (v.getId() == R.id.bt_sub) {
            sub();
            if (mListener != null) {
               mListener.onSubClick(v,value);
            }
        }

    }

    private void sub() {
        //如果数值大于最小值 则进行减1
        if (value > minValue)
            value = value - 1;
        mTextView.setText(value + "");
    }

    private void add() {
        //如果数值小于最大值则进行加1
        if (value < maxValue)
            value = value + 1;
        mTextView.setText(value + "");
    }

    public interface onButtonClickListener {
        void onAddClick(View view, int value);

        void onSubClick(View view, int value);
    }
}
