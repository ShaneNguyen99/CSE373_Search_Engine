package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    int cap = 5000000;
    
    @Test(timeout=15*SECOND)
    public void testArrayHeap() {
        IPriorityQueue<Integer> heap = new ArrayHeap<Integer>();
        for (int i = cap; i > 0; i--) {
            heap.insert(i);
            assertEquals(cap + 1 - i, heap.size());
        }
        for (int i = 0; i < heap.size(); i++) {
            assertEquals(i + 1, heap.removeMin());
            assertEquals(cap -1 - i, heap.size());
        }
    }
    
    @Test(timeout=15*SECOND)
    public void testKTopSort() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i > cap; i--) {
            list.add(i);
            assertEquals(i + 1, list.size());
        }
        IList<Integer> top = Searcher.topKSort(cap - 1, list);
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i + 1, top.delete(0));
            assertEquals(cap - 2 - i, top.size());
        }
    }
    
    @Test(timeout=8*SECOND)
    public void testHeapRemoveMinRandom() {
        int capacity = 3000000;
        IPriorityQueue<Integer> queue = new ArrayHeap<>();
        List<Integer> temp = new ArrayList<Integer>();
        for (int i = capacity; i > 0; i--) {
            int val = (int) (Math.random()*capacity);
            queue.insert(val);
            temp.add(val);
        }
        assertEquals(capacity, queue.size());
        Collections.sort(temp);
        for (int i = 0; i < capacity; i++) {
            assertEquals(queue.removeMin(), temp.get(i));
        }
        assertEquals(0, queue.size());
    }
    
    @Test(timeout=2*SECOND)
    public void testHeapInsert() {
        IPriorityQueue<Integer> queue = new ArrayHeap<>();
        for (int i = 3000000; i > 0; i--) {
            queue.insert(i);
        }
        assertEquals(3000000, queue.size());
    }

    @Test(timeout=4*SECOND)
    public void testHeapRemoveMin() {
        IPriorityQueue<Integer> queue = new ArrayHeap<>();
        for (int i = 5000000; i > 0; i--) {
            queue.insert(i);
        }
        assertEquals(5000000, queue.size());
        for (int i = 5000000; i > 0; i--) {
            queue.removeMin();
        }
        assertEquals(0, queue.size());
    }

    @Test(timeout=SECOND)
    public void testHeapPeekMin() {
        IPriorityQueue<Integer> queue = new ArrayHeap<>();
        queue.insert(1);
        for (int i = 5000000; i > 0; i--) {
            queue.peekMin();
        }
        assertEquals(1, queue.size());
    }
    
    @Test(timeout=SECOND)
    public void testHeapSize() {
        IPriorityQueue<Integer> queue = new ArrayHeap<>();
        queue.insert(1);
        for (int i = 5000000; i > 0; i--) {
            queue.size();
        }
        assertEquals(1, queue.size());
    }

    @Test(timeout=4*SECOND)
    public void testSortHappy() {
        int capacity = 135000;
        int sort = 75000;
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> temp = new ArrayList<Integer>();
        for (int i = capacity; i > 0; i--) {
            int val = (int) (Math.random()*capacity);
            list.add(val);
            temp.add(val);
        }
        list.add(7000000);
        temp.add(7000000);
        Collections.sort(temp);
        IList<Integer> top = Searcher.topKSort(sort, list);
        assertEquals(sort, top.size());
        assertEquals(top.indexOf(7000000), sort - 1);
        assertEquals(capacity + 1, list.size());
        for (int i = 0; i < sort; i++) {
            assertEquals(top.get(i), temp.get(i+capacity-sort+1));
        }
    }
    
}
