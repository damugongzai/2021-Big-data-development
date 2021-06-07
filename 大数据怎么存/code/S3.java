package main1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class S3 {

	private String bucketName;
	private String accessKey;
	private String secretKey;
	private String serviceEndpoint;
	private String signingRegion;
	private BasicAWSCredentials credentials;
	private ClientConfiguration ccfg;
	private EndpointConfiguration endpoint;
	private String filepath;
	private final AmazonS3 s3;
	
	public S3(String bucketName,String accessKey,String secretKey,String serviceEndpoint,String signingRegion,String filepath){
		this.bucketName = bucketName;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.serviceEndpoint = serviceEndpoint;
		this.signingRegion = signingRegion;
		this.credentials = new BasicAWSCredentials(accessKey,secretKey);
		this.ccfg = new ClientConfiguration().withUseExpectContinue(true);
		this.endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
		this.filepath = filepath;
		this.s3 = AmazonS3ClientBuilder.standard()
		          .withCredentials(new AWSStaticCredentialsProvider(credentials))
		          .withClientConfiguration(ccfg)
		          .withEndpointConfiguration(endpoint)
		          .withPathStyleAccessEnabled(true)
		          .build();
	}

	public void downFiles(String keyName,String path) {
		S3ObjectInputStream s3is = null;
		FileOutputStream fos = null;
		try {
			S3Object o = s3.getObject(bucketName, keyName);
			s3is = o.getObjectContent();
			fos = new FileOutputStream(new File(path));
			byte[] read_buf = new byte[64 * 1024];
			int read_len = 0;
			while ((read_len = s3is.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}
		} catch (AmazonServiceException e) {
			System.err.println(e.toString());
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} finally {
			if (s3is != null) try { s3is.close(); } catch (IOException e) { }
			if (fos != null) try { fos.close(); } catch (IOException e) { }
		}
		}

	public void synchronization() {
		ListObjectsV2Result result = s3.listObjectsV2(bucketName);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		for (S3ObjectSummary os : objects) {
		    //System.out.println(os.getKey());
		    String keyName = os.getKey();
		    //如果是文件夹
		    if(keyName.endsWith("/")) {
		    	//File file =new File("D:\\aws-train\\"+keyName);
		    	File file =new File(filepath+keyName);
				if (!file .exists()  && !file .isDirectory())
					file .mkdir();
		    	continue;
		    }
		    //如果是文件
		    else {
		    	String path = filepath+keyName;
		    	File file=new File(path);
		    	//如果文件不存在
		    	if(!file.exists()) {
		    		System.out.println("出现"+file+"新文件，正在同步...");
		    		downloadApartFiles(keyName,path);
		    	}
		    	//如果文件存在
		    	else {
		    		long aws_time = os.getLastModified().getTime();
		    		long local_time = file.lastModified();
		    		if(aws_time>local_time) {
		    			System.out.println	("云端文件发现更新，正在同步...");
		    			downloadApartFiles(keyName,path);
		    		}
		    	}
		    }
		}
		System.out.println("同步成功!");
	}

	public void uploadFiles(String keyName,String path) {
		final File file = new File(path);
        for (int i = 0; i < 2; i++) {
            try {
                s3.putObject(bucketName, keyName, file);
                break;
            } catch (AmazonServiceException e) {
                if (e.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {
                    s3.createBucket(bucketName);
                    continue;
                }
                System.err.println(e.toString());
                System.exit(1);
            } catch (AmazonClientException e) {
                try {
                    // detect bucket whether exists
                    s3.getBucketAcl(bucketName);
                } catch (AmazonServiceException ase) {
                    if (ase.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {
                        s3.createBucket(bucketName);
                        continue;
                    }
                } catch (Exception ignore) {
                }
                System.err.println(e.toString());
                System.exit(1);

            }
            }
	}

	public void deleteFiles(String keyName) {
		try {
		    s3.deleteObject(bucketName, keyName);
		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		    System.exit(1);
		}
	}

	public void uploadApartFiles(String keyName,String filePath) {
		long partSize = 5 << 20;
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		File file = new File(filePath);
		long contentLength = file.length();
		System.out.println(contentLength);
		//System.out.println("contentLength"+contentLength);
		if(contentLength<20*1024*1024) {
			//System.out.println("文件大小小于20MB，正在进行分块上传...");
			uploadFiles(keyName,filePath);
		}
		else {
			System.out.println("文件大小超过20MB，正在进行分块上传...");
			String uploadId = null;
		
		try {
			// Step 1: Initialize.
			InitiateMultipartUploadRequest initRequest = 
					new InitiateMultipartUploadRequest(bucketName, keyName);
			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
			System.out.format("Created upload ID was %s\n", uploadId);

			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, contentLength - filePosition);

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest()
						.withBucketName(bucketName)
						.withKey(keyName)
						.withUploadId(uploadId)
						.withPartNumber(i)
						.withFileOffset(filePosition)
						.withFile(file)
						.withPartSize(partSize);

				// Upload part and add response to our list.
				System.out.format("Uploading part %d\n", i);
				partETags.add(s3.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			System.out.println("Completing upload");
			CompleteMultipartUploadRequest compRequest = 
					new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

			s3.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			System.err.println(e.toString());
			if (uploadId != null && !uploadId.isEmpty()) {
				// Cancel when error occurred
				System.out.println("Aborting upload");
				s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
			}
			System.exit(1);
		}
		}
		System.out.println("Done!");
	}

	public void downloadApartFiles(String keyName,String filePath) {
		File file = new File(filePath);
		long partSize = 5 << 20;
		
		S3Object o = null;
		
		S3ObjectInputStream s3is = null;
		FileOutputStream fos = null;
		
		
		try {
			// Step 1: Initialize.
			ObjectMetadata oMetaData = s3.getObjectMetadata(bucketName, keyName);
			final long contentLength = oMetaData.getContentLength();
			final GetObjectRequest downloadRequest = 
					new GetObjectRequest(bucketName, keyName);

			fos = new FileOutputStream(file);

			// Step 2: Download parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, contentLength - filePosition);

				// Create request to download a part.
				downloadRequest.setRange(filePosition, filePosition + partSize);
				o = s3.getObject(downloadRequest);

				// download part and save to local file.
				System.out.format("Downloading part %d\n", i);

				filePosition += partSize+1;
				s3is = o.getObjectContent();
				byte[] read_buf = new byte[64 * 1024];
				int read_len = 0;
				while ((read_len = s3is.read(read_buf)) > 0) {
					fos.write(read_buf, 0, read_len);
				}
			}

			// Step 3: Complete.
			System.out.println("Completing download");

			System.out.format("save %s to %s\n", keyName, filePath);
		} catch (Exception e) {
			System.err.println(e.toString());
			
			System.exit(1);
		} finally {
			if (s3is != null) try { s3is.close(); } catch (IOException e) { }
			if (fos != null) try { fos.close(); } catch (IOException e) { }
		}
		//System.out.println("Done!");
	}

}
