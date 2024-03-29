package hw21cache.cachehw;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    boolean containsKey(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);

    int size();

    int sizeListenersList();
}
