package com.global.coffer.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.global.coffer.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 1、当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
 * 2、当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
 * 3、上游可以不发送onComplete或onError.
 * 最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError,
 * 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
 * 4、Disposable, 这个单词的字面意思是一次性用品,用完即可丢弃的。调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
 * 5、不带任何参数的subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 老子可不管你发什么.
 * 6、带有一个Consumer参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见
 * 7、subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
 * 8、多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
 * 9、多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
 * 10、在RxJava中, 已经内置了很多线程选项供我们选择, 例如有
 * <p>
 * * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
 * * Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
 * * Schedulers.newThread() 代表一个常规的新线程
 * * AndroidSchedulers.mainThread() 代表Android的主线程
 * 这些内置的Scheduler已经足够满足我们开发的需求, 因此我们应该使用内置的这些选项,
 * 在RxJava内部使用的是线程池来维护这些线程, 所有效率也比较高.
 * <p>
 * 11、通过Map, 可以将上游发来的事件转换为任意的类型, 可以是一个Object, 也可以是一个集合
 * 12、FlatMap将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.
 * 13、flatMap并不保证事件的顺序,如果需要保证顺序则需要使用concatMap.
 * 14、使用flatMap 可以解决接口嵌套问题，例如在请求注册接口完成后，在请求登录接口
 * 15、Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件.
 * * 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据
 * 16、Zip 可以实现从两个服务器接口中获取数据, 而只有当两个都获取到了之后才能进行展示, 这个时候就可以用Zip了
 * 17、当上下游工作在同一个线程中时, 这时候是一个同步的订阅关系, 也就是说上游每发送一个事件必须等到下游接收处理完了以后才能接着发送下一个事件.
 * 18、当上下游工作在不同的线程中时, 这时候是一个异步的订阅关系, 这个时候上游发送数据不需要等待下游接收,
 * 为什么呢, 因为两个线程并不能直接进行通信, 因此上游发送的事件并不能直接到下游里去。
 * 19、
 * 20、
 */

public class RxJavaOperationActivity extends AppCompatActivity {

    private final String TAG = "coffer_rx";

    private Button mButton1;
    private TextView mTextView1;

    private String desc = "";

    String baseUrl = "https://www.wanandroid.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    IwanandroidApi iwanandroidApi = retrofit.create(IwanandroidApi.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_main);
        mButton1 = findViewById(R.id.b0);
        mTextView1 = findViewById(R.id.tv0);
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test7();
//                test8();
//                test12();
                test15();
            }
        });
//        test9();
//        test10();
//        test11();
//        test13();
//        test14();
//        test16();
//        test17();
//        test18();
//        test19();
        test20();
        test21();
        test22();
        test23();
        test24();
        test25();
        test26();
        test27();
        test28();
        test29();

    }

    private void test1() {
        // 1、创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        // 2、创建一个下游Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext : " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
        // 3、建立连接
        observable.subscribe(observer);
        // 注意: 只有当上游和下游建立连接之后, 上游才会开始发送事件. 也就是调用了subscribe() 方法之后才开始发送事件.
    }

    // test2作用和test1 等价，只是写法上是2是链式
    private void test2() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            // Observable 是上游发送者
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            // Observer 是下游接收者
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext : " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }

    private void test3() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "onNext: " + value);
                i++;
                if (i == 2) {
                    Log.d(TAG, "dispose");
                    mDisposable.dispose();
                    Log.d(TAG, "isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void test4() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @SuppressLint("CheckResult")
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                emitter.onComplete();
                Log.d(TAG, "emit 4");
                emitter.onNext(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "onNext: " + integer);
                Log.d(TAG, "Consumer thread is : " + Thread.currentThread().getName());
            }
        });
    }

    /**
     * 实现子线程发消息，主线程接收消息
     */
    @SuppressLint("CheckResult")
    private void test5() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + integer);
            }
        };
        // subscribeOn() 指定的是上游发送事件的线程,
        // observeOn() 指定的是下游接收事件的线程.
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    /**
     * 实现子线程发消息，主线程接收消息
     */
    @SuppressLint("CheckResult")
    private void test6() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
            }
        });

        Consumer<Integer> consumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
