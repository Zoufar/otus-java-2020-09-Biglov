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
          if ((key.getClass() == Integer.class) || (key.getClass() == Long.class)){
          String keyString = key.toString();
          datastoreIntLong.put(keyString,value);
        }
        else { dataStore.put(key,value); }
        for (var weakLsnr : weakListeners) {
            HwListener<K,V> lsnr = weakLsnr.get();
            if (lsnr != null ) lsnr.notify(key, value, "put");
        }
    }

    @Override
    public void remove(K key) {
        V value;
        if ((key.getClass() == Integer.class) || (key.getClass() == Long.class)){
            String keyString = key.toString();
            value = datastoreIntLong.remove(keyString);
        }
        else { value = dataStore.remove(key); }
        for (var weakLsnr : weakListeners) {
            HwListener<K,V> lsnr = weakLsnr.get();
            if (lsnr != null ) lsnr.notify(key, value, "remove");
        }
    }

    @Override
    public V get(K key) {
        V value;
        if ((key.getClass() == Integer.class) || (key.getClass() == Long.class)){
            String keyString = key.toString();
            value = datastoreIntLong.get(keyString);
        }
        else { value = dataStore.get(key); }
        if (value != null) {
            for (var weakLsnr : weakListeners) {
                HwListener<K,V> lsnr = weakLsnr.get();
                if (lsnr != null ) lsnr.notify(key, value, "get");
            }
        }
        return value;
    }

    @Override
    public  boolean containsKey(K key) {
        if ((key.getClass() == Integer.class) || (key.getClass() == Long.class)) {
            String keyString = key.toString();
            return datastoreIntLong.containsKey(keyString);
        }
            return dataStore.containsKey(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        ArrayList<HwListener<K,V>> lsnrs = new ArrayList<>();
        for (var weakLsnr : weakListeners) {
            lsnrs.add(weakLsnr.get());
        }
        if(lsnrs.contains(listener)) return;
        WeakReference<HwListener<K,V>> weakLsnr = new WeakReference<>(listener);
        weakListeners.add(weakLsnr);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        ArrayList<HwListener<K,V>> lsnrs = new ArrayList<>();
            for (var weakLsnr : weakListeners) {
                lsnrs.add(weakLsnr.get());
            }
            lsnrs.remove(listener);
            weakListeners.clear();
        for (var lsnr : lsnrs) {
            weakListeners.add(new WeakReference<>(lsnr));
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

}
