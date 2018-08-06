package com.advertisement.cashcow.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.MovementMethod;
import android.util.AttributeSet;

import com.advertisement.cashcow.R;

/**
 * 作者：吴国洪 on 2018/5/31
 * 描述：短信验证码框
 */
public class VerificationCodeFrame extends AppCompatEditText {

    private int lineCount;//下划线数量

    private int focusedColor;
    private int textColor;
    private int unFocusedColor;
    private float fineTuning;//微调值，用于字体与线位置微调
    private float lightLineWidth;
    private float lineSpace;//间隙
    private float verticalLineSpace;//文字与下划线的间隙

    //============================= 画笔 ==============================/
    private Paint focusedPaint; //获取焦点画笔
    private Paint unFocusedPaint; //失去焦点画笔
    private Paint textPaint;//文字的画笔

    public VerificationCodeFrame(Context context) {
        super(context);
    }

    public VerificationCodeFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public VerificationCodeFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        //绘制下划线
        drawUnderLineAndText(canvas);


        canvas.restore();
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        //关闭 copy/paste/cut 长按文字菜单，使文字不可长按选中
        //Note: 需 setTextIsSelectable(false) 才会生效
        return null;
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrVar(context, attrs);
        initView();
        initPaint();
    }

    private void initAttrVar(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeFrame);


        focusedColor = typedArray.getColor(R.styleable.VerificationCodeFrame_focusedColor, getResources().getColor(R.color.colorPrimaryDark));
        unFocusedColor = typedArray.getColor(R.styleable.VerificationCodeFrame_unFocusedColor, getResources().getColor(R.color.colorPrimary));
        lightLineWidth = typedArray.getDimension(R.styleable.VerificationCodeFrame_lightLineWidth, 10);
        verticalLineSpace = typedArray.getDimension(R.styleable.VerificationCodeFrame_verticalLineSpace, 0);

        fineTuning = typedArray.getInt(R.styleable.VerificationCodeFrame_fineTuning, 0);

        textColor = typedArray.getColor(R.styleable.VerificationCodeFrame_textColor, getResources().getColor(R.color.colorPrimary));

        lineCount = typedArray.getInt(R.styleable.VerificationCodeFrame_lineCount, 1);


        typedArray.recycle();

    }

    private void initView() {
        setCursorVisible(false); //光标不可见
        setInputType(InputType.TYPE_CLASS_NUMBER); //设置输入的是数字
        setTextIsSelectable(false);//设置文字不可选中
        setTextColor(Color.TRANSPARENT);//设置本来文字的颜色为透明
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(lineCount)});

    }

    private void initPaint() {
        focusedPaint = new Paint();
        focusedPaint.setColor(focusedColor);
        focusedPaint.setStyle(Paint.Style.FILL);
        focusedPaint.setStrokeWidth(20);

        unFocusedPaint = new Paint();
        unFocusedPaint.setColor(unFocusedColor);
        unFocusedPaint.setStyle(Paint.Style.FILL);
        unFocusedPaint.setStrokeWidth(20);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(getTextSize());

    }

    public float getFontWidth(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }


    public float getFontHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    private void drawUnderLineAndText(Canvas canvas) {
        lineSpace = getWidth() / lineCount - lightLineWidth;

        float lineStartX, textStartX,stopX, startY;
        String currentText = getText().toString();
        float fontWidth = getFontWidth(textPaint, "我");
        float fontHeight = getFontHeight(textPaint, "我");
        for (int i = 0; i < lineCount; i++) {
            //线条
            stopX = lightLineWidth * (i + 1) + lineSpace * i + lineSpace / 2;
            lineStartX = (lineSpace + lightLineWidth) * i + lineSpace / 2;

            //文字
            if (currentText.length() - 1 >= i) {
                String StrPosition = String.valueOf(currentText.charAt(i));
                startY = (getMeasuredHeight() + fontHeight) / 2.0f;
                textStartX = lineStartX + (lightLineWidth - fontWidth) / 2;
                canvas.drawText(StrPosition, textStartX - fineTuning, startY , textPaint);
                canvas.drawLine(lineStartX, getHeight(), stopX, getHeight(), focusedPaint);
            }else {
                canvas.drawLine(lineStartX, getHeight(), stopX, getHeight(), unFocusedPaint);
            }
        }
    }
}
