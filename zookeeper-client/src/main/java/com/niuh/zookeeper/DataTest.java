package com.niuh.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataTest {

    ZooKeeper zooKeeper;

    @Before
    public void init() throws IOException {
        String conn = "127.0.0.1:2181";
        zooKeeper = new ZooKeeper(conn, 4000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath());
                System.out.println(event);
            }
        });
    }

    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/niuh", false, null);
        System.out.println(new String(data));
    }

    @Test
    public void getData2() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/niuh", true, null);
        System.out.println(new String(data));
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void getData3() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        zooKeeper.getData("/niuh", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    zooKeeper.getData(event.getPath(), this, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(event.getPath());
            }
        }, stat);
        System.out.println(stat);
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void getData4() throws KeeperException, InterruptedException {
        zooKeeper.getData("/niuh", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println(stat);
            }
        }, "");
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void getChild() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/niuh", false);
        children.stream().forEach(System.out::println);
    }

    @Test
    public void createData() throws KeeperException, InterruptedException {
        List<ACL> list = new ArrayList<>();
        int perm = ZooDefs.Perms.ADMIN | ZooDefs.Perms.READ;//cdwra
        ACL acl = new ACL(perm, new Id("world", "anyone"));
        ACL acl2 = new ACL(perm, new Id("ip", "192.168.0.149"));
        ACL acl3 = new ACL(perm, new Id("ip", "127.0.0.1"));
        list.add(acl);
        list.add(acl2);
        list.add(acl3);
        zooKeeper.create("/niuh/lu", "hello".getBytes(), list, CreateMode.PERSISTENT);
    }

    @Test
    public void getChild2() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/niuh", event -> {
            System.out.println(event.getPath());
            try {
                zooKeeper.getChildren(event.getPath(), false);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        children.stream().forEach(System.out::println);
        Thread.sleep(Long.MAX_VALUE);
    }
}
