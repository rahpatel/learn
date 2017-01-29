package test;

import java.util.Arrays;
import java.util.Scanner;

//Copying an array
public class ArraysArithmetic {
	int a[];
	
	
	
	private void leftRotation(int n, int d) {
		int temp[] = Arrays.copyOf(a,a.length);
		
		for(int r=0;r<n;r++){
			int index = (r+d)%n;
			a[r] = temp[index];
		}
		
		
	}
	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int k = in.nextInt();
		ArraysArithmetic aa = new ArraysArithmetic();
		aa.a = new int[n];
		for(int a_i=0;a_i<n;a_i++){
			aa.a[a_i] = in.nextInt();
		}
		System.out.println(Arrays.toString(aa.a));
		aa.leftRotation(n, k);
		System.out.println(Arrays.toString(aa.a));
	}

}
