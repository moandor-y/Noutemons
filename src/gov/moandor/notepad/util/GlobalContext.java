package gov.moandor.notepad.util;

import android.app.Application;

import gov.moandor.notepad.activity.AbsActivity;

public class GlobalContext extends Application {
    private static GlobalContext sInstance;
    private AbsActivity mActivity;
    
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
    
    public AbsActivity getActivity() {
        return mActivity;
    }
    
    public void setActivity(AbsActivity activity) {
        mActivity = activity;
    }
    
    public static GlobalContext getInstance() {
        return sInstance;
    }
}
