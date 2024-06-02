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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class KboService {

    private final DenominationRepository denominationRepository;
    private final EnterpriseRepository enterpriseRepository;

    private static final List<Language> PREFERRED_LANGUAGES = List.of(
            Language.DUTCH,
            Language.FRENCH,
            Language.ENGLISH,
            Language.GERMAN,
            Language.UNKNOWN
    );

    private static final List<DenominationType> PREFERRED_DENOMINATION_TYPES = List.of(
            DenominationType.NAME,
            DenominationType.COMMERCIAL_NAME,
            DenominationType.ABBREVIATION
    );

/*
    public Optional<Enterprise> find(String name) {
        List<Denomination> denominations = denominationRepository.findAllEnterpriseDenominationsByName(name);
        if (denominations.isEmpty()) {
            return Optional.empty();
        } else if (denominations.size() == 1) {
            return denominations.get(0).getEnterprise();
        }
        Set<Enterprise> enterprises = denominations
                .stream()
                .flatMap(d -> d.getEnterprise().stream())
                .collect(Collectors.toSet());
        if (enterprises.size() > 1) {
            // We only want exact matches
            return Optional.empty();
        }
        return Optional.of(enterprises.iterator().next());
    }
*/

    public Optional<Enterprise> find(String name) {
        List<Enterprise> enterprises = enterpriseRepository.findByName(name);
        if (enterprises.isEmpty()) {
            return Optional.empty();
        } else if (enterprises.size() == 1) {
            return enterprises.stream().findFirst();
        }
        log.debug("Found multiple Enterprise named {}: {}", name, enterprises);
        return Optional.empty();
    }
}
