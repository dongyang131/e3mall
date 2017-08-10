package cn.e3mall.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDFSTest {
	@Test
	public void testUploadFile() throws Exception{
		//1.把FastDFS客户端的jar包添加到工程中,jar包在中央仓库中没有
		//2.创建一个配置文件.配置trackerServer所在的ip地址和端口号
		//3.加载配置文件
		ClientGlobal.init("E:/workspace/e3-manager-web/src/main/resources/conf/client.conf");
		//4/创建一个TrackerClient对象,直接new,没有参数
		TrackerClient trackerClient = new TrackerClient();
		//5.通过TrackerClient对象获得TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//6.创建一个StorageClient对象,需要两个参数TrackerServer,StoreServer(null)
		StorageClient storageClient = new StorageClient(trackerServer, null);
		//7.使用StoreClient上传文件,返回文件的路径和文件名
		//参数一:文件名及路径      参数二:文件的扩展名     参数三:元数据
		String[] strings = storageClient.upload_file("E:/Documents/Pictures/image/u=1573503644,1731902687&fm=214&gp=0.jpg", "jpg", null);
		//8.打印结果
		for (String string : strings) {
			System.out.println(string);
		}
	}
	@Test
	public void testUploadFile2() throws Exception{
		ClientGlobal.init("E:/workspace/e3-manager-web/src/main/resources/conf/client.conf");
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageClient storageClient = new StorageClient(trackerServer, null);
		String[] strings = storageClient.upload_file("E:/Documents/Pictures/image/8d5494eef01f3a2927a062b19325bc315c607c60.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	@Test
	public void testFastDFSClient() throws Exception{
		//创建一个FastDFSClient对象
		FastDFSClient fastDFSClient = new FastDFSClient("E:/workspace/e3-manager-web/src/main/resources/conf/client.conf");
		//使用对象上传文件
		String string = fastDFSClient.uploadFile("E:/Documents/Pictures/image/timg.jpg");
		//打印结果
		System.out.println(string);
	}
	
}
