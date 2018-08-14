package com.runtoinfo.teacher.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.runtoinfo.teacher.bean.HttpLoginHead;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */

public class HttpUtils {

    public static final ExecutorService executorService = Executors.newScheduledThreadPool(7);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final OkHttpClient client = new OkHttpClient();

    /**
     * POST 异步加载网络图片
     */
    public static void postAsynchronous(final Activity context, final String url, final ImageView imageView){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final Bitmap bitmap1 = BitmapFactory.decodeStream(response.body().byteStream());
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap1);
                                }
                            });

                        }
                    }
                });
            }
        });

    }

    /**
     * POST 登录传参 密码登录
     */
    public static void post(final android.os.Handler handler, final String url, final HttpLoginHead head){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    json.put("userName", head.getUserName());
                    json.put("passWord", head.getPassWord());
                    json.put("rememberClient", "true");
                    json.put("tenancyName", head.getTenancyName());
                    json.put("client", "student");
                    json.put("campusId", head.getCampusId());

                    RequestBody body = RequestBody.create(JSON, json.toString());

                    Request request = new Request.Builder()
                            .url(url)
                            //.addHeader("Authorization",  "Bearer " + head.getToken())
                            .post(body)
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Message message = new Message();
                        message.what = 3;
                        message.obj = response;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * post 验证码登录
     * @param handler
     * @param url 验证码登录接口 HttpEntity.LOGIN_URL_CAPTCHA
     * @param head
     */
    public static void postCaptcha(final android.os.Handler handler, final String url, final HttpLoginHead head){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject J = new JSONObject();
                    J.put("phoneNumber", head.getUserName());
                    J.put("captcha", head.getPassWord());
                    J.put("rememberClient", true);
                    J.put("tenancyName", head.getTenancyName());
                    J.put("client", "student");

                    RequestBody body = RequestBody.create(JSON, J.toString());
                    Request request = new Request.Builder()
                            .url(url).post(body).build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        Message message = new Message();
                        message.what = 3;
                        message.obj = response;
                        handler.sendMessage(message);
                    }else {
                        handler.sendEmptyMessage(4);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * post 重置密码
     * @param handler
     * @param url HttpEntity.REST_PASSWORD
     */
    public static void postForgetPassWord(final Handler handler, final String url, final String phoneNumber, final String newPassword){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody body = new FormBody.Builder()
                            .add("phoneNumber", phoneNumber)
                            .add("newPassword", newPassword)
                            .build();

                    Request request = new Request.Builder()
                            .url(url).post(body).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        handler.sendEmptyMessage(0);
                    }else{
                        handler.sendEmptyMessage(1);
                    }
                }catch (IOException E){
                    E.printStackTrace();
                }
            }
        });

    }

    /**
     *验证码验证
     */

    public static void postValidate(final Handler handler, final String url, final String phoneNumber, final String code){
        executorService.execute(new Runnable() {
            @Override
            public void run(){
                FormBody formBody = new FormBody.Builder()
                        .add("phoneNumber", phoneNumber)
                        .add("captchaCode", code)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        String body = response.body().string();
                        Log.e("body", body);
                        JSONObject jsonObject = new JSONObject(body);
                        boolean result = jsonObject.getBoolean("result");
                        if (result){
                            handler.sendEmptyMessage(3);
                        }else{
                            handler.sendEmptyMessage(4);
                        }
                    } else{
                        handler.sendEmptyMessage(4);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * GET 异步加载 获取用户名下信息
     */
    public static void getAsy(final Handler handler,final Map<String, List> map,final String url,final String userName){

                OkHttpClient client = new OkHttpClient();
                String requestUrl = url + "?phoneNumber=" + userName + "&client=student";
                final Request request = new Request.Builder()
                        .url(requestUrl)
                        .build();
                final List<String> orgNameList = new ArrayList<>();
                final List<List<String>> schoolList = new ArrayList<>();
                final List<HttpLoginHead> headList = new ArrayList<>();
                final List<String> imgList = new ArrayList<>();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {//回调的方法执行在子线程。
                            //head.setCode(response.code());
                            try {
                                String body = response.body().string();
                                Log.e("response", body);
                                JSONObject obj = new JSONObject(body);
                                JSONObject json = obj.getJSONObject("result");
                                JSONArray array = json.getJSONArray("items");

                                for (int i = 0; i < array.length(); i++) {
                                    HttpLoginHead loginHead = new HttpLoginHead();
                                    JSONObject JSON = array.getJSONObject(i);
                                    String orgName = JSON.getString("brandName");
                                    orgNameList.add(orgName);
                                    imgList.add(JSON.getString("logo"));
                                    JSONArray object = JSON.getJSONArray("campuses");
                                    loginHead.setTenancyName(JSON.getString("tenancyName"));
                                    loginHead.setCampusId(JSON.getInt("id"));
                                    loginHead.setTenantId(JSON.getString("tenantId"));
                                    List<String> schoolName = new ArrayList<>();
                                    for (int j = 0; j < object.length(); j++) {
                                        JSONObject object1 = object.getJSONObject(j);
                                        String name = object1.getString("name");
                                        schoolName.add(name);
                                    }
                                    schoolList.add(schoolName);
                                    headList.add(loginHead);
                                }

                                map.put("org", orgNameList);
                                map.put("school", schoolList);
                                map.put("head", headList);
                                map.put("img", imgList);

                                handler.sendEmptyMessage(5);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
    }

    /**
     * GET 同步加载 获取验证码
     */
    public static void get(final String url, final String phoneNumber ){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    Request request = new Request.Builder()
                            .url(url + "?phoneNumber=" + phoneNumber)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = client.newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        Log.d("kwwl","response.code()=="+response.code());
                        Log.d("kwwl","response.message()=="+response.message());
                        Log.d("kwwl","res=="+response.body().string());
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * PUT
     */
    public static void put(){

    }

    /**
     * delete
     */
    public static void delete(){

    }

    public static String execute(Request request){
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{\"error\":\"fail\"}";
    }
}
