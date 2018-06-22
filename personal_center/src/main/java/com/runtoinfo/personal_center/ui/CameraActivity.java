package com.runtoinfo.personal_center.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.runto.cameraview.JCameraView;
import com.runto.cameraview.listener.ClickListener;
import com.runto.cameraview.listener.ErrorListener;
import com.runto.cameraview.listener.JCameraListener;
import com.runto.cameraview.util.DeviceUtil;
import com.runto.cameraview.util.FileUtil;
import com.runto.curry.yulinproject.R;
import com.runto.curry.yulinproject.base.BaseActivity;

import java.io.File;

import butterknife.BindView;

public class CameraActivity extends BaseActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int RESULT_PHOTO_CODE = 121;
    private static final int RESULT_VIDEO_CODE = 122;
    private static final int RESULT_ERROR_CODE = 123;
    JCameraView cameraView;
    @Override
    public void beforeSetContentView() {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //设置视频保存路径
        cameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "YuLin");
        cameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        cameraView.setTip("轻触拍照,长按录制视频");
        cameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        cameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.e(TAG, "camera error");
                Intent intent = new Intent();
                setResult(RESULT_ERROR_CODE, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "请检查是否允许录音权限", Toast.LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        cameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap("YuLin", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_PHOTO_CODE, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("YuLin", firstFrame);
                Log.e(TAG, "url = " + url + ", Bitmap = " + path);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                intent.putExtra("videoPath",url);
                setResult(RESULT_VIDEO_CODE, intent);
                finish();
            }
        });

        cameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        cameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(CameraActivity.this,"Right", Toast.LENGTH_SHORT).show();
            }
        });

        Log.e(TAG, DeviceUtil.getDeviceModel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }
}
