package com.example.zxf.myapp.helper;

import android.util.Log;

import com.example.zxf.myapp.model.SearchHistory;
import com.example.zxf.myapp.model.User;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * realm 数据库有关操作
 * **注意**：如果你创建Model并运行过，然后修改了Model。那么就需要升级数据库，否则会抛异常
 * RealmConfiguration支持的方法：
 * Builder.name : 指定数据库的名称。如不指定默认名为default。
 * Builder.schemaVersion : 指定数据库的版本号。
 * Builder.encryptionKey : 指定数据库的密钥。
 * Builder.migration : 指定迁移操作的迁移类。
 * Builder.deleteRealmIfMigrationNeeded : 声明版本冲突时自动删除原数据库。
 * Builder.inMemory : 声明数据库只在内存中持久化。
 * build : 完成配置构建。
 * JSON
 * realm将json字符串转化为对象
 * // 一个city model
 * public class City extends RealmObject {
 * private String city;
 * private int id;
 * // getters and setters left out ...
 * }
 * // 使用Json字符串插入数据
 * realm.executeTransaction(new Realm.Transaction() {
 *
 * @Override public void execute(Realm realm) {
 * realm.createObjectFromJson(City.class, "{ city: \"Copenhagen\", id: 1 }");
 * }
 * });
 * // 使用InputStream插入数据
 * realm.executeTransaction(new Realm.Transaction() {
 * @Override public void execute(Realm realm) {
 * try {
 * InputStream is = new FileInputStream(new File("path_to_file"));
 * realm.createAllFromJson(City.class, is);
 * } catch (IOException e) {
 * throw new RuntimeException();
 * }
 * }
 * });
 */
public class DataBaseHelper {

    private final Realm realm;
    //标记需要注入的依赖
    @Inject
    public DataBaseHelper() {
        //默认配置 default.realm
//        realm = Realm.getDefaultInstance();
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        .name("wanandroid.realm")
                        .schemaVersion(0)
//                        .migration(new MigrationOne()) //当发现新旧版本号不一致时，会自动调用迁移类完成迁移操作
                        .build();
        realm = Realm.getInstance(config);
        Log.i("kingblue", "DataBaseHelper: " + realm.getPath());
    }

    /**
     * 增
     * 保存搜索记录，若记录中已有，删除后，重新保存
     * 注意：如果在UI线程中插入过多的数据，可能会导致主线程拥塞
     */
    public void saveKeyWords(String keywords) {
        // 方法一：注意：在UI和后台线程同时开启创建write的事务，可能会导致ANR错误。为了避免该问题，可以使用executeTransactionAsync来实现。
        SearchHistory searchHistory = realm.where(SearchHistory.class).equalTo("keyword", keywords).findFirst();
//        realm.beginTransaction();
//        if (searchHistory != null) {
//            searchHistory.deleteFromRealm();
//        }
//        SearchHistory history = realm.createObject(SearchHistory.class);
//        history.setKeyword(keywords);
//        realm.commitTransaction();
//            方法二：
             realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (searchHistory != null) {
                                searchHistory.deleteFromRealm();
                            }
                            SearchHistory history = realm.createObject(SearchHistory.class);
                            history.setKeyword(keywords);
                        }
                    });
           /* 方法三：使用executeTransactionAsync该方法会开启一个子线程来执行事务，并且在执行完成后进行结果通知。
            注意：如果当Acitivity或Fragment被销毁时，在OnSuccess或OnError中执行UI操作，将导致程序奔溃 。
            用RealmAsyncTask .cancel();可以取消事务在onStop中调用，避免crash*/

