/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cricketmsf.sensesservice.out;

/**
 *
 * @author greg
 */
public class StoreResult {
    private int code;
    private String content;
    private long contentLength;
    private long responseTime;

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getContent() {
        return content;
    }

    /**
     * @param message the message to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the responseTime
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * @param responseTime the responseTime to set
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * @return the contentLength
     */
    public long getContentLength() {
        return contentLength;
    }

    /**
     * @param contentLength the contentLength to set
     */
    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
    
    
}
