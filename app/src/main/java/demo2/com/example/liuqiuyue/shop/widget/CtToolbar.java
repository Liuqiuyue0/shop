package demo2.com.example.liuqiuyue.shop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import demo2.com.example.liuqiuyue.shop.R;


/**
 * Created by Ivan on 15/9/28.
 */
public class CtToolbar extends Toolbar {


    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;


    public CtToolbar(Context context) {
        this(context, null);
    }

    public CtToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CtToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10, 10);
        ///获取属性
        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CtToolbar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.CtToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.CtToolbar_isShowSearchView, false);
            //isShowSearchView为true则显示搜索款框，隐藏title
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
            }
            a.recycle();
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {


        if (mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);


            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }

    }

    /**
     * 放置RightButtonIcon的方法
     *
     * @param icon
     */

    public void setRightButtonIcon(Drawable icon) {

        if (mRightButton != null) {
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonIcon(int icon_id) {
        setRightButtonIcon(getResources().getDrawable(icon_id));

    }


    public void setRightButtonOnClickListener(OnClickListener li) {

        mRightButton.setOnClickListener(li);
    }

    /**
     * 得到Button的方法
     */
    public Button getRightButton() {

        return this.mRightButton;
    }

    public TextView getSearchView() {

        return this.mSearchView;
    }



    /**
     * 重写父类的setTitle方法
     *
     * @param resId
     */
    @Override

    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    /**
     * 设置搜索框和标题的下显示状态方法
     */
    public void showSearchView() {

        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);

    }

    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }


//
//    private void ensureRightButtonView() {
//        if (mRightImageButton == null) {
//            mRightImageButton = new ImageButton(getContext(), null,
//                    android.support.v7.appcompat.R.attr.toolbarNavigationButtonStyle);
//            final LayoutParams lp = generateDefaultLayoutParams();
//            lp.gravity = GravityCompat.START | (Gravity.VERTICAL_GRAVITY_MASK);
//            mRightImageButton.setLayoutParams(lp);
//        }
//    }


}
