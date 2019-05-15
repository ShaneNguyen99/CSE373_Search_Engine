package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }
    
    //move to the target node 
    public Node<T> moveTo(int index){
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (front == null || back == null) {
            throw new EmptyContainerException();
        }
        Node<T> temp;
        if (index < size/2) {
            temp = front;
            for (int i = 0; i < index; i++) {
                if (temp.next == null) {
                    throw new NoSuchElementException();
                }
                temp = temp.next;
            }
        } else {
            temp = back;
            for (int i = size() - 1; i > index; i--) {
                if (temp.prev == null) {
                    throw new NoSuchElementException();
                }
                temp = temp.prev;
            }
        }
        return temp;
    }
    
    @Override
    public void add(T item) {
        size++;
        if (back == null) {
            front = new Node<T>(item);
            back = front;
        } else {
            back.next = new Node<T>(item);
            back.next.prev = back;
            back = back.next;
        }
    }

    @Override
    public T remove() {
        if (back == null) {
            throw new EmptyContainerException();
        }
        size--;
        Node<T> temp = back;
        back = back.prev;
        temp.prev = null;
        if (back == null) {
            front = null;
        } else {
            back.next = null;
        }
        return temp.data;
    }

    @Override
    public T get(int index) {  
        return moveTo(index).data;
    }

    @Override
    public void set(int index, T item) {
        Node<T> temp = moveTo(index);
        Node<T> node = new Node<T>(item);
        if (temp == back) {
            back = node;
        } else {
            temp.next.prev = node;
        }
        if (temp == front) {
            front = node;
        } else {
            temp.prev.next = node;
        }
        node.next = temp.next;
        node.prev = temp.prev;
        temp.prev = null;
        temp.next = null;
        //throw new NotYetImplementedException();
    }

    @Override
    public void insert(int index, T item) {
        if (index == size) {
            add(item);
        } else {
            Node<T> temp = moveTo(index);
            Node<T> node = new Node<T>(item);
            if (temp == front) {
                if (front == null) {
                    back = node;
                }
                front = node;
            } else {
                temp.prev.next = node;
            }
            node.next = temp;
            node.prev = temp.prev;
            temp.prev = node;
            size++;
        }
        //throw new NotYetImplementedException();
    }

    @Override
    public T delete(int index) {
        Node<T> temp = moveTo(index);
        size--;
        if (temp == back) {
            back = back.prev;
        } else {
            temp.next.prev = temp.prev;
        }
        if (temp == front) {
            front = front.next;
        } else {
            temp.prev.next = temp.next;
        }
        temp.prev = null;
        temp.next = null;
        return temp.data;
        //throw new NotYetImplementedException();
    }

    @Override
    public int indexOf(T item) {
        if (front == null) {
            throw new EmptyContainerException();
        }
        Node<T> temp = front;
        int index = 0;
        while (true) {
            if (temp.data == item || (temp.data != null && temp.data.equals(item))) {
                return index;
            } else if (temp.next != null) {
                temp = temp.next;
                index++;
            } else {
                return -1;
            }
        }
    }

    @Override
    public int size() {
        return size;
        //throw new NotYetImplementedException();
    }

    @Override
    public boolean contains(T other) {
        try {
            indexOf(other);
        } catch(Exception e) {
            return false;
        }
        return indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
            //throw new NotYetImplementedException();
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T temp = current.data;
            current = current.next;
            return temp;
            //throw new NotYetImplementedException();
        }
    }
}
