package main1;
import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

public class FileListener extends FileAlterationListenerAdaptor {
	private S3 aws_s3;
	public FileListener(S3 aws_s3) {
		this.aws_s3 = aws_s3;
	}

	@Override
	public void onFileChange(File file) {
		System.out.println("文件"+file.getPath()+"内容改变了");
		System.out.println("正在上传该文件到云端...");
		String local_path = file.getPath();
		String s3_path = local_path.substring(13,local_path.length());
		s3_path = s3_path.replace("\\", "/");
		aws_s3.uploadApartFiles(s3_path, local_path);
		System.out.println("文件更新成功");
	}

	@Override
	public void onFileCreate(File file) {
		System.out.println("文件被创建了" + file.getPath());
		System.out.println("正在上传该文件到云端...");
		String local_path = file.getPath();
		String s3_path = local_path.substring(13,local_path.length());
		s3_path = s3_path.replace("\\", "/");
		aws_s3.uploadApartFiles(s3_path, local_path);
		System.out.println("上传成功");
	}

	@Override
	public void onFileDelete(File file) {
		System.out.println("文件被删除了" + file.getName());
		System.out.println("正在删除云端对应文件...");
		String local_path = file.getPath();
		String s3_path = local_path.substring(13,local_path.length());
		s3_path = s3_path.replace("\\", "/");
		aws_s3.deleteFiles(s3_path);
		System.out.println("删除成功");
	}

}
