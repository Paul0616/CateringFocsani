package ro.duoline.papacatering;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Paul on 27.05.2017.
 */

public class customImageView extends android.support.v7.widget.AppCompatImageView {
    public customImageView(Context context){
        super(context);
    }

    public customImageView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public customImageView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
