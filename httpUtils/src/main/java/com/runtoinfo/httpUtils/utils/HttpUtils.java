package com.runtoinfo.httpUtils.utils;

import android.app.Activity;
import android.content.IntentFilter;
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
import com.runtoinfo.httpUtils.CPRCBean.GetAllCPC;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.AddMemberBean;
import com.runtoinfo.httpUtils.CPRCBean.CPRCDataEntity;
import com.runtoinfo.httpUtils.bean.AddMemberResultEntity;
import com.runtoinfo.httpUtils.bean.ChildContent;
import com.runtoinfo.httpUtils.bean.CourseDataEntity;
import com.runtoinfo.httpUtils.bean.HandWorkEntity;
import com.runtoinfo.httpUtils.bean.HomeCourseEntity;
import com.runtoinfo.httpUtils.bean.HttpLoginHead;
import com.runtoinfo.httpUtils.bean.LeaveEntity;
import com.runtoinfo.httpUtils.bean.MyEventEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.TopiceHttpResultEntity;

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

public class HttpUtils {

    public static final ExecutorService executorService = Executors.newScheduledThreadPool(7);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType PICTURE = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final OkHttpClient client = new OkHttpClient();
    private static final String Authorization = "Authorization";
    private static final String Bearer = "Bearer ";

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
     *获取图片
     */
    public static void postPhoto(final Activity context, final String url, final ImageView imageView){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final Drawable drawable = Drawable.createFromStream(response.body().byteStream(), "image");
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setBackground(drawable);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 类似src
     */
    public static void postSrcPhoto(final Activity context, final String url, final ImageView imageView){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient newClient = client.newBuilder()
                            .sslSocketFactory(HttpsTrustManager.createSSLSocketFactory())
                            .hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier())
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = newClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        final Drawable drawable = Drawable.createFromStream(response.body().byteStream(), "image");
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageDrawable(drawable);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    /**
     * 新闻检索
     * @param handler
     * @param url
     * @param token 登录后的标识
     */
    public static void getSchoolNewsAll(final Handler handler, final String url, final String token){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> object = new HashMap<>();
                    object.put("Title","");
                    object.put("Subtitle", "");
                    object.put("Tag", "");
                    object.put("Status", "");
                    object.put("MaxResultCount", 7);
                    object.put("SkipCount", 0);
                    object.put("Sorting","");
                    Request request = new Request.Builder()
                            .header("Authorization", "Bearer " + token)
                            .url(url + setUrl(object))
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        String result = response.body().string();
                        Message message = new Message();
                        message.what = 5;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取新闻阅读数量
     * @param handler
     * @param url
     * @param token
     * @param id
     */
    public static void getNewsReadNumber(final Handler handler, final String url, final String token, final int id){
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
                    client.newCall(request).enqueue(new Callback(){

                        @Override
                        public void onFailure(Call call, IOException e) {
                            handler.sendEmptyMessage(500);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()){
                                try{
                                    JSONObject json = new JSONObject(response.body().string());
                                    int result = json.getInt("result");
                                    Message msg = new Message();
                                    msg.what = 4;
                                    msg.obj = result;
                                    handler.sendMessage(msg);
                                }catch (JSONException E){
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
     * @param handler
     */
    public static void getEventAll(final Handler handler, final RequestDataEntity entity){
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
                        .header(Authorization,  Bearer + entity.getToken())
                        .url(entity.getUrl() + urlString)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
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
    public static void getMyEvent(final Handler handler, final RequestDataEntity entity, final List<MyEventEntity> dataList){
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl() + "?UserId=" + entity.getUserId())
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try{
                                JSONArray items = getItems(response.body().string(), handler);
                                for (int i = 0; i < items.length(); i++){
                                    JSONObject item = items.getJSONObject(i);
                                    MyEventEntity eventEntity = new Gson().fromJson(item.toString(), new TypeToken<MyEventEntity>(){}.getType());
                                    dataList.add(eventEntity);
                                }
                                handler.sendEmptyMessage(0);
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 添加报名人员
     * @param handler
     * @param entity 请求实体
     */
    public static void postAddMember(final Handler handler, final RequestDataEntity entity, final AddMemberBean addMemberBean, final int type) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                    /*JSONObject json = new JSONObject();
                    json.put("campaignId", addMemberBean.getCampaignId());
                    json.put("userId", addMemberBean.getUserId());
                    json.put("memberType", addMemberBean.getMemberType());
                    json.put("name", addMemberBean.getName());
                    json.put("gender", addMemberBean.getGender());
                    json.put("age", addMemberBean.getAge());
                    json.put("phoneNumber", addMemberBean.getPhoneNumber());*/

                String json = new Gson().toJson(addMemberBean);

                RequestBody body = RequestBody.create(JSON, json);

                Request request = new Request.Builder()
                        .header(Authorization, Bearer + entity.getToken())
                        .url(entity.getUrl())
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
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
                                        int id = result.getInt("id");
                                        Message msg = new Message();
                                        msg.what = 0;
                                        msg.obj = id;
                                        handler.sendMessage(msg);
                                    } catch (JSONException e) {
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

                        }
                    }
                });
            }
        });
    }

    /**
     * 获取精品课类别
     * @param url 请求地址
     * @param mapList 返回的结果集合
     */
    public static void getAllCourseType(final Handler handler, final String url, final List<Map<String, Object>> mapList, final String token){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header("Authorization", "Bearer " + token)
                        .url(url + "?CategoryId=2")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                //if (!TextUtils.isEmpty(json.getString("error"))) {handler.sendEmptyMessage(404); return;}
                                JSONObject result = json.getJSONObject("result");
                                JSONArray items = result.getJSONArray("items");

                                for (int i = 0; i < items.length(); i++){
                                    Map<String, Object> dataMap = new HashMap<>();
                                    JSONObject chileItem = items.getJSONObject(i);
                                    dataMap.put("name", chileItem.getString("name"));
                                    dataMap.put("id", chileItem.getInt("id"));
                                    mapList.add(dataMap);
                                }

                                handler.sendEmptyMessage(0);
                            } catch (JSONException e) {
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     *获取子分类
     */
    public static void getChildType(final Handler handler, final Map<String, Object> dataMap) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header("Authorization", "Bearer " + dataMap.get("token"))
                        .url(dataMap.get("url") + "?CourseType=" + dataMap.get("type") +"&MaxResultCount=5&SkipCount=0")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONArray items = getItems(response.body().string(), handler);
                                List<Map<String, Object>> mapList = new ArrayList<>();
                                for (int i =0; i < items.length(); i++){
                                    JSONObject childItem = items.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", childItem.getString("name"));
                                    map.put("id", childItem.getInt("id"));
                                    map.put("icon", childItem.getString("icon"));
                                    mapList.add(map);
                                }
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = mapList;
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
     * 获取课程的最终数据
     * @param handler
     * @param map  参数集
     * @param list 接受结果集
     */

    public static void getInChildData(final Handler handler, final Map<String, Object> map, final List<CourseDataEntity> list){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> url = new HashMap<>();
                url.put("CourseType", map.get("CourseType") == null ? "": map.get("CourseType"));
                url.put("CourseSubject", map.get("CourseSubject") == null ? "" : map.get("CourseSubject"));
                url.put("MediaType", map.get("MediaType") == null ? "" : map.get("MediaType"));
                url.put("MaxResultCount", 5);
                url.put("SkipCount", 0);

                Request request = new Request.Builder()
                        .header("Authorization", "Bearer " + map.get("token"))
                        .url(map.get("url") + setUrl(url))
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONArray items = getItems(response.body().string(), handler);
                                for (int i = 0; i < items.length(); i++){
                                    JSONObject childItem = items.getJSONObject(i);
                                    CourseDataEntity courseDataEntity = new CourseDataEntity();
                                    courseDataEntity.setName(childItem.getString("name"));
                                    courseDataEntity.setDescription(childItem.getString("description"));
                                    courseDataEntity.setPrice(childItem.getInt("price") + "");
                                    courseDataEntity.setPurchasedNumber(childItem.getInt("purchasedNumber") + "");
                                    courseDataEntity.setCover(childItem.getString("cover"));
                                    courseDataEntity.setStartTime(childItem.getString("startTime"));
                                    courseDataEntity.setMediaType(childItem.getInt("mediaType"));
                                    JSONObject courseContent = childItem.getJSONObject("courseContents");
                                    JSONArray childItems = courseContent.getJSONArray("items");
                                    List<ChildContent> chileList = new ArrayList<>();
                                    for (int j = 0; j < childItems.length(); j++){
                                        JSONObject itemss = childItems.getJSONObject(j);
                                        ChildContent content = new ChildContent();
                                        content.setLeaf(itemss.getBoolean("isLeaf"));
                                        content.setMediaType(itemss.getInt("mediaType"));
                                        content.setTarget(itemss.getString("target"));
                                        content.setName(itemss.getString("name"));
                                        chileList.add(content);
                                    }
                                    courseDataEntity.setCourseContents(chileList);
                                    list.add(courseDataEntity);
                                    handler.sendEmptyMessage(0);
                                }
                            } catch (JSONException e) {
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * 首页今日打卡的课程
     * @param handler
     * @param dataMap 参数集
     * @param courseData 数据集
     */
    public static void getCourseDataList(final Handler handler,  final Map<String, Object> dataMap, final List<HomeCourseEntity> courseData){
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                final Request request = new Request.Builder()
                        .header("Authorization", "Bearer " + dataMap.get("token"))
                        .url(dataMap.get("url").toString() + "?studentId=18&date=2018-08-17")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try{
                                JSONObject json = new JSONObject(response.body().string());
                                if (TextUtils.isEmpty(json.getString("error"))){
                                    handler.sendEmptyMessage(404);
                                    return;
                                }
                                JSONArray result = json.getJSONArray("result");
                                for (int i = 0; i < result.length(); i++){
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
                                    homeCourseEntity.setToken(dataMap.get("token").toString());
                                    courseData.add(homeCourseEntity);
                                }

                                handler.sendEmptyMessage(0);
                            }catch (JSONException e){
                                e.printStackTrace();
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
    public static void postSignIn(final Handler handler, final Map<String, Object> map){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("CourseInstId", map.get("CourseInstId"));
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "Bearer " + map.get("token"))
                            .url(map.get("url").toString())
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            handler.sendEmptyMessage(404);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()){
                                try {
                                    JSONObject json = new JSONObject(response.body().string());
                                    if (TextUtils.isEmpty(json.getString("error"))){
                                        handler.sendEmptyMessage(404);
                                        return;
                                    }
                                    handler.sendEmptyMessage(1);
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
     * 上传图片文件
     * @param handler
     * @param map
     * @param list
     */
    public static void postVideoPhoto(final Handler handler, final Map<String, Object> map, final List<Media> list, final List<String> filePathList){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (int i = 0; i < list.size()-1; i++){
                    File file = new File(list.get(i).path);
                    builder.addFormDataPart("file", file.getPath(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                }
                MultipartBody body = builder.build();
                final Request request = new Request.Builder()
                        .header("Authorization", "Bearer " + map.get("token"))
                        .url(map.get("url").toString())
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(404);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONArray result = jsonObject.getJSONArray("result");
                                for (int i = 0; i < result.length(); i++){
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
     * 提交作业
     * @param
     * @return
     */
    public static void postHomeWork(final Handler handler, final Map<String, Object> dataMap, final List<Map<String, Object>> filePathList){
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
                    for (int i = 0; i < filePathList.size(); i++){
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

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("result", "请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()){
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
     * @param handler
     * @param dataEntity 请求所需要的参数
     */
    public static void postLeave(final Handler handler, final RequestDataEntity dataEntity){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                LeaveEntity leaveEntity = new LeaveEntity();
                leaveEntity.setUserId(dataEntity.getUserId());
                leaveEntity.setScheduledCourseId(dataEntity.getCourseId());
                leaveEntity.setReason(dataEntity.getMsg());
                String json = new Gson().toJson(leaveEntity);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + dataEntity.getToken())
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            handler.sendEmptyMessage(0);
                        }else handler.sendEmptyMessage(404);
                    }
                });
            }
        });
    }

    /**
     * 检索专题
     * @param handler
     * @param dataEntity
     */
    public static void getTopics(final Handler handler, final RequestDataEntity dataEntity, final List<TopiceHttpResultEntity> resultList){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .header(Authorization, Bearer + dataEntity.getToken())
                        .url(dataEntity.getUrl() /*+ "?MaxResultCount=5&SkipCount=0"*/)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                String data = response.body().string();
                                Log.e("data", data);
                                JSONArray items = getItems(data, handler);
                                Gson gson = new Gson();
                                for (int i = 0; i < items.length(); i++){
                                    JSONObject childItem = items.getJSONObject(i);
                                    TopiceHttpResultEntity entity = new TopiceHttpResultEntity();//gson.fromJson(childItem.toString(), new TypeToken<TopiceHttpResultEntity>(){}.getType());
                                    entity.setCampusId(String.valueOf(childItem.get("campusId")));
                                    entity.setCommentNumber(childItem.getString("commentNumber"));
                                    entity.setContentpublic(childItem.getString("content"));
                                    entity.setCoverType(childItem.getString("coverType"));
                                    List<String> imgList = new ArrayList<>();
                                    JSONArray paths = childItem.getJSONArray("coverImgs");
                                    for (int j = 0; j < paths.length(); j++){
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
     * @param handler
     */
    public static void postComment(final Handler handler, final CPRCDataEntity cprcDataEntity){
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                RequestBody body = RequestBody.create(JSON, new Gson().toJson(cprcDataEntity));
                final Request request = new Request.Builder()
                        .header(Authorization, Bearer + cprcDataEntity.getToken())
                        .url(HttpEntity.MAIN_URL + HttpEntity.COURSE_COMMENT_CREATE)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                JSONObject result = json.getJSONObject("result");
                                Message msg = new Message();
                                if (cprcDataEntity.getType() == 1 && cprcDataEntity.getTargetType() == 3) msg.what = 2;
                                else msg.what = cprcDataEntity.getType();
                                msg.obj = result;
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
     * 获取全部评论或者回复
     * @param handler
     * @param cpc
     */
    public static void getCommentAll(final Handler handler, final GetAllCPC cpc){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                /*Map<String, Object> map = new HashMap<>();
                map.put("UserId", cpc.getUserId());
                map.put("Type", cpc.getType());
                map.put("Target", cpc.getTarget());
                map.put("TargetType", cpc.getTargetType());
                map.put("ParentType", cpc.getParentType());
                map.put("ParentId", cpc.getParentId());
                map.put("IsEmptyContent", cpc.isEmptyContent());
                map.put("MaxResultCount", cpc.getMaxResultCount());
                map.put("SkipCount", cpc.getSkipCount());*/

                String json = new Gson().toJson(cpc);

               RequestBody body = RequestBody.create(JSON, json);


                Request request = new Request.Builder()
                        .header(Authorization, Bearer + cpc.getToken())
                        .url(HttpEntity.MAIN_URL + HttpEntity.GET_COMMENT_ALL + "?Type=" + cpc.getType())
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                JSONObject result = json.getJSONObject("result");
                                Message message = new Message();
                                message.what = 20;
                                message.obj = result;
                                handler.sendMessage(message);
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
     * 更新评论、赞、回复内容状态
     * @param handler
     * @param id
     * @param token
     */
    public static void putAllStatue(final Handler handler, final int id, final String token){
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
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()){
                                handler.sendEmptyMessage(30);
                            }
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取评论
     * @param handler
     * @param entity
     */
    public static void getMyCommentOrPraise(final Handler handler, final RequestDataEntity entity){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(entity.getUrl() + "?userId=" + entity.getUserId())
                        .header(Authorization, Bearer + entity.getToken())
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                Message msg = new Message();
                                msg.what = 30;
                                msg.obj = getItems(response.body().string(), handler);
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

    public static void getMyCollection(final Handler handler, final RequestDataEntity entity){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(entity.getUrl() + "?userId=" + entity.getUserId() + "&targetType=" + entity.getType())
                        .header(Authorization, Bearer + entity.getToken())
                        .build();
                getHttpsClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()){
                            try{
                                JSONArray items = getItems(response.body().string(), handler);
                                Message msg = new Message();
                                msg.what = 10;
                                msg.obj = items;
                                handler.sendMessage(msg);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
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


    /**
     * 公共解析返回结果
     * @param body 获取的结果
     * @param handler handler
     * @return JSONArray
     * @throws JSONException JSONException
     */
    public static JSONArray getItems(String body, Handler handler) throws JSONException{
        JSONObject json = new JSONObject(body);
        JSONObject result = json.getJSONObject("result");
        if (TextUtils.isEmpty(json.getString("error"))) {handler.sendEmptyMessage(404); return null;}
        JSONArray items = result.getJSONArray("items");
        return items;
    }

    /**
     * 为get请求拼接参数
     * @param map
     * @return
     */
    public static String setUrl(Map<String, Object> map){
        StringBuilder builder = new StringBuilder("?");
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key =(String) iterator.next();
            builder.append(key).append("=").append(map.get(key)).append("&");
        }
        String s = builder.toString();
        return s.substring(0, s.length() -1);
    }

    /**
     * 为了使用https 而设
     * @return OkHttpClient
     */
    public static OkHttpClient getHttpsClient(){
        OkHttpClient newClient;
        newClient = client.newBuilder()
                .sslSocketFactory(HttpsTrustManager.createSSLSocketFactory())
                .hostnameVerifier(new HttpsTrustManager.TrustAllHostnameVerifier())
                .build();
        return newClient;
    }



    /**
     * 没有完善 暂时不用
     * @param handler
     * @param url
     * @param body
     * @param token
     */

    public static void postAll(final Handler handler, final String url, final RequestBody body, final String token){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .header("Authorization", "Bearer " + token)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){

                        }
                    }
                });
            }
        });
    }
}
