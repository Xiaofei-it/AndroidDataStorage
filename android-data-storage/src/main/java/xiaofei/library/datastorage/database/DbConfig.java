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

/**
 * Created by Xiaofei on 16/5/10.
 */
public class DbConfig {

    private static volatile int sVersion = 2;

    private DbConfig() {

    }

    public static void setVersion(int version) {
        sVersion = version;
    }

    public static int getVersion() {
        return sVersion;
    }

}
