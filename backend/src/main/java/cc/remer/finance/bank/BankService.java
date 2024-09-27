package cc.remer.finance.bank;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankService {

  private final BankRepository bankRepository;

  public List<Bank> list() {
    return bankRepository.findAll();
  }

  public Bank get(Long id) {
    return bankRepository.findById(id).orElse(null);
  }

  public Bank update(Bank bank) {
    return bankRepository.save(bank);
  }

  public Bank add(Bank bank) {
    return bankRepository.save(bank);
  }

  public void delete(Long id) {
    bankRepository.deleteById(id);
  }

}
