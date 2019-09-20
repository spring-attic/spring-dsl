/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.document;

import org.springframework.dsl.document.DefaultRegion;
import org.springframework.dsl.document.Region;
import org.springframework.util.Assert;

public class DefaultDocumentLineTracker implements DocumentLineTracker {

	public final static DocumentText[] DELIMITERS = { DocumentText.from("\r"), DocumentText.from("\n"),
			DocumentText.from("\r\n") };
	private static final boolean ASSERT = true;
	private static final DocumentText NO_DELIM = DocumentText.from("");
	private Node rootNode = new Node(0, NO_DELIM);

	@Override
	public DocumentText[] getLegalLineDelimiters() {
		return DELIMITERS;
	}

	@Override
	public DocumentText getLineDelimiter(int line) {
		Node node = nodeByLine(line);
		return node.delimiter == NO_DELIM ? null : node.delimiter;
	}

	@Override
	public int getNumberOfLines() {
		Node node = rootNode;
		int lines = 0;
		while (node != null) {
			lines += node.line + 1;
			node = node.right;
		}
		return lines;
	}

	@Override
	public int getNumberOfLines(int offset, int length) {
		if (length == 0) {
			return 1;
		}

		int startLine = lineByOffset(offset);
		int endLine = lineByOffset(offset + length);

		return endLine - startLine + 1;
	}

	@Override
	public int getLineOffset(int line) {
		return offsetByLine(line);
	}

	@Override
	public int getLineLength(int line) {
		Node node = nodeByLine(line);
		return node.length;
	}

	@Override
	public int getLineNumberOfOffset(int offset) {
		return lineByOffset(offset);
	}

	@Override
	public Region getLineInformationOfOffset(int offset) {
		// Inline nodeByOffset start as we need both node and offset
		int remaining = offset;
		Node node = rootNode;
		final int lineOffset;

		while (true) {
			if (node == null) {
				fail(offset);
			}

			if (remaining < node.offset) {
				node = node.left;
			} else {
				remaining -= node.offset;
				if (remaining < node.length || remaining == node.length && node.right == null) { // last line
					lineOffset = offset - remaining;
					break;
				}
				remaining -= node.length;
				node = node.right;
			}
		}
		// Inline nodeByOffset end
		return new DefaultRegion(lineOffset, node.pureLength());
	}

	@Override
	public Region getLineInformation(int line) {
		try {
			// Inline nodeByLine start
			int remaining = line;
			int offset = 0;
			Node node = rootNode;

			while (true) {
				if (node == null) {
					fail(line);
				}

				if (remaining == node.line) {
					offset += node.offset;
					break;
				}
				if (remaining < node.line) {
					node = node.left;
				} else {
					remaining -= node.line + 1;
					offset += node.offset + node.length;
					node = node.right;
				}
			}
			// Inline nodeByLine end
			return new DefaultRegion(offset, node.pureLength());
		} catch (BadLocationException x) {
			if (line > 0 && line == getNumberOfLines()) {
				line = line - 1;
				// Inline nodeByLine start
				int remaining = line;
				int offset = 0;
				Node node = rootNode;

				while (true) {
					if (node == null) {
						fail(line);
					}

					if (remaining == node.line) {
						offset += node.offset;
						break;
					}
					if (remaining < node.line) {
						node = node.left;
					} else {
						remaining -= node.line + 1;
						offset += node.offset + node.length;
						node = node.right;
					}
				}
				Node last = node;
				// Inline nodeByLine end
				if (last.length > 0) {
					return new DefaultRegion(offset + last.length, 0);
				}
			}
			throw x;
		}
	}

