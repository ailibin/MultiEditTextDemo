package com.aiitec.multiedittextdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.aiitec.multiedittextdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ailibin
 * @Time: 2019/3/20
 * @Description: 这是一个多个EditText输入验证码的控件，比如输入6为验证码或者密码
 * @Email: ailibin@qq.com
 */
public class MultiInputEditText extends LinearLayout {


    private Paint mTextPaint;

    private int etNumber;

    private List<SecurityEditText> editTextList = new ArrayList<>();

    private int listSize;

    private StringBuilder sb = new StringBuilder();

    private float editTextWidth;
    private float minHeight;
    private float margin;
    private float textSize;
    private int textColor;
    private int inputType;
    private int gravity;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.length() != 1) {
                return;
            }

            //只有一个字符
            for (int k = 0; i < listSize; i++) {
                SecurityEditText editText = editTextList.get(k);
                sb.append(editText.getText().toString().trim());
                if (editText.isFocusable()) {
                    if (i < listSize - 1) {
                        SecurityEditText editText1 = editTextList.get(k + 1);
                        editText1.setFocusable(true);
                        editText1.setFocusableInTouchMode(true);
                    }
                }
                if (k == listSize - 1) {
                    //回调处理
                    if (editText.isFocusable()) {
                        if (onInputCompleteLister != null) {
                            onInputCompleteLister.onComplete(sb.toString());
                        }
                    }
                }
            }


        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() != 1) {
                return;
            }
            for (int k = 0; k < listSize; k++) {
                SecurityEditText editText = editTextList.get(k);
                if (editText.isFocused()) {
                    //上一个失去焦点
                    editText.setFocusable(false);
                    //下一个获得焦点
                    if (k < listSize - 1) {
                        SecurityEditText editText1 = editTextList.get(k + 1);
                        editText1.requestFocus();
                    }
                }
                if (k == listSize - 1) {
                    //to do something
                }
            }
        }
    };


    public MultiInputEditText(Context context) {
        super(context);
    }

    public MultiInputEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiInputEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiInputEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiInputEditText);
        etNumber = typedArray.getInteger(R.styleable.MultiInputEditText_editText_number, 6);
        editTextWidth = typedArray.getDimension(R.styleable.MultiInputEditText_single_editText_width, dp2px(context, 40));
        minHeight = typedArray.getDimension(R.styleable.MultiInputEditText_single_editText_minHeight, dp2px(context, 32));
        margin = typedArray.getDimension(R.styleable.MultiInputEditText_multiEditText_margin, dp2px(context, 38));
        inputType = typedArray.getInt(R.styleable.MultiInputEditText_editText_inputType, 0);
        gravity = typedArray.getInt(R.styleable.MultiInputEditText_editText_gravity, 0);
        textSize = typedArray.getDimension(R.styleable.MultiInputEditText_editText_size, 13f);
        textColor = typedArray.getColor(R.styleable.MultiInputEditText_editText_color, Color.GRAY);

        LinearLayout.LayoutParams vLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vLp.gravity = Gravity.CENTER_VERTICAL;
        vLp.setMargins((int) margin, 0, (int) margin, 0);
        setLayoutParams(vLp);
        setOrientation(LinearLayout.HORIZONTAL);

        editTextList.clear();
        SecurityEditText editText;
        View view;
        LinearLayout.LayoutParams editTextLp = new LinearLayout.LayoutParams((int) editTextWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        for (int i = 0; i < etNumber; i++) {

            view = new View(context);
            view.setLayoutParams(viewLp);

            editText = new SecurityEditText(context);
            editText.setLayoutParams(editTextLp);
            editText.setMaxEms(1);
            editText.setMaxLines(1);
            editText.setMinHeight((int) minHeight);
            editText.setTextSize(textSize);
            editText.setTextColor(textColor);
            switch (gravity) {
                case 0:
                    editText.setGravity(Gravity.CENTER);
                    break;
                case 1:
                    editText.setGravity(Gravity.LEFT);
                    break;
                case 2:
                    editText.setGravity(Gravity.RIGHT);
                    break;
                default:
                    break;
            }
            if (inputType == 0) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            editTextList.add(editText);
            addView(editText);
            if (i != etNumber - 1) {
                addView(view);
            }

        }

        listSize = editTextList.size();
        init();
//        invalidate();
//        postInvalidate();
//        requestLayout();
        typedArray.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init() {

        //去掉空格,第一个EditText获取焦点
        for (int i = 0; i < listSize; i++) {
            SecurityEditText editText = editTextList.get(i);
            editText.getText().toString().replace(" ", "");
            if (i == 0) {
                editText.setFocusable(true);
            } else {
                editText.setFocusable(false);
            }
            editText.addTextChangedListener(mTextWatcher);
        }

    }


    private boolean b = true;

    /**
     * 按下返回键处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            //按下了删除按钮,最后一个按下回删键
            SecurityEditText lastEditText = editTextList.get(listSize - 1);
            SecurityEditText last2EditText = editTextList.get(listSize - 2);
            if (lastEditText.isFocused()) {
                if (lastEditText.getText().toString() == "") {
                    lastEditText.getText().clear();
                    lastEditText.requestFocus();
                    b = false;
                } else if (!b) {
                    lastEditText.clearFocus();
                    lastEditText.setFocusable(false);
                    last2EditText.setFocusableInTouchMode(true);
                    last2EditText.getText().clear();
                    last2EditText.requestFocus();
                    b = true;
                } else {
                    lastEditText.getText().clear();
                    lastEditText.requestFocus();
                    b = false;
                }
            } else {
                for (int i = listSize - 2; i >= 1; i--) {
                    SecurityEditText editText = editTextList.get(i);
                    SecurityEditText frontEditText = editTextList.get(i - 1);
                    if (editText.isFocused()) {
                        editText.clearFocus();
                        editText.setFocusable(false);
                        frontEditText.setFocusableInTouchMode(true);
                        frontEditText.getText().clear();
                        frontEditText.requestFocus();
                    }
                }
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onKeyBackPressLister != null) {
                onKeyBackPressLister.onPressBack();
            }
        }
        return false;
    }


    public OnInputCompleteLister onInputCompleteLister;

    public void setOnInputCompleteLister(OnInputCompleteLister onInputCompleteLister) {
        this.onInputCompleteLister = onInputCompleteLister;
    }

    /**
     * 输入完成结果回调
     */
    public interface OnInputCompleteLister {

        void onComplete(String result);

    }

    public OnKeyBackPressLister onKeyBackPressLister;

    public void setOnKeyBackPressLister(OnKeyBackPressLister onKeyBackPressLister) {
        this.onKeyBackPressLister = onKeyBackPressLister;
    }

    /**
     * 按返回键
     */
    public interface OnKeyBackPressLister {

        void onPressBack();

    }


    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


}
