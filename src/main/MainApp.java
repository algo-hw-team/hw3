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

    private final static String basePath = "c:/hw3/";
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

        int maxPriceCanPay = 0;
        int totalCoins = -1;
        ArrayList<Integer> minCoinsPaid = null;
        ArrayList<Integer> minCoinsReceived = null;

        for (int i = 0; i < numOfCoinTypes; i++) {
            maxPriceCanPay += (coinTypes.get(i) * numOfCoins.get(i));
        }

        for (int price = priceToPay; price <= maxPriceCanPay; price++) {
            // Dynamic Programming start
        }

        // no output
        if (totalCoins < 0) {
            return "-1";
        } else {
            return makeOutputString(totalCoins, minCoinsPaid, minCoinsReceived);
        }
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