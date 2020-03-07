package cn.lvsong.lib.library.rxbus;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * RxBus 辅助管理类
 * 如果在 Activity/Fragment 基类中使用 CompositeDisposable, 则可以不用这个类
 * Created by Jooyer on 2017/2/13
 */
public class RxBusHelper {

    private static LinkedHashMap<Integer, Disposable> mDisposables = new LinkedHashMap<>();

    /**
     * @param code --> 发送数据时使用的 Integer 类的 code
     * @return --> true  取消了订阅
     */
    public static boolean isDisposed(int code) {
        return mDisposables.get(code).isDisposed();
    }

    public static void add(int code, Disposable disposable) {
        if (null != disposable) {
            mDisposables.put(code, disposable);
        }
    }

    public static void remove(int code) {
        if (null != mDisposables.get(code)) {
            mDisposables.remove(code);
        }
    }

    public static void clear() {
        mDisposables.clear();
    }


    public static void unRegister(int code) {
        if (null != mDisposables.get(code)) {
            mDisposables.get(code).dispose();
        }
    }

    public static void unRegisterAll() {
        Set<Integer> keys = mDisposables.keySet();
        for (Integer key : keys) {
            Objects.requireNonNull(mDisposables.get(key)).dispose();
        }
    }


}