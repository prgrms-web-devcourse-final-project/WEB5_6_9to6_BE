package com.grepp.spring.infra.util.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageParam {
    @Min(value = 1, message = "Page는 1보다 작을 수 없습니다.")
    @Max(1000)
    private int page = 1;
    @Min(value = 1, message = "Size는 1보다 작을 수 없습니다.")
    @Max(1000)
    private int size = 5;

    public Pageable getPageable() {
        return PageRequest.of(this.page - 1, this.size);
    }
}
