package com.zoominfo.util.yieldreturn;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class GeneratorTest {

    static abstract class GeneratorTestBase<T> extends Generator<T> {

        // keep track of which items have been generated, for bookkeeping purposes.
        List<T> generatedItems = new ArrayList<T>();
    }

    static Iterable<Integer> range(int hi) {
        return range(0, hi);
    }

    static Iterable<Integer> range(int lo, int hi) {
        return range(lo, hi, 1);
    }

    static Iterable<Integer> range(final int lo, final int hi, final int step) {
        return new GeneratorTestBase<Integer>() {

            @Override
            protected void run() {
                for (int i = lo; i != hi; i += step) {
                    generatedItems.add(i);
                    yield(i);
                }
            }
        };
    }

    static Iterable<Integer> firstn(final int n) {
        return new GeneratorTestBase<Integer>() {

            @Override
            protected void run() {
                int num = 0;
                while (num < n) {
                    generatedItems.add(num);
                    yield(num);
                    num += 1;
                }
            }
        };
    }

    static Iterable<Character> reverse(final CharSequence string) {
        return new GeneratorTestBase<Character>() {

            @Override
            protected void run() {
                for (int i : range(string.length() - 1, -1, -1)) {
                    generatedItems.add(string.charAt(i));
                    yield(string.charAt(i));
                }
            }
        };
    }

    private static Iterable<Integer> Power(final int number, final int exponent) {
        return new GeneratorTestBase<Integer>() {

            @Override
            protected void run() {
                int counter = 0;


                int result = 1;


                while (counter++ < exponent) {
                    result = result * number;
                    generatedItems.add(result);
                    yield(result);
                }
            }
        };
    }

    private static <T> List<T> runTest(Iterable<T> iterable) {
        GeneratorTestBase<T> generator = (GeneratorTestBase<T>) iterable;

        List<T> list = new ArrayList<T>();

        for (T item : generator) {
            list.add(item);
        }

        // assert that no more - and no fewer - items were generated than were returned
        assertEquals(generator.generatedItems.size(), list.size());

        // assert that the lists have the same items in the same order
        for (int i = 0; i
                < list.size(); i++) {
            T item1 = list.get(i);
            T item2 = generator.generatedItems.get(i);

            assertEquals(item1, item2);
        }

        return list;
    }

    @Test
    public void testPower() {
        List<Integer> list = runTest(Power(2, 8));

        assertEquals(8, list.size());
        assertTrue(list.containsAll(Arrays.asList(2, 4, 8, 16, 32, 64, 128, 256)));
    }

    @Test
    public void testRange() {
        runTest(range(0, 1000));

        GeneratorTestBase<Integer> generator = (GeneratorTestBase<Integer>) range(0, 1000);

        for (int i : generator) {
            if (i == 499) {
                break; // stop short
            }
        }

        // assert that we generated only 500 items - 0 through 499, inclusive
        assertEquals(500, generator.generatedItems.size());
        assertEquals(0, (int) generator.generatedItems.get(0));
        assertEquals(499, (int) generator.generatedItems.get(499));
    }

    @Test
    public void testReverse() {
        StringBuilder testString = new StringBuilder("hello, world!");
        StringBuilder sb = new StringBuilder();

        for (Character c : reverse(testString)) {
            sb.append(c);
        }

        assertEquals(testString.reverse().toString(), sb.toString());
    }

    @Test
    public void testFirstN() {
        List<Integer> first_n = runTest(firstn(500));

        assertEquals(500, first_n.size());
        assertEquals(0, (int) first_n.get(0));
        assertEquals(499, (int) first_n.get(499));
    }
}
