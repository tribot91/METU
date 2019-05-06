import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.locks.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main method that starts the simulation
 */
public class Simulation {
    public static void main(String[] args) {
        ArrayList<Smelter> sArr = new ArrayList<Smelter>();
        ArrayList<Constructor> cArr = new ArrayList<Constructor>();
        ArrayList<Transporter> tArr = new ArrayList<Transporter>();
        int NS, IS, CS, TS, RS;
        int NC, IC, CC, TC;
        int NT, IT, ST, CT;
        Smelter tempSmelter;
        Constructor tempConstructor;
        Transporter tempTransporter;
        Lock stLock = new ReentrantLock(); // Smelter - Transporter
        Lock ctLock = new ReentrantLock(); // Transporter - Constructor
        Lock selflock1 = new ReentrantLock();
        Lock selflock2 = new ReentrantLock();

        ExecutorService taskList = Executors.newFixedThreadPool(100);
        HW2Logger.InitWriteOutput();

        // ----------------------------------------------------------
        Scanner input = new Scanner(System.in);
        ArrayList<ArrayList<Integer>> tempArr1 = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> tempArr2 = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> tempArr3 = new ArrayList<ArrayList<Integer>>();

        NS = Integer.parseInt(input.nextLine()); // Number of smelters
        for (int i = 0; i < NS; i++) {
            String[] tempString = input.nextLine().split(" ");
            tempArr1.add(new ArrayList<Integer>());
            for (int j = 0; j < 4; j++) {
                tempArr1.get(i).add(Integer.valueOf(tempString[j]));
            }
        }
        NC = Integer.parseInt(input.nextLine()); // Number of constructors
        for (int i = 0; i < NC; i++) {
            String[] tempString = input.nextLine().split(" ");
            tempArr2.add(new ArrayList<Integer>());
            for (int j = 0; j < 3; j++)
                tempArr2.get(i).add(Integer.valueOf(tempString[j]));
        }
        NT = Integer.parseInt(input.nextLine()); // Number of transporters
        for (int i = 0; i < NT; i++) {
            String[] tempString = input.nextLine().split(" ");
            tempArr3.add(new ArrayList<Integer>());
            for (int j = 0; j < 3; j++)
                tempArr3.get(i).add(Integer.valueOf(tempString[j]));
        }

        if (tempArr1.size() > 0)
            for (int i = 1; i <= NS; i++) {
                IS = tempArr1.get(i - 1).get(0); // Production and wait interval.
                CS = tempArr1.get(i - 1).get(1); // Storage capacity
                TS = tempArr1.get(i - 1).get(2); // IngotType. Iron = 0, Copper = 1
                RS = tempArr1.get(i - 1).get(3); // Total amount of ingot that can be produced from the smelter
                tempSmelter = new Smelter(i, IS, CS, TS, RS, tArr, stLock);
                sArr.add(tempSmelter);
                taskList.execute(tempSmelter);
            }

        if (tempArr2.size() > 0)
            for (int i = 1; i <= NC; i++) {
                IC = tempArr2.get(i - 1).get(0); // Production interval.
                CC = tempArr2.get(i - 1).get(1); // Storage capacity
                TC = tempArr2.get(i - 1).get(2); // IngotType of the smelter. Iron = 0, Copper = 1
                tempConstructor = new Constructor(i, IC, CC, TC, tArr, ctLock);
                cArr.add(tempConstructor);
                taskList.execute(tempConstructor);
            }

        if (tempArr3.size() > 0)
            for (int i = 1; i <= NT; i++) {
                IT = tempArr3.get(i - 1).get(0); // the travel and load/unload time in milliseconds.
                ST = tempArr3.get(i - 1).get(1); // target smelter ID that this transporter should load ingots from
                CT = tempArr3.get(i - 1).get(2); // target constructor ID that this transporter should unload ingots to.
                tempTransporter = new Transporter(i, IT, ST, CT, sArr, cArr, stLock, ctLock, selflock1, selflock2);
                tArr.add(tempTransporter);
                taskList.execute(tempTransporter);
            }

        taskList.shutdown();
    }
}