	@Override
	public void replace(int offset, int length, DocumentText text) {
		// String text = textx.toString();
		if (ASSERT) {
			checkTree();
		}

		// Inlined nodeByOffset as we need both node and offset
		int remaining = offset;
		Node first = rootNode;
		final int firstNodeOffset;

		while (true) {
			if (first == null) {
				fail(offset);
			}

			if (remaining < first.offset) {
				first = first.left;
			} else {
				remaining -= first.offset;
				if (remaining < first.length || remaining == first.length && first.right == null) { // last line
					firstNodeOffset = offset - remaining;
					break;
				}
				remaining -= first.length;
				first = first.right;
			}
		}
		// Inline nodeByOffset end
		if (ASSERT) {
			Assert.isTrue(first != null, "");
		}

		Node last;
		if (offset + length < firstNodeOffset + first.length) {
			last = first;
		} else {
			last = nodeByOffset(offset + length);
		}
		if (ASSERT) {
			Assert.isTrue(last != null, "");
		}

		int firstLineDelta = firstNodeOffset + first.length - offset;
		if (first == last) {
			replaceInternal(first, text, length, firstLineDelta);
		} else {
			replaceFromTo(first, last, text, length, firstLineDelta);
		}

		if (ASSERT) {
			checkTree();
		}
	}

	@Override
	public void set(DocumentText text) {
		rootNode = new Node(0, NO_DELIM);
		replace(0, 0, text);
	}

	/**
	 * Returns the node (line) with the given line number. Note that the last line
	 * is always incomplete, i.e. has the {@link #NO_DELIM} delimiter.
	 *
	 * @param line a line number
	 * @return the line with the given line number
	 * @throws BadLocationException if the line is invalid
	 */
	private Node nodeByLine(final int line) throws BadLocationException {
		int remaining = line;
		Node node = rootNode;
		while (true) {
			if (node == null) {
				fail(line);
			}
			if (remaining == node.line) {
				break;
			}
			if (remaining < node.line) {
				node = node.left;
			} else {
				remaining -= node.line + 1;
				node = node.right;
			}
		}
		return node;
	}

	/**
	 * Returns the offset for the given line number. Note that the last line is
	 * always incomplete, i.e. has the {@link #NO_DELIM} delimiter.
	 *
	 * @param line a line number
	 * @return the line offset with the given line number
	 * @throws BadLocationException if the line is invalid
	 */
	private int offsetByLine(final int line) throws BadLocationException {
		int remaining = line;
		int offset = 0;
		Node node = rootNode;
		while (true) {
			if (node == null) {
				fail(line);
			}
			if (remaining == node.line) {
				return offset + node.offset;
			}
			if (remaining < node.line) {
				node = node.left;
			} else {
				remaining -= node.line + 1;
				offset += node.offset + node.length;
				node = node.right;
			}
		}
	}

	/**
	 * Returns the line number for the given offset. If the offset is between two
	 * lines, the line starting at <code>offset</code> is returned. The last line is
	 * returned if <code>offset</code> is equal to the document length.
	 *
	 * @param offset a document offset
	 * @return the line number starting at or containing <code>offset</code>
	 * @throws BadLocationException if the offset is invalid
	 */
	private int lineByOffset(final int offset) throws BadLocationException {
		int remaining = offset;
		Node node = rootNode;
		int line = 0;
		while (true) {
			if (node == null) {
				fail(offset);
			}
			if (remaining < node.offset) {
				node = node.left;
			} else {
				remaining -= node.offset;
				line += node.line;
				if (remaining < node.length || remaining == node.length && node.right == null) {
					// last line
					return line;
				}
				remaining -= node.length;
				line++;
				node = node.right;
			}
		}
	}

	/**
	 * Replace spanning from one node to another.
	 *
	 * @param node the first affected node
	 * @param last the last affected node
	 * @param text the added text
	 * @param length the replace length, &gt;= <code>firstLineDelta</code>
	 * @param firstLineDelta the number of characters removed from the replacement
	 *                       offset to the end of <code>node</code>, &lt;=
	 *                       <code>length</code>
	 */
	private void replaceFromTo(Node node, Node last, DocumentText text, int length, int firstLineDelta) {
		Node successor = successor(node);
		while (successor != last) {
			length -= successor.length;
			Node toDelete = successor;
			successor = successor(successor);
			updateLength(toDelete, -toDelete.length);
		}

		DelimiterInfo info = text == null ? null : nextDelimiterInfo(text, 0);

		if (info == null || info.delimiter == null) {
			int added = text == null ? 0 : text.length();

			// join the two lines if there are no lines added
			join(node, last, added - length);

		} else {

			// join the first line with the first added
			int consumed = info.delimiterIndex + info.delimiterLength;
			updateLength(node, consumed - firstLineDelta);
			node.delimiter = info.delimiter;
			length -= firstLineDelta;

			// Inline addLines start
			info = nextDelimiterInfo(text, consumed);
			while (info != null) {
				int lineLen = info.delimiterIndex - consumed + info.delimiterLength;
				node = insertAfter(node, lineLen, info.delimiter);
				consumed += lineLen;
				info = nextDelimiterInfo(text, consumed);
			}
			// Inline addLines end

			updateLength(last, text.length() - consumed - length);
		}
	}

