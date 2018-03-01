package edu.southeast.rpc.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.FinalRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本类用于client发现server节点的变化，实现负载均衡
 * @author ifcc
 * @date 2017年10月16日
 * @School SouthEast University
 * @version 1.0
 */
public class ServiceDiscovery {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(ServiceDiscovery.class);
	private CountDownLatch latch=new CountDownLatch(1);
	private volatile List<String> dataList=new ArrayList<String>();
	private String registeyAddress;
	
	public ServiceDiscovery(String registryAddress,String path){
		this.registeyAddress=registryAddress;
		ZooKeeper zk = connectServer();
		if (zk!=null) {
			watchNode(zk,path);
		}
	}
	
	/**
	 * 发现新节点
	 * @return
	 */
	public String discover(){
		String data=null;
		int size=dataList.size();
		if (size>0) {
			if (size==1) {
				data=dataList.get(0);
				LOGGER.debug("using only data:{}",data);
			}else{
				data=dataList.get(ThreadLocalRandom.current().nextInt(size));
				LOGGER.debug("using random data:{}",data);
			}
		}
		return data;
	}
	
	/**
	 * 连接
	 * @return
	 */
	private ZooKeeper connectServer(){
		ZooKeeper zk=null;
		try {
			zk=new ZooKeeper(registeyAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
					if (event.getState()==Event.KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
				
			});
			latch.await();
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return zk;
	}
	
	/**
	 * 监听
	 * @param zk
	 */
	private void watchNode(final ZooKeeper zk,final String path){
		try {
			// 获取所有子节点
			List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH+"/"+path, new Watcher(){

				@Override
				public void process(WatchedEvent event) {
					//节点改变
					if (event.getType()==Event.EventType.NodeChildrenChanged) {
						watchNode(zk,path);
					}
				}
				
			});
			List<String> dataList = new ArrayList<String>();
			//循环子节点
			for (String node : nodeList) {
				//获取子节点的服务器地址
				byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH+"/"+path +"/"+node, false, null);
				//存储到list中
				dataList.add(new String(bytes));
			}
			LOGGER.debug("node data:{}",dataList);
			//将节点信息记录到成员变量中
			this.dataList=dataList;
		} catch (Exception e) {
			LOGGER.error("",e);
		}
	}
}
