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

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wintersleep.kbo.Enterprise;
import org.wintersleep.kbo.EnterpriseRepository;
import org.wintersleep.kbo.QEnterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EnterpriseController {

    private final EnterpriseRepository enterpriseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // TODO Look into: https://persistence.blazebit.com/

/*
    @GetMapping("/enterprises")
    public ResponseEntity<List<EnterpriseListDto>> list(
            // @RequestParam String filter, @RequestParam String sort, @RequestParam String range
            @RequestParam(required = true, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr,
            @RequestParam(required = false, name = "sort") String sortStr) {
        log.info("Enterprise list filter: {}", filterStr);
        log.info("Enterprise list range: {}", rangeStr);
        log.info("Enterprise list sort: {}", sortStr);
        QueryParamWrapper wrapper = QueryParamExtractor.extract(filterStr, rangeStr, sortStr);
        int pageSize = 10;
        var page = enterpriseRepository.findAll(PageRequest.of(0, pageSize, sort(wrapper, "id")));
        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Range")
                .header("Content-Range", format("enterprises 0-%s/100", pageSize - 1))
                .body(page.getContent().stream()
                        .map(e -> EnterpriseListDto
                                .builder()
                                .id(e.getId())
                                .type(e.getType())
                                .status(e.getStatus())
                                .startDate(e.getStartDate())
                                .build())
                        .toList());
    }
*/

    @GetMapping("/enterprises")
    public ResponseEntity<List<EnterpriseListDto>> list(
            // @RequestParam String filter, @RequestParam String sort, @RequestParam String range
            @RequestParam(required = true, name = "filter") String filterStr,
            @RequestParam(required = false, name = "range") String rangeStr,
            @RequestParam(required = false, name = "sort") String sortStr) {
        log.info("Enterprise list filter: {}", filterStr);
        log.info("Enterprise list range: {}", rangeStr);
        log.info("Enterprise list sort: {}", sortStr);
        //QueryParamWrapper wrapper = QueryParamExtractor.extract(filterStr, rangeStr, sortStr);
        EntityRange range = EntityRange.parse(rangeStr);

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QEnterprise enterprise = QEnterprise.enterprise;
        var results = queryFactory.selectFrom(enterprise)
                .offset(range.offset())
                .limit(range.limit())
                .orderBy(orderBy(enterprise, sortStr))
                .fetchResults();
        return ResponseEntity.ok()
                .header("Access-Control-Expose-Headers", "Content-Range")
                .header("Content-Range", contentRange(results))
                .body(results.getResults().stream()
                        .map(e -> EnterpriseListDto
                                .builder()
                                .id(e.getId())
                                .type(e.getType())
                                .status(e.getStatus())
                                .startDate(e.getStartDate())
                                .build())
                        .toList());
    }

    private String contentRange(QueryResults<Enterprise> results) {
        return format("enterprises" + " %s-%s/%s",
                results.getOffset(), results.getOffset() + results.getResults().size(), results.getTotal());
    }

    @GetMapping("/enterprises/{id}")
    public Optional<EnterpriseGetDto> get(@PathVariable String id) {
        return enterpriseRepository.findById(id)
                .map(e -> EnterpriseGetDto
                        .builder()
                        .id(e.getId())
                        .type(e.getType())
                        .status(e.getStatus())
                        .startDate(e.getStartDate())
                        .build());
    }

    // TODO
//    @PostMapping("/enterprises/")
//    public EnterpriseGetDto post(@RequestBody EnterprisePutDto dto) {
//        // TODO generate new unused enterprise number
//        return toGetDto(enterpriseRepository.save(enterprise));
//    }

    @PutMapping("/enterprises/{id}")
    public EnterpriseGetDto put(@PathVariable String id, @RequestBody EnterprisePutDto dto) {
        Enterprise enterprise = enterpriseRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        enterprise.setType(dto.type());
        enterprise.setStatus(dto.status());
        enterprise.setStartDate(dto.startDate());
        return toGetDto(enterpriseRepository.save(enterprise));
    }

    // TODO Does not work yet because of constraints
    @DeleteMapping("/enterprises/{id}")
    public void delete(@PathVariable String id) {
        enterpriseRepository.deleteById(id);
    }

    private static EnterpriseGetDto toGetDto(Enterprise e) {
        return EnterpriseGetDto
                .builder()
                .id(e.getId())
                .type(e.getType())
                .status(e.getStatus())
                .startDate(e.getStartDate())
                .build();
    }

    private OrderSpecifier<?>[] orderBy(QEnterprise enterprise, String sortStr) {
        if (sortStr == null || sortStr.isBlank()) {
            return new OrderSpecifier[]{
                    enterprise.id.desc()
            };
        }
        JSONArray sort = new JSONArray(sortStr);
        if (sort.length() % 2 != 0) {
            throw new IllegalArgumentException("sort should have even length given as array e.g ['name', 'ASC', 'birthDate', 'DESC']");
        }
        List<OrderSpecifier<?>> result = new ArrayList<>();
        for (int i = 0; i < sort.length(); i += 2) {
            String fieldName = sort.getString(i);
            String direction = sort.getString(i + 1);
            OrderSpecifier<?> orderSpecifier;
            if ("ASC".equalsIgnoreCase(direction)) {
                orderSpecifier = field(enterprise, fieldName).asc();
            } else if ("DESC".equalsIgnoreCase(direction)) {
                orderSpecifier = field(enterprise, fieldName).desc();
            } else {
                throw new IllegalArgumentException("Unsupported sort direction: " + direction);
            }
            result.add(orderSpecifier);
        }
        return result.toArray(OrderSpecifier[]::new);
    }

    ComparableExpressionBase<?> field(QEnterprise enterprise, String name) {
        return switch (name) {
            case "id" -> enterprise.id;
            case "startDate" -> enterprise.startDate;
            case "type" -> enterprise.type;
            case "status" -> enterprise.status;
            default -> throw new IllegalArgumentException("Unknown field: " + name);
        };
    }



/*
    private Sort sort(QueryParamWrapper wrapper, String sortStr) {
        return Sort.by(sortHelper(wrapper.getSort(), sortStr));
    }

    private List<Sort.Order> sortHelper(JSONArray sort, String primaryKeyName) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        //String usesSnakeCase = this.env.getProperty("spring-boot-rest-api-helpers.use-snake-case");
        if (sort.length() % 2 != 0) {
            throw new IllegalArgumentException("sort should have even length given as array e.g ['name', 'ASC', 'birthDate', 'DESC']");
        } else {
            for (int i = 0; i < sort.length(); i += 2) {
//                String sortBy;
//                if (usesSnakeCase != null && usesSnakeCase.equals("true")) {
//                    sortBy = this.convertToCamelCase((String)sort.get(i));
//                } else {
//                    sortBy = (String)sort.get(i);
//                }
                String sortBy = (String) sort.get(i);

                sortOrders.add(new Sort.Order(Sort.Direction.valueOf((String) sort.get(i + 1)), sortBy));
            }

            if (sortOrders.isEmpty()) {
                sortOrders.add(new Sort.Order(Sort.Direction.ASC, primaryKeyName));
            }

            return sortOrders;
        }
    }
*/

}
