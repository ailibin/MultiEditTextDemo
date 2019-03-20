package com.aiitec.multiedittextdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
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

    private int etNumber;
    private List<SecurityEditText> editTextList = new ArrayList<>();
    private int listSize;
    private StringBuilder sb = new StringBuilder();

    public static final String TAG = "ailibin";

    private float editTextWidth;
    private float minHeight;
    private float margin;
    private float textSize;
    private int textColor;
    private int inputType;
    private int gravity;
    private int currentPosition1 = 0;
    private int currentPosition2 = 0;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.length() != 1) {
                return;
            }

//            Log.e(TAG, "onTextChanged--currentPosition: " + currentPosition1);
            SecurityEditText editText = editTextList.get(currentPosition1);
            sb.append(editText.getText().toString().trim());
            if (currentPosition1 < listSize - 1) {
                SecurityEditText editText1 = editTextList.get(currentPosition1 + 1);
                editText1.setFocusable(true);
                editText1.setFocusableInTouchMode(true);
                currentPosition1++;
            } else {
                //回调处理
                if (editText.isFocusable()) {
                    if (onInputCompleteLister != null) {
                        onInputCompleteLister.onComplete(sb.toString());
                    }
                }
            }
//            Log.e(TAG, "onTextChanged--currentPosition--after: " + currentPosition1);

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() != 1) {
                return;
            }
//            Log.e(TAG, "afterTextChanged--currentPosition: " + currentPosition2);
            EditText editText = editTextList.get(currentPosition2);
            if (editText.isFocused()) {
                if (currentPosition2 < listSize - 1) {
                    //上一个失去焦点, 下一个获得焦点
                    editText.setFocusable(false);
                    EditText editText1 = editTextList.get(currentPosition2 + 1);
                    editText1.requestFocus();
                    currentPosition2++;
                } else {
                    //to do something
                }
            }
//            Log.e(TAG, "afterTextChanged--currentPosition--after: " + currentPosition2);

        }
    };


    public MultiInputEditText(Context context) {
        this(context, null);
    }

    public MultiInputEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiInputEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
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

        editTextList.clear();
        SecurityEditText editText;
        View view;
        LinearLayout.LayoutParams editTextLp = new LinearLayout.LayoutParams((int) editTextWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        for (int i = 0; i < etNumber; i++) {

            //占位view
            view = new View(context);
            view.setLayoutParams(viewLp);

            editText = new SecurityEditText(context);
            editText.setLayoutParams(editTextLp);
            editText.setMaxLines(1);
            //设置最大只能输入一个字符
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setMinHeight((int) minHeight);
            //如果直接设置字体要比原生大,先获取画笔,然后再设置就正常了(editText.setTextSize(textSize));
            editText.getPaint().setTextSize(textSize);
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
        if (listSize <= 2) {
            throw new RuntimeException("multiInputEditText size is must be > 2 ");
        }
        init();
        typedArray.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        Log.e(TAG, "width: " + width + "  height: " + height);

        // get calculate mode of width and height
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // get recommend width and height
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // wrap_content
        if (modeWidth == MeasureSpec.AT_MOST) {
            sizeWidth = Math.min(100, sizeWidth);
            modeWidth = MeasureSpec.EXACTLY;
        }

        // wrap_content
        if (modeHeight == MeasureSpec.AT_MOST) {
            sizeHeight = Math.min(dp2px(getContext(), 40), sizeHeight);
            modeHeight = MeasureSpec.EXACTLY;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {

        //去掉空格,第一个EditText获取焦点
        for (int i = 0; i < listSize; i++) {
            EditText editText = editTextList.get(i);
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

    public boolean onKeyDownInView(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            Log.e(TAG, "onKeyDown");
            //按下了删除按钮,最后一个按下回删键listSize必须要大于2以上
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
                    currentPosition1--;
                    currentPosition2--;
                    b = true;
                } else {
                    lastEditText.getText().clear();
                    lastEditText.requestFocus();
                    b = false;
                }
            } else {
                //listSize-2
                if (currentPosition1 >= 1) {
                    SecurityEditText editText = editTextList.get(currentPosition1);
                    SecurityEditText frontEditText = editTextList.get(currentPosition1 - 1);
                    if (editText.isFocused()) {
                        editText.clearFocus();
                        editText.setFocusable(false);
                        frontEditText.setFocusableInTouchMode(true);
                        frontEditText.getText().clear();
                        frontEditText.requestFocus();
                        currentPosition1--;
                        currentPosition2--;
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
