package ch.epfl.alpano;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.BitSet;
import java.util.Scanner;

public class Test {

	
	
	public static void main(String[] args) {
		//BitSet bits = new BitSet(257);
		//System.out.println(bits.size() + " " + bits.get(3));
Scanner scanner = new Scanner(System.in);
		Integer x = scanner.nextInt();
		
		//int roundedX = value.setScale(0, RoundingMode.HALF_UP).intValueExact();
		//System.out.println(value.movePointRight(4).setScale(0, RoundingMode.HALF_UP).intValueExact());
		System.out.println(new BigDecimal(x).movePointLeft(4).toPlainString());
		
	}

}
