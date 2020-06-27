package moneyspray.controller;

import moneyspray.exception.ServiceException;
import moneyspray.service.SprayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SprayController {
    private static Logger logger = LoggerFactory.getLogger(SprayController.class);

    private final SprayService sprayService;

    public SprayController(SprayService sprayService) {
        this.sprayService = sprayService;
    }

    @PostMapping("/apis/spray/create")
    public ResponseEntity createSpray(int money, int member,
                                      @RequestHeader("X-USER-ID") Long userId,
                                      @RequestHeader("X-ROOM-ID") String roomId) {

        try {
            return ResponseEntity.ok(sprayService.createSprayService(money, member, userId, roomId));
        } catch (ServiceException se) {
            return ResponseEntity.ok(se.toString());
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.ok("ERROR");
        }

    }

    @PostMapping("/apis/spray/receive")
    public ResponseEntity receiveMoney(String token,
                                       @RequestHeader("X-USER-ID") Long userId,
                                       @RequestHeader("X-ROOM-ID") String roomId) {

        try {
            return ResponseEntity.ok(sprayService.receiveMoney(token, userId, roomId));
        } catch (ServiceException se) {
            return ResponseEntity.ok(se.toString());
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.ok("ERROR");
        }
    }

    @GetMapping("/apis/spray/status")
    public ResponseEntity getSprayStatus(String token,
                                         @RequestHeader("X-USER-ID") Long userId,
                                         @RequestHeader("X-ROOM-ID") String roomId) {

        try {
            return ResponseEntity.ok(sprayService.getSprayStatus(token, userId, roomId));
        } catch (ServiceException se) {
            return ResponseEntity.ok(se.toString());
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.ok("ERROR");
        }
    }

}
