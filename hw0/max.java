public class max{

	static int[] array = {1,2,3,4}; 

	static int max(){
		int max_val = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max_val){
				max_val = i; 
			}
		}
	return max_val; 
	}
	public static void main(String[] args) {
		System.out.print(max()); 
	}
}