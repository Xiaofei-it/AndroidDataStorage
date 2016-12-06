# AndroidDataStorage

一个简洁易用并且具有高性能的Android存储库

##特色

1、实现安卓设备上的对象持久化，能存储和读取任何对象。

2、读写速度比SharedPreference等基于文件的存储方案高。

3、大量对象读写操作的场景下，使用此框架能极大提升性能，比通常的数据库方案高效。

4、上层使用缓存，读写数据快速高效。底层使用数据库，在进程被终止并重新启动后能快速恢复对象。

5、接口简单易用，用户无需了解安卓存储机制。

6、经过严格测试，有良好的容错能力和稳定性。

##Gradle

```
dependencies {
    compile 'xiaofei.library:android-data-storage:1.3.0'
}
```

##Maven

```
<dependency>
  <groupId>xiaofei.library</groupId>
  <artifactId>android-data-storage</artifactId>
  <version>1.3.0</version>
  <type>pom</type>
</dependency>
```

##用法

###1. 获取接口

   获取IDataStorage接口，目前只支持数据库类型。
```
IDataStorage dataStorage = DataStorageFactory.getInstance(
                               getApplicationContext(),
                               DataStorageFactory.TYPE_DATABASE);
```
###2、存储原理

存储数据的时候，索引是类id和对象id。如果这两个id相同，则老数据被覆盖。读取数据的时候也需要提供这两个id。

###3、类id

给需要存储的类加上ClassId注解，里面写上类id。需要保证不同的类有不同的id。

如果不同版本的代码在混淆后，能保证该类的类名不变，那就不需要加注解。框架将类的包名和类名作为类id。

###4、对象id

如果类的某个字段就是对象id，那在那个字段之前加上ObjectId注解。该字段必须是String。框架将这个字段作为对象id。

如果没有这个字段，那读写数据的时候需要提供对象id作为函数的参数。

以下给出一个例子：

```
@ClassId("Order")
public class Order {
    @ObjectId
    private String mId;

    private int mState;

    public int getState() {
        return mState;
    }
    ...
    ...
}
```

###5、APIs

下文介绍存储和读取数据的API。

所有API操作都发生在内存中，而相应的数据库操作放在了后台线程中，所以这些API都可以在主线程调用，不会阻塞主线程。

####1、存储数据

存储对象order：

```
dataStorage.saveOrUpdate(order);
```

存储一个order列表：

```
List<Order> list = new ArrayList<Order>();
...
dataStorage.saveOrUpdate(list);
```

如果Order内部没有对象id的字段：

```
dataStorage.saveOrUpdate(order, "1001");

List<Order> list = new ArrayList<Order>();
List<String> ids = new ArrayList<ids>();
...
//list和ids一一对应
dataStorage.saveOrUpdate(list, ids);
```

####2、读取数据

读取一个数据

```
Order order = dataStorage.load(Order.class, "1001");
```

读取所有Order

```
List<Order> list = dataStorage.loadAll(Order.class);
```

读取mState为10的所有Order

```
List<Order> list = dataStorage.load(Order.class, new Condition() {
                     @Override
                     public boolean satisfy(Order o) {
                         return o.getState() == 10;
                     }
                   });
```

读取一批id的Order

```
List<String> ids = new ArrayList<String>();
...
List<Order> list = dataStorage.load(Order.class, ids);
```

以上函数都有一个参数Comparator供选择，提供Comparator后，获取的List是经过排序的。

```
List<Order> list = dataStorage.loadAll(Order.class, comparator);
```

我还做了一个工具类，可以方便地自动生成Comparator。详见[这里](https://github.com/Xiaofei-it/ComparatorGenerator)。

####3、删除数据

删除一个数据

```
dataStorage.delete(order);
```

如果Order类里没有提供对象id，那么

```
dataStorage.delete(Order.class, "1001");
```

删除所有Order

```
dataStorage.deleteAll(Order.class);
```

删除一批Order

```
List<Order> list = new ArrayList<Order>();
dataStorage.delete(list);
```

如果Order类里没有提供对象id，那么

```
List<String> ids = new ArrayList<String>();
...
dataStorage.delete(Order.class, ids);
```

删除mState为10的Order


```
dataStorage.delete(Order.class, new Condition() {
                     @Override
                     public boolean satisfy(Order o) {
                         return o.getState() == 10;
                     }
                   });
```

####4、其他API

还有许多API，具体请看[xiaofei.library.datastorage.IDataStorage](https://github.com/Xiaofei-it/AndroidDataStorage/blob/master/android-data-storage/src/main/java/xiaofei/library/datastorage/IDataStorage.java)。
