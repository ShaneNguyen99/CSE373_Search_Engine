package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
//import java.util.Collection;
//import java.util.NoSuchElementException;


import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInsertLargeToSmall() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 20; i > 0; i--) {
            heap.insert(i);
            assertEquals(21 - i, heap.size());
        }
        for (int i = 1; i <= 20; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(i, heap.removeMin());
            assertEquals(20 - i, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertSmallToLarge() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1; i <= 20; i++) {
            heap.insert(i);
            assertEquals(i, heap.size());
        }
        for (int i = 1; i <= 20; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(i, heap.removeMin());
            assertEquals(20 - i, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertNagative() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -1; i >= -20; i--) {
            heap.insert(i);
            assertEquals(-1 * i, heap.size());
        }
        for (int i = -20; i <= -1; i++) {
            assertEquals(i, heap.peekMin());
            assertEquals(i, heap.removeMin());
            assertEquals(-1 * i - 1, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertDuplicate() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 10; i++) {
            heap.insert(1);
            assertEquals(i + 1, heap.size());
        }
        for (int i = 0; i < 10; i++) {
            heap.insert(0);
            assertEquals(i + 11, heap.size());
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(0, heap.peekMin());
            assertEquals(0, heap.removeMin());
            assertEquals(19 - i, heap.size());
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(1, heap.peekMin());
            assertEquals(1, heap.removeMin());
            assertEquals(9 - i, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertNotSorted() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int[] nums = {2, 6, 12, 12, 9, -5, 67, -2, 84, 24, -34, 27, -1, 6};
        for (int i = 0; i < nums.length; i++) {
            heap.insert(nums[i]);
            assertEquals(i + 1, heap.size());
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            assertEquals(nums[i], heap.peekMin());
            assertEquals(nums[i], heap.removeMin());
            assertEquals(nums.length - i - 1, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveErrorHandling() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertNull() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testResizing() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.size() == 0);
        for (int i = 0; i < 10; i++) {
            heap.insert(i);            
        }
        assertEquals(10, heap.size());
        for (int i = 10; i < 20; i++) {
            heap.insert(i);            
        }
        assertEquals(20, heap.size());
        for (int i = 20; i < 40; i++) {
            heap.insert(i);            
        }
        assertEquals(40, heap.size());
        for (int i = 40; i < 100; i++) {
            heap.insert(i);            
        }
        assertEquals(100, heap.size());
        for (int i = 100; i < 200; i++) {
            heap.insert(i);            
        }
        assertEquals(200, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemovePeekEqual() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(heap.peekMin(), heap.removeMin());
            assertEquals(19 - i, heap.size());
        }
    }
    
    @Test(timeout= 2 * SECOND)
    public void testInsertRemoveSameTime() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -100; i < 100; i++) {
            heap.insert(i);
            assertEquals(1, heap.size());
            assertEquals(i, heap.removeMin());
            assertEquals(0, heap.size());
        }
    }
    
    @Test(timeout=SECOND)
    public void testCharacter() {
        IPriorityQueue<Character> heap = this.makeInstance();
        String str = "abcdefg";
        for (int i = str.length() - 1; i >= 0; i--) {
            heap.insert(str.charAt(i));
        }
        char prev = heap.removeMin();
        assertEquals('a', prev);
        for (int i = 1; i < str.length(); i++) {
            assertTrue(heap.peekMin().compareTo(prev) > 0);
            prev = heap.removeMin();
        } 
    }
    
    @Test(timeout=SECOND)
    public void testString() {
        IPriorityQueue<String> heap = this.makeInstance();
        String[] str = {"a", "b", "c", "apple", "append", "boy", "box", "cat"};
        for (int i = str.length - 1; i >= 0; i--) {
            heap.insert(str[i]);
        }
        String prev = heap.removeMin();
        assertEquals("a", prev);
        for (int i = 1; i < str.length; i++) {
            assertTrue(heap.peekMin().compareTo(prev) > 0);
            prev = heap.removeMin();
        } 
    }
}
