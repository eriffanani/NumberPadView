package com.erif.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.dynamicanimation.animation.SpringAnimation;

import com.google.android.material.card.MaterialCardView;

public class NumberPadView extends FrameLayout {

    private NumberPadListener listener;

    private final int textSize = 100;
    private float spacing = 0f;
    private int textColor = Color.BLACK;
    private int leftPaneColor = Color.BLACK;
    private int backspaceColor = Color.BLACK;
    private float padSize = 0f;

    private float padTypeCardElevation = 0f;

    public static final int LEFT_PANE_NONE = 0;
    public static final int LEFT_PANE_COMMA = 1;
    public static final int LEFT_PANE_DOTS = 2;
    public static final int LEFT_PANE_ICON = 3;
    private int leftPaneInput = LEFT_PANE_NONE;

    public static final int PAD_TYPE_NONE = 0;
    public static final int PAD_TYPE_BORDER = 1;
    public static final int PAD_TYPE_SOLID = 2;
    public static final int PAD_TYPE_CARD = 3;
    private int padType = PAD_TYPE_NONE;
    private int padTypeLeftPane = PAD_TYPE_NONE;
    private int padTypeBackspace = PAD_TYPE_NONE;

    private GridLayout gridLayout;

    public NumberPadView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public NumberPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public NumberPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public NumberPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        gridLayout = new GridLayout(context);
        gridLayout.setId(View.generateViewId());
        gridLayout.setColumnCount(3);
        gridLayout.setClipToPadding(false);
        LayoutParams paramGrid = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        paramGrid.gravity = Gravity.CENTER;
        gridLayout.setLayoutParams(paramGrid);
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.NumberPadView, defStyleAttr, 0
            );
            try {
                float padDefaultSize = dimen(R.dimen.pad_default_size);
                padSize = typedArray.getDimension(R.styleable.NumberPadView_padSize, padDefaultSize);
                spacing = typedArray.getDimension(R.styleable.NumberPadView_android_spacing, 0f);
                textColor = typedArray.getColor(R.styleable.NumberPadView_android_textColor, Color.BLACK);
                leftPaneColor = typedArray.getColor(R.styleable.NumberPadView_leftPaneColor, Color.BLACK);
                backspaceColor = typedArray.getColor(R.styleable.NumberPadView_backspaceColor, Color.BLACK);
                leftPaneInput = typedArray.getInteger(R.styleable.NumberPadView_leftPaneInput, LEFT_PANE_NONE);
                padType = typedArray.getInteger(R.styleable.NumberPadView_padType, PAD_TYPE_NONE);
                padTypeLeftPane = typedArray.getInteger(R.styleable.NumberPadView_padTypeLeftPane, PAD_TYPE_NONE);
                padTypeBackspace = typedArray.getInteger(R.styleable.NumberPadView_padTypeBackspace, PAD_TYPE_NONE);
                float padCardDefaultElevation = dimen(R.dimen.pad_default_card_elevation);
                padTypeCardElevation = typedArray.getDimension(R.styleable.NumberPadView_padTypeCardElevation, padCardDefaultElevation);
            } finally {
                typedArray.recycle();
            }
        }
        gridLayout.addView(createNumber("1", 0));
        gridLayout.addView(createNumber("2", 25));
        gridLayout.addView(createNumber("3", 50));
        gridLayout.addView(createNumber("4", 75));
        gridLayout.addView(createNumber("5", 100));
        gridLayout.addView(createNumber("6", 125));
        gridLayout.addView(createNumber("7", 150));
        gridLayout.addView(createNumber("8", 175));
        gridLayout.addView(createNumber("9", 200));
        int delayLeftPane = 225;
        if (leftPaneInput == LEFT_PANE_ICON) {
            int iconLeft = R.drawable.number_pad_ic_fingerprint;
            View leftPane = createImage(
                    iconLeft, leftPaneColor, delayLeftPane, false
            );
            gridLayout.addView(leftPane);
        } else if (leftPaneInput == LEFT_PANE_COMMA) {
            gridLayout.addView(createNumber(",", delayLeftPane));
        } else if (leftPaneInput == LEFT_PANE_DOTS) {
            gridLayout.addView(createNumber(".", delayLeftPane, true));
        } else {
            gridLayout.addView(emptyView());
        }
        gridLayout.addView(createNumber("0", 250));
        int iconBackSpace = R.drawable.number_pad_ic_backspace;
        View backSpace = createImage(
                iconBackSpace, backspaceColor, 275, true
        );
        gridLayout.addView(backSpace);
        addView(gridLayout);
    }

    private View createNumber(String number, int delay) {
        return createNumber(number, delay, false);
    }

    private View createNumber(String number, int delay, boolean isLeftPane) {
        FrameLayout parent = createParent();
        if (isLeftPane)
            parent.setBackground(background(padTypeLeftPane));
        parent.setOnClickListener(v -> {
            if (listener != null)
                listener.onClickNumber(number);
        });

        // Text Number
        TextView textView = createText(number);
        parent.addView(textView);

        animateView(parent, delay);

        return parent;
    }

    private View createImage(int imageId, int color, int delay, boolean isBackspace) {
        FrameLayout parent = createParent();
        if (isBackspace) {
            parent.setBackground(background(padTypeBackspace));
        } else {
            parent.setBackground(background(padTypeLeftPane));
        }
        parent.setOnClickListener(v -> {
            if (listener != null) {
                if (isBackspace) {
                    listener.onClickBackSpace();
                } else {
                    listener.onClickLeftPane();
                }
            }
        });

        // Icon
        ImageView imageView = createImage(imageId, color);
        parent.addView(imageView);

        animateView(parent, delay);

        if (isBackspace) {
            if (isCardPad() && padTypeCardElevation > 0) {
                int paddingStart = (int) padTypeCardElevation;
                int paddingTop = (int) (padTypeCardElevation / 1.5f);
                int paddingEnd = (int) padTypeCardElevation;
                int paddingBottom = (int) (padTypeCardElevation * 2);
                gridLayout.setPadding(
                        paddingStart, paddingTop,
                        paddingEnd, paddingBottom
                );
            }
        }

        return parent;
    }

    public void addListener(NumberPadListener listener) {
        this.listener = listener;
    }

    private Drawable drawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    private Drawable background(int type) {
        Drawable drawable;
        if (type == PAD_TYPE_BORDER) {
            drawable = drawable(R.drawable.number_pad_ripple_border);
        } else if (type == PAD_TYPE_SOLID) {
            drawable = drawable(R.drawable.number_pad_ripple_solid);
        } else {
            drawable = drawable(R.drawable.number_pad_ripple);
        }
        return drawable;
    }

    private FrameLayout createParent() {
        int size = (int) padSize;
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.width = size;
        param.height = size;
        FrameLayout parent = new FrameLayout(getContext());
        parent.setId(View.generateViewId());
        parent.setBackground(background(padType));
        CardView cardView = null;
        if (isCardPad()) {
            cardView = new CardView(getContext());
            cardView.setId(View.generateViewId());
            cardView.setCardElevation(padTypeCardElevation);
            cardView.setRadius(size / 2f);
        }
        int gridItemCount = gridLayout.getChildCount();
        int index = gridItemCount + 1;
        if (index % 3 != 0)
            param.rightMargin = (int) spacing;
        if (index > 3)
            param.topMargin = (int) spacing;
        if (isCardPad()) {
            if (cardView != null)
                cardView.setLayoutParams(param);
        } else {
            parent.setLayoutParams(param);
        }
        return isCardPad() ? cardView : parent;
    }

    private TextView createText(String number) {
        TextView textView = new TextView(getContext());
        textView.setId(View.generateViewId());
        LayoutParams paramText = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        paramText.gravity = Gravity.CENTER;
        textView.setIncludeFontPadding(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setLayoutParams(paramText);
        textView.setTextColor(textColor);
        textView.setText(number);
        return textView;
    }

    private ImageView createImage(int imageId, int color) {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(View.generateViewId());
        float iconSize =  0.8f * textSize;
        int finalIconSize = (int) iconSize;
        FrameLayout.LayoutParams paramImage = new FrameLayout.LayoutParams(
                finalIconSize, finalIconSize
        );
        paramImage.gravity = Gravity.CENTER;
        imageView.setLayoutParams(paramImage);
        imageView.setImageResource(imageId);
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
        return imageView;
    }

    private View emptyView() {
        View view = new View(getContext());
        int size = (int) padSize;
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.width = size;
        param.height = size;
        view.setLayoutParams(param);
        return view;
    }

    private boolean isCardPad() {
        return padType == PAD_TYPE_CARD;
    }

    private void animateView(View view, int delay) {
        /*view.setAlpha(0.1f);
        view.setTranslationY(50f);

        view.post(() -> view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(250L)
                .setStartDelay(delay)
                .start()
        );*/

        /*view.setAlpha(0f);
        view.setScaleX(0.7f);
        view.setScaleY(0.7f);

        view.post(() -> {
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(300L)
                    .setStartDelay(delay)
                    .start();
        });*/

    }

    private float dimen(int id) {
        return getContext().getResources().getDimension(id);
    }

}
