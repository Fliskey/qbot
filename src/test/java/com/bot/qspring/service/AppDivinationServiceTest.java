package com.bot.qspring.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class AppDivinationServiceTest {

//    @Autowired
//    private AppDivinationService appDivinationService;
//
//    @Test
//    void encodeTest(){
//        Integer weekRecord = 0;
//        Integer MonLevel = 2;
//        Integer TueLevel = 3;
//        Integer WedLevel = 1;
////        Integer ThuLevel = 5;
//        Integer FriLevel = 4;
//        Integer SatLevel = 3;
//        Integer SunLevel = 5;
//        weekRecord = appDivinationService.encodeWeek(weekRecord, 1, MonLevel);
//        assertEquals(weekRecord, Integer.valueOf(2));
//        weekRecord = appDivinationService.encodeWeek(weekRecord, 2, TueLevel);
//        assertEquals(weekRecord, Integer.valueOf(32));
//        weekRecord = appDivinationService.encodeWeek(weekRecord, 3, WedLevel);
//        assertEquals(weekRecord, Integer.valueOf(132));
////        weekRecord = appDivinationService.encodeWeek(weekRecord, 4, ThuLevel);
////        assertEquals(weekRecord, Integer.valueOf(5132));
//        weekRecord = appDivinationService.encodeWeek(weekRecord, 5, FriLevel);
//        assertEquals(weekRecord, Integer.valueOf(40132));
//        weekRecord = appDivinationService.encodeWeek(weekRecord, 6, SatLevel);
//        assertEquals(weekRecord, Integer.valueOf(340132));
//        weekRecord = appDivinationService.encodeWeek(weekRecord, 7, SunLevel);
//        assertEquals(weekRecord, Integer.valueOf(5340132));
//    }
//    @Test
//    void decodeTest(){
//        Integer weekRecord = 5345132;
//        List<Integer> week = appDivinationService.decodeWeek(weekRecord);
//        List<Integer> stand = Arrays.asList(2, 3, 1, 5, 4, 3, 5);
//        assertEquals(week, stand);
//    }
//
//    @Test
//    void judgeSpecialDay() {
//        int ret = appDivinationService.judgeSpecialDay();
//        assertEquals(0, ret);
//    }
//
//    @Autowired
//    private DivinationDao divinationDao;
//
//    @Test
//    void testDivinationSingle(){
//        List<Divination> list = new ArrayList<>();
//        for(int i = 0 ; i < 10 ; i++){
//            Divination divination = divinationDao.getSpecialDivination(1);
//            list.add(divination);
//        }
//        list.forEach((System.out::println));
//    }
}
