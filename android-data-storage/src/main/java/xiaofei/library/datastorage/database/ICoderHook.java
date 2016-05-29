package xiaofei.library.datastorage.database;

/**
 * Created by Eric on 16/5/29.
 */
public interface ICoderHook {

    String encode(String input);

    String decode(String input);

}
