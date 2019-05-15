package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int capacity;
    private int size;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = this.makeArrayOfChains(100);
        this.capacity = 100;
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arrSize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[arrSize];
    }

    private void expand() {
        this.capacity = this.capacity*2;
        IDictionary<K, V>[] newChains = makeArrayOfChains(this.capacity);
        for (int i = 0; i < this.capacity/2; i++) {
            if (this.chains[i] != null) {
                for (KVPair<K, V> pair : this.chains[i]) {
                    K key = pair.getKey();
                    V value = pair.getValue();
                    int index = this.getIndex(key);
                    if (newChains[index] == null) {
                        newChains[index] = new ArrayDictionary<K, V>();
                    }
                    newChains[index].put(key, value);
                }
            }
        }
        this.chains = newChains;
    }

    // Pass in a key and return an integer hash
    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        hash = Math.abs(hash);
        hash = hash % this.capacity;
        return hash;
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return this.chains[index].get(key);
    }

    @Override
    public void put(K key, V value) {
        if (this.size >= this.capacity) {
            this.expand();
        }
        int index = this.getIndex(key);
        if (this.chains[index] == null) {
            this.chains[index] = new ArrayDictionary<K, V>();
        }
        if (!this.containsKey(key)) {
            this.size++;
        }
        this.chains[index].put(key, value);
    }

    @Override
    public V remove(K key) {
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int index = this.getIndex(key);
        this.size--;
        return this.chains[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int index = this.getIndex(key);
        return this.chains[index] != null && this.chains[index].containsKey(key);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private Iterator<KVPair<K, V>> chainIter;
        private int chainNum;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null) {
                    chainNum = i;
                    chainIter = chains[chainNum].iterator();
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            if (chainIter == null) {
                return false;
            }
            if (!chainIter.hasNext()){
                for (int i = chainNum+1; i < chains.length; i++) {
                    if (chains[i] != null) {
                        chainNum = i;
                        chainIter = chains[chainNum].iterator();
                        return hasNext();
                    }
                }
                return false;
            }
            return true;
            //throw new NotYetImplementedException();
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return chainIter.next();
            //throw new NotYetImplementedException();
        }
    }
}
