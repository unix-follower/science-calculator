package org.example.sciencecalc.math;

import java.math.BigDecimal;

public final class NumberUtils {
    private NumberUtils() {
    }

    public static double[] splitWholeAndFractionViaModulo(double value) {
        final double fractionalPart = value % 1;
        final double wholePart = value - fractionalPart;
        return new double[]{wholePart, fractionalPart};
    }

    public static long[] splitToWholeAndFraction(double value) {
        final var bd = new BigDecimal(Double.toString(value));
        final long fractionalPart = bd.remainder(BigDecimal.ONE)
            .movePointRight(bd.scale())
            .abs()
            .longValue();
        final long wholePart = (long) (value - fractionalPart);
        return new long[]{wholePart, fractionalPart};
    }

    public static long[] splitWholeAndFractionViaStr(double value) {
        final String[] text = Double.toString(value).split("\\.");
        final long wholePart = Long.parseLong(text[Constants.ARR_1ST_INDEX]);
        final long fractionalPart = Long.parseLong(text[Constants.ARR_2ND_INDEX]);
        return new long[]{wholePart, fractionalPart};
    }

    public static void checkGreater0(double value) {
        final int inclusiveBound = 0;
        if (value <= inclusiveBound) {
            checkGreater(value, inclusiveBound);
        }
    }

    public static void checkGreater(double value, double inclusiveBound) {
        if (value <= inclusiveBound) {
            throw new IllegalArgumentException("This value must be > " + inclusiveBound);
        }
    }

    public static void checkLessOrEq(double value, double inclusiveBound) {
        if (value > inclusiveBound) {
            throw new IllegalArgumentException("This value must be <= " + inclusiveBound);
        }
    }
}
