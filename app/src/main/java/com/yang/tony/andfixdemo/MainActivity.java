package com.yang.tony.andfixdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


/**
 * main activity
 */
public class MainActivity extends Activity {
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);

        toast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //打包1.apk后，修改toast内容，打包2.apk
    private void toast() {
        //        tv.setText("热修复前");
        //        Toast.makeText(this, "old", Toast.LENGTH_SHORT).show();

        tv.setText("热修后");
        Toast.makeText(this, "new", Toast.LENGTH_SHORT).show();
    }
}
