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

import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Xiaofei on 16/6/16.
 */
@RunWith(AndroidJUnit4.class)
public class Test01 extends TestCase {
    int i = 0;
    @Before
    public void f() {
        System.out.println("Before");
        assertEquals(i, 0);
        i = 1;
    }

    @Test
    public void g() {
        System.out.println("Test");
        i = i + 1;
        assertEquals(i, 2);
    }

    @Test
    public void h() {
        ++i;
        assertEquals(i, 2);
    }
}
