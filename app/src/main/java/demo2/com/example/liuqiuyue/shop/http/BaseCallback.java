package demo2.com.example.liuqiuyue.shop.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/4/9.
 * 基本的回调
 */

public abstract class BaseCallback<T> {
    /**
     * type用于Gson解析
     */
    public Type mType;

    /**
     * 将type装换成相应的类
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 构造方法，获得type的类
     */
    public BaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    /**
     * 访问网络之前调用
     *
     * @param request
     */
    protected abstract void OnRequestBefore(Request request);

    /**
     * 访问网络失败
     *
     * @param call
     * @param e
     */
    public abstract void onFailure(Call call, IOException e);

    public abstract void onResponse(Response response);

    /**
     * 访问网络成功且接收
     *
     * @param call
     * @param response
     * @param t
     */
    public abstract void onSuccess(Call call, Response response, T t);

    /**
     * 访问网络成功但未接受，状态码小于200，大于300
     *
     * @param call
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Call call, Response response, int code, Exception e);

    /**
     * Token验证失败，验证码：401 402 403 等是调用这个方法
     * @param response
     * @param code
     */
    public  abstract void onTokenError( Response response, int code);

}
