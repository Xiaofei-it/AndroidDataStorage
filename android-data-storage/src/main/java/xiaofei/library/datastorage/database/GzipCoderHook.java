package xiaofei.library.datastorage.database;

import java.io.UnsupportedEncodingException;

import xiaofei.library.datastorage.util.CodeUtils;
import xiaofei.library.datastorage.util.GzipUtils;

/**
 * Created by Eric on 16/5/29.
 */
public class GzipCoderHook implements ICoderHook {
    @Override
    public String decode(String input) {
        try {
            return new String(GzipUtils.decompress(CodeUtils.decode(input)), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    @Override
    public String encode(String input) {
        try {
            return CodeUtils.encode(GzipUtils.compress(input.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
