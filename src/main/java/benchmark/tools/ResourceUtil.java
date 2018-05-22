package benchmark.tools;

import java.io.InputStream;
import java.nio.file.NoSuchFileException;

public class ResourceUtil {

	private String filePath;

	public ResourceUtil(String filePath) {
		this.filePath = filePath;

		if (filePath.startsWith("/")) {
			throw new IllegalArgumentException(
					"Relative paths may not have a leading slash!");
		}
	}

	public InputStream getResource() throws NoSuchFileException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		//ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		
		InputStream inputStream = classLoader.getResourceAsStream(filePath);

		if (inputStream == null) {
			throw new NoSuchFileException(
					"Resource file not found. Note that the current directory is the source folder!");
		}

		return inputStream;
	}
}
