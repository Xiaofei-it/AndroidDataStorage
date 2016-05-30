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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import xiaofei.library.datastorage.util.CodeUtils;


/**
 * Created by Xiaofei on 16/3/21.
 */
public class SerialObjectCoder implements DbService.Coder {
    @Override
    public <T> T decode(String string, Class<T> clazz) {
        if (string == null || string.length() < 0) {
            throw new IllegalArgumentException();
        }
        try {
            byte[] bytes = CodeUtils.decode(string);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream oos = new ObjectInputStream(bais);
            return (T) oos.readObject();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String encode(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            byte[] bytes = baos.toByteArray();
            return CodeUtils.encode(bytes);
        } catch (NotSerializableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
