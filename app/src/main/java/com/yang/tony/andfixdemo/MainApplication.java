package com.yang.tony.andfixdemo;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.IOException;

/**
 * sample application
 *
 * @author sanping.li@alipay.com
 */
public class MainApplication extends Application {
    private static final String TAG = "euler";
    private static final String APATCH_PATH = "/out.apatch";
    private static final String DIR = "apatch";//补丁文件夹
    /**
     * patch manager
     */
    private PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize
        mPatchManager = new PatchManager(this);
        mPatchManager.init("1.0");
        Log.d(TAG, "inited.");

        // load patch
        mPatchManager.loadPatch();
        //        Log.d(TAG, "apatch loaded.");

        // add patch at runtime
        try {
            // .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + APATCH_PATH;
            mPatchManager.addPatch(patchFileString);
            Log.d(TAG, "apatch:" + patchFileString + " added.");

            //这里我加了个方法，复制加载补丁成功后，删除sdcard的补丁，避免每次进入程序都重新加载一次
//            File f = new File(this.getFilesDir(), DIR + APATCH_PATH);
//            if (f.exists()) {
//                boolean result = new File(patchFileString).delete();
//                if (!result)
//                    Log.e(TAG, patchFileString + " delete fail");
//            }
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }

    }
