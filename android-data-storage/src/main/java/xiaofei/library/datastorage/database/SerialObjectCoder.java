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
            byte[] bytes = CodeUtils.decodeToBytes(string);
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
