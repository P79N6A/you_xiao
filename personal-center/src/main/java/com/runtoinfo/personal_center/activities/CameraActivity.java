package com.runtoinfo.personal_center.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runto.cameraview.JCameraView;
import com.runto.cameraview.listener.ClickListener;
import com.runto.cameraview.listener.ErrorListener;
import com.runto.cameraview.listener.JCameraListener;
import com.runto.cameraview.util.DeviceUtil;
import com.runto.cameraview.util.FileUtil;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityCameraBinding;

import java.io.File;


@Route(path = "/camera/activity")
@SuppressWarnings("all")
public class CameraActivity extends BaseActivity {


    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int RESULT_PHOTO_CODE = 121;
    private static final int RESULT_VIDEO_CODE = 122;
    private static final int RESULT_ERROR_CODE = 123;
    JCameraView cameraView;
    private ActivityCameraBinding binding;
    public void beforeSetContentView() {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

    }

    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    public void initPresenter() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getLayoutId());

        checkPermission();

        initView();
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},1);
        }
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        } else {
            //Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            //Log.e(TAG_SERVICE, "checkPermission: 已经授权！");
        }
    }

    public void initView() {
        //设置视频保存路径
        try {
            cameraView = findViewById(R.id.jcameraview);
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "YouXiao";
            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
                //filePath = new File(path);
            }
            cameraView.setSaveVideoPath(path);
            cameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
            //cameraView.setTip("轻触拍照,长按录制视频");
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
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        //JCameraView监听
        cameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path = FileUtil.saveBitmap("YouXiao", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_PHOTO_CODE, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("YouXiao", firstFrame);
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
