package com.db.tradestore.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeInfo {
  private String tradeId;
  private Integer version;
  private String counterPartyId;
  private String bookId;
  private LocalDate maturityDate;
  private LocalDate createdDate;
  private String expired;
}
