package com.grepp.spring.infra.util.page;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class CursorPagingHelper {

    public <T, C> CursorResult<T, C> get(
        C cursor,
        int size,
        Function<PageRequest, Slice<T>> firstPageFetcher,
        BiFunction<C, PageRequest, Slice<T>> nextPageFetcher,
        Function<T, C> cursorExtractor
    ) {
        // 조회
        Slice<T> slice = fetchSlice(cursor, size, firstPageFetcher, nextPageFetcher);
        List<T> values = slice.getContent();

        // 다음 커서 계산
        C nextCursor = null;
        if (slice.hasNext() && !values.isEmpty()) {
            T lastElement = values.get(values.size() - 1);
            nextCursor = cursorExtractor.apply(lastElement);
        }

        return new CursorResult<>(values, slice.hasNext(), nextCursor);
    }

    private <T, C> Slice<T> fetchSlice(
        C cursor,
        int size,
        Function<PageRequest, Slice<T>> firstPageFetcher,
        BiFunction<C, PageRequest, Slice<T>> nextPageFetcher
    ) {
        PageRequest pageRequest = PageRequest.of(0, size);
        if (cursor == null) {
            // 첫 페이지
            return firstPageFetcher.apply(pageRequest);
        }
        // 다음 페이지
        return nextPageFetcher.apply(cursor, pageRequest);
    }


}