	/**
	 * Joins two consecutive node lines, additionally adjusting the resulting length
	 * of the combined line by <code>delta</code>. The first node gets deleted.
	 *
	 * @param one the first node to join
	 * @param two the second node to join
	 * @param delta the delta to apply to the remaining single node
	 */
	private void join(Node one, Node two, int delta) {
		int oneLength = one.length;
		updateLength(one, -oneLength);
		updateLength(two, oneLength + delta);
	}

	/**
	 * Returns the node (line) including a certain offset. If the offset is between
	 * two lines, the line starting at <code>offset</code> is returned.
	 * <p>
	 * This means that for offsets smaller than the length, the following holds:
	 * </p>
	 * <p>
	 * <code>line.offset <= offset < line.offset + offset.length</code>.
	 * </p>
	 * <p>
	 * If <code>offset</code> is the document length, then this is true:
	 * </p>
	 * <p>
	 * <code>offset= line.offset + line.length</code>.
	 * </p>
	 *
	 * @param offset a document offset
	 * @return the line starting at or containing <code>offset</code>
	 * @throws BadLocationException if the offset is invalid
	 */
	private Node nodeByOffset(final int offset) throws BadLocationException {
		/*
		 * Works for any binary search tree.
		 */
		int remaining = offset;
		Node node = rootNode;
		while (true) {
			if (node == null) {
				fail(offset);
			}

			if (remaining < node.offset) {
				node = node.left;
			} else {
				remaining -= node.offset;
				if (remaining < node.length || remaining == node.length && node.right == null) { // last line
					break;
				}
				remaining -= node.length;
				node = node.right;
			}
		}

		return node;
	}

	/**
	 * Throws an exception.
	 *
	 * @param offset the illegal character or line offset that caused the exception
	 * @throws BadLocationException always
	 */
	private void fail(int offset) {
		throw new BadLocationException("Line not in bounds");
	}

	private DelimiterInfo fDelimiterInfo= new DelimiterInfo();

	protected DelimiterInfo nextDelimiterInfo(DocumentText text, int offset) {

		char ch;
		int length= text.length();
		for (int i= offset; i < length; i++) {

			ch= text.charAt(i);
			if (ch == '\r') {

				if (i + 1 < length) {
					if (text.charAt(i + 1) == '\n') {
						fDelimiterInfo.delimiter= DELIMITERS[2];
						fDelimiterInfo.delimiterIndex= i;
						fDelimiterInfo.delimiterLength= 2;
						return fDelimiterInfo;
					}
				}

				fDelimiterInfo.delimiter= DELIMITERS[0];
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;

			} else if (ch == '\n') {

				fDelimiterInfo.delimiter= DELIMITERS[1];
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;
			}
		}

		return null;
	}

	/**
	 * Updates the differential indices following the parent chain. All nodes from
	 * <code>from.parent</code> to the root are updated.
	 *
	 * @param node the child of the first node to update
	 * @param deltaLength the character delta
	 * @param deltaLines the line delta
	 */
	private void updateParentChain(Node node, int deltaLength, int deltaLines) {
		updateParentChain(node, null, deltaLength, deltaLines);
	}

	/**
	 * Updates the differential indices following the parent chain. All nodes from
	 * <code>from.parent</code> to <code>to</code> (exclusive) are updated.
	 *
	 * @param from the child of the first node to update
	 * @param to the first node not to update
	 * @param deltaLength the character delta
	 * @param deltaLines the line delta
	 */
	private void updateParentChain(Node from, Node to, int deltaLength, int deltaLines) {
		Node parent = from.parent;
		while (parent != to) {
			// only update node if update comes from left subtree
			if (from == parent.left) {
				parent.offset += deltaLength;
				parent.line += deltaLines;
			}
			from = parent;
			parent = from.parent;
		}
	}

