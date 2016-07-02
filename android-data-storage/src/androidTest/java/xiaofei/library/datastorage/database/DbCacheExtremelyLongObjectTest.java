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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Xiaofei on 16/7/2.
 */
@RunWith(AndroidJUnit4.class)
public class DbCacheExtremelyLongObjectTest extends TestCase {

    private static final int LENGTH = 10000000;

    private DbCache dbCache;

    @Before
    public void init() {
        dbCache = DbCache.getInstance(InstrumentationRegistry.getContext());
        dbCache.clearTable();
        assertEquals(dbCache.getAllObjects(String.class).size(), 0);
    }

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; ++i) {
            sb.append("a");
        }
        dbCache.insertObject(sb.toString(), "key");
        dbCache.clearCache();
        String s = dbCache.getObject(String.class, "key");
        assertEquals(s.length(), LENGTH);
    }


    @After
    public void finish() {
        dbCache.clearTable();
        assertEquals(dbCache.getAllObjects(String.class).size(), 0);
    }

}
