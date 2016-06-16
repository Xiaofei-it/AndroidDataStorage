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

package xiaofei.library.datastorage;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import xiaofei.library.datastorage.database.DatabaseStorage;
import xiaofei.library.datastorage.database.DbCache;

/**
 * Created by Xiaofei on 16/6/16.
 */
@RunWith(AndroidJUnit4.class)
public class DbCacheTest extends TestCase {
    private DatabaseStorage storage;
    @Before
    public void init() {
        storage = DatabaseStorage.getInstance(InstrumentationRegistry.getContext());
    }

    @Test
    public void storeAndLoad() {
        storage.storeOrUpdate("ok", "1");
        String str = storage.load(String.class, "1");
        assertEquals(str, "ok");
    }
}
