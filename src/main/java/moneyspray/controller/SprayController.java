package moneyspray.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SprayController {

    @PostMapping("/apis/spray/new")
    public ResponseEntity createSpray(int money, int member) {
        return ResponseEntity.ok("TEST 1 OK");
    }

    @PostMapping("/apis/spray/receive")
    public ResponseEntity receiveMoney(String token) {
        return ResponseEntity.ok("TEST 2 OK");
    }

    @GetMapping("/apis/spray/status")
    public ResponseEntity getSprayStatus(String token) {
        return ResponseEntity.ok("TEST 3 OK");
    }

}
