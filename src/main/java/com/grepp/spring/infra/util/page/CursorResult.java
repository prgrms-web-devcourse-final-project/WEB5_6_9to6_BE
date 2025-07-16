package com.grepp.spring.infra.util.page;

import java.util.List;

// T: data, C: cursor
public record CursorResult<T, C> (
    List<T> values,
    Boolean hasNext,
    C nextCursor
) {

}
