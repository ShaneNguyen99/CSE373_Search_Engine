package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        size = 0;
        pairs = makeArrayOfPairs(10);
        //throw new UnsupportedOperationException();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }
    
    private void expand() {
        Pair<K, V>[] newArr = makeArrayOfPairs(size*2);
        for (int i = 0; i < size; i++) {
            newArr[i] = pairs[i];
        }
        pairs = newArr;
    }

    private int indexOf(K key) {
        for (int i = 0; i < size; i++) {
            if (key == pairs[i].key || (key != null && pairs[i].key != null && pairs[i].key.equals(key))) {
                return i;  
            }
        }
        return -1;
    }
    
    @Override
    public V get(K key) {
        int i = indexOf(key);
        if (i != -1) {
            return pairs[i].value;  
        }
        throw new NoSuchKeyException();
        //throw new NotYetImplementedException();
    }

    @Override
    public void put(K key, V value) {
        if (size == pairs.length) {
            expand();
        }
        int i = indexOf(key);
        if (i != -1) {
            pairs[i].value = value;
        } else {
            pairs[size] = new Pair<K, V>(key, value);
            size++;
        }
        //throw new NotYetImplementedException();
    }

    @Override
    public V remove(K key) {
        int i = indexOf(key);
        if (i != -1) {
            V temp = pairs[i].value;
            pairs[i] = pairs[size -1];
            size--;
            return temp;  
        }
        throw new NoSuchKeyException();
        //throw new NotYetImplementedException();
    }

    @Override
    public boolean containsKey(K key) {
        return indexOf(key) != -1;
        //throw new NotYetImplementedException();
    }

    @Override
    public int size() {
        return size;
        //throw new NotYetImplementedException();
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new DictIterator<>(this.pairs);
    }
        
    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private class DictIterator<k, v> implements Iterator<KVPair<k, v>> {
        private Pair<k, v>[] pairs;
        private int index;

        public DictIterator(Pair<k, v>[] pairs) {
            this.pairs = pairs;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return (index < size);
        }

        @Override
        public KVPair<k, v> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<k, v> result = new KVPair<k, v>(pairs[index].key, pairs[index].value);
            this.index += 1;
            return result;
        }
    }
}
