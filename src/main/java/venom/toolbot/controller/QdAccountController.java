package venom.toolbot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import venom.toolbot.entity.QdAccount;
import venom.toolbot.service.QdAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class QdAccountController {

    private final QdAccountService qdAccountService;

    @GetMapping
    public List<QdAccount> getAllAccounts() {
        return qdAccountService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QdAccount> getAccountById(@PathVariable Long id) {
        return qdAccountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public QdAccount createAccount(@RequestBody QdAccount account) {
        return qdAccountService.save(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QdAccount> updateAccount(@PathVariable Long id, @RequestBody QdAccount account) {
        if (qdAccountService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        account.setAccountId(id);
        return ResponseEntity.ok(qdAccountService.update(account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (qdAccountService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        qdAccountService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
