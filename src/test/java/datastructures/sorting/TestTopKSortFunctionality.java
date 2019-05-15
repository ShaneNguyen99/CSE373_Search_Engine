package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    private IList<Integer> makeList(int[] input){
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < input.length; i++) {
            list.add(input[i]);
        }
        return list;
    }
    
    private IList<Character> makeList(char[] input){
        IList<Character> list = new DoubleLinkedList<Character>();
        for (int i = 0; i < input.length; i++) {
            list.add(input[i]);
        }
        return list;
    }
    
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testCase1() {
        IList<Integer> list = new DoubleLinkedList<>();
        List<Integer> refer = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i%2==0?i/2:i);
            refer.add(i%2==0?i/2:i);
        }
        IList<Integer> top = Searcher.topKSort(7, list);
        assertEquals(7, top.size());
        Collections.sort(refer);
        for (int i = 7; i > 0; i--) {
            assertEquals(refer.get(refer.size() - i), top.get(top.size() - i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testCase2() {
        int[] nums = new int[]{1, 3, 5, 6, 4, 2, 0, -1, -3, -4, -2};
        IList<Integer> list = makeList(nums);
        List<Integer> refer = new LinkedList<>();
        for (int num: nums) {
            refer.add(num);
        }
        Collections.sort(refer);
        IList<Integer> top = Searcher.topKSort(9, list);
        assertEquals(9, top.size());
        for (int i = 9; i > 0; i--) {
            assertEquals(refer.get(refer.size() - i), top.get(top.size() - i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testCase3() {
        String chars = "XYZabcdefgxyzABCEDFG";
        IList<Character> list = makeList(chars.toCharArray());
        IList<Character> top = Searcher.topKSort(10, list);
        assertEquals(10, top.size());
        char[] arr = chars.toCharArray();
        Arrays.sort(arr);
        for (int i = 10; i > 0; i--) {
            assertEquals(arr[arr.length - i], top.get(top.size() - i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testCase4() {
        String chars = "<>,./?;':\"{}[]\\_+-=~!@#$%^&*()";
        IList<Character> list = makeList(chars.toCharArray());
        IList<Character> top = Searcher.topKSort(15, list);
        assertEquals(15, top.size());
        char[] arr = chars.toCharArray();
        Arrays.sort(arr);
        for (int i = 15; i > 0; i--) {
            assertEquals(arr[arr.length - i], top.get(top.size() - i));
        }
    }
    
    /**
     * This test will check if a list contains exactly the same elements as
     * the "expected" array. See the tests you were provided for example
     * usage.
     *
     * Please do not modify this method: our private tests rely on this.
     */
    protected <T> void assertListMatches(T[] expected, IList<T> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());
        for (int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i], actual.get(i));
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }

    @Test(timeout=SECOND)
    public void testSortHappy() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 100; i > 0; i--) {
            list.add((int) (Math.random() * 500000));
        }
        IList<Integer> top = Searcher.topKSort(80, list);
        assertEquals(80, top.size());
        assertEquals(100, list.size());
        int prev = -1;
        for (int val : top) {
            assertTrue(val > prev);
            prev = val;
        }
    }
        
    @Test(timeout=SECOND)
    public void testInputIsEmpty() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {}, top);
    }
    
    @Test(timeout=SECOND)
    public void testInputIsOneTwoElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        IList<Integer> top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(1, list);
        this.assertListMatches(new Integer[] {1}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {1}, top);
        list.add(2);
        top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(1, list);
        this.assertListMatches(new Integer[] {2}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {1, 2}, top);
    }
    
    @Test(timeout=SECOND)
    public void testInputIsSameElement() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        IList<Integer> top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {1, 1, 1, 1, 1}, list);
        this.assertListMatches(new Integer[] {1, 1, 1}, top);
        top = Searcher.topKSort(5, list);
        this.assertListMatches(new Integer[] {1, 1, 1, 1, 1}, top);
        top = Searcher.topKSort(7, list);
        this.assertListMatches(new Integer[] {1, 1, 1, 1, 1}, top);
        this.assertListMatches(new Integer[] {1, 1, 1, 1, 1}, list);
    }
    
    @Test(timeout=SECOND)
    public void testInputIsSorted() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(7);
        list.add(9);
        IList<Integer> top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {5, 7, 9}, top);
        top = Searcher.topKSort(5, list);
        this.assertListMatches(new Integer[] {1, 3, 5, 7, 9}, list);
        this.assertListMatches(new Integer[] {1, 3, 5, 7, 9}, top);
        top = Searcher.topKSort(7, list);
        this.assertListMatches(new Integer[] {1, 3, 5, 7, 9}, top);
        this.assertListMatches(new Integer[] {1, 3, 5, 7, 9}, list);
    }
    
    @Test(timeout=SECOND)
    public void testInputIsNotSorted() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(9);
        list.add(7);
        list.add(5);
        list.add(3);
        list.add(1);
        IList<Integer> top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {5, 7, 9}, top);
        top = Searcher.topKSort(5, list);
        this.assertListMatches(new Integer[] {9, 7, 5, 3, 1}, list);
        this.assertListMatches(new Integer[] {1, 3, 5, 7, 9}, top);
        top = Searcher.topKSort(7, list);
        this.assertListMatches(new Integer[] {1, 3, 5, 7, 9}, top);
        this.assertListMatches(new Integer[] {9, 7, 5, 3, 1}, list);
    }
    
    @Test(timeout=SECOND)
    public void testThrowException() {
        IList<Integer> list = new DoubleLinkedList<>();
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        list.add(1);
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInputIsNegative() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(-1);
        list.add(-3);
        list.add(-5);
        list.add(-7);
        list.add(-9);
        IList<Integer> top = Searcher.topKSort(0, list);
        this.assertListMatches(new Integer[] {}, top);
        top = Searcher.topKSort(3, list);
        this.assertListMatches(new Integer[] {-5, -3, -1}, top);
        top = Searcher.topKSort(5, list);
        this.assertListMatches(new Integer[] {-1, -3, -5, -7, -9}, list);
        this.assertListMatches(new Integer[] {-9, -7, -5, -3, -1}, top);
        top = Searcher.topKSort(7, list);
        this.assertListMatches(new Integer[] {-9, -7, -5, -3, -1}, top);
        this.assertListMatches(new Integer[] {-1, -3, -5, -7, -9}, list);
    }
}
