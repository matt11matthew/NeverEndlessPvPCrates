package net.neverendlesspvp.cratekeys.utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Matthew E on 12/12/2017.
 */
public class RandomUtils {
    //Very gross method
    //TODO recode my 2 year old method from stack overflow
    public static <E, Integer> List<E> chooseRandomElements(Map<E, Integer> list, int amount) {
        // Avoid a deadlock
        if (amount >= list.keySet().size()) {
            return new ArrayList<>(list.keySet());
        }

        List<E> selected = new ArrayList<>();
        Random random = new Random();
        int listSize = list.keySet().size();

        // Get a random item until we got the requested amount
        while (selected.size() < amount) {
            int randomIndex = random.nextInt(listSize);
            E e = new ArrayList<>(list.keySet()).get(randomIndex);
            double max = 0;
            int c = java.lang.Integer.parseInt(String.valueOf(list.get(e)));
            if (c > max) {
                max = c;
            }
            DecimalFormat df = new DecimalFormat("####.##");
            String randomChanceNum = df.format(0 + max * random.nextDouble());
            double chanceNum = Double.parseDouble(randomChanceNum);
            ArrayList<E> winablePrizes = new ArrayList<>();
            for (E reward : list.keySet()) {
                double chance = c;
                if (chanceNum <= chance) {
                    winablePrizes.add(reward);
                }
            }
            E wonPrize;
            if (winablePrizes.size() > 1) {
                int prizeToPick = random.nextInt(winablePrizes.size());
                wonPrize = winablePrizes.get(prizeToPick);
            } else {
                wonPrize = winablePrizes.get(0);
            }
            E element = wonPrize;
            if (!selected.contains(element)) {
                selected.add(element);
            }
        }
        return selected;
    }
}
