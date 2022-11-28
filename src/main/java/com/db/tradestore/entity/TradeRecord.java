package com.db.tradestore.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRecord {
  @Id
  private String id;
  private String tradeId;
  private int version;
  private String counterPartyId;
  private String bookId;
  private LocalDate maturityDate;
  private LocalDate createdDate;
  private String expired;
}
