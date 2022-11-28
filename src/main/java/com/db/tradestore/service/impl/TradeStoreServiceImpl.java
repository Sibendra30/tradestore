package com.db.tradestore.service.impl;

import com.db.tradestore.entity.TradeRecord;
import com.db.tradestore.exception.InvalidTradeVersionException;
import com.db.tradestore.model.TradeInfo;
import com.db.tradestore.repository.TradeRepository;
import com.db.tradestore.service.TradeStoreService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeStoreServiceImpl implements TradeStoreService {

  private final TradeRepository tradeRepository;

  @Autowired
  public TradeStoreServiceImpl(TradeRepository tradeRepository) {
    this.tradeRepository = tradeRepository;
  }

  private TradeRecord convertTradeInfoToTradeRecord(TradeInfo tradeInfo) {
    return new TradeRecord(UUID.randomUUID().toString(), tradeInfo.getTradeId(),
        tradeInfo.getVersion(), tradeInfo.getCounterPartyId(), tradeInfo.getBookId(),
        tradeInfo.getMaturityDate(), LocalDate.now(), tradeInfo.getExpired());
  }

  private TradeInfo convertTradeRecordToTradeInfo(TradeRecord tradeRecord) {
    return new TradeInfo(tradeRecord.getTradeId(),
        tradeRecord.getVersion(), tradeRecord.getCounterPartyId(), tradeRecord.getBookId(),
        tradeRecord.getMaturityDate(), LocalDate.now(), tradeRecord.getExpired());
  }

  @Override
  public TradeInfo createTrade(TradeInfo tradeInfo) {
    List<TradeRecord> tradeRecordList = this.tradeRepository.findAllByTradeId(tradeInfo.getTradeId());
    TradeRecord tradeRecord = convertTradeInfoToTradeRecord(tradeInfo);
    if (!tradeRecordList.isEmpty()) {
      tradeRecordList.sort((t1, t2) ->  {
        if (t1.getVersion() < t2.getVersion()) {
          return 1;
        }
        return -1;
      });

      if (tradeInfo.getVersion() < tradeRecordList.get(0).getVersion()) {
        throw new InvalidTradeVersionException();
      } else if (tradeInfo.getVersion() == tradeRecordList.get(0).getVersion()) {
        tradeRecord.setId(tradeRecordList.get(0).getId());
      }
    }
    TradeRecord result = this.tradeRepository.save(tradeRecord);
    return this.convertTradeRecordToTradeInfo(result);
  }
}
