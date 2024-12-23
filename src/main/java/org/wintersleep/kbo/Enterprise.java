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

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "enterprise")
@PrimaryKeyJoinColumn(name = "EnterpriseNumber")
@NoArgsConstructor
@Getter
@Setter
public class Enterprise extends KboEntity {

    @Column(name = "Status", nullable = false)
    String status;

    @Column(name = "JuridicalSituation", nullable = false)
    String juridicalSituation;

    @Column(name = "TypeOfEnterprise", nullable = false)
    EnterpriseType type;

    @Column(name = "JuridicalForm")
    String juridicalForm;

    @Column(name = "JuridicalFormCAC")
    String juridicalFormCAC;

    @Column(name = "StartDate", nullable = false)
    LocalDate startDate;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL)
    private Set<Establishment> establishments;

    @Override
    public String toString() {
        return new StringJoiner(", ", Enterprise.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("status='" + status + "'")
                .add("juridicalSituation='" + juridicalSituation + "'")
                .add("type=" + type)
                .add("juridicalForm=" + juridicalForm)
                .add("juridicalFormCAC='" + juridicalFormCAC + "'")
                .add("startDate=" + startDate)
                .toString();
    }
}
