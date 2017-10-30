package pt.inesc.gsd.homepad.server.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AppElementClassLoader extends ClassLoader{

	private String _appClassName;
	private String _appClassPath;
	
    public AppElementClassLoader(ClassLoader parent, String appClassName, String appClassPath) {
        super(parent);
        _appClassName = appClassName;
        _appClassPath = appClassPath;
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(!_appClassName.equals(name))
                return super.loadClass(name);

        try {
        	String url = "file:" + _appClassPath + _appClassName.replace('.', '/') + ".class";
		    URL myUrl = new URL(url);
		    URLConnection connection = myUrl.openConnection();
		    InputStream input = connection.getInputStream();
		    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		    int data = input.read();
            while(data != -1){
                buffer.write(data);
                data = input.read();
            }
            input.close();
            
            byte[] classData = buffer.toByteArray();
            return defineClass(_appClassName, classData, 0, classData.length);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}