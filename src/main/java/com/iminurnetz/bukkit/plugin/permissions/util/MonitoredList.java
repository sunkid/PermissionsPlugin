package com.iminurnetz.bukkit.plugin.permissions.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MonitoredList<E> implements List<E> {

    private List<E> backer;
    private List<ListChangeMonitor> monitors = new LinkedList<ListChangeMonitor>();

    public MonitoredList(List<E> backer, ListChangeMonitor monitor) {
        this.backer = backer;
        monitors.add(monitor);
    }

    public boolean add(E e) {
        boolean returnValue = backer.add(e);
        callPostUpdates();
        return returnValue;
    }

    public boolean remove(Object o) {
        boolean returnValue = backer.remove(o);
        callPostUpdates();
        return returnValue;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean returnValue = backer.addAll(c);
        callPostUpdates();
        return returnValue;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        boolean returnValue = backer.addAll(index, c);
        callPostUpdates();
        return returnValue;
    }

    public boolean removeAll(Collection<?> c) {
        boolean returnValue = backer.removeAll(c);
        callPostUpdates();
        return returnValue;
    }

    public boolean retainAll(Collection<?> c) {
        boolean returnValue = backer.retainAll(c);
        callPostUpdates();
        return returnValue;
    }

    public void clear() {
        backer.clear();
        callPostUpdates();
    }

    public E set(int index, E element) {
        E returnValue = backer.set(index, element);
        callPostUpdates();
        return returnValue;
    }

    public void add(int index, E element) {
        backer.add(index, element);
        callPostUpdates();
    }

    public E remove(int index) {
        E returnValue = backer.remove(index);
        callPostUpdates();
        return returnValue;
    }

    private void callPostUpdates() {
        for (ListChangeMonitor monitor : monitors) {
            monitor.postUpdate();
        }
    }

    public boolean addMonitor(ListChangeMonitor monitor) {
        return monitors.add(monitor);
    }

    public boolean removeMonitor(ListChangeMonitor monitor) {
        return monitors.remove(monitor);
    }

    public int size() {
        return backer.size();
    }

    public boolean isEmpty() {
        return backer.isEmpty();
    }

    public boolean contains(Object o) {
        return backer.contains(o);
    }

    public Iterator<E> iterator() {
        return backer.iterator();
    }

    public Object[] toArray() {
        return backer.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return backer.toArray(a);
    }

    public boolean containsAll(Collection<?> c) {
        return backer.containsAll(c);
    }

    public E get(int index) {
        return backer.get(index);
    }

    public int indexOf(Object o) {
        return backer.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return backer.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return backer.listIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return backer.listIterator();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return backer.subList(fromIndex, toIndex);
    }
}
