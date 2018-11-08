package com.runtoinfo.httpUtils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.dmcbig.mediapicker.entity.Media;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CPRCBean.CommentRequestResultEntity;
import com.runtoinfo.httpUtils.CenterEntity.LearnTrackEntity;
import com.runtoinfo.httpUtils.CenterEntity.LeaveRecordEntity;
import com.runtoinfo.httpUtils.CenterEntity.PersonalInformationEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.AddMemberBean;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.bean.CourseDataEntity;
import com.runtoinfo.httpUtils.bean.CourseEntity;
import com.runtoinfo.httpUtils.bean.EventAddResultBean;
import com.runtoinfo.httpUtils.bean.FineClassCourseEntity;
import com.runtoinfo.httpUtils.bean.GeoAreaEntity;
import com.runtoinfo.httpUtils.bean.GetSchoolSettingEntity;
import com.runtoinfo.httpUtils.bean.HandWorkEntity;
import com.runtoinfo.httpUtils.bean.HomeCourseEntity;
import com.runtoinfo.httpUtils.bean.HttpLoginHead;
import com.runtoinfo.httpUtils.bean.LeaveEntity;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.bean.PersonalCenterEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.SchoolDynamicsNewEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageEntity;
import com.runtoinfo.httpUtils.bean.TopiceHttpResultEntity;
import com.runtoinfo.httpUtils.bean.VersionEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by QiaoJunChao on 2018/8/10.
 */
@SuppressWarnings("all")
public class HttpUtils<T> {

    public static final ExecutorService executorService = Executors.newScheduledThreadPool(7);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType PICTURE = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final OkHttpClient client = new OkHttpClient();
    private static final String Authorization = "Authorization";
    private static final String Bearer = "Bearer ";
    private Context context;

    public HttpUtils(Context context) {
        this.context = context;
    }

    /**
     * POST 异步加载网络图片
     */
    public void postAsynchronous(final Activity context, final String url, final ImageView imageView) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final Drawable drawable = Drawable.createFromStream(response.body().byteStream(), "image");
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setBackground(drawable);
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    /**
     * 获取图片
     */
    public void postPhoto(final Activity context, final String url, final ImageView imageView) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final Drawable drawable = Drawable.createFromStream(response.body().byteStream(), "image");
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setBackground(drawable);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    /**
     * 类似src
     */
    public void postSrcPhoto(final Activity context, final String url, final ImageView imageView) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final Drawable drawable = Drawable.createFromStream(response.body().byteStream(), "image");
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageDrawable(drawable);
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
    public void post(final android.os.Handler handler, final String url, final HttpLoginHead head) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("userName", head.getUserName());
                    json.put("passWord", head.getPassWord());
                    json.put("rememberClient", "true");
                    json.put("tenancyName", head.getTenancyName());
                    json.put("client", "student");
                    json.put("campusId", head.getCampusId());

                    RequestBody body = RequestBody.create(JSON, json.toString());

