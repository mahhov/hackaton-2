package list;

import java.util.Iterator;

// single linked list, add to front, remove from back, generic queue

public class Queue<T> implements Iterable<T> {
	Node head, tail;
	public int length;

	// add to back
	public void add(T item) {
		if (length++ != 0) {
			Node ntail = new Node(item, null);
			tail.next = ntail;
			tail = ntail;
		} else {
			head = new Node(item, null);
			tail = head;
		}
	}

	// adds to front, to be removed first
	public void pushBack(T item) {
		if (length++ != 0) {
			head = new Node(item, head);
		} else {
			head = new Node(item, null);
			tail = head;
		}
	}

	// removes from front (first in, first out)
	public T remove() {
		if (length == 0)
			return null;
		length--;
		T item = head.item;
		if (length != 0) {
			head = head.next;
		} else {
			head = null;
			tail = null;
		}
		return item;
	}

	// gets item from front
	public T get() {
		if (length == 0)
			return null;
		return head.item;
	}

	// finds last occurrence (nearest tail) and removes
	// if none found, return false
	public boolean findAndRemove(T t) {
		Node occurance = null;
		boolean occured = false;
		Node prev = null;
		Node cur = head;
		while (cur != null) {
			if (cur.item.equals(t)) {
				occurance = prev;
				occured = true;
			}
			prev = cur;
			cur = cur.next;
		}

		// none found
		if (!occured)
			return false;

		length--;
		// remove head
		if (occurance == null) {
			if (length != 0) {
				head = head.next;
			} else {
				head = null;
				tail = null;
			}
		} else { // remove non-head
			if (tail == occurance.next)
				tail = occurance;
			occurance.next = occurance.next.next;
		}
		return true;
	}

	class Node {
		private Node next;
		private T item;

		Node(T item, Node next) {
			this.item = item;
			this.next = next;
		}

	}

	public Iterator<T> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<T> {

		Node c;

		private QueueIterator() {
			c = head;
		}

		public boolean hasNext() {
			return c != null;
		}

		public T next() {
			T r = c.item;
			c = c.next;
			return r;
		}

		public void remove() {
		}
	}

}
