package com.snapdeal.objectstore;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.snapdeal.objectstore.api.IObjectStore;
import com.snapdeal.objectstore.impl.ConcurrentObjectStore;

public class ObjectStoreTest {

	private IObjectStore os;

	@Before
	public void setup() {
		os = new ConcurrentObjectStore();
	}

	@Test
	public void testMultiplePut() throws InterruptedException {

		final long noOfObjects = 1000l;

		Runnable runT = new Runnable() {

			@Override
			public void run() {
				try {
					putApi();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		List<Thread> allT = new ArrayList<Thread>();
		for (int x = 0; x < 10; x++) {
			Thread thread = new Thread(runT);
			thread.start();
			allT.add(thread);
		}

		int x = 1001;
		while (x-- > 0) {
			if (x % 50 == 0) {
				System.out.println("Called bg process");
				Thread.currentThread().sleep(500);
				((ConcurrentObjectStore) os).bgProcess();
			}
		}

		
		for (Thread a : allT) {
			a.join();
		}
		Thread.sleep(10000);
	}

	private void putApi() throws InterruptedException {
		long noOfObjects = 25l;
		while (noOfObjects-- > 0) {
			Thread.currentThread().sleep(100);
			String data = UUID.randomUUID().toString();
			long id = os.put(data.getBytes());
			byte[] retValue = os.get(id);
			Assert.assertArrayEquals(data.getBytes(), retValue);
		}
	}


	@Test
	public void testGet() {
		long id = 786l;
		byte[] data = os.get(id);
		System.out.println(new String(data));
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	private byte[] getBytes() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().getBytes();
	}
}