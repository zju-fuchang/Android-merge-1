package cc.vipazoo.www.ui.controller;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.vipazoo.www.ui.model.Web_Message;
import cc.vipazoo.www.ui.model.User;
import cc.vipazoo.www.ui.model.Converter;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class LoginController {
    private static User user = new User();
    static Converter conv = new Converter();
    private static final OkHttpClient connection = new OkHttpClient();
    public String ret_login;
    public String ret_signup;
    public User getUser() {
        return user;
    }

//   public static void main(String[] argv)
//    {
////        user.setName("3160104050");
////        user.setPasswd("123456");
//        //user.setEmail_address("3160104050@zju.edu.cn");
//        LoginController loginController = new LoginController("", "");
//        try
//        {
//            loginController.login();
//            loginController.logout();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }


    public LoginController(String name, String passwd)
    {
        user.setName(name);
        user.setPasswd(passwd);
    }
    public LoginController(String name, String passwd, String email_address)
    {
        user.setName(name);
        user.setPasswd(passwd);
        user.setEmail_address(email_address);
    }

    public LoginController(User user)
    {
        this.user = user;
    }

    public void login()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return (f.getName().equals("shadow$monitor") || f.getName().equals("shadow$klass"));
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                }).create();
                RequestBody formbody = new FormBody.Builder()
                        .add("username", user.getName())
                        .add("password", user.getPasswd())
                        .build();
                Request request = new Request.Builder()
                        .url("http://10.15.82.223:9090/app_get_data/app_signincheck")
                        .post(formbody)
                        .build();
                Response response = connection.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code" + response);
                }
                String js = Converter.unicodeToUtf8(response.body().string());
                Web_Message msg;
                msg = gson.fromJson(js, Web_Message.class);
                System.out.println(msg.getmsg());
                ret_login = msg.getmsg();
                    if (ret_login.equals("登录成功")) {
                    user.settoken(msg.gettoken());
                }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void logout()
    {
        new Thread(new Runnable() {
            @Override
            public void run(){
                try{
                    Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return (f.getName().equals("shadow$monitor") || f.getName().equals("shadow$klass"));
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).create();
                    FormBody formbody = new FormBody.Builder()
                            .add("token", user.gettoken())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://10.15.82.223:9090/app_get_data/app_logout")
                            .post(formbody)
                            .build();
                    Response response = connection.newCall(request).execute();
                    if(!response.isSuccessful())
                    {
                        throw new IOException("Unexpected code" + response);
                    }
                    String js = new String(conv.unicodeToUtf8(response.body().string()));
                    Web_Message msg;
                    msg = gson.fromJson(js, Web_Message.class);
                    final String ret = msg.getmsg();
                    switch(ret) {
                        case("登出成功"):
                        {
                            // System.out.println("Successfully logout");
                        }
                        default:
                        {

                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();

    }
    public void register() throws  Exception
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return (f.getName().equals("shadow$monitor") || f.getName().equals("shadow$klass"));
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).create();
                    RequestBody formbody = new FormBody.Builder()
                            .add("username", user.getName())
                            .add("email", user.getEmail_address())
                            .add("password", user.getPasswd())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://10.15.82.223:9090/app_get_data/app_register")
                            .post(formbody)
                            .build();
                    Response response = connection.newCall(request).execute();
                    if(!response.isSuccessful())
                    {
                        throw new IOException("Unexpected code" + response);
                    }
                    String js = new String(conv.unicodeToUtf8(response.body().string()));
                    Web_Message msg;
                    msg = gson.fromJson(js, Web_Message.class);
                    ret_signup = msg.getmsg();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();

        }
}