package xiaofei.library.datastorage.database;

import com.google.gson.Gson;

import xiaofei.library.datastorage.util.CodeUtils;

/**
 * Created by Xiaofei on 16/3/16.
 */
public class GsonObjectCoder implements DbService.Coder {

    private Gson mGson = new Gson();

    @Override
    public <T> T decode(String string, Class<T> clazz) {
        try {
            return mGson.fromJson(CodeUtils.decode(string), clazz);
        } catch (RuntimeException e) {
            //To handle the exception, just simply ignore this element.
            return null;
        }
    }

    @Override
    public String encode(Object object) {
        return CodeUtils.encode(mGson.toJson(object));
    }

}
