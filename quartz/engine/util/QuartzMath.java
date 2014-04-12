package quartz.engine.util;


public class QuartzMath {
	/**
	 * NOTE: very demanding! do not use (often)
	 * Rotate an array.  EG <br>
	 * <u>1 | 2 | 3 </u><br>
	 * 4 | 5 | 6 <br> <br>
	 * 
	 * {@code rotateArray(2, array, 3, 2)} <br>
	 * 
	 * will make <br>
	 * <u>6 | 5 | 4 </u><br>
	 * 3 | 2 | 1 <br><br>
	 * 
	 * NOTE: squares are more reliable...
	 * 
	 * @param rotationPoints = a amount to rotate (1 through 4)
	 * @param array = the array to rotate (make sure to use {@code }
	 * @param width
	 * @param height
	 * @return
	 */
	public static int[] rotateArray(int rotationPoints, int[] array, int width, int height) {
		int[] tArray = array;
		
		for (int i = 0; i < rotationPoints; i++) {
			tArray = rotateArrayOnce(tArray, width, height);
		}
		return array;
	}
	
	/**
	 * Can turn "1 | 2" info "2 | 1"
	 * @param lr = left right? or up down (true = left right false = up down)
	 * @param array = the array to flip
	 * @param width = the width of it (only use for multi-line arrays)
	 * @param height = the height of it (esp. images -- null if one)
	 * @return the flipped array
	 */
	public static int[] flipArray(boolean lr, int[] array, int width, int height) {
		return array;
	}
	
	private static int[] rotateArrayOnce(int[] array, int width, int height) {
		int[] oldArray = array;
		// now to rotate...
				
		// [y][x]
		int[][] lines = new int[6000][8000];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {	
				lines[y][x] = oldArray[x + y * width];
			}
		}
		
		for (int y = 0; y < height; y++) {
			for (int x = width; x >= 0; x--) {
				array[x + y * width] = lines[x][y];
			}
		}
		
		return array;
	}
}
