package cn.lvsong.lib.library.rxbus;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/** https://github.com/JakeWharton/RxRelay
 *  事件总线核心类
 *
 *  https://www.jianshu.com/p/b5339f7bdfb3?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=qq
 *    ---> 可以参考大佬的
 *
 * Created by Jooyer on 2017/2/13
 */
public class RxBus {

    private static final String TAG = "RxBus";
    private static volatile RxBus mDefaultInstance;
    private Relay<Object> bus = null;



    //禁用构造方法
    private RxBus() {
        bus = PublishRelay.create().toSerialized();
    }

    public static RxBus getDefault(){
        if (null == mDefaultInstance){
            synchronized (RxBus.class){
                if (null == mDefaultInstance){
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }


    public void send(RxMessage event) {
        bus.accept(event);
    }




    public Disposable register(Class<RxMessage> eventType, final int code,
                               Scheduler scheduler,
                               Consumer<RxMessage> onNext) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
//                .delay(1, TimeUnit.SECONDS)
                .observeOn(scheduler)
                .subscribe(onNext);
    }


    public Disposable register(Class<RxMessage> eventType, final int code,
                               Scheduler scheduler,
                               Consumer<RxMessage> onNext, Consumer onError) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
//                .delay(1, TimeUnit.SECONDS)
                .observeOn(scheduler)
                .subscribe(onNext, onError);
    }


    public Disposable register(Class<RxMessage> eventType, final int code,
                               Scheduler scheduler,
                               Consumer<RxMessage> onNext, Consumer onError,
                               Action onComplete) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
//                .delay(1, TimeUnit.SECONDS)
                .observeOn(scheduler)
                .subscribe(onNext, onError, onComplete);
    }

    public Disposable register(Class<RxMessage> eventType, final int code,
                               Scheduler scheduler,
                               Consumer<RxMessage> onNext, Consumer onError,
                               Action onComplete, Consumer onSubscribe) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
//                .delay(1, TimeUnit.SECONDS)
                .observeOn(scheduler)
                .subscribe(onNext, onError, onComplete, onSubscribe);
    }

    /*-------------------------------------------------- 默认在主线程 -------------------------------------------------------------------*/
    public Disposable register(Class<RxMessage> eventType, final int code,
                               Consumer<RxMessage> onNext) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    public Disposable register(Class<RxMessage> eventType, final int code,int delay,
                               Consumer<RxMessage> onNext) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
                .delay(delay, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    public Disposable register(Class<RxMessage> eventType, final int code,
                               Consumer<RxMessage> onNext, Consumer onError,
                               Action onComplete) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
//                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete);
    }


    public Disposable register(Class<RxMessage> eventType, final int code,
                               Consumer<RxMessage> onNext, Consumer onError,
                               Action onComplete, Consumer onSubscribe) {
        return toObservable(eventType)
                .filter(new Predicate<RxMessage>() {
                    @Override
                    public boolean test(@NonNull RxMessage message) throws Exception {
                        return code == message.getCode();
                    }
                })
//                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError, onComplete, onSubscribe);
    }

    /*---------------------------------------------------------------------------------------------------------------------*/



    public void unregister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public Observable<RxMessage> toObservable(Class<RxMessage> eventType) {
        return bus.ofType(eventType);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }


}
