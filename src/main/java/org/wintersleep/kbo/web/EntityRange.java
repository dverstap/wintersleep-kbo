package org.wintersleep.kbo.web;

/*-
 * #%L
 * org.wintersleep.kbo:wintersleep-kbo
 * %%
 * Copyright (C) 2023 - 2024 Davy Verstappen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

// The React Admin DataProvider API uses pages: pagination: { page: number, perPage: number }
// But the ra-data-simple-rest DataProvider implementation turns this into a range: [0, 24]

import lombok.NonNull;
import org.json.JSONArray;

// inclusive range
public record EntityRange(
        long from,
        long to
) {


    public static @NonNull EntityRange parse(String str) {
        if (str == null || str.isBlank()) {
            return new EntityRange(0, 9);
        }
        JSONArray array = new JSONArray(str);
        if (array.length() != 2) {
            throw new IllegalArgumentException("Invalid entity range JSON: " + str + " (Expected exactly two numbers)");
        }
        long from = array.getLong(0);
        long to = array.getLong(1);
        return new EntityRange(from, to);
    }

    public long offset() {
        return from;
    }

    public long limit() {
        return to - from + 1;
    }
}
