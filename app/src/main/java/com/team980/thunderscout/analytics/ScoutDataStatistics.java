/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.team980.thunderscout.analytics;

import com.team980.thunderscout.schema.ScoutData;

import java.util.Date;
import java.util.List;

import java8.util.function.Function;
import java8.util.function.ToIntFunction;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Wrapper methods to get summary statistics from ScoutData fields
 * <p>
 * Using the ported power of Java 8 Streams we can make this super elegant
 * I could just iterate over the lambda result, but this is basically the same thing
 */
public class ScoutDataStatistics {

    public static double getAverage(List<ScoutData> dataList, ToIntFunction<ScoutData> fieldGetter) {
        return StreamSupport.stream(dataList).mapToInt(fieldGetter).average().getAsDouble();
    }

    public static int getMaximum(List<ScoutData> dataList, ToIntFunction<ScoutData> fieldGetter) {
        return StreamSupport.stream(dataList).mapToInt(fieldGetter).max().getAsInt();
    }

    public static int getMinimum(List<ScoutData> dataList, ToIntFunction<ScoutData> fieldGetter) {
        return StreamSupport.stream(dataList).mapToInt(fieldGetter).min().getAsInt();
    }

    public static double getPercentage(List<ScoutData> dataList, Function<ScoutData, Boolean> fieldGetter) {
        return (((double) StreamSupport.stream(dataList).filter(fieldGetter::apply).count()) / dataList.size()) * 100;
    }

    public static List<String> getStringList(List<ScoutData> dataList, Function<ScoutData, String> fieldGetter, Function<ScoutData, String> prefixer) {
        return StreamSupport.stream(dataList).sorted().filter(data -> fieldGetter.apply(data) != null && !fieldGetter.apply(data).isEmpty())
                .map(data -> prefixer.apply(data) + " " + fieldGetter.apply(data)).collect(Collectors.toList());
    }

    public static Date getLastUpdated(List<ScoutData> dataList) {
        return new Date(StreamSupport.stream(dataList).mapToLong(data -> data.getDate().getTime()).max().getAsLong());
    }

}