	/**
	 * Helper method for moving a child, ensuring that parent pointers are set
	 * correctly.
	 *
	 * @param parent the new parent of <code>child</code>, <code>null</code> to
	 *               replace the root node
	 * @param child  the new child of <code>parent</code>, may be
	 *               <code>null</code>
	 * @param isLeftChild <code>true</code> if <code>child</code> shall become
	 *                    <code>parent</code>'s left child, <code>false</code> if it
	 *                    shall become <code>parent</code>'s right child
	 */
	private void setChild(Node parent, Node child, boolean isLeftChild) {
		if (parent == null) {
			if (child == null) {
				rootNode = new Node(0, NO_DELIM);
			} else {
				rootNode = child;
			}
		} else {
			if (isLeftChild) {
				parent.left = child;
			} else {
				parent.right = child;

			}
		}
		if (child != null) {
			child.parent = parent;
		}
	}

	/**
	 * Returns the successor of a node, <code>null</code> if node is the last node.
	 *
	 * @param node a node
	 * @return the successor of <code>node</code>, <code>null</code> if there is
	 *         none
	 */
	private Node successor(Node node) {
		if (node.right != null) {
			return successorDown(node.right);
		}

		return successorUp(node);
	}

	/**
	 * Searches the successor of <code>node</code> in its parent chain.
	 *
	 * @param node a node
	 * @return the first node in <code>node</code>'s parent chain that is reached
	 *         from its left subtree, <code>null</code> if there is none
	 */
	private Node successorUp(final Node node) {
		Node child = node;
		Node parent = child.parent;
		while (parent != null) {
			if (child == parent.left) {
				return parent;
			}
			child = parent;
			parent = child.parent;
		}
		if (ASSERT)
			Assert.isTrue(node.delimiter == NO_DELIM, "");
		return null;
	}

	/**
	 * Searches the left-most node in a given subtree.
	 *
	 * @param node a node
	 * @return the left-most node in the given subtree
	 */
	private Node successorDown(Node node) {
		Node child = node.left;
		while (child != null) {
			node = child;
			child = node.left;
		}
		return node;
	}

	/**
	 * Left rotation - the given node is rotated down, its right child is rotated
	 * up, taking the previous structural position of <code>node</code>.
	 *
	 * @param node the node to rotate around
	 */
	private void rotateLeft(Node node) {
		if (ASSERT)
			Assert.notNull(node, "");
		Node child = node.right;
		if (ASSERT)
			Assert.notNull(child, "");
		boolean leftChild = node.parent == null || node == node.parent.left;

		// restructure
		setChild(node.parent, child, leftChild);

		setChild(node, child.left, false);
		setChild(child, node, true);

		// update relative info
		// child becomes the new parent, its line and offset counts increase as the
		// former parent
		// moves under child's left subtree
		child.line += node.line + 1;
		child.offset += node.offset + node.length;
	}

	/**
	 * Right rotation - the given node is rotated down, its left child is rotated
	 * up, taking the previous structural position of <code>node</code>.
	 *
	 * @param node the node to rotate around
	 */
	private void rotateRight(Node node) {
		if (ASSERT)
			Assert.notNull(node, "");
		Node child = node.left;
		if (ASSERT)
			Assert.notNull(child, "");
		boolean leftChild = node.parent == null || node == node.parent.left;

		setChild(node.parent, child, leftChild);

		setChild(node, child.right, true);
		setChild(child, node, false);

		// update relative info
		// node loses its left subtree, except for what it keeps in its new subtree
		// this is exactly the amount in child
		node.line -= child.line + 1;
		node.offset -= child.offset + child.length;
	}

	/**
	 * A left rotation around <code>parent</code>, whose structural position is
	 * replaced by <code>node</code>.
	 *
	 * @param node the node moving up and left
	 * @param parent the node moving left and down
	 */
	private void singleLeftRotation(Node node, Node parent) {
		rotateLeft(parent);
		node.balance = 0;
		parent.balance = 0;
	}

