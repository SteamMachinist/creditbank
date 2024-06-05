package steammachinist.util;

import java.util.ArrayList;
import java.util.List;

public class CalculatorUtil {
    public static List<boolean[]> generateBooleanCombinations() {
        List<boolean[]> combinations = new ArrayList<>();

        boolean[] values = {false, true};
        for (boolean first : values) {
            for (boolean second : values) {
                combinations.add(new boolean[]{first, second});
            }
        }
        return combinations;
    }
}
