package com.romankukin.matrixspliterator.test;

import com.romankukin.matrixspliterator.MatrixSpliterator;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MatrixSpliteratorTest {
  
  @Test
  public void test1dHorizontalEmpty() {
    int[][] array = new int[0][0];

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(0, spliterator.estimateSize());
    assertNull(left);
  }

  @Test
  public void test1dHorizontalOneElem() {
    int[][] array = new int[1][];
    array[0] = new int[]{1};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(1, spliterator.estimateSize());
    assertNull(left);
  }

  @Test
  public void test1dHorizontal2Elems() {
    int[][] array = new int[1][];
    array[0] = new int[]{1, 2};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(1, spliterator.estimateSize());
    assertEquals(1, left.estimateSize());
  }

  @Test
  public void test1dHorizontalEven() {
    int[][] array = new int[1][];
    array[0] = new int[]{1, 2, 3, 4};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(2, spliterator.estimateSize());
    assertEquals(2, left.estimateSize());
  }

  @Test
  public void test1dHorizontalOdd() {
    int[][] array = new int[1][];
    array[0] = new int[]{1, 2, 3, 4, 5};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(3, spliterator.estimateSize());
    assertEquals(2, left.estimateSize());
  }

  // 1
  // 2
  // 3
  // 4
  // 5
  @Test
  public void test1dVerticalOdd() {
    int[][] array = new int[5][];
    for (int i = 0; i < 5; i++) {
      array[i] = new int[]{i + 1};
    }

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(3, spliterator.estimateSize());
    assertEquals(2, left.estimateSize());
  }

  // 1
  // 2
  // 3
  // 4
  @Test
  public void test1dVerticalEven() {
    int[][] array = new int[4][];
    for (int i = 0; i < 4; i++) {
      array[i] = new int[]{i + 1};
    }

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(2, spliterator.estimateSize());
    assertEquals(2, left.estimateSize());
  }

  // 1 2 3 4 5
  // 6 7 8 9 10
  @Test
  public void test2dHorizontalOdd() {
    int[][] array = new int[2][5];
    array[0] = new int[]{1, 2, 3, 4, 5};
    array[1] = new int[]{6, 7, 8, 9, 10};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(6, spliterator.estimateSize());
    assertEquals(4, left.estimateSize());
  }

  // 1 2 3 4
  // 5 6 7 8
  @Test
  public void test2dEven() {
    int[][] array = new int[2][4];
    array[0] = new int[]{1, 2, 3, 4};
    array[1] = new int[]{5, 6, 7, 8};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(4, spliterator.estimateSize());
    assertEquals(4, left.estimateSize());
  }

  // 1 2
  // 3 4
  // 5 6
  // 7 8
  @Test
  public void test2dVerticalEven() {
    int[][] array = new int[4][2];
    array[0] = new int[]{1, 2};
    array[1] = new int[]{3, 4};
    array[2] = new int[]{5, 6};
    array[3] = new int[]{7, 8};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(4, spliterator.estimateSize());
    assertEquals(4, left.estimateSize());
  }

  // 1 2
  // 3 4
  // 5 6
  // 7 8
  // 9 10
  @Test
  public void test2dVerticalOdd() {
    int[][] array = new int[5][2];
    array[0] = new int[]{1, 2};
    array[1] = new int[]{3, 4};
    array[2] = new int[]{5, 6};
    array[3] = new int[]{7, 8};
    array[3] = new int[]{9, 10};

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    MatrixSpliterator left = spliterator.trySplit();

    assertEquals(6, spliterator.estimateSize());
    assertEquals(4, left.estimateSize());
  }

  @Test
  public void testSplitChaining() {
    int[][] array = new int[128][128];

    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[i].length; j++) {
        array[i][j] = ThreadLocalRandom.current().nextInt(21);
      }
    }

    int expectedSize = 128 * 128;

    MatrixSpliterator spliterator = MatrixSpliterator.of(array);
    assertEquals(expectedSize, spliterator.estimateSize());

    for (int i = 0; i < 8; i++) {
      expectedSize /= 2;
      MatrixSpliterator left = spliterator.trySplit();
      assertEquals(expectedSize, left.estimateSize());
    }

    for (int i = 0; i < 16; i++) {
      expectedSize--;
      spliterator.tryAdvance((IntConsumer) j -> {
      });
      assertEquals(expectedSize, spliterator.estimateSize());
    }
  }

}
