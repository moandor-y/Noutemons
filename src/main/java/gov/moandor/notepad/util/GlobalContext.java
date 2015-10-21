package gov.moandor.notepad.util;

import android.app.Application;

import gov.moandor.notepad.activity.AbsActivity;

public class GlobalContext extends Application {
    private static GlobalContext sInstance;
    
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static GlobalContext getInstance() {
        return sInstance;
    }
}
