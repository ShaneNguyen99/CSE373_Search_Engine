package datastructures.concrete;

import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int size;

    public ArrayHeap() {
        heap = makeArrayOfT(10);
        size = 0;
    }
    
    public ArrayHeap(IList<T> input) {
        heap = makeArrayOfT(input.size());
        size = 0;
        for (T item: input) {
            if (item != null) {
                heap[size] = item;
                size++;
            }
        }
        for (int i = size - 1; i >= 0; i--) {
            moveDown(i);
        }
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arrSize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arrSize]);
    }
    
    private void expand() {
        T[] newArr = makeArrayOfT(heap.length * 2);
        for (int i = 0; i < heap.length; i++) {
            newArr[i] = heap[i];
        }
        heap = newArr;
    }
    
    private void moveDown(int index) {
        int min = comprChildren(index);
        if (min != index) {
            T temp = heap[index];
            heap[index] = heap[min];
            heap[min] = temp;
            moveDown(min);
        }
    }
    
    private int comprChildren(int index) {
        int min = index;
        for (int i = 1; i <= NUM_CHILDREN; i++) {
            if (index*NUM_CHILDREN+i < heap.length && heap[index*NUM_CHILDREN+i] != null &&
                heap[min] != null && heap[index*NUM_CHILDREN+i].compareTo(heap[min]) < 0) {
                min = index*NUM_CHILDREN+i;
            }
        }
        return min;
    }
    
    private void moveUp(int index) {
        int parent = (index-1)/NUM_CHILDREN;
        if (parent >= 0 && heap[parent] != null &&
            heap[index] != null && heap[index].compareTo(heap[parent]) < 0) {
            T temp = heap[index];
            heap[index] = heap[parent];
            heap[parent] = temp;
            moveUp(parent);
        }
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        T temp = heap[0];
        heap[0] = heap[size-1];
        heap[size-1] = null;
        size--;
        if (size > 0) {
            moveDown(0);
        }
        return temp;
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        } else if (size == heap.length) {
            expand();
        }
        heap[size] = item;
        moveUp(size);
        size++;
    }

    @Override
    public int size() {
        return size;
    }
}
