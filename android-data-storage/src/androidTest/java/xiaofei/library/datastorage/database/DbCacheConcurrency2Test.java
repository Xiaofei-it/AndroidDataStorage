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
public class DbCacheConcurrency2Test extends TestCase {

    private static final int MAX = 20;

    private DbCache dbCache;

    private ExecutorService executor = Executors.newFixedThreadPool(MAX);

    private AtomicInteger finish;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    @Before
    public void init() {
        dbCache = DbCache.getInstance(InstrumentationRegistry.getContext());
        dbCache.clearTable();
        assertEquals(dbCache.getAllObjects(String.class).size(), 0);
        //singleton will stay there!!!
    }

    @Test
    public void insert() {
        finish = new AtomicInteger(0);
        for (int i = 0; i < MAX; ++i) {
            final int tmp = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final String threadName = Thread.currentThread().getName();
                    for (int j = 0; j < MAX * MAX; ++j) {
                        String value = "Haha" + threadName + " " + tmp;
                        String id = threadName + " " + tmp + " " + j;
                        dbCache.insertObject(value,  id);
                        //dbCache.clearCache();
                        List<Pair<String, String>> list = dbCache.getObjects(String.class, new xiaofei.library.datastorage.util.Condition<String>() {
                            @Override
                            public boolean satisfy(String o) {
                                return o.equals("Haha" + threadName + " " + tmp);
                            }
                        });
                        assertEquals(list.size(), j + 1);
                    }
                    lock.lock();
                    finish.getAndIncrement();
                    condition.signalAll();
                    lock.unlock();
                }
            });
        }
        try {
            lock.lock();
            while (finish.get() < MAX) {
                condition.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        assertEquals(dbCache.getAllObjects(String.class).size(), MAX * MAX * MAX);

    }

    @Test
    public void insertAndClear() {
        for (int j = 0; j < MAX * MAX; ++j) {
            String value = "Haha";
            String id = "" + j;
            dbCache.insertObject(value,  id);
            dbCache.clearCache();
            List<Pair<String, String>> list = dbCache.getAllObjects(String.class);
            assertEquals(list.size(), j + 1);
        }
    }


    @After
    public void finish() {
        dbCache.clearTable();
        assertEquals(dbCache.getAllObjects(String.class).size(), 0);
    }

}
