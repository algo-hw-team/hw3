package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainApp {

    private final static String basePath = "/Users/Join/dev/homeworks-0302/algo/hw3/";
    private final static String inputPath = basePath + "input.txt";
    private final static String outputPath = basePath + "2013147550.txt";

    private static StringBuilder builder = new StringBuilder();

    public static void main(String[] args) {

        try {
            //모든 인풋 텍스트를 라인단위로 리스트에 저장한다.
            BufferedReader br = new BufferedReader(new FileReader(inputPath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
            String sCurrentLine;
            ArrayList<String> inputlist = new ArrayList<>();
            while ((sCurrentLine = br.readLine()) != null) {
                inputlist.add(sCurrentLine);
            }

            br.close();

            // algorithm
            int indexOfInput = 0;
            int numOfTest = Integer.parseInt(inputlist.get(indexOfInput++));

            for (int indexOfTest = 0; indexOfTest < numOfTest; indexOfTest++) {
                String[] tokens = inputlist.get(indexOfInput++).split(" ");

                int numOfCoinTypes = Integer.parseInt(tokens[0]);
                int priceToPay = Integer.parseInt(tokens[1]);

                String[] coinTypeStrings = inputlist.get(indexOfInput++).split(" ");
                String[] numOfCoinStrings = inputlist.get(indexOfInput++).split(" ");
                ArrayList<Integer> coinTypes = new ArrayList<>();
                ArrayList<Integer> numOfCoins = new ArrayList<>();

                for (int i = 0; i < numOfCoinTypes; i++) {
                    coinTypes.add(Integer.parseInt(coinTypeStrings[i]));
                }
                for (int i = 0; i < numOfCoinTypes; i++) {
                    numOfCoins.add(Integer.parseInt(numOfCoinStrings[i]));
                }

                builder
                        .append(runTestCase(priceToPay, numOfCoinTypes, coinTypes, numOfCoins))
                        .append(System.getProperty("line.separator"));
            }

            String output = builder
                    .toString()
                    .trim();

            bw.write(output);
            bw.flush();
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String runTestCase(int priceToPay,
                                      int numOfCoinTypes,
                       ArrayList<Integer> coinTypes,
                       ArrayList<Integer> numOfCoins) {

        int minTotalNumOfCoins = -1;
        int minNumOfPaidCoins = -1;
        ArrayList<Integer> minCoinsPaid = null;
        ArrayList<Integer> minCoinsReceived = null;
        CoinCalculator calculator = new CoinCalculator(coinTypes, numOfCoins);
        ArrayList<Integer> payablePrices = getPayablePrices(priceToPay,
                numOfCoinTypes,
                coinTypes,
                numOfCoins);

        for (int price : payablePrices) {
            // Dynamic Programming start

            // no combination to pay `price`
            ArrayList<Integer> paidCombination = calculator.findPay(price);
            if (paidCombination == null) {
                continue;
            }

            // no combination to receive changes (`price - priceToPay`)
            ArrayList<Integer> receivedCombination = calculator.findChanges(price - priceToPay);
            if (receivedCombination == null) {
                continue;
            }

            int numOfPaidCoins = getNumOfCoinsUsed(paidCombination);
            int numOfReceivedCoins = getNumOfCoinsUsed(receivedCombination);
            int totalNumOfCoins = numOfPaidCoins + numOfReceivedCoins;

            boolean isFirstCase = minTotalNumOfCoins < 0;
            boolean hasUsedLesserCoins = totalNumOfCoins < minTotalNumOfCoins;
            boolean hasUsedSameCoinsAndPaidLess = (minTotalNumOfCoins == totalNumOfCoins) &&
                    (numOfPaidCoins < minNumOfPaidCoins);

            // if true, current iteration is case that uses minimum coins
            if (isFirstCase || hasUsedLesserCoins || hasUsedSameCoinsAndPaidLess) {
                minTotalNumOfCoins = totalNumOfCoins;
                minNumOfPaidCoins = numOfPaidCoins;
                minCoinsPaid = paidCombination;
                minCoinsReceived = receivedCombination;
            }
        }

        if (minTotalNumOfCoins < 0) {
            // no output
            return "-1";
        } else {
            return makeOutputString(minTotalNumOfCoins, minCoinsPaid, minCoinsReceived);
        }
    }

    private static ArrayList<Integer> getPayablePrices(int priceToPay,
                                                       int numOfCoinTypes,
                                                       ArrayList<Integer> coinTypes,
                                                       ArrayList<Integer> numOfCoins) {
        ArrayList<Integer> payablePrices = new ArrayList<>();
        int lastIndex = numOfCoinTypes - 1;
        int availableCoinCombinations = 1;
        int maxPriceCanPay = 1;

        for (int numOfCoin : numOfCoins) {
            availableCoinCombinations *= numOfCoin;
        }
        for (int i = 0; i < numOfCoinTypes; i++) {
            maxPriceCanPay += coinTypes.get(i) * numOfCoins.get(i);
        }

        // 최대 금액 - 타겟 금액이 가능한 동전 조합의 경우의수 보다 작을 경우
        if ((maxPriceCanPay - priceToPay) <= availableCoinCombinations) {
            for (int i = priceToPay; i <= maxPriceCanPay; i++) {
                payablePrices.add(i);
            }

            return payablePrices;
        }

        // 그 외에는 가능한 경우의 수 중에서 priceToPay보다 크게 지불하는 경우를 전부 구함
        ArrayList<Integer> current = new ArrayList<>(numOfCoins);

        while (current.get(lastIndex) >= 0) {
            int price = getPrice(numOfCoinTypes, coinTypes, current);

            // adds payable price only when it is bigger than priceToPay
            if (price >= priceToPay) {
                payablePrices.add(price);
            }

            // calculate next available combination
            current.set(0, current.get(0) - 1);
            for (int i = 0; i < numOfCoinTypes - 1; i++) {
                if (current.get(i) < 0) {
                    current.set(i, numOfCoins.get(i));
                    current.set(i + 1, current.get(i + 1) - 1);
                }
            }
        }

        return payablePrices;
    }

    private static int getPrice(int numOfCoinTypes,
                                ArrayList<Integer> coinTypes,
                                ArrayList<Integer> combination) {
        int result = 0;

        for (int i = 0; i < numOfCoinTypes; i++) {
            result += (coinTypes.get(i) * combination.get(i));
        }

        return result;
    }

    private static int getNumOfCoinsUsed(ArrayList<Integer> combinations) {
        int totalCoins = 0;

        for (int coinUsed : combinations) {
            totalCoins += coinUsed;
        }

        return totalCoins;
    }

    private static String makeOutputString(int totalCoins,
                                           ArrayList<Integer> paidCoins,
                                           ArrayList<Integer> receivedCoins) {
        StringBuilder builder = new StringBuilder();

        builder.append(totalCoins);

        for (Integer element : paidCoins) {
            builder.append(" ").append(element);
        }
        for (Integer element : receivedCoins) {
            builder.append(" ").append(element);
        }

        return builder.toString();
    }

}