//        RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                if (searchHistory != null) {
//                    searchHistory.deleteFromRealm();
//                }
//                SearchHistory history = realm.createObject(SearchHistory.class);
//                history.setKeyword(keywords);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                //操作成功
//                Log.i("realmtest", "onSuccess: ");
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                //操作失败
//                Log.i("realmtest", "onError: ");
//            }
//        });
//        return transaction;

        /*
        使用copyToRealmOrUpdate或copyToRealm方法插入数据
        当Model中存在主键的时候，推荐使用copyToRealmOrUpdate方法插入数据。如果对象存在，就更新该对象；
        反之，它会创建一个新的对象。若该Model没有主键，使用copyToRealm方法，否则将抛出异常。

            final User user = new User();
            user.setName("Jack");
            user.setId("2");
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(user);
                }
            });
         */

    }

    /**
     * 查
     * 查询搜索记录
     * 值得注意的是，这里并不会马上查到数据，是有一定延时的。也就是说，你马上使用userList的时候，里面是没有数据的。
     * 可以注册RealmChangeListener监听器，或者使用isLoaded()方法，判断是否查询完成
     * if (result.isLoaded()) {
     * // 完成查询
     * }
     */
    public List<SearchHistory> querySearchHistory() {
        RealmResults<SearchHistory> realmResults = realm.where(SearchHistory.class).findAll();
        return realm.copyFromRealm(realmResults);
        /*
            findAllAsync——异步查询
            当数据量较大，可能会引起ANR的时候，就可以使用findAllAsync
                     RealmResults<SearchHistory> realmResults = realm.where(SearchHistory.class).findAllAsync();
            多条件查询
                    RealmResults<User> userList = mRealm.where(User.class)
                    .equalTo("name", "Gavin")
                    .equalTo("dogs.name", "二哈")
                    .findAll();
            查询条件
                    sum()：对指定字段求和。
                    average()：对指定字段求平均值。
                    min(): 对指定字段求最小值。
                    max() : 对指定字段求最大值。count : 求结果集的记录数量。
                    findAll(): 返回结果集所有字段，返回值为RealmResults队列
                    findAllSorted() : 排序返回结果集所有字段，返回值为RealmResults队列
                    between(), greaterThan(),lessThan(), greaterThanOrEqualTo() & lessThanOrEqualTo()
                    equalTo() & notEqualTo()
                    contains(), beginsWith() & endsWith()
                    isNull() & isNotNull()
                    isEmpty()& isNotEmpty()
             查询name为“Gavin”和“Eric”的用户
                     RealmResults<User> userList = mRealm.where(User.class)
                    .equalTo("name", "Gavin")
                    .or().equalTo("name", "Eric")
                    .findAll();
              排序
                    RealmResults<User> userList = mRealm.where(User.class) .findAll();
                    userList = result.sort("age"); //根据age，正序排列
                    userList = result.sort("age", Sort.DESCENDING);//逆序排列
              聚合
                    RealmResult自带一些聚合方法：
                    RealmResults<User> results = realm.where(User.class).findAll();
                    long   sum     = results.sum("age").longValue();
                    long   min     = results.min("age").longValue();
                    long   max     = results.max("age").longValue();
                    double average = results.average("age");
                    long   matches = results.size();
         */
    }

    /**
     * 删除所有记录
     */
    public void deleteAllSearchHistory() {
        realm.beginTransaction();
        realm.where(SearchHistory.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * 删除某个数据
     * userList.deleteFirstFromRealm(); //删除user表的第一条数据
     * userList.deleteLastFromRealm();//删除user表的最后一条数据
     * results.deleteAllFromRealm();//删除user表的全部数据
     */
    public void deleteSingleSeachHistory(String keywords) {
        realm.beginTransaction();
        realm.where(SearchHistory.class).equalTo("keyword", keywords).findFirst().deleteFromRealm();
        realm.commitTransaction();
    }

    /**
     * 更改某个数据
     */
    public void updateSearchHistory(String keywords, String updaeWords) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SearchHistory searchHistory = realm.where(SearchHistory.class).equalTo("keyword", keywords).findFirst();
                searchHistory.setKeyword(updaeWords);
            }
        });
    }


    /**
     * --------------------------------------------保存个人信息---------------------------------------------
     **/
    public void saveUserInfo(String userName, String password, boolean isLogin) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.createObject(User.class);
                user.setPassword(password);
                user.setUsername(userName);
                user.setLogin(isLogin);
            }
        });
    }

    public User getUserInfo() {
        return realm.where(User.class).equalTo("isLogin", true).findFirst();
    }

    public void deleteUserInfo() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getUserInfo().deleteFromRealm();
            }
        });
    }


    /**
     * 关闭realm
     */
    public void closeRealm() {
        if (realm != null) {
            realm.close();
        }
    }


}
