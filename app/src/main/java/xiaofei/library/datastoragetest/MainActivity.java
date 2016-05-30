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

package xiaofei.library.datastoragetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;
import xiaofei.library.datastorage.annotation.ObjectId;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final IDataStorage dataStorage = DataStorageFactory.getInstance(this, DataStorageFactory.TYPE_DATABASE);
        findViewById(R.id.store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<B> list = new ArrayList<B>();
                list.add(new B(new A("abc"), new BigDecimal("0.9")));
                list.add(new B(new A("xyz"), new BigDecimal("90")));
                dataStorage.storeOrUpdate(list);
            }
        });
        findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<B> list = dataStorage.loadAll(B.class);
                StringBuilder sb = new StringBuilder();
                for (B b : list) {
                    sb.append(b.a.s).append(' ').append(b.bigDecimal.toString()).append(' ');
                }
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class A {
        String s = "Test";
        A(String s) {
            this.s = s;
        }
    }

    private static class B {
        A a;
        BigDecimal bigDecimal;
        @ObjectId
        public String get() {
            return a.s;
        }
        B(A a, BigDecimal bigDecimal) {
            this.a = a;
            this.bigDecimal = bigDecimal;
        }
    }
}
