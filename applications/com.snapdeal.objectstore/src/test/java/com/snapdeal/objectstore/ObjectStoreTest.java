package com.snapdeal.objectstore;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.snapdeal.objectstore.api.IObjectStore;
import com.snapdeal.objectstore.impl.ObjectStoreImpl;

public class ObjectStoreTest {

    private static IObjectStore os;
    private AtomicInteger i = new AtomicInteger(1000);

    @BeforeClass
    public static void setup() {
        os = new ObjectStoreImpl();
    }

    @Test
    public void testMultiplePut() throws InterruptedException {

        final long noOfObjects = 100l;

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
                ((ObjectStoreImpl) os).bgProcess();
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
        long id = 20;
        byte[] data = os.get(id);
        String expected = "String " + 1020;
        Assert.assertArrayEquals(expected.getBytes(), data);
    }

    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }

    private byte[] getBytes() {
        String uuid = "String " + i.getAndIncrement();
        return uuid.getBytes();
    }
}