	/**
	 * A right rotation around <code>parent</code>, whose structural position is
	 * replaced by <code>node</code>.
	 *
	 * @param node the node moving up and right
	 * @param parent the node moving right and down
	 */
	private void singleRightRotation(Node node, Node parent) {
		rotateRight(parent);
		node.balance = 0;
		parent.balance = 0;
	}

	/**
	 * A double left rotation, first rotating right around <code>node</code>, then
	 * left around <code>parent</code>.
	 *
	 * @param node the node that will be rotated right
	 * @param parent the node moving left and down
	 */
	private void rightLeftRotation(Node node, Node parent) {
		Node child = node.left;
		rotateRight(node);
		rotateLeft(parent);
		if (child.balance == 1) {
			node.balance = 0;
			parent.balance = -1;
			child.balance = 0;
		} else if (child.balance == 0) {
			node.balance = 0;
			parent.balance = 0;
		} else if (child.balance == -1) {
			node.balance = 1;
			parent.balance = 0;
			child.balance = 0;
		}
	}

	/**
	 * A double right rotation, first rotating left around <code>node</code>, then
	 * right around <code>parent</code>.
	 *
	 * @param node the node that will be rotated left
	 * @param parent the node moving right and down
	 */
	private void leftRightRotation(Node node, Node parent) {
		Node child = node.right;
		rotateLeft(node);
		rotateRight(parent);
		if (child.balance == -1) {
			node.balance = 0;
			parent.balance = 1;
			child.balance = 0;
		} else if (child.balance == 0) {
			node.balance = 0;
			parent.balance = 0;
		} else if (child.balance == 1) {
			node.balance = -1;
			parent.balance = 0;
			child.balance = 0;
		}
	}

	/**
	 * Re-balances a node whose parent has a double positive balance.
	 *
	 * @param node the node to re-balance
	 */
	private void rebalanceAfterInsertionRight(Node node) {
		Node parent = node.parent;
		if (node.balance == 1) {
			singleLeftRotation(node, parent);
		} else if (node.balance == -1) {
			rightLeftRotation(node, parent);
		} else if (ASSERT) {
			Assert.isTrue(false, "");
		}
	}

	/**
	 * Re-balances a node whose parent has a double negative balance.
	 *
	 * @param node the node to re-balance
	 */
	private void rebalanceAfterInsertionLeft(Node node) {
		Node parent = node.parent;
		if (node.balance == -1) {
			singleRightRotation(node, parent);
		} else if (node.balance == 1) {
			leftRightRotation(node, parent);
		} else if (ASSERT) {
			Assert.isTrue(false, "");
		}
	}

	/**
	 * Re-balances a node whose parent has a double positive balance.
	 *
	 * @param node the node to re-balance
	 * @return <code>true</code> if the re-balancement leaves the height at
	 *         <code>node.parent</code> constant, <code>false</code> if the height
	 *         changed
	 */
	private boolean rebalanceAfterDeletionLeft(Node node) {
		Node parent = node.parent;
		if (node.balance == 1) {
			singleLeftRotation(node, parent);
			return false;
		} else if (node.balance == -1) {
			rightLeftRotation(node, parent);
			return false;
		} else if (node.balance == 0) {
			rotateLeft(parent);
			node.balance = -1;
			parent.balance = 1;
			return true;
		} else {
			if (ASSERT)
				Assert.isTrue(false, "");
			return true;
		}
	}

	/**
	 * Re-balances a node whose parent has a double negative balance.
	 *
	 * @param node the node to re-balance
	 * @return <code>true</code> if the re-balancement leaves the height at
	 *         <code>node.parent</code> constant, <code>false</code> if the height
	 *         changed
	 */
	private boolean rebalanceAfterDeletionRight(Node node) {
		Node parent = node.parent;
		if (node.balance == -1) {
			singleRightRotation(node, parent);
			return false;
		} else if (node.balance == 1) {
			leftRightRotation(node, parent);
			return false;
		} else if (node.balance == 0) {
			rotateRight(parent);
			node.balance = 1;
			parent.balance = -1;
			return true;
		} else {
			if (ASSERT)
				Assert.isTrue(false, "");
			return true;
		}
	}

