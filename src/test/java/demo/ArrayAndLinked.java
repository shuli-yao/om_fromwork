package demo;


import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName ArrayAndLinked
 * @Description
 * @Author shuliyao
 * @CreateTime 2018/8/1 下午3:11
 */
public class ArrayAndLinked {
    public static void main(String[] args) throws InterruptedException {

//        List<String> list = new ArrayList<>(100000);
        BlockingQueue<String> downloadQueue = new ArrayBlockingQueue<String>(10000000); //创建阻塞队列对象
        Date beginDate = new Date();
        for (int i = 0; i < 10000000; i++) {
            downloadQueue.add("aaaaaaaaa");
            String s = downloadQueue.take();
        }
        Date endDate = new Date();
        Long addL1 = endDate.getTime()-beginDate.getTime();

        Date beginDate1 = new Date();
//        while (downloadQueue.size()!=0){
//
//        }
        Date endDate1 = new Date();

        Long taskL1 = endDate1.getTime()-beginDate1.getTime();
        System.out.println(addL1+","+taskL1+","+(addL1+taskL1));





        BlockingQueue<String> downloadQueue1 = new LinkedBlockingQueue<String>(10000000); //创建阻塞队列对象
        Date beginDate2 = new Date();
        for (int i = 0; i < 10000000; i++) {
            downloadQueue1.add("aaaaaaaaa");
            String s = downloadQueue1.take();
        }
        Date endDate2 = new Date();

        Long addl =endDate2.getTime()-beginDate2.getTime();
        Date beginDate3 = new Date();
//        while (downloadQueue1.size()!=0){
//        }
        Date endDate3 = new Date();
        Long taskL = endDate3.getTime()-beginDate3.getTime();
        System.out.println(addl+","+taskL+","+(addl+taskL));

//
//        Date beginDate1 = new Date();
//        for (int i = 0; i < 100000; i++) {
//            list.remove(0);
//        }
//        Date endDate1 = new Date();
//
//        System.out.println(endDate1.getTime()-beginDate1.getTime());
//
//
//        Date beginDate2 = new Date();
//        for (int i = 0; i < 100000; i++) {
//            list.add("奥迪啊哈哈哈哈哈哈哈哈哈哈哈哈");
//        }
//        Date endDate2 = new Date();
//
//        System.out.println(endDate2.getTime()-beginDate2.getTime());



    }
}
