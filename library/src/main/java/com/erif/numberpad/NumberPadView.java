package com.erif.numberpad;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;

public class NumberPadView extends FrameLayout {

    private NumberPadListener listener;

    private float textSize = 100f;
    private float spacing = 0f;
    private int textColor = Color.BLACK;
    private int leftPaneIconColor = Color.BLACK;
    private int backspaceIconColor = Color.BLACK;
    private float padSize = 0f;

    private float padTypeCardElevation = 0f;

    private int padBackgroundColor = Color.WHITE;
    private int leftPaneBackgroundColor = Color.WHITE;
    private int backspaceBackgroundColor = Color.WHITE;

    private boolean boldText = false;

    private float strokeWidth = 0f;

    private int fontResource = 0;

    private int leftPaneIcon = 0;
    private float leftPaneIconSize = 0f;
    private int backSpaceIcon = 0;
    private float backSpaceIconSize = 0f;

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

    private static final int NUMBER_PAD = 0;
    private static final int LEFT_PAD = 1;
    private static final int BACKSPACE_PAD = 2;

    private static final int ANIMATION_NONE = 0;
    private static final int ANIMATION_SCALE_IN = 1;
    private static final int ANIMATION_SCALE_OUT = 2;
    private static final int ANIMATION_SLIDE_UP = 3;
    private static final int ANIMATION_SLIDE_DOWN = 4;
    private static final int ANIMATION_SLIDE_LEFT = 5;
    private static final int ANIMATION_SLIDE_RIGHT = 6;

