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

import static myapplication.MyApplication.msgDisplayListener;
import static myapplication.MyApplication.spUtils;

/**
 * Created by QiaoJunChao on 2018/9/28.
 */

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
                .setSecretMetaData("24880740-1", "5d187b5215dd27026457f27caaf33be6", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC401SBxpD2uh8jZZoJRYxUvLYzJDjd9ueV4jjocdkhINN+2bxkcXRWK/Z+hK7kY9B9Yvqf3vNFwGtbSOz2R5frB7I/VNxkNBiHcx+/ODJ1D7DPTKD0R3YnydW1wPciC5GMEgobqmYmexuQO3TuCiQCvbImEgRlR2HDi78aVJpMJhHF7NZNUlXb/R/DqkZ6frGzzUxZibjmZ5YB1w8j6B6uk74QY3JBaT99VWOG+HcmULYelxIuuKZQikI0iceigZHCCt/rlKCsy4vHQwaj3ffODQ/FI+SvcwSuB2Su3iE0eqC4jaioaxLQp8jCHV0ngxz/Pk+6Lncfy5NrqTw5sHlBAgMBAAECggEAVMqO8GKaSrakZmbMPgisEovowpmRhTMql3aLSQhB5pLatE1LwoDSMF5G3VAdtCWelod8Fy//Rza5ic1w+KNrrKWBPtC9szK7Nhi4rc8oRtAF+3RaOvc125ca9iEqv3GzOiFkhcCs0GaWBJtiT3pTfvZr7k9BuP0Aeud3qqq5hskAWBRvOYycDlE83e1NOF24jstUEYlOXYRXcu+jL2tYghGqDUtVk9gsFsvIQTucxZDvfs/YcYk4P9UyJc8G5oKj30IS19yEhN9aR75pqI2/KS1MvJdiSzqMn5/2Kly6j1kq++XTbX54ZTivx6bMSLkaNncDEGmrC+cBrpoUXOe1YQKBgQDnBdj/tLjtZl+0KhHvIh63X/ElPe8AR6nP0K9WyqagO2Qzm/P4kzqtm5fK5P/yXGkGhmwtQwUKbW6rvcOLnJ1pYxB+jiadlSstDxrDiDFT62h4WAjNMdpjGIRni8muqWvR5KoQJd0TMe1IZ6eH4NrYr6vYvDb8lMJXrmfWKUsWjQKBgQDMztoqkTXBquNmdtqkpMKI9bTb6qI7XQCoG4ktXUUsQUWKokzhr0p7NSry5Vbk/l/edf7c8HSjA28VBDV67s21v5FyDduTIWb9IfiIzOcAbmFyM4dw595IKYZG+Q+r4T61TxJHTbJoa51bG4yreTArP13vCSpq6fkyBNknnA1KhQKBgFEETPhHoX6nozJkeouZNJpsS/4afSFlAFk/vu25/bs9eMNckq9+ulo9sXrW2iddXvuJ0pAA69ifTetujg/86XsW4abwJPyoXk7b+C/QKDRc3vMtNFD5hbOw2mYRsLXO9l/SSA2HqcLchHEa4LaXFUheOu6z+riTVqA3KnUBYDLVAoGAbYZFBzSGYd4oI3h17kwpuCJlqKFuRn6yxjOLhVX2kvcI4F1w7dPW6dKB+EkK0gTycb36IF76kN5aPgv1KZuhKIuclTix7pmTQNU02rtw4TsmI3NI/+kPZtnF9Bhm+AzJgx4Y8UasnMjH6pvt+xkg2E/XxOfK5kffOo97LYJ3XR0CgYEAo7+GcM854EsKJoE1v66uZPhhEQD4e1wzv4aooe5XdCCLPOB6v3024knYl91KoVaGPy2VGf03G29s8QypxGTRuLmwsjBy3JqOvIZv9s1i2phz7isbjWn9jTmu5osksk0N1G9+wz4v4FZz4f4h5J6fLjTd3VgaH3ibqrjb0HMEx5I=")
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
