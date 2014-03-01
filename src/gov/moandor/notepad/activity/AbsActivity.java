package gov.moandor.notepad.activity;

import android.app.Activity;
import android.os.Bundle;

import gov.moandor.notepad.util.GlobalContext;

public abstract class AbsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalContext.getInstance().setActivity(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        GlobalContext.getInstance().setActivity(this);
    }
}
