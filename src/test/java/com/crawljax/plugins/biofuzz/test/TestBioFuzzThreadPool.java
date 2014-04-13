package com.crawljax.plugins.biofuzz.test;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import com.crawljax.plugins.biofuzz.core.BioFuzzThreadPool;

public class TestBioFuzzThreadPool {


	public class BioFuzzBarrier {

		private int cnt = 0;
		private Lock lock = null;
		private Condition ready = null;
		private ThreadPoolExecutor executor = null;
		private int max = 0;

		public BioFuzzBarrier(ThreadPoolExecutor executor, int max) {
			lock = new ReentrantLock();
			ready = lock.newCondition();
			this.executor = executor;
			this.max = max;
		}

		public void await() {

			lock.lock();
			try {
				this.cnt = (this.cnt+1)%(max+1);
				//System.out.println("Psize: " + executor.getPoolSize());
				//System.out.println("Count: " + this.cnt);
				while(this.cnt < max) {
					//Thread.currentThread().interrupt();
					ready.await();
				}
				ready.signalAll();				
			} catch (InterruptedException e) {
				//System.out.println("Interrupted");
			} finally {
				lock.unlock();	
			}
			
		}
		


	}



	public class MyThreadA extends Thread {

		BioFuzzBarrier barrier = null;

		MyThreadA(BioFuzzBarrier barrier) {
			this.barrier = barrier;
		}

		MyThreadA() {

		}
		@Override
		public void run() {
			
			System.out.println(">> before");

			//BioFuzzThreadPool.await();
			
			
			System.out.println(">> after");

			
			//BioFuzzThreadPool.await();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println(">> tada");
		}
	}

	public class MyThreadB extends Thread {

		BioFuzzBarrier barrier = null;

		MyThreadB(BioFuzzBarrier barrier) {
			this.barrier = barrier;
		}
		
		MyThreadB() {

		}


		@Override
		public void run() {
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//BioFuzzThreadPool.await();
			System.out.println(">> test");
			
			
			//BioFuzzThreadPool.await();
			
			System.out.println(">> holla");
		}
	}


	@Test
	public void test() {
		BioFuzzThreadPool p = BioFuzzThreadPool.getInstance();

	
//		ThreadPoolExecutor executor = new ThreadPoolExecutor(8,8,0L,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
//
//		BioFuzzBarrier barrier = new BioFuzzBarrier(executor,8);
//		
//		MyThreadA a = new MyThreadA(barrier);
//		MyThreadA b = new MyThreadA(barrier);
//		MyThreadA e = new MyThreadA(barrier);
//		MyThreadA f = new MyThreadA(barrier);
//		
//		MyThreadB c = new MyThreadB(barrier);
//		MyThreadB d = new MyThreadB(barrier);
//		MyThreadB g = new MyThreadB(barrier);
//		MyThreadB h = new MyThreadB(barrier);
//
//		executor.submit(a);
//		executor.submit(b);
//		executor.submit(c);
//		executor.submit(d);
//		executor.submit(e);
//		executor.submit(f);
//		executor.submit(g);
//		executor.submit(h);
//		
//		executor.shutdown();
//		try {
//			executor.awaitTermination(10, TimeUnit.MINUTES);
//		} catch (InterruptedException t) {
//			// TODO Auto-generated catch block
//			t.printStackTrace();
//		}
//
//		
//		executor.shutdown();
//		
//		MyThreadA a = new MyThreadA();
//		MyThreadA b = new MyThreadA();
//		MyThreadA e = new MyThreadA();
//		MyThreadA f = new MyThreadA();
//		
//		MyThreadB c = new MyThreadB();
//		MyThreadB d = new MyThreadB();
//		MyThreadB g = new MyThreadB();
//		MyThreadB h = new MyThreadB();
//		
//		p.registerThread(a);
//		p.registerThread(b);
//		p.registerThread(c);
//		p.registerThread(d);
//		p.registerThread(e);
//		p.registerThread(f);
//		p.registerThread(g);
//		p.registerThread(h);
//		
//		p.submitAll();
//		
		
		
	}


}
