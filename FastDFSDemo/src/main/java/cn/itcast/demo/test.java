package cn.itcast.demo;

import com.pinyougou.common.util.FastDFSClient;

public class test {
    public static void main(String[] args) throws Exception {
//        ClientGlobal.init("C:\\develop\\IdeaProject\\pinyougou-parent\\FastDFSDemo\\src\\fdfs_client.conf");
/*

        TrackerClient client = new TrackerClient();

        TrackerServer trackerServer = client.getConnection();

        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
*/
        FastDFSClient client = new FastDFSClient("classpath:fdfs_client.conf");

        String uploadFileResult = client.uploadFile("D:\\1.jpg", "jpg");
        System.out.println(uploadFileResult);


    }
}
