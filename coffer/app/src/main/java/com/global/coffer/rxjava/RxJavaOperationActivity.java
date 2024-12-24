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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 1、当上游发送了一个onComplete后, 上游onComplete之后的事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
 * 2、当上游发送了一个onError后, 上游onError之后的事件将继续发送, 而下游收到onError事件之后将不再继续接收事件.
 * 3、上游可以不发送onComplete或onError.
 * 最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
 * 4、Disposable, 这个单词的字面意思是一次性用品,用完即可丢弃的。调用dispose()并不会导致上游不再继续发送事件, 上游会继续发送剩余的事件.
 * 5、不带任何参数的subscribe() 表示下游不关心任何事件,你上游尽管发你的数据去吧, 老子可不管你发什么.
 * 6、带有一个Consumer参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见
 * 7、subscribeOn() 指定的是上游发送事件的线程, observeOn() 指定的是下游接收事件的线程.
 * 8、多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
 * 9、多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
 * 10、在RxJava中, 已经内置了很多线程选项供我们选择, 例如有
 *
 *  * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
 *  * Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
 *  * Schedulers.newThread() 代表一个常规的新线程
 *  * AndroidSchedulers.mainThread() 代表Android的主线程
 *  这些内置的Scheduler已经足够满足我们开发的需求, 因此我们应该使用内置的这些选项,
 *  在RxJava内部使用的是线程池来维护这些线程, 所有效率也比较高.
 *
 * 11、
 * 12、
 * 13、
 * 14、
 * 15、
 * 16、
 * 17、
 * 18、
 * 19、
 * 20、
 */

public class RxJavaOperationActivity extends AppCompatActivity {

    private final String TAG = "coffer_rx";

    private Button mButton1;
    private TextView mTextView1;

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
                test7();
            }
        });

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
    private void test7(){
        String baseUrl = "https://www.wanandroid.com/banner/json/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        IBannerApi iBannerApi = retrofit.create(IBannerApi.class);
        iBannerApi.getBannerInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BannerBean bannerBean) {
                        Log.d(TAG, "onNext thread is : " + Thread.currentThread().getName());
                        if (bannerBean != null){
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
}