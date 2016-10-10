package main;

import java.util.ArrayList;
import java.util.HashMap;

public class CoinCalculator {
	ArrayList<Integer> coinTypes;
	ArrayList<Integer> numOfCoins;
	HashMap<Integer,ArrayList<Integer>> DPMap;
	
	public CoinCalculator(ArrayList<Integer> coinTypes, ArrayList<Integer> numOfCoins) {
		this.coinTypes = coinTypes;
		this.numOfCoins = numOfCoins;
		DPMap = new HashMap<>();
		ArrayList<Integer> defaultCoins = new ArrayList<>();
		for (int i = 0; i < coinTypes.size(); i++) {
			defaultCoins.add(0);
		}
		DPMap.put(0, defaultCoins);
	}
	
	public ArrayList<Integer> find (int target) {
		if (DPMap.containsKey(target)) {
			return DPMap.get(target);
		}
		
		ArrayList<Integer> result = null;
		ArrayList<ArrayList<Integer>> list = new ArrayList<>();
		for (int i = 0; i < coinTypes.size(); i++) {
			boolean isLegal = true;
			if (target - coinTypes.get(i) >= 0) {
				ArrayList<Integer> currentList = new ArrayList<>();
				ArrayList<Integer> beforeList = this.find(target - coinTypes.get(i));
				if (beforeList == null) {
					isLegal = false;
					continue;
				}
				for (int j = 0; j < coinTypes.size(); j++) {
					if (i == j) {
						if (beforeList.get(j) + 1 > numOfCoins.get(j)) {
							isLegal = false;
							break;
						}
						currentList.add(beforeList.get(j) + 1);
					} else {
						currentList.add(beforeList.get(j));
					}
				}
				if (isLegal) {
					list.add(currentList);
				}
			}
		}
		result = this.selectMin(list);
		DPMap.put(target, result);
		return result;
	}
	
	private ArrayList<Integer> selectMin (ArrayList<ArrayList<Integer>> list) {
		ArrayList<Integer> result = null;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			int sum = 0;
			for (int j = 0; j < list.get(i).size(); j++) {
				sum += list.get(i).get(j);
			}
			if (min > sum) {
				min = sum;
				result = list.get(i);
			}
		}
		return result;
	}
}
