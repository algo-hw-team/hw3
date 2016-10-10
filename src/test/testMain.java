package test;

import java.util.ArrayList;

import main.CoinCalculator;

public class testMain {

	public static void main(String[] args) {
		ArrayList<Integer> coinTypes = new ArrayList<>();
		coinTypes.add(2);
		coinTypes.add(5);
		coinTypes.add(7);
		coinTypes.add(20);
		coinTypes.add(30);
		ArrayList<Integer> numOfCoins = new ArrayList<>();
		numOfCoins.add(5);
		numOfCoins.add(5);
		numOfCoins.add(0);
		numOfCoins.add(5);
		numOfCoins.add(5);
		
		CoinCalculator CC = new CoinCalculator(coinTypes, numOfCoins);

		ArrayList<Integer> result = CC.findPay(17);
		if (result == null) {
			System.out.println("-1");
		} else {
			for (int i = 0; i < result.size(); i++) {
				System.out.print(coinTypes.get(i));
				System.out.println(": " + result.get(i));
			}
		}
		result = CC.findChanges(17);
		if (result == null) {
			System.out.println("-1");
		} else {
			for (int i = 0; i < result.size(); i++) {
				System.out.print(coinTypes.get(i));
				System.out.println(": " + result.get(i));
			}
		}

	}

}
