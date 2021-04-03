package hw21cache.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> dataStore = new WeakHashMap<>();
    private final Map<String, V> datastoreIntLong = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K,V>>> weakListeners = new ArrayList<>();

    public MyCache(){   }

    @Override
    public void put(K key, V value) {
        if (checkKeyIfIntOrLong(key)){
            datastoreIntLong.put(key.toString(),value);
        }
        else {
            dataStore.put(key,value);
        }
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value;
        if (checkKeyIfIntOrLong(key)){
            value = datastoreIntLong.remove(key.toString());
        }
        else {
            value = dataStore.remove(key);
        }
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value;
        if (checkKeyIfIntOrLong(key)){
            value = datastoreIntLong.get(key.toString());
        }
        else {
            value = dataStore.get(key);
        }
        if (value != null) {
            notify(key, value, "get");
        }
        else {
            notify(key, value, "got null");
        }
        return value;
    }

    @Override
    public  boolean containsKey(K key) {
        if (checkKeyIfIntOrLong(key)) {
            return datastoreIntLong.containsKey(key.toString());
        }
        return dataStore.containsKey(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        for (var weakLsnr : weakListeners) {
            if(listener == weakLsnr.get()) return;
        }

        WeakReference<HwListener<K,V>> weakLsnr = new WeakReference<>(listener);
        weakListeners.add(weakLsnr);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        for (int i = weakListeners.size()-1; i >=0; i-- ) {
            HwListener<K, V> lsnr = weakListeners.get(i).get();
            if (lsnr == listener || lsnr == null) {
                weakListeners.remove(i);
            }
        }
    }

    @Override
    public int size(){
        return Math.max(dataStore.size(),datastoreIntLong.size());
    }

    @Override
    public int sizeListenersList(){
        return weakListeners.size();
    }

    private boolean checkKeyIfIntOrLong(K key) {
        return (key.getClass() == Integer.class) || (key.getClass() == Long.class);
    }

    private void notify(K key, V value, String action){
        for (int i = weakListeners.size()-1; i >=0; i-- ) {
            HwListener<K, V> lsnr = weakListeners.get(i).get();
            if (lsnr != null) {
                try {
                    lsnr.notify(key, value, action);
                } catch (RuntimeException e) {
                    logger.error("listener exception");}
            }
            else {
                weakListeners.remove(i);
            }
        }
    }

}