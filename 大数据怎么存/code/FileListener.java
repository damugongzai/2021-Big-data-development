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
		System.out.println("�ļ�"+file.getPath()+"���ݸı���");
		System.out.println("�����ϴ����ļ����ƶ�...");
		String local_path = file.getPath();
		String s3_path = local_path.substring(13,local_path.length());
		s3_path = s3_path.replace("\\", "/");
		aws_s3.uploadApartFiles(s3_path, local_path);
		System.out.println("�ļ����³ɹ�");
	}

	@Override
	public void onFileCreate(File file) {
		System.out.println("�ļ���������" + file.getPath());
		System.out.println("�����ϴ����ļ����ƶ�...");
		String local_path = file.getPath();
		String s3_path = local_path.substring(13,local_path.length());
		s3_path = s3_path.replace("\\", "/");
		aws_s3.uploadApartFiles(s3_path, local_path);
		System.out.println("�ϴ��ɹ�");
	}

	@Override
	public void onFileDelete(File file) {
		System.out.println("�ļ���ɾ����" + file.getName());
		System.out.println("����ɾ���ƶ˶�Ӧ�ļ�...");
		String local_path = file.getPath();
		String s3_path = local_path.substring(13,local_path.length());
		s3_path = s3_path.replace("\\", "/");
		aws_s3.deleteFiles(s3_path);
		System.out.println("ɾ���ɹ�");
	}

}
