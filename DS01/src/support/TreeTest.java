package support;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import org.junit.Before;

public class TreeTest {
	Tree<String> tree1, tree2, tree3, tree4, tree5, tree6, tree7, tree8;
	Tree<String> a1, b1, c1, d1, e1, f1, g1, a2, b2, c2, d2, e2, f2, g2;

	@Before
	public void setUp() throws Exception {
		tree1 = new Tree<String>("one");
		tree2 = new Tree<String>("two");
		tree3 = new Tree<String>("three");
		tree4 = new Tree<String>("four");
		tree5 = new Tree<String>("five");
		tree6 = new Tree<String>("six");
		tree7 = new Tree<String>("seven");
		tree8 = new Tree<String>("eight");

		tree1.addChild(tree2);
		tree1.addChild(tree3);
		tree3.addChild(tree4);
		tree3.addChild(tree5);
		tree5.addChild(tree6);
		tree5.addChild(tree7);
		tree5.addChild(tree8);

		a1 = new Tree<String>("a");
		b1 = new Tree<String>("b");
		c1 = new Tree<String>("c");
		d1 = new Tree<String>("d");
		e1 = new Tree<String>("e");
		f1 = new Tree<String>("f");
		g1 = new Tree<String>("g");

		a1.addChild(b1);
		a1.addChild(c1);
		b1.addChild(d1);
		b1.addChild(e1);
		c1.addChild(f1);
		c1.addChild(g1);

		a2 = new Tree<String>("a");
		b2 = new Tree<String>("b");
		c2 = new Tree<String>("c");
		d2 = new Tree<String>("d");
		e2 = new Tree<String>("e");
		f2 = new Tree<String>("f");
		g2 = new Tree<String>("g");

		a2.addChild(b2);
		a2.addChild(c2);
		b2.addChild(e2);
		b2.addChild(d2);
		c2.addChild(f2);
		c2.addChild(g2);

	}

	@Test
	public final void testTree() {
		Tree<String> t = new Tree<String>("a");
		assertEquals("a", t.getValue());

	}

	@Test
	public final void testEquals() {
		assertTrue(g1.equals(g2));
		assertFalse(d1.equals(e1));
		assertTrue(a2.equals(a2));
		assertFalse(a1.equals(a2));
		assertTrue(c1.equals(c2));
		assertFalse(b1.equals(b2));
		assertFalse(e1.equals(null));// to verify
		Tree<String> m = new Tree<String>("m");
		Tree n = new Tree(null);// to verify
		assertFalse(m.equals(n));
		assertFalse(n.equals(m));

	}

	@Test
	public final void testAddChildTree() {

		Tree<String> p = new Tree<String>("p");
		Tree<String> q = new Tree<String>("q");
		Tree<String> r = new Tree<String>("r");
		a1.addChild(p);
		a1.addChild(q);
		a1.addChild(r);
		ArrayList<Tree> expected = new ArrayList<Tree>(Arrays
				.asList(new Tree[] { b1, c1, p, q, r }));
		assertEquals(expected, a1.children());
		assertEquals(new ArrayList<Tree<String>>(), d1.children());
		try {
			d1.addChild(b1);
			fail();
		} catch (Exception e) {
		}

		try {
			c1.addChild(c1);
			fail();
		} catch (Exception e) {
		}

		try {
			d1.addChild(a1);
			fail();
		} catch (Exception e) {
		}

	}

	@Test
	public void testAddChildString() {
		Tree<String> p = new Tree<String>("p");
		Tree<String> q = new Tree<String>("q");
		Tree<String> r = new Tree<String>("r");

		a1.addChild("p");
		a1.addChild("q");
		a1.addChild("r");
		ArrayList<Tree> expected = new ArrayList<Tree>(Arrays
				.asList(new Tree[] { b1, c1, p, q, r }));
		assertEquals(expected, a1.children());

	}

	@Test
	public final void testChildren() {

		Tree<String> p = new Tree<String>("p1");
		Tree<String> q = new Tree<String>("q1");
		Tree<String> r = new Tree<String>("r1");
		a1.addChild(p);
		a1.addChild(q);
		a1.addChild(r);
		ArrayList<Tree> expected = new ArrayList<Tree>(Arrays
				.asList(new Tree[] { b1, c1, p, q, r }));
		assertEquals(expected, a1.children());

	}

	@Test(timeout = 100)
	public final void testIterator() {

		Tree<String> p1 = new Tree<String>("p");
		Tree<String> q1 = new Tree<String>("q");
		p1.addChild(q1);
		Iterator<Tree<String>> iter = p1.iterator();
		assertTrue(iter.hasNext());

	}

	@Test
	public final void testSetValue() {
		b1.setValue("B");
		assertEquals("B", b1.getValue());

	}

	@Test
	public final void testGetValue() {
		assertEquals("e", e1.getValue());
	}

	@Test
	public void testToparse() {
		Tree<String> p1 = new Tree<String>("p");
		Tree<String> q1 = new Tree<String>("q");
		p1.addChild(q1);
		Tree<String> act = Tree.parse("p(q)");
		assertEquals(p1, act);

		Tree<String> actual = Tree
				.parse("one(two three(four five(six seven eight)))");
		assertEquals(tree1, actual);

	}

	@Test
	public final void testSize() {

		assertEquals(2, c1.size());
		assertEquals(0, g1.size());
	}

	@Test
	public final void testContains() {
		assertTrue(a1.contains(a1));
		assertTrue(a1.contains(b1));
		assertTrue(a1.contains(d1));
		assertTrue(b1.contains(d1));
		assertFalse(b1.contains(a1));
		assertFalse(d1.contains(a1));
		assertFalse(d1.contains(b1));
		assertFalse(b1.contains(c1));
	}

}
