package hw03.generics;

//import jdk.internal.util.ArraysSupport;

import java.util.*;

//import java.util.function.Consumer;
//import java.util.function.Predicate;
//import java.util.function.UnaryOperator;
//import jdk.internal.access.SharedSecrets;
//import jdk.internal.util.ArraysSupport;



public class DIYArrayList<E> implements List<E> {

    private static final Object[] EMPTY_BASE = {};

    private int size = 0;

    transient Object[] innerDIYBase;

    public DIYArrayList() {
        this.innerDIYBase = EMPTY_BASE;
    }

    public DIYArrayList(Collection<? extends E> c) {
        Object[] a = c.toArray();
        size = a.length;
        if (size != 0) {
            innerDIYBase = Arrays.copyOf(a, size, Object[].class);
        } else {
            innerDIYBase = EMPTY_BASE;
        }
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(innerDIYBase, size);
    }

    @Override
    public boolean add(E e) {
        int s = innerDIYBase.length;
        innerDIYBase = Arrays.copyOf(innerDIYBase, s + 1);
        innerDIYBase[s] = e;
        size = s + 1;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return (E) innerDIYBase[index];
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = (E) innerDIYBase[index];
        innerDIYBase[index] = element;
        return oldValue;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        Arrays.sort((E[]) innerDIYBase, c);
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {throw new UnsupportedOperationException(); }

    @Override
    public void add(int index, E element) {throw new UnsupportedOperationException();}

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DIYArrayList.ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
       return new DIYArrayList.ListItr(index);
    }

    private class ListItr implements ListIterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
          ListItr(int index) {
            cursor = index;
            System.out.println("\n *** listIterator started ***");
        }

        public boolean hasNext() {
            return cursor != size;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public E next() {
            int i = cursor;
//            Objects.checkIndex(i, size);
            Object[] innerItrBase = DIYArrayList.this.innerDIYBase;
            cursor = i + 1;
            return (E) innerItrBase[lastRet = i];
        }


        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                DIYArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public E previous() {
            int i = cursor - 1;
            Objects.checkIndex(i, size);
            Object[] innerItrBase = DIYArrayList.this.innerDIYBase;
            cursor = i;
            return (E) innerItrBase[lastRet = i];
        }

         public void add(E e) {throw new UnsupportedOperationException();}

         public void remove() {throw new UnsupportedOperationException();}

         public int nextIndex() {
             return cursor;
         }

         public int previousIndex() {
             return cursor - 1;
         }

    }

    @Override
   public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();}
}
