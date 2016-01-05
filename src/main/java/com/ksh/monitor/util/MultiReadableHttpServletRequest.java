package com.ksh.monitor.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오후 12:31
 * HttpServletRequestWrapper를 상속하네요
 */
public class MultiReadableHttpServletRequest extends HttpServletRequestWrapper{

    private static Logger logger = LoggerFactory.getLogger(MultiReadableHttpServletRequest.class);

    private ByteArrayOutputStream cachedBytes;

    public MultiReadableHttpServletRequest(HttpServletRequest request) {
        super(request);
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(cachedBytes == null)
            cacheInputStream();

        return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException{
        cachedBytes = new ByteArrayOutputStream();

        logger.info("super.getInputStream() : {}",super.getInputStream().toString());

        IOUtils.copy(super.getInputStream(), cachedBytes);
    }


    public class CachedServletInputStream extends ServletInputStream{
        private ByteArrayInputStream input;

        public CachedServletInputStream(){
            input = new ByteArrayInputStream(cachedBytes.toByteArray());
        }

        /**
         * Reads the next byte of data from the input stream. The value byte is
         * returned as an <code>int</code> in the range <code>0</code> to
         * <code>255</code>. If no byte is available because the end of the stream
         * has been reached, the value <code>-1</code> is returned. This method
         * blocks until input data is available, the end of the stream is detected,
         * or an exception is thrown.
         * <p>
         * <p> A subclass must provide an implementation of this method.
         *
         * @return the next byte of data, or <code>-1</code> if the end of the
         * stream is reached.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        public int read() throws IOException {
           return input.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }


    }
}