	/**
	 * Updates the balance information in the parent chain of node.
	 *
	 * @param node the first node that needs balance updating
	 * @param wasLeftChild <code>true</code> if the deletion happened on
	 *                     <code>node</code>'s left subtree, <code>false</code> if
	 *                     it occurred on <code>node</code>'s right subtree
	 */
	private void updateParentBalanceAfterDeletion(Node node, boolean wasLeftChild) {
		while (node != null) {
			if (wasLeftChild) {
				node.balance++;
			} else {
				node.balance--;
			}

			Node parent = node.parent;
			if (parent != null) {
				wasLeftChild = node == parent.left;
			}

			switch (node.balance) {
			case 1:
			case -1:
				return; // done, no tree change
			case -2:
				if (rebalanceAfterDeletionRight(node.left))
					return;
				break; // propagate up
			case 2:
				if (rebalanceAfterDeletionLeft(node.right))
					return;
				break; // propagate up
			case 0:
				break; // propagate up
			default:
				if (ASSERT)
					Assert.isTrue(false, "");
			}

			node = parent;
		}
	}

	/**
	 * Deletes a node from the tree, re-balancing it if necessary. The differential
	 * indices in the node's parent chain have to be updated in advance to calling
	 * this method. Generally, don't call <code>delete</code> directly, but call
	 * <code>update_length(node, -node.length)</code> to properly remove a node.
	 *
	 * @param node the node to delete.
	 */
	private void delete(Node node) {
		if (ASSERT)
			Assert.isTrue(node != null, "node cannot be null");
		if (ASSERT)
			Assert.isTrue(node.length == 0, "node length must be 0");

		Node parent = node.parent;
		Node toUpdate; // the parent of the node that lost a child
		boolean lostLeftChild;
		boolean isLeftChild = parent == null || node == parent.left;

		if (node.left == null || node.right == null) {
			// 1) node has one child at max - replace parent's pointer with the only child
			// also handles the trivial case of no children
			Node replacement = node.left == null ? node.right : node.left;
			setChild(parent, replacement, isLeftChild);
			toUpdate = parent;
			lostLeftChild = isLeftChild;
			// no updates to do - subtrees stay as they are
		} else if (node.right.left == null) {
			// 2a) node's right child has no left child - replace node with right child,
			// giving node's
			// left subtree to the right child
			Node replacement = node.right;
			setChild(parent, replacement, isLeftChild);
			setChild(replacement, node.left, true);
			replacement.line = node.line;
			replacement.offset = node.offset;
			replacement.balance = node.balance;
			toUpdate = replacement;
			lostLeftChild = false;
//		} else if (node.left.right == null) {
//			// 2b) symmetric case
//			Node replacement= node.left;
//			set_child(parent, replacement, isLeftChild);
//			set_child(replacement, node.right, false);
//			replacement.balance= node.balance;
//			toUpdate= replacement;
//			lostLeftChild= true;
		} else {
			// 3) hard case - replace node with its successor
			Node successor = successor(node);

			// successor exists (otherwise node would not have right child, case 1)
			if (ASSERT)
				Assert.notNull(successor, "");
			// successor has no left child (a left child would be the real successor of
			// node)
			if (ASSERT)
				Assert.state(successor.left == null, "");
			if (ASSERT)
				Assert.state(successor.line == 0, "");
			// successor is the left child of its parent (otherwise parent would be smaller
			// and
			// hence the real successor)
			if (ASSERT)
				Assert.state(successor == successor.parent.left, "");
			// successor is not a child of node (would have been covered by 2a)
			if (ASSERT)
				Assert.state(successor.parent != node, "");

			toUpdate = successor.parent;
			lostLeftChild = true;

			// update relative indices
			updateParentChain(successor, node, -successor.length, -1);

			// delete successor from its current place - like 1)
			setChild(toUpdate, successor.right, true);

			// move node's subtrees to its successor
			setChild(successor, node.right, false);
			setChild(successor, node.left, true);

			// replace node by successor in its parent
			setChild(parent, successor, isLeftChild);

			// update the successor
			successor.line = node.line;
			successor.offset = node.offset;
			successor.balance = node.balance;
		}

		updateParentBalanceAfterDeletion(toUpdate, lostLeftChild);
	}

