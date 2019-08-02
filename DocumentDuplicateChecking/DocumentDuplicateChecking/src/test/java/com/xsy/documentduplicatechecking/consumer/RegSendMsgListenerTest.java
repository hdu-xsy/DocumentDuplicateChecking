package com.xsy.documentduplicatechecking.consumer;

import org.junit.Test;

import java.util.Random;

/*@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration*/
public class RegSendMsgListenerTest {

    @Test
    public void codeTest() {
        for (int i = 0; i < 100000; i++) {
            Random random = new Random();
            int a = Math.abs(random.nextInt(10));
            if (a < 0) {
                System.out.println(a);
            }
        }
    }

}