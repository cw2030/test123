package threadpool;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * 任务池管理器<br>
 * 1)指定任务的大小<br>
 * 2)初始化任务池队列<br>
 * 3)检查任务执行状态<br>
 * 4)关闭任务池
 * 
 * @author user
 * 
 */
public class TaskPoolManager<T> {
    private final static Logger log = LoggerFactory.getLogger(TaskPoolManager.class);
    private ExecutorService es = null;
    private final ITask<T> task;
    private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();
    private int poolSize = 1;
    private volatile boolean isRunning = false;
    private final String taskPoolName;
    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * @param poolSize
     *            线程池大小
     * @param dataQueueSzie
     *            数据队列大小
     * @param task
     *            所需要执行的任务
     */
    public TaskPoolManager(int poolSize,
                           int dataQueueSzie,
                           String taskPoolName,
                           ITask<T> task) {
        if (poolSize < 0) {
            this.poolSize = 1;
        } else if (poolSize > 100) {
            this.poolSize = 100;
        } else {
            this.poolSize = poolSize;
        }
        this.task = task;
        String tpn = taskPoolName;
        if (Strings.isNullOrEmpty(tpn)) {
            tpn = "TaskPoolManager-" + poolSize + "-TPM";
        }
        this.taskPoolName = tpn;
        es = Executors.newFixedThreadPool(poolSize,new DefaultThreadFactory(taskPoolName));
    }

    public void add(T t) {
        queue.add(t);
    }

    public void addAll(List<T> tList) {
        queue.addAll(tList);
    }

    public void start() {
        final CountDownLatch lock = new CountDownLatch(1);
        while (poolSize > 0) {
            es.submit(new Thread(taskPoolName + poolSize) {
                @Override
                public void run() {
                    T t = null;
                    try {
                        lock.await();
                        while(isRunning){
                            t = queue.poll(1, TimeUnit.SECONDS);
                            if (t == null) {
                                continue;
                            }
                            task.process(t);
                        }
                    }
                    catch (Exception e) {
                        log.error("", e);
                    }
                }
            });
            poolSize--;
        }
        isRunning = true;
        lock.countDown();
        log.info("{}启动成功",taskPoolName);
    }
    
    /**
     * 等待数据处理结束
     * @param seconds，最多等待多少秒返回
     */
    public void blockUntilTerminal(int seconds){
        while(seconds > 0){
            if(queue.size() > 0 || count.get() > 0){
                try{
                    TimeUnit.SECONDS.sleep(1); 
                }catch(Exception e){}
            }else{
                return;
            }
            seconds--;
        }
        isRunning = false;
    }

    public void shutdown() {
        try {
            isRunning = false;
            es.shutdown();
            es.awaitTermination(10, TimeUnit.SECONDS);
            log.info("{}关闭成功",taskPoolName);
        }
        catch (Exception e) {
            log.error("{}关闭失败", taskPoolName);
        }

    }

    public static interface ITask<T> {
        void process(T t);
    }
}
