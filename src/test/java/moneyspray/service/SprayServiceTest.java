package moneyspray.service;

import moneyspray.exception.ServiceException;
import moneyspray.repository.SprayTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class SprayServiceTest {

    @Mock
    SprayTaskRepository sprayTaskRepository;

    @Mock
    SprayService sprayService;

    @Test(expected = ServiceException.class)
    public void receiveMoneyTest() {

        try {

            given(sprayTaskRepository.findByTokenAndRoom("7HD", "j39dko")).willReturn(null);


            sprayService.receiveMoney("ktk", 34103310l, "j93jfk");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
