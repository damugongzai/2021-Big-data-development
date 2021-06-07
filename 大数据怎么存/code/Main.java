package main1;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

//import main.FileListener;
//�����ļ�
public class Main {
	private final static String bucketName = "new1";
	private final static String accessKey = "F1C89FE5FFD52C4614EE";
	private final static String secretKey = "Wzk3MUE2MTU5NUQ3Nzg1ODBENDdDMDNDNzE0NTA3";
	private final static String serviceEndpoint = "http://scut.depts.bingosoft.net:29997";
	private final static String signingRegion = "";
	
	public static void main(String[] args) throws Exception {
		System.out.println("������󶨱����ļ��е�·��:");
		Scanner scanner = new Scanner(System.in);
		String filePath=scanner.next();
		S3 aws_s3 = new S3(bucketName,accessKey,secretKey,serviceEndpoint,signingRegion,filePath);
		System.out.println("�󶨳ɹ�!");
		System.out.println("��ʼͬ�������ļ���...");
		aws_s3.synchronization();
		System.out.println("��ʼ�����ļ��м��...");
		//String filePath = "D:\\aws-train\\";// ���Ŀ¼
		long interval = TimeUnit.MILLISECONDS.toMillis(100);//���ü��0.1��
		FileAlterationObserver observer = new FileAlterationObserver(filePath);
		FileListener ahh = new FileListener(aws_s3);
		observer.addListener(ahh);//�����ļ��仯������
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);//��������
		monitor.start();//��ʼ����
	}
	public void listener() {
		
	}
}