                    Request request = new Request.Builder()
                            .header(Authorization, Bearer + head.getToken())
                            .url(url)
                            .post(body)
                            .build();
                    getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    Message message = new Message();
                                    JSONObject json = new JSONObject(response.body().string());
                                    boolean success = json.getBoolean("success");
                                    if (!success) {
                                        JSONObject error = json.getJSONObject("error");
                                        String mes = error.getString("message");
                                        String details = error.getString("details");
                                        message.what = 400;
                                        message.obj = mes;
                                        handler.sendMessage(message);
                                    } else {
                                        message.obj = json.getString("result");
                                        message.what = 3;
                                        handler.sendMessage(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * post 验证码登录
     *
     * @param handler
     * @param url     验证码登录接口 HttpEntity.LOGIN_URL_CAPTCHA
     * @param head
     */
    public void postCaptcha(final android.os.Handler handler, final String url, final HttpLoginHead head) {
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

                    getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Message message = new Message();
                                message.what = 3;
                                message.obj = response;
                                handler.sendMessage(message);
                            } else {
                                handler.sendEmptyMessage(4);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * post 重置密码
     *
     * @param handler
     * @param url     HttpEntity.REST_PASSWORD
     */
    public void postForgetPassWord(final Handler handler, final String url, final String phoneNumber, final String newPassword) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FormBody body = new FormBody.Builder()
                        .add("phoneNumber", phoneNumber)
                        .add("newPassword", newPassword)
                        .build();

                Request request = new Request.Builder()
                        .url(url).post(body).build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    }
                });

            }
        });

    }

    /**
     * 验证码验证
     */

    public void postValidate(final Handler handler, final String url, final String phoneNumber, final String code) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder()
                        .add("phoneNumber", phoneNumber)
                        .add("captchaCode", code)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                try {
                    Response response = getClient().newCall(request).execute();
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.e("body", body);
                        JSONObject jsonObject = new JSONObject(body);
                        boolean result = jsonObject.getBoolean("result");
                        if (result) {
                            handler.sendEmptyMessage(3);
                        } else {
                            handler.sendEmptyMessage(4);
                        }
                    } else {
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
    public void getAsy(final Handler handler, final Map<String, List> map, final String url, final String userName) {

        String requestUrl = url + "?phoneNumber=" + userName + "&client=student";
        final Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        final List<String> orgNameList = new ArrayList<>();
        final List<List<String>> schoolList = new ArrayList<>();
        final List<HttpLoginHead> headList = new ArrayList<>();
        final List<String> imgList = new ArrayList<>();
        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(500);
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
    public void get(final String url, final String phoneNumber) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url + "?phoneNumber=" + phoneNumber)//请求接口。如果需要传参拼接到接口后面。
                            .build();//创建Request 对象
                    Response response = null;
                    response = getClient().newCall(request).execute();//得到Response 对象
                    if (response.isSuccessful()) {
                        Log.d("kwwl", "response.code()==" + response.code());
                        Log.d("kwwl", "response.message()==" + response.message());
                        Log.d("kwwl", "res==" + response.body().string());
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
    public static void put() {

    }

    /**
     * delete 删除报名人员
     */
    public void deleteMember(final Handler handler, final RequestDataEntity requestDataEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                    JSONObject json = new JSONObject();
//                    json.put("MemberId", requestDataEntity.getSignId());
//                    RequestBody body = RequestBody.create(JSON, json.toString());
                FormBody formBody = new FormBody.Builder()
                        .add("MemberId", requestDataEntity.getSignId() + "")
                        .build();

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .delete(formBody)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    handler.sendEmptyMessage(200);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        } else {
                            handler.sendEmptyMessage(400);
                        }
                    }
                });
            }
        });
    }

    /**
     * 删除评论、赞、收藏
     *
     * @param handler
     * @param requestDataEntity
     */
    public void delectColleciton(final Handler handler, final RequestDataEntity requestDataEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder()
                        .add("Id", requestDataEntity.getId() + "")
                        .build();
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .delete(formBody)
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    handler.sendEmptyMessage(200);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 新闻检索
     *
     * @param handler
     * @param url
     * @param token   登录后的标识
     */
    public void getSchoolNewsAll(final Handler handler, final RequestDataEntity requestDataEntity, final List<SchoolDynamicsNewEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> object = new HashMap<>();
                object.put("type", requestDataEntity.getType());
                object.put("MaxResultCount", 10);
                object.put("SkipCount", 0);
                object.put("Sorting", "publishTime desc");
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl() + setUrl(object))
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray items = result.getJSONArray("items");
                                    for (int item = 0; item < items.length(); item++) {
                                        JSONObject childItem = items.getJSONObject(item);
                                        SchoolDynamicsNewEntity entity = new Gson().fromJson(childItem.toString(),
                                                new TypeToken<SchoolDynamicsNewEntity>() {
                                                }.getType());
                                        dataList.add(entity);
                                    }
                                    handler.sendEmptyMessage(5);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });
    }

    /**
     * 获取新闻阅读数量
     *
     * @param handler
     * @param url
     * @param token
     * @param id
     */
    public void getNewsReadNumber(final Handler handler, final String url, final String token, final int id) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("newsId", id);
                    RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
                    final Request request = new Request.Builder()
                            .header("Authorization", "Bearer " + token)
                            .url(url)
                            .post(requestBody)
                            .build();
                    getClient().newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            handler.sendEmptyMessage(500);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject json = new JSONObject(response.body().string());
                                    int result = json.getInt("result");
                                    Message msg = new Message();
                                    msg.what = 4;
                                    msg.obj = result;
                                    handler.sendMessage(msg);
                                } catch (JSONException E) {
                                    E.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取所有的活动
     *
     * @param handler
     */
    public void getEventAll(final Handler handler, final RequestDataEntity entity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> urlMap = new HashMap<>();
                urlMap.put("name", "");
                urlMap.put("MaxResultCount", "5");
                urlMap.put("SkipCount", "0");
                urlMap.put("Sorting", "");
                String urlString = setUrl(urlMap);

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + urlString)
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Message msg = new Message();
                            msg.obj = response.body().string();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取我的报名
     */
    public void getMyEvent(final Handler handler, final RequestDataEntity entity, final List<MyEventEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + "?UserId=" + entity.getUserId())
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                JSONArray result = json.getJSONArray("result");
                                if (success) {
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject item = result.getJSONObject(i);
                                        MyEventEntity eventEntity = new Gson().fromJson(item.toString(), new TypeToken<MyEventEntity>() {
                                        }.getType());
                                        dataList.add(eventEntity);
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }catch (IllegalStateException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取精品课类别
     *
     * @param url     请求地址
     * @param mapList 返回的结果集合
     */
    public void getAllCourseType(final Handler handler, final RequestDataEntity entity, final List<FineClassCourseEntity> mapList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + "?CategoryId=111")
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray items = result.getJSONArray("items");

                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject chileItem = items.getJSONObject(i);
                                        FineClassCourseEntity fineClassCourseEntity =
                                                new Gson().fromJson(chileItem.toString(), new TypeToken<FineClassCourseEntity>() {
                                                }.getType());
                                        mapList.add(fineClassCourseEntity);
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                handler.sendEmptyMessage(500);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 添加报名人员
     *
     * @param handler
     * @param entity  请求实体
     */
    public void postAddMember(final Handler handler, final RequestDataEntity entity, final AddMemberBean addMemberBean, final int type) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = new Gson().toJson(addMemberBean);
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .header(Authorization, Bearer + entity.getToken())
                            .url(entity.getUrl())
                            .post(body)
                            .build();
                    getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            handler.sendEmptyMessage(500);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                switch (type) {
                                    case 0://单独报名
                                        handler.sendEmptyMessage(0);
                                        break;
                                    case 1://添加随行人员报名
                                        try {
                                            JSONObject json = new JSONObject(response.body().string());
                                            JSONObject result = json.getJSONObject("result");
                                            EventAddResultBean resultBean = new Gson().fromJson(result.toString(),
                                                    new TypeToken<EventAddResultBean>(){}.getType());
                                            Message msg = new Message();
                                            msg.what = 0;
                                            msg.obj = resultBean;
                                            handler.sendMessage(msg);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 2://继续添加随行人员
                                        handler.sendEmptyMessage(0);
                                        break;
                                    case 3://完成提交
                                        handler.sendEmptyMessage(3);
                                        break;
                                }

                            }else{
                                handler.sendEmptyMessage(404);
                            }
                        }
                    });
                }catch (IllegalStateException e){
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        });
    }

    /**
     * 获取子分类
     */
    public void getChildType(final Handler handler, final RequestDataEntity requestDataEntity, final Map<String, Object> dataMap, final List<FineClassCourseEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl() + setUrl(dataMap))
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray items = getItems(response.body().string(), handler);
                                List<Map<String, Object>> mapList = new ArrayList<>();
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject childItem = items.getJSONObject(i);
                                    FineClassCourseEntity fineClassCourseEntity = new Gson()
                                            .fromJson(childItem.toString(), new TypeToken<FineClassCourseEntity>() {
                                            }.getType());
                                    dataList.add(fineClassCourseEntity);
                                }
                                handler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }else{
                            handler.sendEmptyMessage(404);
                        }
                    }
                });
            }
        });

    }

    /**
     * 获取课程的最终数据
     *
     * @param handler
     * @param map     参数集
     * @param list    接受结果集
     */

    public void getInChildData(final Handler handler, final RequestDataEntity entity, final Map<String, Object> map, final List<CourseDataEntity> list) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + setUrl(map))
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray items = getItems(response.body().string(), handler);
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject childItem = items.getJSONObject(i);
                                    CourseDataEntity courseDataEntity = new Gson().fromJson(childItem.toString(),
                                            new TypeToken<CourseDataEntity>() {
                                            }.getType());
                                    list.add(courseDataEntity);
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }else{
                            handler.sendEmptyMessage(404);
                        }
                    }
                });
            }
        });
    }

    /**
     * 首页今日打卡的课程
     *
     * @param handler
     * @param dataMap    参数集
     * @param courseData 数据集
     */
    public void getCourseDataList(final Handler handler, final RequestDataEntity entity, final Map<String, Object> dataMap, final List<HomeCourseEntity> courseData) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("IOException", e.toString());
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (!success) {
                                    handler.sendEmptyMessage(400);
                                } else {
                                    JSONArray result = json.getJSONArray("result");
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject childResult = result.getJSONObject(i);
                                        HomeCourseEntity homeCourseEntity = new HomeCourseEntity();
                                        homeCourseEntity.setBeginTime(childResult.getString("beginTime"));
                                        homeCourseEntity.setClassId(childResult.getInt("classId"));
                                        homeCourseEntity.setClassName(childResult.getString("className"));
                                        homeCourseEntity.setCourseId(childResult.getInt("courseId"));
                                        homeCourseEntity.setCourseName(childResult.getString("courseName"));
                                        homeCourseEntity.setCourseInstId(childResult.getInt("courseInstId"));
                                        homeCourseEntity.setCoverPhoto(childResult.getString("coverPhoto"));
                                        homeCourseEntity.setSignIn(childResult.getBoolean("isSignIn"));
                                        homeCourseEntity.setToken(entity.getToken());
                                        courseData.add(homeCourseEntity);
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 签到
     */
    public void postSignIn(final Handler handler, final Map<String, Object> map) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("CourseInstId", map.get("CourseInstId"));
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header(Authorization, Bearer + map.get("token"))
                            .url(map.get("url").toString())
                            .post(body)
                            .build();
                    getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            handler.sendEmptyMessage(500);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject json = new JSONObject(response.body().string());
                                    if (TextUtils.isEmpty(json.getString("error"))) {
                                        handler.sendEmptyMessage(400);
                                        return;
                                    }
                                    handler.sendEmptyMessage(1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(404);
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 上传图片文件
     *
     * @param handler
     * @param map
     * @param list
     */
    public void postVideoPhoto(final Handler handler, final RequestDataEntity requestDataEntity, final List<Media> list, final List<String> filePathList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (int i = 0; i < list.size() - 1; i++) {
                    File file = new File(list.get(i).path);
                    builder.addFormDataPart("file", file.getPath(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                }
                MultipartBody body = builder.build();
                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .post(body)
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONArray result = jsonObject.getJSONArray("result");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject childResult = result.getJSONObject(i);
                                    String filePath = childResult.getString("filePath");
                                    filePathList.add(filePath);
                                }

                                handler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                handler.sendEmptyMessage(300);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 上传单个文件
     *
     * @param handler
     * @param entity
     */
    public void postOneFile(final Handler handler, final RequestDataEntity entity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(entity.getUrl())
                        .header(Authorization, Bearer + entity.getToken())
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    String path = result.getString("filePath");
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = path;
                                    handler.sendMessage(msg);
                                } else
                                    handler.sendEmptyMessage(400);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 修改用户信息
     *
     * @param handler
     * @param requestDataEntity
     * @param entity
     */
    public void updateUserInfor(final Handler handler, final RequestDataEntity requestDataEntity, final PersonalInformationEntity entity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                entity.setId(requestDataEntity.getUserId());
                String json = new Gson().toJson(entity);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .post(body)
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 提交作业
     *
     * @param
     * @return
     */
    public void postHomeWork(final Handler handler, final Map<String, Object> dataMap, final List<Map<String, Object>> filePathList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HandWorkEntity workEntity = new HandWorkEntity();
                    workEntity.setCourseId(Integer.parseInt(dataMap.get("courseId").toString()));
                    workEntity.setCourseInsId(Integer.parseInt(dataMap.get("courseInsId").toString()));
                    workEntity.setStudentId(18);
                    workEntity.setUserId(Integer.parseInt(dataMap.get("userId").toString()));
                    workEntity.setRemark(dataMap.get("remark").toString());
                    List<HandWorkEntity.WorkItems> object = new ArrayList<>();
                    for (int i = 0; i < filePathList.size(); i++) {
                        HandWorkEntity.WorkItems workItems = new HandWorkEntity.WorkItems();
                        workItems.setPath(filePathList.get(i).get("path").toString());
                        workItems.setType((int) filePathList.get(i).get("type"));
                        object.add(workItems);
                    }
                    workEntity.setWorkItems(object);
                    String json = new Gson().toJson(workEntity);
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .header("Authorization", "Bearer " + dataMap.get("token"))
                            .url(dataMap.get("url").toString())
                            .post(body)
                            .build();

                    getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("result", "请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Log.e("result", "上传成功");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 提交请假
     *
     * @param handler
     * @param dataEntity 请求所需要的参数
     */
    public void postLeave(final Handler handler, final RequestDataEntity dataEntity, final LeaveEntity entity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                String json = new Gson().toJson(entity);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + dataEntity.getToken())
                        .url(dataEntity.getUrl())
                        .post(body)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            handler.sendEmptyMessage(0);
                        } else handler.sendEmptyMessage(404);
                    }
                });
            }
        });
    }

    /**
     * 检索专题
     *
     * @param handler
     * @param dataEntity
     */
    public void getTopics(final Handler handler, final RequestDataEntity dataEntity, final List<TopiceHttpResultEntity> resultList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + dataEntity.getToken())
                        .url(dataEntity.getUrl() /*+ "?MaxResultCount=5&SkipCount=0"*/)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                String data = response.body().string();
                                Log.e("data", data);
                                JSONArray items = getItems(data, handler);
                                Gson gson = new Gson();
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject childItem = items.getJSONObject(i);
                                    TopiceHttpResultEntity entity = new TopiceHttpResultEntity();//gson.fromJson(childItem.toString(), new TypeToken<TopiceHttpResultEntity>(){}.getType());
                                    entity.setCampusId(String.valueOf(childItem.get("campusId")));
                                    entity.setCommentNumber(childItem.getString("commentNumber"));
                                    entity.setContentpublic(childItem.getString("content"));
                                    entity.setCoverType(childItem.getString("coverType"));
                                    List<String> imgList = new ArrayList<>();
                                    JSONArray paths = childItem.getJSONArray("coverImgs");
                                    for (int j = 0; j < paths.length(); j++) {
                                        imgList.add(paths.get(j).toString());
                                    }
                                    entity.setCoverImgs(imgList);
                                    entity.setPageView(childItem.getString("pageView"));
                                    entity.setId(childItem.getInt("id"));
                                    entity.setPraiseNumber(childItem.getString("praiseNumber"));
                                    entity.setPublisher(childItem.getString("publisher"));
                                    entity.setPublishTime(childItem.getString("publishTime"));
                                    entity.setReplyNumber(childItem.getString("replyNumber"));
                                    entity.setTitle(childItem.getString("title"));
                                    //entity.setSubtitle(childItem.getString("subTitle"));
                                    resultList.add(entity);
                                }
                                handler.sendEmptyMessage(200);
                            } catch (JSONException e) {
                                handler.sendEmptyMessage(400);
                                e.printStackTrace();
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 评论、赞、回复、收藏
     * type：评论：0；回复：1 ；赞：2；收藏：3；
     * parentId：评论或者回复的id；
     * parentType：回复类型：评论：0；回复：1；
     * target：新闻或专题中文章id；
     * targetType：新闻：0；专题：1；评论：2； 回复：3；
     * level：层级
     *
     * @param handler
     */
    public void postComment(final Handler handler, final CPRCDataEntity cprcDataEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                RequestBody body = RequestBody.create(JSON, new Gson().toJson(cprcDataEntity));
                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + cprcDataEntity.getToken())
                        .url(HttpEntity.MAIN_URL + HttpEntity.COURSE_COMMENT_CREATE)
                        .post(body)
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    Message msg = new Message();
                                    if (cprcDataEntity.getType() == 1 && cprcDataEntity.getTargetType() == 3)
                                        msg.what = 2;
                                    else msg.what = cprcDataEntity.getType();
                                    msg.obj = result;
                                    handler.sendMessage(msg);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取全部评论或者回复
     * @param handler
     * @param cpc
     * @param requestType 请求类型，0，评论，1，评论的回复，2，回复的回复
     */
    public void getCommentAll(final Handler handler, final Map<String, Object> map /*final GetAllCPC cpc*/, final int requestType, final List<CommentRequestResultEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
//                Map<String, Object> map = new HashMap<>();
//                map.put("Type", cpc.getType());
//                map.put("Target", cpc.getTarget());
//                map.put("TargetType", cpc.getTargetType());
//                map.put("ParentType", cpc.getParentType());
//                map.put("ParentId", cpc.getParentId());
//                map.put("MaxResultCount", cpc.getMaxResultCount());
//                map.put("SkipCount", cpc.getSkipCount());
//                map.put("Sorting", "approvedTime desc");

                Request request = new Request.Builder()
                        .header(Authorization, Bearer.concat(map.get("token").toString()))
                        .url(HttpEntity.MAIN_URL + HttpEntity.GET_COMMENT_ALL + setUrl(map))
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray array = result.getJSONArray("items");
                                    for (int i = 0; i < array.length(); i++) {
                                        String item = array.getJSONObject(i).toString();
                                        CommentRequestResultEntity requestResultEntity = new Gson().fromJson(item,
                                                new TypeToken<CommentRequestResultEntity>() {}.getType());
                                        dataList.add(requestResultEntity);
                                    }
                                }
                                if (requestType != 2)
                                    handler.sendEmptyMessage(20);
                                else
                                    handler.sendEmptyMessage(30);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取单条评论
     * @param handler
     * @param requestDataEntity
     * @param dataList
     */
    public void getComment(final Handler handler, final RequestDataEntity requestDataEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl() + "?Id=" + requestDataEntity.getId())
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    CommentRequestResultEntity resultEntity = new Gson().fromJson(result.toString(),
                                            new TypeToken<CommentRequestResultEntity>(){}.getType());
                                    Message message = new Message();
                                    message.obj = resultEntity;
                                    message.what = 50;
                                    handler.sendMessage(message);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 更新评论、赞、回复内容状态
     *
     * @param handler
     * @param id
     * @param token
     */
    public void putAllStatue(final Handler handler, final int id, final String token) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", id);
                    RequestBody body = RequestBody.create(JSON, json.toString());

                    Request request = new Request.Builder()
                            .header(Authorization, Bearer + token)
                            .url(HttpEntity.MAIN_URL + HttpEntity.DELETE_COMMENT_CREATE)
                            .delete(body)
                            .build();
                    getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                handler.sendEmptyMessage(40);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取评论
     *
     * @param handler
     * @param entity
     */
    public void getMyCommentOrPraise(final Handler handler, final RequestDataEntity entity, final Map<String, Object> dataMap) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + setUrl(dataMap))
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray items = getItems(response.body().string(), handler);
                                Message message = new Message();
                                message.what = 30;
                                message.obj = items;
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    public void getMyCollection(final Handler handler, final RequestDataEntity entity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(entity.getUrl() + "?userId=" + entity.getUserId() + "&targetType=" + entity.getType())
                        .header(Authorization, Bearer + entity.getToken())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONArray items = getItems(response.body().string(), handler);
                                Message msg = new Message();
                                msg.what = 10;
                                msg.obj = items;
                                handler.sendMessage(msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取用户未读信息
     *
     * @param handler
     * @param requestDataEntity 请求体
     */
    public void getUserUnreadNotification(final Handler handler, final RequestDataEntity requestDataEntity, final List<SystemMessageEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .url(requestDataEntity.getUrl() + "?tenantId=" + requestDataEntity.getTenantId() + "&userId=" + requestDataEntity.getUserId() + "&notificationName=" + requestDataEntity.getMsg())
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            //Log.e("Notification", response.body().string());
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray items = result.getJSONArray("items");
                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject item = items.getJSONObject(i);
                                        SystemMessageEntity messageEntity =
                                                new Gson().fromJson(item.toString(), new TypeToken<SystemMessageEntity>() {
                                                }.getType());
                                        if (!isEmpty(item.getString("cover")))
                                            messageEntity.setItemType(2);
                                        else messageEntity.setItemType(3);
                                        dataList.add(messageEntity);
                                    }
                                    handler.sendEmptyMessage(0);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        } else {
                            handler.sendEmptyMessage(500);
                        }
                    }
                });
            }
        });
    }

    /**
     * 将消息更改为已读
     *
     * @param handler
     * @param requestDataEntity
     */
    public void setNoticeReader(final Handler handler, final RequestDataEntity requestDataEntity, final Map<String, Object> dataMap) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String data = new Gson().toJson(dataMap);
                RequestBody body = RequestBody.create(JSON, data);

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .post(body)
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    handler.sendEmptyMessage(200);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取版本号
     *
     * @param handler
     * @param requestDataEntity
     * @param dataMap
     */
    public void checkVersion(final Handler handler, final RequestDataEntity requestDataEntity, final Map<String, Object> dataMap) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(requestDataEntity.getUrl() + setUrl(dataMap))
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray items = result.getJSONArray("items");
                                    List<VersionEntity> versionList = new ArrayList<>();
                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject item = items.getJSONObject(i);
                                        VersionEntity versionEntity =
                                                new Gson().fromJson(item.toString(), new TypeToken<VersionEntity>() {
                                                }.getType());
                                        versionList.add(versionEntity);
                                    }
                                    if (versionList.size() <= 0) {
                                        return;
                                    }
                                    Message message = new Message();
                                    message.obj = versionList.get(0);
                                    message.what = 0;
                                    handler.sendMessage(message);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 检索课程
     * @param handler
     * @param entity 请求类
     * @param dataMap 参数
     * @param dataList 结果集
     * @param type 结果类型0，年；1，月；2，日
     */
    public void getCouseAll(final Handler handler, final RequestDataEntity entity, final Map<String, Object> dataMap, final List<CourseEntity> dataList, final int type) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + setUrl(dataMap))
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONArray result = json.getJSONArray("result");
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject item = result.getJSONObject(i);
                                        CourseEntity courseEntity = new Gson().fromJson(item.toString(), new TypeToken<CourseEntity>() {}.getType());
                                        dataList.add(courseEntity);
                                    }

                                    handler.sendEmptyMessage(type);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取学习轨迹
     *
     * @param request
     * @return
     */
    public void getLearnTacks(final Handler handler, final RequestDataEntity requestDataEntity, final Map<String, Object> map, final List<LearnTrackEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl() + setUrl(map))
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray items = result.getJSONArray("items");
                                    for (int i = 0; i < items.length(); i++) {
                                        JSONObject item = items.getJSONObject(i);
                                        LearnTrackEntity trackEntity =
                                                new Gson().fromJson(item.toString(), new TypeToken<LearnTrackEntity>() {
                                                }.getType());
                                        dataList.add(trackEntity);
                                    }
                                    handler.sendEmptyMessage(0);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取请假
     *
     * @param handler
     * @param requestDataEntity
     * @param dataMap
     * @param dataList
     */
    public void getLeaveRecord(final Handler handler, final RequestDataEntity requestDataEntity, final Map<String, Object> dataMap, final List<LeaveRecordEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl() + setUrl(dataMap))
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray itmes = result.getJSONArray("items");
                                    for (int i = 0; i < itmes.length(); i++) {
                                        JSONObject item = itmes.getJSONObject(i);
                                        LeaveRecordEntity leaveRecordEntity =
                                                new Gson().fromJson(item.toString(), new TypeToken<LeaveRecordEntity>() {
                                                }.getType());
                                        dataList.add(leaveRecordEntity);
                                    }
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });

    }

    /**
     * 上课记录
     *
     * @param handler
     * @param entity
     * @param map
     * @param list
     */
    public void getCourseRecord(final Handler handler, final RequestDataEntity entity, final Map<String, Object> map, final List<CourseEntity> list) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + setUrl(map))
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean sussess = json.getBoolean("success");
                                if (sussess) {
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray array = result.getJSONArray("items");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject item = array.getJSONObject(i);
                                        CourseEntity courseEntity = new Gson().fromJson(item.toString(),
                                                new TypeToken<CourseEntity>() {
                                                }.getType());
                                        list.add(courseEntity);
                                    }
                                    handler.sendEmptyMessage(2);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取校长电话
     *
     * @param handler
     * @param requestDataEntity
     */

    public void getSchoolSetting(final Handler handler, final RequestDataEntity requestDataEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    GetSchoolSettingEntity settingEntity =
                                            new Gson().fromJson(result.toString(), new TypeToken<GetSchoolSettingEntity>() {
                                            }.getType());
                                    Message msg = new Message();
                                    msg.what = 10;
                                    msg.obj = settingEntity;
                                    handler.sendMessage(msg);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }

                        }
                    }
                });
            }
        });
    }

    /**
     * 切换学校
     *
     * @param request
     * @return
     */

    public void switchCampusId(final Handler handler, final RequestDataEntity requestDataEntity) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FormBody body = new FormBody.Builder()
                        .add("tenantId", String.valueOf(requestDataEntity.getTenantId()))
                        .add("campusId", String.valueOf(requestDataEntity.getCampusId()))
                        .build();
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + requestDataEntity.getToken())
                        .url(requestDataEntity.getUrl())
                        .post(body)
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    Message msg = new Message();
                                    msg.what = 20;
                                    msg.obj = result;
                                    handler.sendMessage(msg);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 添加地址选择
     *
     * @param handler
     * @param entity
     * @param dataList
     */
    public void getGeoArea(final Handler handler, final RequestDataEntity entity, final List<GeoAreaEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + "?ParentId=" + entity.getCode())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    if (dataList.size() > 0) dataList.clear();
                                    JSONObject result = json.getJSONObject("result");
                                    JSONArray array = result.getJSONArray("items");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject item = array.getJSONObject(i);
                                        GeoAreaEntity geoAreaEntity = new Gson().fromJson(item.toString(),
                                                new TypeToken<GeoAreaEntity>() {
                                                }.getType());
                                        dataList.add(geoAreaEntity);
                                    }
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        } else {
                            handler.sendEmptyMessage(500);
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取用户的未读数量
     *
     * @param handler
     * @param entity
     */
    public void getNotificationCount(final Handler handler, final RequestDataEntity entity, final Map<String, Object> map) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder()
                        .url(entity.getUrl() + setUrl(map))
                        .header(Authorization, Bearer + entity.getToken())
                        .build();
                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    Message message = new Message();
                                    message.obj = json.getInt("result");
                                    switch (entity.getType()) {
                                        case 1://系统信息
                                            message.what = 31;
                                            break;
                                        case 2://上课提醒
                                            message.what = 32;
                                            break;
                                        case 3://学校通知
                                            message.what = 33;
                                            break;
                                        default:
                                            message.what = 30;
                                            break;
                                    }
                                    handler.sendMessage(message);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            }
                        }
                    }
                });
            }
        });
    }

    public static String execute(Request request) {
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

    public Type getType() {
        TypeToken<T> typeToken = new TypeToken<T>() {
        };
        return typeToken.getType();
    }

    /**
     * 公共解析返回结果
     *
     * @param body    获取的结果
     * @param handler handler
     * @return JSONArray
     * @throws JSONException JSONException
     */
    public static JSONArray getItems(String body, Handler handler) throws JSONException {
        JSONObject json = new JSONObject(body);
        boolean success = json.getBoolean("success");
        if (success) {
            JSONObject result = json.getJSONObject("result");
            JSONArray items = result.getJSONArray("items");
            return items;
        } else {
            handler.sendEmptyMessage(400);
        }
        return null;
    }

    /**
     * 为get请求拼接参数
     *
     * @param map
     * @return
     */
    public static String setUrl(Map<String, Object> map) {
        StringBuilder builder = new StringBuilder("?");
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            builder.append(key).append("=").append(map.get(key)).append("&");
        }
        String s = builder.toString();
        return s.substring(0, s.length() - 1);
    }

    /**
     * 为了使用https 而设
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getHttpsClient() {
//        OkHttpClient.Builder newClient = new OkHttpClient.Builder();
//         newClient.sslSocketFactory(HttpsTrustManager.createSSLSocketFactory())
//                .hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier())
//                .build();

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        return okHttpClient;
    }

    public OkHttpClient getClient() {
        OkHttpClient newClient;
        newClient = HttpsSettings.setCertificates(context);
        return newClient;
    }

    public static boolean isEmpty(String flag) {
        if (!TextUtils.isEmpty(flag) && !flag.equals("null"))
            return false;
        else return true;
    }


    /**
     * 请求个人信息
     *
     * @param handler
     * @param url
     * @param body
     * @param token
     */

    public void postPersonlInfo(final Handler handler, final RequestDataEntity entity, final List<PersonalCenterEntity> dataList) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + "?id=" + entity.getUserId())
                        .build();

                getClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                boolean success = json.getBoolean("success");
                                if (success) {
                                    JSONObject result = json.getJSONObject("result");
                                    PersonalCenterEntity personalCenterEntity = new Gson().fromJson(result.toString(),
                                            new TypeToken<PersonalCenterEntity>() {
                                            }.getType());
                                    dataList.add(personalCenterEntity);
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(400);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(404);
                            } catch (IllegalStateException e){
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }
}