//                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
//                Log.d(TAG, "onNext: " + integer);
            }
        };
        // subscribeOn() 指定的是上游发送事件的线程,
        // observeOn() 指定的是下游接收事件的线程.
        observable
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "Observer_haha thread is :" + Thread.currentThread().getName());
                        Log.d(TAG, "onNext_haha: " + integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "Observer_lala thread is :" + Thread.currentThread().getName());
                        Log.d(TAG, "onNext_lala: " + integer);
                    }
                })
                .subscribe(consumer);
    }

    //进行网络请求
    private void test7() {
        iwanandroidApi.getBannerInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BannerBean bannerBean) {
                        Log.d(TAG, "onNext thread is : " + Thread.currentThread().getName());
                        if (bannerBean != null) {
                            Log.d(TAG, "bannerBean Desc: " + bannerBean.getData().get(0).getDesc());
                            Log.d(TAG, "bannerBean title: " + bannerBean.getData().get(1).getTitle());
                            mTextView1.setText(bannerBean.getData().get(0).getDesc());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete thread is : " + Thread.currentThread().getName());
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void test8() {
        iwanandroidApi.getBannerInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bannerBean -> {
                    Log.d(TAG, "onNext thread is : " + Thread.currentThread().getName());
                    if (bannerBean != null) {
                        Log.d(TAG, "bannerBean Desc: " + bannerBean.getData().get(0).getDesc());
                        Log.d(TAG, "bannerBean title: " + bannerBean.getData().get(1).getTitle());
                        mTextView1.setText(bannerBean.getData().get(0).getDesc());
                    }
                }, throwable -> {
                    Log.d(TAG, "onNext thread is : " + Thread.currentThread().getName());
                    Log.d(TAG, "登录失败");
                });
    }

    @SuppressLint("CheckResult")
    private void test9() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            Log.d(TAG, "emitter is : " + Thread.currentThread().getName());
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                Log.d(TAG, "apply thread is : " + Thread.currentThread().getName());
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept thread is : " + Thread.currentThread().getName());
                Log.d(TAG, s);
            }
        });
    }

    /**
     * 我们在flatMap中将上游发来的每个事件转换为一个新的发送三个String事件的水管, 为了看到flatMap结果是无序的,所以加了10毫秒的延时
     */
    @SuppressLint("CheckResult")
    private void test10() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(20, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    /**
     * concatMap, 它和flatMap的作用几乎一模一样, 只是它的结果是严格按照上游发送的顺序来发送的,
     */
    @SuppressLint("CheckResult")
    private void test11() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                return Observable.fromIterable(list).delay(20, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void test12() {
//        String baseUrl = "https://www.wanandroid.com/banner/json/";
        iwanandroidApi.getBannerInfo()  // 发起获取Banner信息请求
                .subscribeOn(Schedulers.io()) // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 回到主线程去处理请求结果
                .doOnNext(new Consumer<BannerBean>() {
                    @Override
                    public void accept(BannerBean bannerBean) throws Exception {
                        Log.d(TAG, "bannerBean accept thread is : " + Thread.currentThread().getName());
                        if (bannerBean != null) {
                            desc = bannerBean.getData().get(0).getDesc();
                            Log.d(TAG, "bannerBean Desc: " + desc);
                        }
                    }
                })
                .observeOn(Schedulers.io()) // 回到IO线程去获取流行接口数据信息
                .flatMap(new Function<BannerBean, ObservableSource<PopularInfoBean>>() {
                    @Override
                    public ObservableSource<PopularInfoBean> apply(BannerBean bannerBean) throws Exception {
                        return iwanandroidApi.getPopularInfo();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  // 回到主线程去处理流行数据获取成功的结果
                .subscribe(new Consumer<PopularInfoBean>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void accept(PopularInfoBean popularInfoBean) throws Exception {
                        Log.d(TAG, "PopularInfoBean accept thread is : " + Thread.currentThread().getName());
                        mTextView1.setText(desc + popularInfoBean.getData().get(0).getName());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "获取流行数据失败");
                    }
                });
    }

    /**
     * 下面的Demo 是 第一个水管发完了消息，然后再发第二个水管里的数据，因为这两个都在同一个线程里，因此会有先后顺序
     */
    @SuppressLint("CheckResult")
    private void test13() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                Log.d(TAG, "emit complete1");
                emitter.onComplete();
            }
        });

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit A");
                emitter.onNext("A");
                Log.d(TAG, "emit B");
                emitter.onNext("B");
                Log.d(TAG, "emit C");
                emitter.onNext("C");
                Log.d(TAG, "emit complete2");
                emitter.onComplete();
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }

    /**
     * 下面是将两个发布水管放在各自独立的IO线程中
     */
    @SuppressLint("CheckResult")
    private void test14() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit 4");
                emitter.onNext(4);
                Log.d(TAG, "emit complete1");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "emit A");
                emitter.onNext("A");
                Log.d(TAG, "emit B");
                emitter.onNext("B");
                Log.d(TAG, "emit C");
                emitter.onNext("C");
                Log.d(TAG, "emit complete2");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
    }

    @SuppressLint("CheckResult")
    private void test15() {
        Observable<BannerBean> observable1 = iwanandroidApi.getBannerInfo().subscribeOn(Schedulers.io());
        Observable<PopularInfoBean> observable2 = iwanandroidApi.getPopularInfo().subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<BannerBean, PopularInfoBean, String>() {
            @Override
            public String apply(BannerBean bannerBean, PopularInfoBean popularInfoBean) throws Exception {
                String res = "";
                if (bannerBean != null && popularInfoBean != null) {
                    res = bannerBean.getData().get(0).getDesc() + popularInfoBean.getData().get(0).getName();
                }
                return res;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mTextView1.setText(s);
            }
        });
    }

    /**
     * 使用filter 过滤上游发的数据
     */
    @SuppressLint("CheckResult")
    private void test16() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; ; i++) {
                            emitter.onNext(i);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 10 == 0;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });
    }

    /**
     * 使用sample 取样，让它每隔2秒取一个事件给下游
     */
    @SuppressLint("CheckResult")
    private void test17() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0;;i++){
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .sample(2,TimeUnit.SECONDS)//sample取样
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "" + integer);
            }
        });
    }

    /**
     * 既然上游发送事件的速度太快, 那我们就适当减慢发送事件的速度, 从速度上取胜。我们给上游加上延时了之后
     * 内存即不会暴涨导致OOM，上游的事件数据也没有丢失。
     */
    @SuppressLint("CheckResult")
    private void test18() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0;;i++){
                            emitter.onNext(i);
                            //每次发送完事件延时2秒
                            Thread.sleep(2);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });
    }

    /**
     * 使用Flowable 创建上游，使用Subscriber 创建下游。
     * Flowable在设计的时候采用了一种新的思路也就是响应式拉取的方式来更好的解决上下游流速不均衡的问题
     *
     * 在下游的onSubscribe方法中传给我们的不再是Disposable了, 而是Subscription,
     * 它俩有什么区别呢, 首先它们都是上下游中间的一个开关, 之前我们说调用Disposable.dispose()方法可以切断水管,
     * 同样的调用Subscription.cancel()也可以切断水管, 不同的地方在于Subscription增加了一个void request(long n)方法。
     *
     * request当做是一种能力, 当成下游处理事件的能力, 下游能处理几个就告诉上游我要几个,
     * 这样只要上游根据下游的处理能力来决定发送多少事件, 就不会造成一窝蜂的发出一堆事件来,
     * 从而导致OOM. 这也就完美的解决之前我们所学到的两种方式的缺陷, 过滤事件会导致事件丢失, 减速又可能导致性能损失.
     * 而这种方式既解决了事件丢失的问题, 又解决了速度的问题, 完美 !
     *
     * 这里需要注意的是, 只有当上游正确的实现了如何根据下游的处理能力来发送事件的时候, 才能达到这种效果,
     * 如果上游根本不管下游的处理能力, 一股脑的瞎他妈发事件, 仍然会产生上下游流速不均衡的问题,
     */
    @SuppressLint("CheckResult")
    private void test19() {
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); // BackpressureStrategy 这里是设置背压策略

        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
        upstream.subscribe(downstream);
    }

    @SuppressLint("CheckResult")
    private void test20() {

    }
    @SuppressLint("CheckResult")
    private void test21() {

    }
    @SuppressLint("CheckResult")
    private void test22() {

    }
    @SuppressLint("CheckResult")
    private void test23() {

    }
    @SuppressLint("CheckResult")
    private void test24() {

    }
    @SuppressLint("CheckResult")
    private void test25() {

    }
    @SuppressLint("CheckResult")
    private void test26() {

    }
    @SuppressLint("CheckResult")
    private void test27() {

    }
    @SuppressLint("CheckResult")
    private void test28() {

    }
    @SuppressLint("CheckResult")
    private void test29() {

    }

}
