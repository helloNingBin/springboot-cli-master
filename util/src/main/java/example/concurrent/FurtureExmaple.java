package example.concurrent;

import com.ning.date.TimeCal;
import com.ning.thread.ThreadUtil;
import example.concurrent.bean.FileBean;

import java.util.concurrent.*;

public class FurtureExmaple {
    public static void main(String[] args) throws Exception {
        exmpale_FutureTask(1);
    }
    /**
     *  有这样的一个需求：
     *  根据文件id去查询它的文件详情，以及下载文件
     *  如果并行执行的话，需要2+2=4s
     *  但现在用异步执行，异步获取的方式，来同步执行，只需要2s
     */
    public static FileBean exmpale_executor(int id) throws ExecutionException, InterruptedException {
        TimeCal timeCal = new TimeCal();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //executor的submit方法可以提交Callable和Runnable
        Future<String> future_detail = executorService.submit(()->{
            return searchById(id);
        });
        Future<String> future_file = executorService.submit(()->{
            return downloadById(id);
        });
        FileBean fileBean = new FileBean(id,future_detail.get(),future_file.get());
        timeCal.printTimeCost();
        return fileBean;
    }

    /**
     * FutureTask是Future的实现类
     * 用FutureTask来包装Future或Runnable，好像也没什么意义，最终还是得交给Executor或Thread来运行
     */
    public static FileBean exmpale_FutureTask(int id) throws ExecutionException, InterruptedException {
        TimeCal timeCal = new TimeCal();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        FutureTask<String> future_detail = new FutureTask<>(()->{
            return searchById(id);
        });
        FutureTask<String> future_file = new FutureTask<>(()->{
            return downloadById(id);
        });
        executorService.submit(future_detail);
        executorService.submit(future_file);
        FileBean fileBean = new FileBean(id,future_detail.get(),future_file.get());
        timeCal.printTimeCost();
        System.out.println(fileBean);
        return fileBean;
    }

    //查询数据库及处理数据需要2s
    public static String searchById(int id){
        System.out.println("searchDB..........");
        ThreadUtil.sleep(2000);
        return "something from DB";
    }
    //查询数据库及处理数据需要2s
    public static String downloadById(int id){
        System.out.println("downloadSomething..........");
        ThreadUtil.sleep(2000);
        return "downloadSomething";
    }
}
