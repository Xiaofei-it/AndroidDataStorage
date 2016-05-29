package xiaofei.library.datastorage.database;

/**
 * Created by Eric on 16/5/29.
 */
public class DefaultCoderHook implements ICoderHook {

    @Override
    public String decode(String input) {
        return input;
    }

    @Override
    public String encode(String input) {
        return input;
    }
}
