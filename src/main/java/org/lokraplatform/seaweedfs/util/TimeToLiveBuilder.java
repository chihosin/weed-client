/*
 * Copyright (c) 2016 Lokra Studio
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

package org.lokraplatform.seaweedfs.util;

/**
 * @author Chiho Sin
 */
public class TimeToLiveBuilder {

    public static String buildMinutes(int count) {
        return String.valueOf(count) + "m";
    }

    public static String buildHours(int count) {
        return String.valueOf(count) + "h";
    }

    public static String buildDays(int count) {
        return String.valueOf(count) + "d";
    }

    public static String buildWeeks(int count) {
        return String.valueOf(count) + "w";
    }

    public static String buildMonths(int count) {
        return String.valueOf(count) + "M";
    }

    public static String buildYears(int count) {
        return String.valueOf(count) + "y";
    }

}
