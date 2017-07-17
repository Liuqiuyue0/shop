package demo2.com.example.liuqiuyue.shop.utils;

import android.content.Context;
import android.text.TextUtils;

import demo2.com.example.liuqiuyue.shop.Contants;
import demo2.com.example.liuqiuyue.shop.bean.User;

/**
 * ProjectName:YayaShop
 * Autor： <a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * <p/>
 */
public class UserLocalData {

    public static void putUser(Context context,User user){

        String user_json =  JSONUtil.toJSON(user);  //转成Json格式
        PreferencesUtils.putString(context, Contants.USER_JSON,user_json);

    }

    public static void putToken(Context context,String token){

        PreferencesUtils.putString(context, Contants.TOKEN,token);
    }


    public static User getUser(Context context){

        String user_json= PreferencesUtils.getString(context,Contants.USER_JSON);
        if(!TextUtils.isEmpty(user_json)){
            return  JSONUtil.fromJson(user_json,User.class);  //转成Json对象
        }
        return  null;
    }

    public static  String getToken(Context context){

        return  PreferencesUtils.getString( context,Contants.TOKEN);

    }


    public static void clearUser(Context context){

        PreferencesUtils.putString(context, Contants.USER_JSON,"");

    }

    public static void clearToken(Context context){

        PreferencesUtils.putString(context, Contants.TOKEN,"");
    }



}
