package com.example.javastudy;

import cn.hutool.core.date.DateUtil;
import com.example.javastudy.spring.dao.UserDao;
import com.example.javastudy.多线程.sales;
import jdk.nashorn.internal.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

@SpringBootTest
class JavastudyApplicationTests {

//    spring 注解
//    @Autowired
//        @Qualifier("userDapImpll1")
//      UserDao userDao;
//    Resource  不是spring官方包是javax提供的
     @Resource(name = "userDapImpll1")

    UserDao userDao;

    @Test
    void  testspring(){

        userDao.say();
    }










    @Test
    void contextLoads() {

        System.out.println("开始运行"
        );
        sales a = new sales();

        Thread Sale1 = new Thread(a, "售票口1");
        Thread Sale2 = new Thread(a, "售票口2");
        Thread Sale3 = new Thread(a, "售票口3");
        Thread Sale4 = new Thread(a, "售票口4");
        // 启动线程，开始售票
        Sale1.start();
        Sale2.start();
        Sale3.start();
        Sale4.start();
//        Thread win1 = new Thread(a);
//        Thread win2 = new Thread(a);
//        win1.setName("窗口1");
//        win1.setName("窗口2");
//        win1.start();
//        win2.start();


    }



//listmap数组的遍历

  @Test
  void testlistmaop(){
      Map<String,String> map1 = new HashMap<String,String>();
      map1.put("556","fds");
      map1.put("855","das");
      Map<String,String> map2 = new HashMap<String,String>();
      map2.put("556","fds");
      map2.put("855","das");
      List<Map<String,String>>  list=new ArrayList<Map<String,String>>();
      list.add(map2);
      list.add(map1);
      for (Map<String ,String> m:list){
          for (String s:m.keySet()){
//              System.out.println("小标"+s+m.get(s));
          }
      }

  }



  //单个listmap相互比较
//    @Test
//    void  listmaponecompare(){
//        List<Map<String,String>>  l=new ArrayList<Map<String,String>>();
//        Map<String,String>  m1=new HashMap<>() ;
//        m1.put("count","1");
//        m1.put("name","帮宝适尿裤");
//        l.add(m1);
//
//        Map<String, String> m2 = new HashMap<String, String>();
//        m2.put("count", "1");
//        m2.put("name", "帮宝适纸尿裤L164 超薄干爽夏季透气专用婴儿尿不湿");
//        m2.put("province", "江苏省");
//        m2.put("date", "2014-09-23 10:13:39");
//        m2.put("channel", "东环大润发");
//        m2.put("city", "泰州市");
//        l.add(m2);
//
//        Map<String, String> m3 = new HashMap<String, String>();
//        m3.put("count", "1");
//        m3.put("name", "帮宝适纸尿裤L164 超薄干爽夏季透气专用婴儿尿不湿");
//        m3.put("province", "江苏省");
//        m3.put("date", "2014-09-23 10:13:39");
//        m3.put("channel", "东环大润发");
//        m3.put("city", "泰州市");
//        l.add(m3);
//        System.out.println(l);
//        Map<String, String> m45 = new HashMap<String, String>();
//        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
//        l.forEach( m45 ->System.out.println( m45));
//
//
//
//
//
////        list.forEach(person -> System.out.println(person.toString()));
//
//        int a=l.size();
////        System.out.println(a);
////        for (int i=0; i<a;i++
////             ) {
////
////            Map<String, String> consquence=l.get(i);
////
////            for (  String s:consquence.keySet()){
////
////
////                System.out.println(consquence.get(s));
////            }
////            System.out.println("小标"+consquence);
////
////        }
//
//    }

  //两个；listmap数据相加减
//    @Test
//    void testlistmapcompare(){
//        for (int i = 0; i < inOrderList.size(); i++) {//循环获取入库数据
//            String number1 = String.valueOf(inOrderList.get(i).get("number"));
//            String depotName1 = inOrderList.get(i).get("name");
//            String proId1 = inOrderList.get(i).get("id");
//            for (int j = 0; j < outOrderList.size(); j++) {//循环获取出库数据
//                String number2 = String.valueOf(outOrderList.get(j).get("number"));
//                String depotName2 = outOrderList.get(j).get("name");
//                String proId2 = outOrderList.get(j).get("id");
//                if (depotName1.equals(depotName2) && proId1.equals(proId2)) {
//                    Integer sum =Integer.parseInt(number1) - Integer.parseInt(number2);//判断是否为同一条数据，同条数据数量相减
//                    inOrderList.get(i).put("number", sum + "");
//                }
//            }
//        }
//    }

    //iteraol遍历
    @Test
    void testitera() {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add("你好"
        );
        objects.add("未来可期");
        //转换成数组
        Object[] objects1 = objects.toArray();
        Iterator<Object>   a = objects.iterator();
        for( int i=0;i<objects1.length;i++){

          System.out.println( objects1[i]);

        }
        //数组转换为集合

        List<Object> asList = Arrays.asList(objects1);

        for ( Object b: objects1 ){

            System.out.println(b);

        }


//        objects.forEach();
//        while (a.hasNext()) {
//            String b = (String) a.next();
//            System.out.println(b);
//
//        }
    }
    @Test
            void hutu(){
    String dateStr = "2017-03-01 22:33:23";
    Date date = DateUtil.parse(dateStr);

    //一天的开始，结果：2017-03-01 00:00:00
    Date beginOfDay = DateUtil.beginOfDay(date);

    //一天的结束，结果：2017-03-01 23:59:59
    Date endOfDay = DateUtil.endOfDay(date);
        System.out.println(endOfDay);
    }

}
