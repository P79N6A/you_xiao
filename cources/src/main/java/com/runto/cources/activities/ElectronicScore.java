package com.runto.cources.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.DrmInitData;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runto.cources.R;
import com.runto.cources.databinding.ActivityElectronicScoreBinding;
import com.runto.cources.utils.FileDownloadUtils;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;

import java.io.File;

@Route(path = "/electronic/electronicScore")
public class ElectronicScore extends BaseActivity {

    public ActivityElectronicScoreBinding binding;
    public int clickCount = 0;
    public String url = "http://files.iboome.com/upload/image/201803/306365802714495205119310572.mxl";
    public ProgressDialog dialog;
    public String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/music/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initFileDownload() {
        // 1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        // 2.配置Builder
        // 配置下载文件保存的文件夹
        builder.configFileDownloadDir(filePath);
        // 配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(1);
        // 配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
        // 开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
        // 配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒
        // 3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }


    public void initProgressDialog(Context context){
        dialog = new ProgressDialog(context);
        dialog.setTitle("正在加载");
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) dialog.cancel();
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_electronic_score);
        setStatusBar();
        verifyStoragePermissions(this);
        initFileDownload();
        //initData();
        initProgressDialog(this);
    }

    @Override
    public void initData(){
        binding.courseLeaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.eleBuyCourseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.eleBuyAfterButton.setText("学习曲谱");
                binding.eleBuyAfterButton.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                binding.electronicBottomLayout.setVisibility(View.GONE);
            }
        });

        binding.eleBuyAfterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.eleBuyAfterButton.getText().equals("学习曲谱")){
                    dialog.show();
                    FileDownloadUtils.loadingFile(ElectronicScore.this, url, dialog);
                    //ARouter.getInstance().build("/electronic/playerActivity").navigation();
                }
            }
        });

        binding.eleCollectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount == 0) {
                    clickCount++;
                    binding.boutiqueEleCourseCollectionImage.setImageResource(R.drawable.boutique_course_collectioned);
                }else{
                    clickCount = 0;
                    binding.boutiqueEleCourseCollectionImage.setImageResource(R.drawable.boutique_course_collection);
                }
            }
        });
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 8      * Checks if the app has permission to write to device storage
     * 9      *
     * 10      * If the app does not has permission then the user will be prompted to
     * 11      * grant permissions
     * 12      *
     * 13      * @param activity
     * 14
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
