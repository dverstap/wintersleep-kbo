package org.wintersleep.kbo;

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

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final KboService kboService;

    private final EnterpriseRepository enterpriseRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> names = Files.readAllLines(Path.of(args[0]));
        Set<String> noMatches = new TreeSet<>();
        MultiValueMap<String, Enterprise> duplicateMatches = new LinkedMultiValueMap<>();
        Map<String, Enterprise> exactMatches = new TreeMap<>();
        for (String name : names) {
            List<Enterprise> enterprises = enterpriseRepository.findByName(name);
            if (enterprises.isEmpty()) {
                noMatches.add(name);
            } else if (enterprises.size() == 1) {
                exactMatches.put(name, enterprises.stream().findFirst().orElseThrow());
            } else {
                duplicateMatches.put(name, enterprises);
            }
        }
        for (Map.Entry<String, Enterprise> entry : exactMatches.entrySet()) {
            Enterprise enterprise = entry.getValue();
            System.out.println(entry.getKey() + ": " + enterprise + ": " + enterprise.getNames());
        }
        System.out.println("No matches:        " + noMatches.size());
        System.out.println("Exact matches:     " + exactMatches.size());
        System.out.println("Duplicate matches: " + duplicateMatches.size());
    }

}
