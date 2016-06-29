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

package xiaofei.library.datastorage.util;

/**
 * Created by Xiaofei on 16/3/17.
 *
 * Utilities for encoding and decoding.
 */
public class CodeUtils {

    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    };

    private CodeUtils() {

    }

    public static String encode(byte[] input) {
        if (input.length < 0) {
            throw new IllegalArgumentException("Input length is less than 0.");
        }
        StringBuilder result = new StringBuilder();
        int len = input.length;
        for (int i = 0; i < len; i++) {
            result.append(DIGITS[(0xF0 & input[i]) >>> 4]).append(DIGITS[0x0F & input[i]]);
        }
        return result.toString();
    }

    private static int charToInt(char ch) {
        return '0' <= ch && ch <= '9' ? ch - '0' : ch - 'A' + 10;
    }

    public static byte[] decode(String input) {
        int len = input.length();
        if (len < 0 || len % 2 != 0) {
            throw new IllegalArgumentException("Input error.");
        }
        byte[] result = new byte[len / 2];
        for (int i = 0, j = 0; i < len;) {
            int tmp = charToInt(input.charAt(i++)) << 4;
            tmp = tmp | charToInt(input.charAt(i++));
            result[j++] = (byte) tmp;
        }
        return result;
    }

}