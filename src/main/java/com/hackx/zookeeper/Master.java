package com.hackx.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

public class Master implements Watcher {

    static boolean isLeader = false;
    ZooKeeper zooKeeper;
    String hostPort;
    String serverId = Integer.toHexString(new Random().nextInt());


    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    public static void main(String[] args) throws Exception {
        Master master = new Master("localhost:2181");
        master.startZK();
        master.runForMaster();
        if (isLeader) {
            System.out.println("I'm a leader!");
            Thread.sleep(60000);
        } else {
            System.out.println("Some one else is a leader!");
        }
        master.stopZK();
    }

    boolean checkMaster() {
        while (true) {
            try {
                Stat stat = new Stat();
                byte[] data = zooKeeper.getData("/master", false, stat);
                isLeader = new String(data).equals(serverId);
                return true;
            } catch (KeeperException e) {
                e.printStackTrace();
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void runForMaster() throws InterruptedException {
        while (true) {
            try {
                zooKeeper.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (KeeperException.NodeExistsException e) {
                isLeader = false;
                break;
            } catch (KeeperException e) {

            }
            if (checkMaster()) {
                break;
            }
        }
    }

    void startZK() throws IOException {
        zooKeeper = new ZooKeeper(hostPort, 15000, this);
    }

    void stopZK() throws InterruptedException {
        zooKeeper.close();
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event);
    }
}
