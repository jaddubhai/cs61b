public class ThreeSum{
	static int[] array = {1, 2, 5}; 

	static boolean helper() {
	int val = array[0]; 

	if (val*3 == 0) {
		return true; 
		}

	for (int i = 0; i < array.length; i++){
		int x = array[i];
		for (int p = i; p<array.length; p++){
			int y = array[p];
			for (int q = p; q<array.length; q++){
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