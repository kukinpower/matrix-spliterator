package com.romankukin.matrixspliterator;


import java.util.Spliterator;
import java.util.function.IntConsumer;

public interface MatrixSpliterator extends Spliterator.OfInt {

  @Override
  MatrixSpliterator trySplit();

  @Override
  boolean tryAdvance(IntConsumer action);

  @Override
  long estimateSize();

  static MatrixSpliterator of(int[][] array) {
    return new MatrixSpliterator() {

      private int[][] arr = array.clone();
      private int row = 0;
      private int col = 0;

      @Override
      public MatrixSpliterator trySplit() {
        if (arr == null || arr.length == 0) {
          return null;
        }
        // horizontal 1 dimension
        if (arr.length == 1) {
          // 1
          if (arr[0].length == 1) {
            return null;
          }
          // 1 2 3 4
          int border = arr[0].length / 2;

          int[][] ret = new int[1][border];

          for (int i = 0; i < border; i++) {
            ret[0][i] = arr[0][i];
          }

          int[][] newArray = new int[1][arr[0].length - border];

          for (int i = border; i < newArray[0].length + border; i++) {
            newArray[0][i - border] = arr[0][i];
          }

          arr = newArray;

          return MatrixSpliterator.of(ret);
        }

        // vertical 1 dimension
        if (arr[0].length == 1) {
          int border = arr.length / 2;

          int[][] ret = new int[border][1];

          for (int i = 0; i < border; i++) {
            ret[i][0] = arr[i][0];
          }

          int[][] newArray = new int[arr.length - border][1];

          for (int i = border; i < newArray.length + border; i++) {
            newArray[i - border][0] = arr[i][0];
          }

          arr = newArray;

          return MatrixSpliterator.of(ret);
        }

        // horizontal 2 dimension
        if (arr.length <= arr[0].length) {
          // 1 2 3 4 5
          // 6 7 8 9 10
          int rowBorder = arr[0].length / 2;

          int[][] ret = new int[arr.length][rowBorder];

          for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < rowBorder; j++) {
              ret[i][j] = arr[i][j];
            }
          }

          int[][] newArray = new int[arr.length][arr[0].length - rowBorder];

          for (int i = 0; i < arr.length; i++) {
            for (int j = rowBorder; j < newArray[i].length + rowBorder; j++) {
              newArray[i][j - rowBorder] = arr[i][j];
            }
          }

          arr = newArray;

          return MatrixSpliterator.of(ret);
        }

        // vertical 2 dimension
        // 1 2
        // 3 4
        // 5 6
        // 7 8
        // 9 10
        int colBorder = arr.length / 2;
        int rowLen = arr[0].length;

        int[][] ret = new int[colBorder][rowLen];

        for (int i = 0; i < colBorder; i++) {
          for (int j = 0; j < rowLen; j++) {
            ret[i][j] = arr[i][j];
          }
        }

        int[][] newArray = new int[arr.length - colBorder][rowLen];

        for (int i = colBorder; i < arr.length; i++) {
          for (int j = 0; j < rowLen; j++) {
            newArray[i - colBorder][j] = arr[i][j];
          }
        }

        arr = newArray;

        return MatrixSpliterator.of(ret);

      }

      @Override
      public boolean tryAdvance(IntConsumer action) {
        if (arr == null || arr.length == 0 || arr.length == row + 1 && arr[0].length == col) {
          return false;
        }

        action.accept(arr[row][col]);

        if (col + 1 == arr[0].length) {
          if (row + 1 == arr.length) {
            return false;
          }
          row++;
          col = 0;
        } else {
          col++;
        }

        return true;
      }

      @Override
      public long estimateSize() {
        if (arr == null) {
          return 0;
        }

        if (row != 0 || col != 0) {
          long size = (long) (arr.length - row - 1) * arr[0].length;
          size += arr[0].length - col;
          return size;
        }

        if (arr.length == 1) {
          return arr[0].length;
        }
        if (arr.length == 0) {
          return 0;
        }
        return (long) arr.length * arr[0].length;
      }

      @Override
      public int characteristics() {
        return IMMUTABLE | ORDERED | SIZED | SUBSIZED | NONNULL;
      }
    };

  }

}
