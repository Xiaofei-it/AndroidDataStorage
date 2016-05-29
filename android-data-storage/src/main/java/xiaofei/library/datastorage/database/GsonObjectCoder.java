/**
 *
 * Copyright 2015-2016 Xiaofei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package xiaofei.library.datastorage.database;

import com.google.gson.Gson;

import xiaofei.library.datastorage.util.CodeUtils;

/**
 * Created by Xiaofei on 16/3/16.
 */
public class GsonObjectCoder implements DbService.Coder {

    private Gson mGson;

    private ICoderHook mCoderHook;

    public GsonObjectCoder() {
        this(new DefaultCoderHook());
    }

    public GsonObjectCoder(ICoderHook coderHook) {
        mGson = new Gson();
        mCoderHook = coderHook;
    }

    @Override
    public <T> T decode(String string, Class<T> clazz) {
        try {
            return mGson.fromJson(mCoderHook.decode(string), clazz);
        } catch (RuntimeException e) {
            //To handle the exception, just simply ignore this element.
            return null;
        } catch (Exception e) {
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public String encode(Object object) {
        try {
            return mCoderHook.encode(mGson.toJson(object));
        } catch (RuntimeException e) {
            return null;
        } catch (Exception e) {
            return null;
        } catch (Throwable t) {
            return null;
        }
    }

}
