package myapplication;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.Log;

import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import static myapplication.MyApplication.spUtils;

/**
 * Created by QiaoJunChao on 2018/9/28.
 */

@SuppressWarnings("all")
public class SopHixApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //如果需要使用MultiDex，需要在此处调用。
        //MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "1.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("24881419-1", "5230e8b220d1da5cd21feba70819262b", "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDc0xJgxgfEHR4oLjY+TNrDjSwTOFvaXww98RW23OWEIrev9Sz4AhhShJAEQ9Y5c4/iFFMp6CzJuNi72IF534GzBfxH0H09fQi58vkFaF8W0qq6D/sMqSekvlRLmt2MRRXkSzHnNinPjub4LzH1yfJjf1ABHCSPwxUDbjv1FoVnAeJuYJHq96Qu0G7M4sjDN8VPJSjKNxiurx8WO0e2BDXoTPcy6J6F05XtI6nFHkB4U6uuWQYo6R6p1aXJWnCLE1xVdCz+PPv7j4JW1X5AvEx9dxbXGWGXvNLD+FtbwhQxvZ/ab0AuQ/293IWsgSMOgqTMThXj6+LeuIAygtsUxBsJAgMBAAECggEANbL6WCAwCUfPapP6SuIjnMQ6wJ3G8G6GqpVy41oKUjyjEJ39hn3z7jUgVHkuoh/xEFhC9QDpy1AxHFUE/18TyjYdaFVR7171fUqRtffwj7tBGygBs816v3wtQYlycMboEPEcPI6WUkv87MOFUbD/SaTPDsYKOg+Fj6UB3ZLRc/+0gakwW1UbD+TeIvU1ZuilXY89F/PgD8WbWfyFfDaq24EObsVibtdyui8vcSyd8pD0zdqbSsYJr81wVUpTIfu7Z+VJxdk9+sPlt4CPdsGZja61O/FyxO3skgRIbcOoSEDXsvnH5iRwCU/opXYXXa942gbdXznYI69Wz2KqJAAggQKBgQD7K3X1mN/QR5Nkpnk2de0upNZD2tCx5gVjz2NBM/JRdfXoB67P7Zk8Ie9RtDqEDewJvHCbUM+Oo8W3H3IKP8V3H2CySsS+mVNh0T3ndoIGcr4DY1QitR94FypUW/vleRvVTlSQbhI24r2ZnO8BwhUipeYwHiJay4S3jyfvB71gWQKBgQDhEjeyqAIKJhxyYyRZY1A0zf2zO337bDlub8qGU96Z7S1YEQ/MX9DSd4ASeWVpo4b7a5ixb56Cve+s0yXNdqUlCVhIlN5z926rm2rq7JX9Yh5vuaJ5LDSQywWpzKS41Z/IuA/IFnsWKgpYg5FYgvL0B8s57S/ZO+5VmPh+fSS6MQKBgQDqvG5szqf1nr8opNDJziKRoipBlkHO0RuecVbgvCyuZyEf7fY5i2PXqBti7550hhJf8wYGkdq5A2QnxbKt03W349JaLrh7LQ6Zb5V8OexBHIFq5yzdlwrHEFdWKfVcO/iAPMmZhdVuCDCMXPeeXAWmsQdVVwRieKwbZSY8Ja6eWQKBgQCGVMQvejz1Z7e0wd1xCGhXi9ZuwgQ4KmjAtDIxKB+EyV014cEHHuzYoQAsho3zlek7aAWTaWY27Dv/b2pxje2VBFXPxCJdHoHMJI+UtS30NDqsPiRto+efv7yakoyB0OZz+v9YUaqXGPJhz+zQ4h81bO1rnlfflPhz2D//5pskEQKBgQDQtN2QSqYQStm+UYaD4/7WQqtEhyK0RWs4drKL7gqSjHAWU3+Bf2/v1HO6s9l9CCS9iyGHOjCw3cXlwVQJqj40VdTn2Yv7JAJWpyDawqrGrHv7FmCAfKOiGuvpGGEGiy8CWCqK6vqjVpTiXvja3mTjJgsaCAfZtTwL515HydC+Mg==")
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {

                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.e(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.e(TAG, "sophix preload patch success. restart app to make effect.");
                            spUtils.setBoolean(Entity.SOP_HIX_SUCCESS, true);
                        }
                    }
                }).initialize();
    }
}