	/**
	 * Adjusts the length of a node by <code>delta</code>, also adjusting the parent
	 * chain of <code>node</code>. If the node's length becomes zero and is not the
	 * last (incomplete) node, it is deleted after the update.
	 *
	 * @param node  the node to adjust
	 * @param delta the character delta to add to the node's length
	 */
	private void updateLength(Node node, int delta) {
		if (ASSERT)
			Assert.isTrue(node.length + delta >= 0, "");

		// update the node itself
		node.length += delta;

		// check deletion
		final int lineDelta;
		boolean delete = node.length == 0 && node.delimiter != NO_DELIM;
		if (delete) {
			lineDelta = -1;
		} else {
			lineDelta = 0;
		}

		// update parent chain
		if (delta != 0 || lineDelta != 0) {
			updateParentChain(node, delta, lineDelta);
		}

		if (delete) {
			delete(node);
		}
	}

	/**
	 * Updates the balance information in the parent chain of node until it reaches
	 * the root or finds a node whose balance violates the AVL constraint, which is
	 * the re-balanced.
	 *
	 * @param node the child of the first node that needs balance updating
	 */
	private void updateParentBalanceAfterInsertion(Node node) {
		Node parent = node.parent;
		while (parent != null) {
			if (node == parent.left) {
				parent.balance--;
			} else {
				parent.balance++;
			}

			switch (parent.balance) {
			case 1:
			case -1:
				node = parent;
				parent = node.parent;
				continue;
			case -2:
				rebalanceAfterInsertionLeft(node);
				break;
			case 2:
				rebalanceAfterInsertionRight(node);
				break;
			case 0:
				break;
			default:
				if (ASSERT)
					Assert.isTrue(false, "unknown balance");
			}
			return;
		}
	}

	/**
	 * Inserts a line with the given length and delimiter after <code>node</code>.
	 *
	 * @param node the predecessor of the inserted node
	 * @param length the line length of the inserted node
	 * @param delimiter the delimiter of the inserted node
	 * @return the inserted node
	 */
	private Node insertAfter(Node node, int length, DocumentText delimiter) {
		/*
		 * An insertion really shifts the key of all succeeding nodes. Hence we insert
		 * the added node between node and the successor of node. The added node becomes
		 * either the right child of the predecessor node, or the left child of the
		 * successor node.
		 */
		Node added = new Node(length, delimiter);

		if (node.right == null) {
			setChild(node, added, false);
		} else {
			setChild(successorDown(node.right), added, true);
		}

		// parent chain update
		updateParentChain(added, length, 1);
		updateParentBalanceAfterInsertion(added);

		return added;
	}

	/**
	 * Replace happening inside a single line.
	 *
	 * @param node the affected node
	 * @param text the added text
	 * @param length the replace length, &lt; <code>firstLineDelta</code>
	 * @param firstLineDelta the number of characters from the replacement offset to
	 *                       the end of <code>node</code> &gt; <code>length</code>
	 */
	private void replaceInternal(Node node, DocumentText text, int length, int firstLineDelta) {
		// 1) modification on a single line

		DelimiterInfo info = text == null ? null : nextDelimiterInfo(text, 0);

		if (info == null || info.delimiter == null) {
			// a) trivial case: insert into a single node, no line mangling
			int added = text == null ? 0 : text.length();
			updateLength(node, added - length);
		} else {
			// b) more lines to add between two chunks of the first node
			// remember what we split off the first line
			int remainder = firstLineDelta - length;
			DocumentText remDelim = node.delimiter;

			// join the first line with the first added
			int consumed = info.delimiterIndex + info.delimiterLength;
			int delta = consumed - firstLineDelta;
			updateLength(node, delta);
			node.delimiter = info.delimiter;

			// Inline addlines start
			info = nextDelimiterInfo(text, consumed);
			while (info != null) {
				int lineLen = info.delimiterIndex - consumed + info.delimiterLength;
				node = insertAfter(node, lineLen, info.delimiter);
				consumed += lineLen;
				info = nextDelimiterInfo(text, consumed);
			}
			// Inline addlines end

			// add remaining chunk merged with last (incomplete) additional line
			insertAfter(node, remainder + text.length() - consumed, remDelim);
		}
	}

	/**
	 * A node represents one line. Its character and line offsets are 0-based and
	 * relative to the subtree covered by the node. All nodes under the left subtree
	 * represent lines before, all nodes under the right subtree lines after the
	 * current node.
	 */
	private static final class Node {

