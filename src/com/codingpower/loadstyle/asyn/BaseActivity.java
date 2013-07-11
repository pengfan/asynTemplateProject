package com.codingpower.loadstyle.asyn;

import android.app.Activity;
import android.view.View;

public class BaseActivity extends Activity {

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

}