    private int animation = ANIMATION_NONE;

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
        setClipToPadding(false);
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
                    attrs, R.styleable.NumberPadView, defStyleAttr, defStyleRes
            );
            try {
                float padDefaultSize = dimen(R.dimen.pad_default_size);
                textSize = typedArray.getDimension(R.styleable.NumberPadView_android_textSize, 100f);
                boldText = typedArray.getBoolean(R.styleable.NumberPadView_boldText, false);
                padSize = typedArray.getDimension(R.styleable.NumberPadView_padSize, padDefaultSize);
                spacing = typedArray.getDimension(R.styleable.NumberPadView_android_spacing, 0f);
                textColor = typedArray.getColor(R.styleable.NumberPadView_android_textColor, Color.BLACK);
                leftPaneIconColor = typedArray.getColor(R.styleable.NumberPadView_leftPaneIconColor, Color.BLACK);
                backspaceIconColor = typedArray.getColor(R.styleable.NumberPadView_backspaceIconColor, Color.BLACK);
                leftPaneInput = typedArray.getInteger(R.styleable.NumberPadView_leftPaneInput, LEFT_PANE_NONE);
                padType = typedArray.getInteger(R.styleable.NumberPadView_padType, PAD_TYPE_NONE);
                padTypeLeftPane = typedArray.getInteger(R.styleable.NumberPadView_padTypeLeftPane, PAD_TYPE_NONE);
                padTypeBackspace = typedArray.getInteger(R.styleable.NumberPadView_padTypeBackspace, PAD_TYPE_NONE);
                float padCardDefaultElevation = dimen(R.dimen.pad_default_card_elevation);
                padTypeCardElevation = typedArray.getDimension(R.styleable.NumberPadView_padTypeCardElevation, padCardDefaultElevation);
                padBackgroundColor = typedArray.getColor(R.styleable.NumberPadView_padBackgroundColor, Color.WHITE);
                leftPaneBackgroundColor = typedArray.getColor(R.styleable.NumberPadView_leftPaneBackgroundColor, Color.WHITE);
                backspaceBackgroundColor = typedArray.getColor(R.styleable.NumberPadView_backspaceBackgroundColor, Color.WHITE);
                fontResource = typedArray.getResourceId(R.styleable.NumberPadView_fontFamily, 0);
                float defaultStrokeWidth = dimen(R.dimen.pad_default_stroke_width);
                strokeWidth = typedArray.getDimension(R.styleable.NumberPadView_strokeWidth, defaultStrokeWidth);
                float defaultIconSize = dimen(R.dimen.pad_default_icon_size);
                leftPaneIcon = typedArray.getResourceId(R.styleable.NumberPadView_leftPaneIcon, 0);
                leftPaneIconSize = typedArray.getDimension(R.styleable.NumberPadView_leftPaneIconSize, defaultIconSize);
                backSpaceIcon = typedArray.getResourceId(R.styleable.NumberPadView_backspaceIcon, 0);
                backSpaceIconSize = typedArray.getDimension(R.styleable.NumberPadView_backspaceIconSize, defaultIconSize);
                animation = typedArray.getInteger(R.styleable.NumberPadView_animateOnShow, ANIMATION_NONE);
            } catch (Exception e){
                typedArray.close();
            }finally {
                typedArray.recycle();
            }
        }
        gridLayout.addView(createNumber("1", isSlideRight() ? 50 : isSlideDown() ? 225 : 0));
        gridLayout.addView(createNumber("2", isSlideDown() ? 250 : 25));
        gridLayout.addView(createNumber("3", isSlideRight() ? 0 : isSlideDown() ? 275 : 50));
        gridLayout.addView(createNumber("4", isSlideRight() ? 125 : isSlideDown() ? 150 : 75));
        gridLayout.addView(createNumber("5", isSlideDown() ? 175 : 100));
        gridLayout.addView(createNumber("6", isSlideRight() ? 75 : isSlideDown() ? 200 : 125));
        gridLayout.addView(createNumber("7", isSlideRight() ? 200 : isSlideDown() ? 75 : 150));
        gridLayout.addView(createNumber("8", isSlideDown() ? 100 : 175));
        gridLayout.addView(createNumber("9", isSlideRight() ? 175 : isSlideDown() ? 125: 200));
        int delayLeftPane = isSlideRight() ? 275 : isSlideDown() ? 0 : 225;
        if (leftPaneInput == LEFT_PANE_ICON) {
            Drawable icon = getIcon(leftPaneIcon, R.drawable.number_pad_ic_fingerprint);
            View leftPane = createImage(
                    icon, leftPaneIconColor, delayLeftPane, false
            );
            gridLayout.addView(leftPane);
        } else if (leftPaneInput == LEFT_PANE_COMMA) {
            gridLayout.addView(createNumber(",", delayLeftPane));
        } else if (leftPaneInput == LEFT_PANE_DOTS) {
            gridLayout.addView(createNumber(".", delayLeftPane));
        } else {
            gridLayout.addView(emptyView());
        }
        gridLayout.addView(createNumber("0", isSlideDown() ? 25 : 250));
        Drawable iconBackSpace = getIcon(backSpaceIcon, R.drawable.number_pad_ic_backspace);
        View backSpace = createImage(
                iconBackSpace, backspaceIconColor, isSlideRight() ? 225 : isSlideDown() ? 50 : 275, true
        );
        gridLayout.addView(backSpace);
        //gridLayout.addView(emptyView());
        addView(gridLayout);
    }

    private View createNumber(String number, int delay) {
        CardView parent = createParent();
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

    private View createImage(Drawable icon, int color, int delay, boolean isBackspace) {
        CardView parent = createParent(isBackspace ? BACKSPACE_PAD : LEFT_PAD);
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
        ImageView imageView = createImage(icon, color, isBackspace);
        parent.addView(imageView);

        animateView(parent, delay);

        if (isBackspace) {
            boolean isCardPad = isCardPad(padType) || isCardPad(padTypeLeftPane) || isCardPad(padTypeBackspace);
            if (isCardPad && padTypeCardElevation > 0) {
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

    private CardView createParent() {
        return createParent(NUMBER_PAD);
    }

    private CardView createParent(int pad) {
        CardView card;
        if (pad == LEFT_PAD) {
            card = createCard(pad, padTypeLeftPane);
        } else if (pad == BACKSPACE_PAD) {
            card = createCard(pad, padTypeBackspace);
        } else {
            card = createCard(pad, padType);
        }
        return card;
    }

    private GridLayout.LayoutParams paramCard() {
        int size = (int) padSize;
        GridLayout.LayoutParams param = new GridLayout.LayoutParams();
        param.width = size;
        param.height = size;
        int gridItemCount = gridLayout.getChildCount();
        int index = gridItemCount + 1;
        if (index % 3 != 0)
            param.rightMargin = (int) spacing;
        if (index > 3)
            param.topMargin = (int) spacing;
        return param;
    }

    private CardView createCard(int pad, int type) {
        // Card Shape
        int size = (int) padSize;
        CardView cardView = new CardView(getContext());
        cardView.setId(View.generateViewId());
        cardView.setCardElevation(isCardPad(type) ? padTypeCardElevation : 0f);
        cardView.setRadius(size / 2f);
        // Card Color
        int transparent = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cardBackground = isCardOrSolid(type) ? bgColor(pad) : transparent;
        cardView.setCardBackgroundColor(cardBackground);
        // Ripple
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        Drawable ripple = drawable(outValue.resourceId);
        cardView.setForeground(ripple);
        // If Bordered
        boolean isBordered = type == PAD_TYPE_BORDER;
        if (isBordered) {
            View borderView = new View(getContext());
            int borderSize = (int) padSize;
            LayoutParams paramBorder = new LayoutParams(borderSize, borderSize);
            borderView.setLayoutParams(paramBorder);
            GradientDrawable borderDrawable = (GradientDrawable) drawable(R.drawable.number_pad_border).mutate();
            borderDrawable.setStroke((int) strokeWidth, bgColor(pad));
            borderView.setBackground(borderDrawable);
            cardView.addView(borderView);
        }
        cardView.setLayoutParams(paramCard());
        return cardView;
    }

    private TextView createText(String number) {
        TextView textView = new TextView(getContext());
        textView.setId(View.generateViewId());
        LayoutParams paramText = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        boolean isDivider = number.equals(".") || number.equals(",");
        if (isDivider)
            paramText.bottomMargin = (int) (padSize / 7f);
        paramText.gravity = Gravity.CENTER;
        textView.setIncludeFontPadding(false);
        float size = isDivider ? (textSize * 1.5f) : textSize;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        Typeface typeface = fontResource != 0 ? getFont(fontResource) : Typeface.DEFAULT;
        textView.setTypeface(typeface, boldText ? Typeface.BOLD : Typeface.NORMAL);
        textView.setLayoutParams(paramText);
        textView.setTextColor(textColor);
        textView.setText(number);
        return textView;
    }

    private ImageView createImage(Drawable icon, int color, boolean isBackspace) {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(View.generateViewId());
        int finalIconSize = isBackspace ? (int) backSpaceIconSize : (int) leftPaneIconSize;
        LayoutParams paramImage = new LayoutParams(
                finalIconSize, finalIconSize
        );
        paramImage.gravity = Gravity.CENTER;
        imageView.setLayoutParams(paramImage);
        imageView.setImageDrawable(icon);
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

    private boolean isCardPad(int type) {
        return type == PAD_TYPE_CARD;
    }

    private boolean isCardOrSolid(int type) {
        return type == PAD_TYPE_CARD || type == PAD_TYPE_SOLID;
    }

    private int bgColor(int pad) {
        if (pad == LEFT_PAD) {
            return leftPaneBackgroundColor;
        } else if (pad == BACKSPACE_PAD) {
            return backspaceBackgroundColor;
        } else {
            return padBackgroundColor;
        }
    }

    private void animateView(View view, int delay) {
        if (animation != ANIMATION_NONE) {
            if (isAnimationSlideVertical()) {
                animSlideVertical(view, delay, animation == ANIMATION_SLIDE_UP);
            } else if (isAnimationSlideHorizontal()) {
                animSlideHorizontal(view, delay, animation == ANIMATION_SLIDE_LEFT);
            } else if (isAnimationScale()) {
                animScale(view, delay, animation == ANIMATION_SCALE_IN);
            }
        }
    }

    private boolean isAnimationSlideVertical() {
        return animation == ANIMATION_SLIDE_UP || animation == ANIMATION_SLIDE_DOWN;
    }

    private void animSlideVertical(View view, int delay, boolean up) {
        view.setAlpha(0.1f);
        view.setTranslationY(up ? 150f : -100f);
        view.post(() -> {
            view.animate()
                    .translationY(0f)
                    .setDuration(300L)
                    .setStartDelay(delay)
                    .start();
            view.animate()
                    .alpha(1f)
                    .setDuration(300L)
                    .setStartDelay(delay+200)
                    .start();
        });
    }

    private boolean isAnimationSlideHorizontal() {
        return animation == ANIMATION_SLIDE_LEFT || animation == ANIMATION_SLIDE_RIGHT;
    }

    private void animSlideHorizontal(View view, int delay, boolean left) {
        view.setAlpha(0.1f);
        view.setTranslationX(left ? 150f : -150f);
        view.post(() -> {
            view.animate()
                    .translationX(0f)
                    .setDuration(300L)
                    .setStartDelay(delay)
                    .start();
            view.animate()
                    .alpha(1f)
                    .setDuration(300L)
                    .setStartDelay(delay+50)
                    .start();
        });
    }

    private boolean isAnimationScale() {
        return animation == ANIMATION_SCALE_IN || animation == ANIMATION_SCALE_OUT;
    }

    private void animScale(View view, int delay, boolean in) {
        view.setAlpha(0.1f);
        view.setScaleX(in ? 1.2f : 0.5f);
        view.setScaleY(in ? 1.2f : 0.5f);

        view.post(() -> view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(in ? 300L : 200L)
                .setStartDelay(delay)
                .start());
    }

    private boolean isSlideRight() {
        return animation == ANIMATION_SLIDE_RIGHT;
    }

    private boolean isSlideDown() {
        return animation == ANIMATION_SLIDE_DOWN;
    }

    private float dimen(int id) {
        return getContext().getResources().getDimension(id);
    }

    private Drawable drawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    private Typeface getFont(int fontRes) {
        Typeface typeface = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                typeface = getContext().getResources().getFont(fontRes);
            else
                typeface = ResourcesCompat.getFont(getContext(), fontRes);
        } catch (Resources.NotFoundException e) {
            Log.d("NumberPadView", "Font resource not found");
        }
        return typeface;
    }

    private Drawable getIcon(int id, int defaultId) {
        Drawable icon = null;
        try {
            icon = ContextCompat.getDrawable(getContext(), id);
        } catch (Resources.NotFoundException e) {
            Log.d("NumberPadView", "Icon resource not found");
        }
        if (icon == null)
            icon = ContextCompat.getDrawable(getContext(), defaultId);
        return icon;
    }

}