		Node(int length, DocumentText delimiter) {
			this.length = length;
			this.delimiter = delimiter;
		}

		/**
		 * The line index in this node's line tree, or equivalently, the number of lines
		 * in the left subtree.
		 */
		int line;
		/**
		 * The line offset in this node's line tree, or equivalently, the number of
		 * characters in the left subtree.
		 */
		int offset;
		/** The number of characters in this line. */
		int length;
		/** The line delimiter of this line, needed to answer the delimiter query. */
		DocumentText delimiter;
		/** The parent node, <code>null</code> if this is the root node. */
		Node parent;
		/** The left subtree, possibly <code>null</code>. */
		Node left;
		/** The right subtree, possibly <code>null</code>. */
		Node right;
		/** The balance factor. */
		byte balance;

		@Override
		public final String toString() {
			String bal;
			switch (balance) {
			case 0:
				bal = "=";
				break;
			case 1:
				bal = "+";
				break;
			case 2:
				bal = "++";
				break;
			case -1:
				bal = "-";
				break;
			case -2:
				bal = "--";
				break;
			default:
				bal = Byte.toString(balance);
			}
			return "[" + offset + "+" + pureLength() + "+" + delimiter.length() + "|" + line + "|" + bal + "]";
		}

		/**
		 * Returns the pure (without the line delimiter) length of this line.
		 *
		 * @return the pure line length
		 */
		int pureLength() {
			return length - delimiter.length();
		}
	}

	/**
	 * Combines the information of the occurrence of a line delimiter.
	 * <code>delimiterIndex</code> is the index where a line delimiter starts,
	 * whereas <code>delimiterLength</code>, indicates the length of the delimiter.
	 */
	private static class DelimiterInfo {
		public int delimiterIndex;
		public int delimiterLength;
		public DocumentText delimiter;
	}

	private void checkTree() {
		checkTreeStructure(rootNode);

		try {
			checkTreeOffsets(nodeByOffset(0), new int[] { 0, 0 }, null);
		} catch (BadLocationException x) {
			throw new AssertionError();
		}
	}
	/**
	 * Debug-only method that validates the tree structure below <code>node</code>.
	 * I.e. it checks whether all parent/child pointers are consistent and whether
	 * the AVL balance information is correct.
	 *
	 * @param node the node to validate
	 * @return the depth of the tree under <code>node</code>
	 */
	private byte checkTreeStructure(Node node) {
		if (node == null) {
			return 0;
		}
		byte leftDepth = checkTreeStructure(node.left);
		byte rightDepth = checkTreeStructure(node.right);
		Assert.isTrue(node.balance == rightDepth - leftDepth, "");
		Assert.isTrue(node.left == null || node.left.parent == node, "");
		Assert.isTrue(node.right == null || node.right.parent == node, "");
		return (byte) (Math.max(rightDepth, leftDepth) + 1);
	}

	/**
	 * Debug-only method that checks the differential offsets of the tree, starting
	 * at <code>node</code> and continuing until <code>last</code>.
	 *
	 * @param node the first <code>Node</code> to check, may be <code>null</code>
	 * @param offLen an array of length 2, with <code>offLen[0]</code> the expected
	 *               offset of <code>node</code> and <code>offLen[1]</code> the
	 *               expected line of <code>node</code>
	 * @param last the last <code>Node</code> to check, may be <code>null</code>
	 * @return an <code>int[]</code> of length 2, with the first element being the
	 *         character length of <code>node</code>'s subtree, and the second
	 *         element the number of lines in <code>node</code>'s subtree
	 */
	private int[] checkTreeOffsets(Node node, int[] offLen, Node last) {
		if (node == last) {
			return offLen;
		}
		Assert.isTrue(node.offset == offLen[0], "");
		Assert.isTrue(node.line == offLen[1], "");
		if (node.right != null) {
			int[] result = checkTreeOffsets(successorDown(node.right), new int[2], node);
			offLen[0] += result[0];
			offLen[1] += result[1];
		}
		offLen[0] += node.length;
		offLen[1]++;
		return checkTreeOffsets(node.parent, offLen, last);
	}
}
