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


import com.google.common.base.Preconditions;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "kbo_entity")
@NoArgsConstructor
@Getter
@Setter
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
public abstract class KboEntity implements Comparable<KboEntity> {

    @Id
    @Column(name = "EntityNumber")
    protected String id;

    @OneToMany(mappedBy = "entity")
    private Set<Denomination> denominations;

    public Optional<Denomination> getOptionalDenomination() {
        Preconditions.checkArgument(denominations.size() <= 1, "Expected at most one Denomination for %s", this);
        return denominations.stream().findFirst();
    }

    public Denomination getExactDenomination() {
        Preconditions.checkArgument(denominations.size() == 1, "Expected exactly one Denomination for %s", this);
        return denominations.stream().findFirst().orElseThrow();
    }

    @Override
    public final int compareTo(KboEntity o) {
        return this.id.compareTo(o.id);
    }

    public SortedSet<String> getNames() {
        return denominations
                .stream()
                .map(Denomination::getName)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
