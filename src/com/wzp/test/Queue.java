package com.wzp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue<T> {
	final ExecutorService exeService;
	final List<IEventProcessor<T>> list = new ArrayList<IEventProcessor<T>>();
	final int size;
	final Object[] objects;
	AtomicInteger readIdx = new AtomicInteger(-1);
	AtomicInteger writeIdx = new AtomicInteger(0);
	private boolean isRunning = false;

	public Queue(int bufferSize,Class<T> c,ExecutorService service)throws Exception{
		if (bufferSize < 1)
        {
            throw new IllegalArgumentException("bufferSize must not be less than 1");
        }
		if (Integer.bitCount(bufferSize) != 1)
        {
            throw new IllegalArgumentException("bufferSize must be a power of 2");
        }
		this.size = bufferSize;
		objects = new Object[bufferSize];
		int count = 0;
		do{
			objects[count] = c.newInstance();
			count++;
		}while(count < bufferSize);
		
		if(service == null){
			exeService = Executors.newFixedThreadPool(1);
		}else{
			this.exeService = service;
		}
	}
	
	public void addConsumer(IEventProcessor<T> processor){
		list.add(processor);
	}
	
	public synchronized int next(){
		if(writeIdx.get() == 0)
			return 0;
		int w = writeIdx.get() % size;
		int r = readIdx.get() % size;
		if(w > r  && (w+1 % size) != r){
			return writeIdx.incrementAndGet();
		}
		return -1;
	}
	@SuppressWarnings("unchecked")
	public synchronized T get(int idx){
		try{
			return (T)objects[idx % size];
		}catch(Exception e){
			System.out.println("main : " + idx);
			return null;
		}
		
	}
	
	public void publish(){
		if(writeIdx.get() == 0)
			writeIdx.incrementAndGet();
		if(!isRunning){
			if(list.size() == 0){
				System.err.println("Can't find queue consumer.");
				return;
			}
			isRunning = true;
			new Notify().start();
		}
	}
	
	class Notify extends Thread{

		@Override
		public void run() {
			while(true){
				if(readIdx.get() + 1 <writeIdx.get()){
					int seq = readIdx.incrementAndGet();
					for(IEventProcessor<T> call : list){
						try{
							call.process(seq, get(seq));
						}catch(Exception e){
							System.err.println("error-->" + seq);
						}
						
					}
				}else{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public interface IEventProcessor<T>{
		public void process(int sequence,T t);
	}
	
	public static void main(String[] args)throws Exception{
		Queue<Bean1> q = new Queue<Bean1>(32, Bean1.class, Executors.newFixedThreadPool(2));
		q.addConsumer(new IEventProcessor<Bean1>() {
			@Override
			public void process(int sequence, Bean1 t) {
				System.out.println(sequence + "-->" + t.a);
				
			}
		});
		
		for(int i = 0 ; i < 64; i++){
			int seq = q.next();
			if(seq == -1)continue;
			Bean1 b = q.get(seq);
			if(b == null) continue;
			b.a = "a:" + System.currentTimeMillis();
			q.publish();
		}
		
	}
}
