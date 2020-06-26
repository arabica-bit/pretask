package moneyspray.controller;

import moneyspray.service.SprayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SprayController {

    private final SprayService sprayService;

    public SprayController(SprayService sprayService) {
        this.sprayService = sprayService;
    }

    @PostMapping("/apis/spray/new")
    public ResponseEntity createSpray(int money, int member,
                                      @RequestHeader("X-USER-ID") Long userId,
                                      @RequestHeader("X-ROOM-ID") String roomId) {

        try {
            return ResponseEntity.ok(sprayService.createSprayService(money, member, userId, roomId));
        } catch (Exception e) {
            return ResponseEntity.ok("ERROR 500");
        }

    }

    @PostMapping("/apis/spray/receive")
    public ResponseEntity receiveMoney(String token,
                                       @RequestHeader("X-USER-ID") Long userId,
                                       @RequestHeader("X-ROOM-ID") String roomId) {
        return ResponseEntity.ok("TEST 2 OK");
    }

    @GetMapping("/apis/spray/status")
    public ResponseEntity getSprayStatus(String token,
                                         @RequestHeader("X-USER-ID") Long userId,
                                         @RequestHeader("X-ROOM-ID") String roomId) {

        return ResponseEntity.ok("TEST 3 OK with " + userId + ", " + roomId);
    }

}
