package com.brona.zpivac.dev.util;

import reactor.util.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class LoopingQueue<E> {

    public static final int PLAY_ONCE = 0;
    public static final int REPEAT_INFINITELY = -1;

    protected final LinkedList<LoopingElement<E>> delegate = new LinkedList<>();

    public void clear() {
        delegate.clear();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public int size() {
        return delegate.size();
    }

    public E peek() {
        return LoopingElement.get(delegate.peek());
    }

    public E poll() {
        LoopingElement<E> value = delegate.poll();

        if (value == null)
            return null;

        if (value.getRepeats() < 0) {
            delegate.add(value);
        } else if (value.getRepeats() > 0) {
            delegate.add(new LoopingElement<>(value.get(), value.getRepeats() - 1));
        }

        return value.get();
    }

    public E pollAndPeek() {
        this.poll();
        return this.peek();
    }

    public void add(E element) {
        add(element, delegate.size(), PLAY_ONCE);
    }

    public void addAt(E element, int position) {
        add(element, position, PLAY_ONCE);
    }

    public void addWithRepeat(E element, int repeatCount) {
        add(element, delegate.size(), repeatCount);
    }

    public void add(E element, int position, int repeatCount) {
        delegate.add(position, new LoopingElement<>(element, repeatCount));
    }

    public void addAll(Collection<E> elements) {
        addAll(elements, delegate.size(), PLAY_ONCE);
    }

    public void addAllAt(Collection<E> elements, int position) {
        addAll(elements, position, PLAY_ONCE);
    }

    public void addAllWithRepeat(Collection<E> elements, int repeatCount) {
        addAll(elements, delegate.size(), repeatCount);
    }

    public void addAll(Collection<E> elements, int position, int repeatCount) {
        delegate.addAll(position, elements
                .stream()
                .map(element -> new LoopingElement<>(element, repeatCount))
                .collect(Collectors.toList())
        );
    }

    public LoopingElement<E> getElement(int position) {
        return delegate.get(position);
    }

    public E get(int position) {
        return getElement(position).get();
    }

    public int getRepeat(int position) {
        return getElement(position).getRepeats();
    }

    public void setRepeat(int position, int repeatCount) {
        delegate.set(position, new LoopingElement<>(getElement(position).get(), repeatCount));
    }

    public void setRepeatAll(int position, int count, int repeatCount) {
        if (count <= 0)
            throw new IllegalArgumentException();

        for (int i = 0; i < count; i++)
            delegate.set(position, new LoopingElement<>(getElement(position).get(), repeatCount));
    }

    public void move(int position, int newPosition) {
        if (position == newPosition)
            return;

        LoopingElement<E> value = delegate.remove(position);

        if (position < newPosition) {
            delegate.add(newPosition - 1, value);
        } else {
            delegate.add(newPosition, value);
        }

    }

    public void moveAll(int position, int count, int newPosition) {
        if (count <= 0)
            throw new IllegalArgumentException();

        if (position == newPosition)
            return;

        List<LoopingElement<E>> moved = new ArrayList<>(count);

        for (int i = 0; i < count; i++)
            moved.add(delegate.remove(position));


        if (position < newPosition) {
            delegate.addAll(newPosition - count, moved);
        } else {
            delegate.addAll(newPosition, moved);
        }

    }

    public E remove(int position) {
        return delegate.remove(position).get();
    }

    public List<E> removeAll(int position, int count) {
        if (count <= 0)
            throw new IllegalArgumentException();

        List<E> removed = new ArrayList<>(count);

        for (int i = 0; i < count; i++)
            removed.add(delegate.remove(position).get());

        return Collections.unmodifiableList(removed);
    }

    public List<LoopingElement<E>> getElements() {
        return Collections.unmodifiableList(delegate);
    }

    public static class LoopingElement<E> {

        protected final E element;
        protected final int repeatCount;

        public LoopingElement(E element, int repeatCount) {
            this.element = element;
            this.repeatCount = repeatCount;
        }

        public E get() {
            return element;
        }

        public int getRepeats() {
            return repeatCount;
        }

        public static <E> E get(@Nullable LoopingElement<E> value) {
            return value == null ? null : value.get();
        }
    }

}
