public class ThreeSum_distinct{
	static int[] array = {1, 2, 5}; 

	static boolean helper() {

	for (int i = 0; i < array.length; i++){
		int x = array[i];
		for (int p = i+1; p<array.length; p++){
			int y = array[p];
			for (int q = p+1; q<array.length; q++){
				int z = array[q];
				if (x+y+z == 0){
					return true; 
				}
			}
		}
	}

	return false;
	
	}

	public static void main (String[] args){
		System.out.print(helper()); 
	}
}