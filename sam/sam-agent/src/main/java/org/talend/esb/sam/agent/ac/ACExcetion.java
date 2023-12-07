package org.talend.esb.sam.agent.ac;

/**
 * 访问控制异常
 */
public class ACExcetion extends Exception {

    public ACExcetion() {
    }

    public ACExcetion(String message) {
        super(message);
    }

    public ACExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public ACExcetion(Throwable cause) {
        super(cause);
    }

    public ACExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
