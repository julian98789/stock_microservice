package com.stock_service.stock.application.dto.articledto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCartRequest {
    private List<Long> articleIds;
}
