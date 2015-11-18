package com.e9w.ocr.serviceprovider.recognize.provider.tesseract;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.e9w.ocr.constant.ErrorCode;
import com.e9w.ocr.service.RecognizeService;
import com.e9w.ocr.service.ServiceException;
import com.e9w.ocr.serviceprovider.recognize.Recognize;
import com.e9w.ocr.util.Env;
import com.e9w.ocr.util.IOUtils;
import com.e9w.ocr.util.WorkingResourceUtil;

public class Tesseract extends  Recognize{
	final static long max_filesize = 2048;
	final static String Servicename = "Tesseract service";
	final static Logger logger = LoggerFactory
			.getLogger(RecognizeService.class);
	@Override
	public String recognize(File imageFile)  throws ServiceException {
		File outputFile = new File(imageFile.getAbsolutePath());
		File outputFileDone = new File(imageFile.getAbsolutePath() + ".txt");
		String shell = Env.getShell();
		ProcessBuilder pb = new ProcessBuilder(shell, WorkingResourceUtil
				.getFile("recognize.sh").getAbsolutePath(),
				Env.getFileCacheDir(), imageFile.getPath(),
				outputFile.getPath());
		int exitCode = 0;
		try {
			exitCode = pb.start().waitFor();
		} catch (Exception e) {
			logger.error("脚本执行错误", e);
			throw new ServiceException(Servicename,
					ErrorCode.ILLEGAL_STATE, "脚本执行错误");
		}
		if (exitCode != 0) {
			throw new ServiceException(Servicename,
					ErrorCode.ILLEGAL_STATE, "脚本执行错误");
		}
		if (!outputFileDone.exists()) {
			throw new ServiceException(Servicename,
					ErrorCode.ILLEGAL_STATE, "文件创建失败:" + outputFileDone.getPath());
		}
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(outputFileDone),Env.getCharset());
			long length = outputFileDone.length();
			if (length > max_filesize) {
				throw new ServiceException(Servicename,
						ErrorCode.ILLEGAL_ARGUMENT, "无法处理大于" + max_filesize
								+ "的文件");
			}
			char buff[] = new char[(int) length];
			int readsize = reader.read(buff);
			String content = new String(buff, 0, readsize);
			content = content.replaceAll("[^0-9 a-z A-Z \u4e00-\u9fa5]", "");
			return content;

		} catch (Exception e) {
			throw new ServiceException(Servicename,
					ErrorCode.ILLEGAL_STATE, "文件读取失败:" + outputFileDone.getPath());
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	@Override
	public int getProviderId() {

		return 1;
	}

}
