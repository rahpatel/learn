package support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.*;

/**
 * Tree API.
 * 
 *@author Rahul
 *@version January 22,2007
 */

/*
 * A class to have a pair of objects, one of which is a String and other is a
 * Tree<String>.
 */

class TreeIndex {
	Tree<String> tree;
	int index;
}

/*
 * 
 * @param <V> The type of the value of nodes.
 */

public class Tree<V> {

	public V value;
	private ArrayList<Tree<V>> children;

	/**
	 * Constructor for Tree objects. Creates a node with the given value.
	 * 
	 * @param value
	 *            The value of the tree node.
	 * 
	 */

	public Tree(V value) {
		this.value = value;
		this.children = new ArrayList<Tree<V>>();
	}

	/*
	 * Tests whether this Tree is equal to the given Object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Tree))
			return false;
		Tree<V> another = (Tree<V>) obj;
		if (this.value == null)
			return (another.value == null);
		if (this.children.size() == 0 && another.children.size() == 0) {

			if ((this.value).equals(another.value))
				return true;
			else
				return false;
		} else {
			if (this.children.size() == another.children.size()) {

				if ((this.value).equals(another.value)) {
					Iterator<Tree<V>> iter = this.children.iterator();
					Iterator<Tree<V>> itere = another.children.iterator();
					boolean result = true;
					while (iter.hasNext()) {
						Tree<V> element = iter.next();
						Tree<V> elementanother = itere.next();
						result = element.equals(elementanother);
						if (result == false)
							break;
					}
					return result;
				} else
					return false;

			} else
				return false;
		}
	}

	/**
	 * Returns a string representation of this tree.
	 * <code>value(child, child,...,child)</code>.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = (this.value).toString();
		if (this.children.size() == 0)
			return str;
		else {
			str = str + "(";
			Iterator<Tree<V>> iter = this.children.iterator();
			while (iter.hasNext()) {
				Tree<V> element = iter.next();
				str = str + " " + element.toString();
			}
			str = str + ")";
			return str;
		}
	}

	/**
	 * Adds the given node to this node as the new last child.
	 * 
	 * @param child
	 *            the tree to be added.
	 */

	public Tree<V> addChild(Tree<V> child) throws IllegalArgumentException {
		if (child.contains(this)) {
			String message = "The child being added contains the node to which it is added. And so creates a loop";
			throw new IllegalArgumentException(message);
		}
		this.children.add(child);
		return this;
	}

	/**
	 * Adds a leaf node with value of type V to this tree. Returns the tree with
	 * the added leaf node.
	 * 
	 * @param value
	 * @return Tree<V>
	 */

	public Tree<V> addChild(V value) {
		Tree<V> tree = new Tree<V>(value);
		this.children.add(tree);
		return this;
	}

	/**
	 * Returns a list of the children of this node.
	 * 
	 * @return The children of this node.
	 */

	public List<Tree<V>> children() {
		return children;
	}

	/**
	 * Returns an iterator on children of this tree.
	 * 
	 *@return Iterator on children on this tree.
	 */

	public Iterator<Tree<V>> iterator() {
		Iterator<Tree<V>> iter = this.children.iterator();
		return iter;
	}

	/**
	 * Returns value in this node of the tree.
	 * 
	 * @param value
	 *            The value to put in this node.
	 */

	public void setValue(V value) {
		this.value = value;
	}

	/**
	 * Returns the value in this node of the tree.
	 * 
	 * @return Value in this node of tree.
	 */

	public V getValue() {
		return this.value;
	}

	/**
	 * 
	 * @return Returns the no. of children of this tree.
	 */

	public static Tree<String> parse(String s) {

		ArrayList<String> alchar = new ArrayList<String>();
		String str;
		StringTokenizer st = new StringTokenizer(s, " ()", true);
		while (st.hasMoreTokens()) {
			str = st.nextToken();
			if (!(str.equals(" ")))
				alchar.add(str);
		}
		// System.out.println(alchar);

		TreeIndex objec = parsing(alchar, 0);
		Tree<String> treee = objec.tree;
		return treee;

	}

	public static TreeIndex parsing(ArrayList<String> alstr, int n) {
		Tree<String> tr = new Tree<String>(alstr.get(n));
		TreeIndex object = new TreeIndex();

		if (alstr.get(n + 1).equals("(")) {
			int h = n;
			while (!(alstr.get(h + 2).equals(")"))) {
				TreeIndex obj = new TreeIndex();
				obj = parsing(alstr, h + 2);
				tr.children.add(obj.tree);
				h = obj.index;

			}
			object.tree = tr;
			object.index = h + 1;
			return object;
		}

		else {
			object.tree = tr;
			object.index = n - 1;
			return object;
		}
	}

	/**
	 * Returns the no of children of this tree
	 * 
	 * @return No of children of this tree.
	 */
	public int size() {
		return children.size();

	}

	/**
	 * Prints this tree as in structured format.
	 */

	public void print() {

		printing(1);

	}

	public void printing(int count) {

		System.out.println(value);
		if (this.size() == 0) {
		} else {

			Iterator<Tree<V>> iter = children.iterator();
			while (iter.hasNext()) {
				Tree<V> element = iter.next();
				for (int i = 0; i < count; i++)
					System.out.print(" | ");
				element.printing(count + 1);
			}
		}
	}

	/**
	 * Checks whether the tree passed as argument is contained in the this tree.
	 * 
	 * @param another
	 *            The tree to be searched.
	 * @return Returns true if tree is contained in this tree.
	 */

	public boolean contains(Tree<V> another) {
		if (this.equals(another))
			return true;
		else {
			if (this.children.size() != 0) {
				Iterator<Tree<V>> iter = this.children.iterator();
				boolean result = false;
				while (iter.hasNext()) {
					Tree<V> element = iter.next();
					result = element.contains(another);
					if (result == true)
						break;
				}
				return result;
			} else
				return false;
		}
	}

	/**
	 * Parses a string of form <code>value(child,child,...,child)</code>
	 * 
	 * @param s
	 *            The string that will be parsed
	 * @return The result tree<String>
	 */
	/**
	 * Tests parsing and printing of trees. This is only a test method.
	 * 
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		String str = "one (two three(four five(six seven eight)))";
		System.out.println();
		System.out.println("Testing String :" + str);
		System.out.println();
		Tree<String> tree = Tree.parse(str);
		String strin = tree.toString();
		System.out.println("String format represent of tree is  :  " + strin);
		System.out.println();
		System.out.println("The Tree built from the string : " + str);
		tree.print();
		System.out.println("Tests completed");

	}

}
