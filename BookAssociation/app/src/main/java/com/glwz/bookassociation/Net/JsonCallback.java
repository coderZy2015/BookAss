package com.glwz.bookassociation.Net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;

/**
 * 把服务器返回的数据解析成对象
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback(Type type){
        this.type = type;
    }

    public JsonCallback(Class<T> clazz){
        this.clazz = clazz;
    }


    /**
     * 拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
     * @param response 需要转换的对象
     * @return 转换后的结果
     * @throws Exception 转换过程发生的异常
     */
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null){
            return null;
        }

        T mData = null;
        Gson gson = new Gson();

        JsonReader jsonReader = new JsonReader(body.charStream());

        if(type != null){
            mData = gson.fromJson(jsonReader, type);
        } else if(clazz != null){
            mData = gson.fromJson(jsonReader,clazz);
        }else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            mData = gson.fromJson(jsonReader, type);
        }
        return mData;
    }

    @Override
    public void onError(Response<T> response) {
        Throwable exception = response.getException();
        if(exception != null){
            exception.printStackTrace();
        }
        if(exception instanceof UnknownHostException || exception instanceof ConnectException){
            Log.e("exception", "网络连接失败，请检查网络");
        }else if(exception instanceof SocketTimeoutException){
            Log.e("exception", "网络请求超时");
        }else if(exception instanceof HttpException){
            Log.e("exception", "网络端响应码404或者505了，请联系服务器开发人员");
        }else if(exception instanceof StorageException){
            Log.e("exception", "SD卡不存在或者没有权限");
        }else if(exception instanceof IllegalStateException){
            String message = exception.getMessage();
            Log.e("exception", message);
        }
    }

    //子类不需要去设置response.body()了，只需要拿到mResponse直接用就好了
    @Override
    public void onSuccess(Response<T> response) {

    }

}