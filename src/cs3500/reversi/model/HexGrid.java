package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * A HexGrid is a fixed size point up hexagonal array.
 * Locations in the HexGrid are accessed using their axial coordinates q and r
 * where q is the horizontal row number and r is the down-right pointing row number.
 * The point (0, 0) is the center of the array, and q increases going up while r increases
 * moving to the right.
 * @param <T> The type of element this hexGrid holds.
 */
public class HexGrid<T> {
  //outer list is q coordinate, inner is r
  private final HexList<HexList<T>> grid;

  /**
   * Creates a new point up hexagonal array.
   * @param size the length of the longest row in the array
   */
  public HexGrid(int size) {
    this.grid = new HexList<HexList<T>>(size);
    int offset = (size - 1) / 2;
    int rowSize = size % 2 + 1;
    for (int q = -offset; q <= offset; q++) {
      this.grid.set(q, new HexList<T>(rowSize));
      rowSize += q < 0 ? 1 : -1;
    }
  }

  // A HexList is a fixed size list centered on index 0, the length needs to be odd
  private static final class HexList<U> implements Iterable<U> {
    private final int offset;
    private final ArrayList<U> baseList;

    private HexList(int length) {
      if (length % 2 == 0) {
        throw new IllegalArgumentException("length must be odd");
      }
      this.baseList = new ArrayList<U>(length);
      this.offset = (length - 1) / 2;
      for (int i = 0; i < length; i++) {
        this.baseList.add(null);
      }
    }

    /** Set the value at the given index to the specified element.
     *
     * @param index The location in this hexList to insert the element
     * @param element The element to insert into this list
     * @throws IndexOutOfBoundsException if the index is outside the size of this list
     */
    void set(int index, U element) {
      if (Math.abs(index) > offset) {
        throw new IndexOutOfBoundsException();
      }
      baseList.add(index + this.offset, element);
    }

    /**
     * Get the value at the given index.
     * @param index the location in this list to get the element
     * @return the element at the given location in the list or null if no element has been put in
     *         this list yet
     */
    U get(int index) {
      if (Math.abs(index) > offset) {
        throw new IndexOutOfBoundsException();
      }
      return baseList.get(index + this.offset);
    }

    /**
     * Finds whether the given element is in this list.
     * @param element The element to look for
     * @return True if the element is in this list
     */
    boolean contains(U element) {
      return this.baseList.contains(element);
    }

    /**
     * Return the index of the given element in this list.
     * @param element The element to find in this list
     * @return the index of the element in this list.
     * @throws NoSuchElementException if the element is not in this list
     */
    int indexOf(U element) {
      if (!this.contains(element)) {
        throw new NoSuchElementException();
      }
      return this.baseList.indexOf(element) - this.offset;
    }

    Stream<U> stream() {
      return this.baseList.stream();
    }

    public Iterator<U> iterator() {
      return this.baseList.iterator();
    }
  }

  /**
   * Get the element at the given axial coordinates in this hexGrid.
   * @param q the axial coordinate q corresponding to which horizontal row the element is in
   *          with row 0 in the center
   * @param r the axial coordinate r corresponding to which down-right pointing
   *          row the element is in with row 0 in the center
   * @throws IndexOutOfBoundsException if q or r is out of this array
   */
  T get(int q, int r) {
    return this.grid.get(q).get(r);
  }

  /**
   * Set the element at the given axial coordinates to the given element.
   * @param q the axial coordinate q corresponding to which horizontal row the element is in
   *          with row 0 in the center
   * @param r the axial coordinate r corresponding to which down-right pointing
   *          row the element is in with row 0 in the center
   * @throws IndexOutOfBoundsException if q or r is out of this array
   */
  void set(int q, int r, T element) {
    this.grid.get(q).set(r, element);
  }

  /**
   * Determines if this HexGrid contains the given element.
   * @param element the element to look for
   * @return True if the element is in this HexGrid
   */
  boolean contains(T element) {
    return this.grid.stream().anyMatch((HexList<T> diagRow) -> diagRow.contains(element));
  }

  /**
   * Get the q coordinate of the first location of the given element in this grid.
   * @param element the element to find
   * @return The index
   * @throws NoSuchElementException if the element is not in this grid.
   */
  int qIndexOf(T element) {
    for (HexList<T> diagRow : this.grid) {
      if (diagRow.contains(element)) {
        return diagRow.indexOf(element);
      }
    }

    throw new NoSuchElementException();
  }

  /**
   * Get the r coordinate of the first location of the given element in this grid.
   * @param element the element to find
   * @return The index
   * @throws NoSuchElementException if the element is not in this grid.
   */
  int rIndexOf(T element) {
    for (HexList<T> diagRow : this.grid) {
      if (diagRow.contains(element)) {
        return diagRow.indexOf(element);
      }
    }
    throw new NoSuchElementException();
  }
}
