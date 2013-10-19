package list;

import java.util.Iterator;

import list.LinkedList.Node;

public class LinkedList implements Iterable<Node> {

	public Node head;
	public short length;

	public void add(ListItem item) {
		head = new Node(item, head);
		length++;
	}

	public void remove(Node node) {
		node.item = head.item;
		head = head.next;
		length--;
	}

	public Node find(Object id) {
		for (Node n : this)
			if (((Findable) n.item).identical(id))
				return n;
		return null;
	}

	public void findAndRemove(short id) {
		for (Node n : this)
			if (((Findable) n.item).identical(id)) {
				remove(n);
				break;
			}
	}

	public class Node {
		public ListItem item;
		public Node next;

		Node(ListItem item, Node next) {
			this.item = item;
			this.next = next;
		}
	}

	public Iterable<Node> iterateFront(short length) {
		LinkedList t = new LinkedList();
		t.head = head;
		t.length = length;
		return t;
	}

	public Iterator<Node> iterator() {
		return new LinkedListIterator(length);
	}

	public Iterator<Node> iterator(short length) {
		return new LinkedListIterator(length);
	}

	class LinkedListIterator implements Iterator<Node> {

		Node cur;
		short iterlength;

		LinkedListIterator(short length) {
			cur = head;
			iterlength = length;
		}

		public boolean hasNext() {
			return cur != null && iterlength > 0;
		}

		public Node next() {
			iterlength--;
			Node r = cur;
			cur = cur.next;
			return r;
		}

		public void remove() {
		}

	}

}
