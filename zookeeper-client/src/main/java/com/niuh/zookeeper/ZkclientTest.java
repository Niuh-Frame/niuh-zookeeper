package com.niuh.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;

public class ZkclientTest {
    ZkClient zkClient;
    @Before
    public void init() {
         zkClient = new ZkClient("127.0.0.1:2181", 5000, 5000);
    }
    public void createTest(){
    }
}
