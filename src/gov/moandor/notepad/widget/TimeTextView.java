package gov.moandor.notepad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import gov.moandor.notepad.util.Utilities;

public class TimeTextView extends TextView {
    private long mTimeMillis;
    
    public TimeTextView(Context context) {
        super(context);
    }
    
    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setTime(long timeMillis) {
        if (mTimeMillis != timeMillis) {
            mTimeMillis = timeMillis;
            setText(Utilities.getListTime(timeMillis));
        }
    }
}
