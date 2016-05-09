package xiaofei.library.datastorage.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Xiaofei on 16/3/25.
 *
 * Indicates the class id of the annotated class.
 *
 * Before an app is released, its source code is always be obfuscated. The class name is therefore
 * also obfuscated.
 *
 * The data storage stores the objects according to their class id and object id.
 *
 * For simplification, the class name can be used as the unique id of each class. However, the name
 * of the class may not be the same after obfuscation each time the app of different versions is
 * released. Objects thus cannot be stored and loaded across the app of different versions.
 *
 * Therefore, an new technique should be applied to guarantee the objects stored in earlier version
 * can be loaded in later version. This annotation indicates the class id, which is the value of the
 * annotation, of the annotated class.
 *
 * Distinct classes should have distinct class ids. If not, the behavior is unspecified.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassId {

    String value();

}
