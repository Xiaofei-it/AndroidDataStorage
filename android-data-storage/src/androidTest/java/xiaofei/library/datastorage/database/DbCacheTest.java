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

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import xiaofei.library.datastorage.util.Condition;

/**
 * Created by Xiaofei on 16/6/16.
 */
@RunWith(AndroidJUnit4.class)
public class DbCacheTest extends TestCase {
    private DbCache dbCache;
    @Before
    public void init() {
        dbCache = DbCache.getInstance(InstrumentationRegistry.getContext());
        dbCache.clearTable();
        //singleton will stay there!!!
    }

    @Test
    public void storeAndLoad() {
        dbCache.insertObject("ok", "1");
        dbCache.clearCache();
        String str = dbCache.getObject(String.class, "1");
        assertEquals(str, "ok");

        dbCache.clearCache();
        List<Object> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("A");
        list1.add("B");
        list2.add("2");
        list2.add("3");
        dbCache.insertObjects(list1, list2);
        dbCache.clearCache();
        List<Pair<String, String>> stringList = dbCache.getAllObjects(String.class);
        assertEquals(stringList.size(), 3);

        dbCache.clearCache();
        stringList = dbCache.getObjects(String.class, new Condition<String>() {
            @Override
            public boolean satisfy(String o) {
                return true;
            }
        });
        assertEquals(stringList.size(), 3);

        dbCache.clearCache();
        dbCache.deleteObject(String.class, "1");
        dbCache.clearCache();
        stringList = dbCache.getAllObjects(String.class);
        assertEquals(stringList.size(), 2);

        dbCache.clearCache();
        dbCache.insertObject("C", "4");
        dbCache.clearCache();
        dbCache.deleteObjects(String.class, list2);
        dbCache.clearCache();
        stringList = dbCache.getAllObjects(String.class);
        assertEquals(stringList.size(), 1);

        dbCache.clearCache();
        dbCache.deleteObjects(String.class, new Condition<String>() {
            @Override
            public boolean satisfy(String o) {
                return true;
            }
        });
        stringList = dbCache.getAllObjects(String.class);
        assertEquals(stringList.size(), 0);

        dbCache.insertObject("A", "1");
        dbCache.insertObject("B", "2");
        dbCache.clearCache();
        stringList = dbCache.getAllObjects(String.class);
        assertEquals(stringList.size(), 2);

        dbCache.clearCache();
        dbCache.deleteAllObjects(String.class);
        dbCache.clearCache();
        stringList = dbCache.getAllObjects(String.class);
        assertEquals(stringList.size(), 0);

    }

}
