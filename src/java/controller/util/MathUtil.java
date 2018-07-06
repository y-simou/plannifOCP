/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Younes
 */
public class MathUtil {

    public static BigDecimal calculerMax(List<BigDecimal> resultas) {
        BigDecimal max = resultas.get(0);
        for (int i = 0; i < resultas.size(); i++) {
            if (max.compareTo(resultas.get(i)) == -1) {
                max = resultas.get(i);
            }

        }
        System.out.println("ha max mn util ::" + max);
        return max;
    }

}
