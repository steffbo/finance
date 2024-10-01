package cc.remer.finance.bank;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank")
public class BankController {

  private final BankService bankService;

  @GetMapping("/{id}")
  public ResponseEntity<Bank> get(@PathVariable("id") Long id) {
    Bank bank = bankService.get(id);
    return ResponseEntity.ok(bank);
  }

  @GetMapping
  public ResponseEntity<List<Bank>> list() {
    List<Bank> banks = bankService.list();
    return ResponseEntity.ok(banks);
  }

  @PostMapping
  ResponseEntity<Bank> add(@RequestBody Bank bank) {
    Bank added = bankService.add(bank);
    return ResponseEntity.ok(added);
  }

  @PutMapping
  public ResponseEntity<Bank> update(@RequestBody Bank bank) {
    Bank updated = bankService.update(bank);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping
  public ResponseEntity<Boolean> delete(@RequestParam Long id) {
    bankService.delete(id);
    return ResponseEntity.ok(true);
  }

}
