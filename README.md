# AndroidDataStorage
An easy-to-use and high-performance library for storing data in the Android system.

[Chinese Readme 中文文档](https://github.com/Xiaofei-it/AndroidDataStorage/blob/master/README-ZH-CN.md)

##Features

1. A framework for object persistence in Android, which can store any kind of objects.

2. Using cache to improve the performance of storing and accessing data.

3. Easy to use, even if you have no knowledge of the mechanics of Android storage.

4. Good Ability of fault tolerance and high stability.

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

##Usage

###1. Obtain the implementation of data storage

   Obtain the implementation of data storage through DataStorageFactory. Now it only supports the database version of data storage and more versions will be supported in the future.
```
IDataStorage dataStorage = DataStorageFactory.getInstance(
                               getApplicationContext(),
                               DataStorageFactory.TYPE_DATABASE);
```
###2. Principle

The framework stores an object according to its class's id and its own id. The former one is called the "class id" and the latter one is called the "object id".

If the framework stores two objects which have the same class id and the same object id, the one stored earlier is replaced by the one stored later.

The following will tell you how to give an object a class id and an object id.

###3. Class id

Add a @ClassId annotation on the class you want to store. Give the annotation a value, which indicates the class id.

Distinct classes should have distinct class ids.

If you guarantee that the class name remains the same in different versions of apps even after ProGuard is used, then the @ClassId annotation is not necessary. In this case, if you do not add a @ClassId annotation, the class name will be the class id.

###4. Object id

Often the object contains an object id as a field in itself. If there is such a field, then add an @ObjectId annotation on the field. Note that this field must be of the String type.

If there is no such a field, then you should provide an object id when storing and loading the object.

The following is an example illustrating how to add @ClassId and @ObjectId:

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

###5. APIs

The following introduces APIs for storing and loading data.

All of the APIs perform operations in the memory cache and put the database operations in the background, so feel free to call the APIs in the main thread.

####1. Storing

Store an order:

```
dataStorage.saveOrUpdate(order);
```

Store a list of orders:

```
List<Order> list = new ArrayList<Order>();
...
dataStorage.saveOrUpdate(list);
```

If there is no id in the Order class, then you should provide an object id:

```
dataStorage.saveOrUpdate(order, "1001");

List<Order> list = new ArrayList<Order>();
List<String> ids = new ArrayList<ids>();
...
// Each order in the list has a corresponding id in ids.
dataStorage.saveOrUpdate(list, ids);
```

####2. Loading

Load an order:

```
Order order = dataStorage.load(Order.class, "1001");
```

Load all orders:

```
List<Order> list = dataStorage.loadAll(Order.class);
```

Load the orders whose state is 10:

```
List<Order> list = dataStorage.load(Order.class, new Condition() {
                     @Override
                     public boolean satisfy(Order o) {
                         return o.getState() == 10;
                     }
                   });
```

Load a list of orders:

```
List<String> ids = new ArrayList<String>();
...
List<Order> list = dataStorage.load(Order.class, ids);
```

Each loading method has an alternative into which you can pass a comparator as a parameter. The return value of such a method will be sorted using the comparator.

```
List<Order> list = dataStorage.loadAll(Order.class, comparator);
```

I have write a helper class to provide an easy way to generate a comparator. See [Here](https://github.com/Xiaofei-it/ComparatorGenerator).

####3. Deleting

Delete an order:

```
dataStorage.delete(order);
```

If there is no id in the Order class, then you should provide an object id:

```
dataStorage.delete(Order.class, "1001");
```

Delete all orders:

```
dataStorage.deleteAll(Order.class);
```

Delete a list of orders:

```
List<Order> list = new ArrayList<Order>();
dataStorage.delete(list);
```

If there is no id in the Order class, then you should provide object ids:

```
List<String> ids = new ArrayList<String>();
...
dataStorage.delete(Order.class, ids);
```

Delete the orders whose state is 10:


```
dataStorage.delete(Order.class, new Condition() {
                     @Override
                     public boolean satisfy(Order o) {
                         return o.getState() == 10;
                     }
                   });
```

####4. Other APIs

There are a lot of APIs. Please See [xiaofei.library.datastorage.IDataStorage](https://github.com/Xiaofei-it/AndroidDataStorage/blob/master/android-data-storage/src/main/java/xiaofei/library/datastorage/IDataStorage.java).
