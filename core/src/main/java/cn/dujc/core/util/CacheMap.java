package cn.dujc.core.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * 通过弱引用缓存数据
 *
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> implements Map<K, V> {

    private WeakHashMap<K, WeakReference<V>> mCacheContainer = new WeakHashMap<>();

    @Override
    public int size() {
        return mCacheContainer.size();
    }

    @Override
    public boolean isEmpty() {
        return mCacheContainer.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return mCacheContainer.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        if (value == null)
            return mCacheContainer.containsValue(value);

        Set<K> keys = mCacheContainer.keySet();
        for (K key : keys) {
            WeakReference<V> val = mCacheContainer.get(key);
            boolean result = val != null && val.get() != null &&
                    (val == value || val.equals(value) || val.get() == value || val.get().equals(value));
            if (result) return true;
        }

        return false;
    }

    @Nullable
    @Override
    public V get(@Nullable Object key) {
        WeakReference<V> weakValue = mCacheContainer.get(key);
        return weakValue != null ? weakValue.get() : null;
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        mCacheContainer.put(key, new WeakReference<V>(value));
        return value;
    }

    @Nullable
    @Override
    public V remove(@Nullable Object key) {
        WeakReference<V> weakValue = mCacheContainer.remove(key);
        return weakValue != null ? weakValue.get() : null;
    }

    @Override
    public void putAll(@NonNull Map<? extends K, ? extends V> m) {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0)
            return;

        for (Entry<? extends K, ? extends V> e : m.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() {
        mCacheContainer.clear();
    }

    @NonNull
    @Override
    public Set<K> keySet() {
        return mCacheContainer.keySet();
    }

    @NonNull
    @Override
    public Collection<V> values() {
        Collection<WeakReference<V>> values = mCacheContainer.values();
        if (values.isEmpty()) return Collections.emptyList();
        List<V> result = new ArrayList<>(values.size());
        for (WeakReference<V> value : values) {
            if (value != null && value.get() != null) result.add(value.get());
        }
        return result;
    }

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, WeakReference<V>>> entries = mCacheContainer.entrySet();
        Set<Entry<K, V>> result = new LinkedHashSet<>(entries.size());
        for (Entry<K, WeakReference<V>> entry : entries) {
            K key = entry.getKey();
            if (key != null) {
                WeakReference<V> value = entry.getValue();
                if (value != null) {
                    final V val = value.get();
                    result.add(new SimpleEntry<K, V>(key, val));
                }
            }
        }
        return result;
    }

    public static class SimpleEntry<K, V> implements Entry<K, V> {
        private final K mKey;
        private V mVal;

        public SimpleEntry(K key, V val) {
            mKey = key;
            mVal = val;
        }

        @Override
        public K getKey() {
            return mKey;
        }

        @Override
        public V getValue() {
            return mVal;
        }

        @Override
        public V setValue(V value) {
            return mVal = value;
        }
    }
}
