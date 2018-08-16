package com.elderbyte.commons.process;

/**
 * This exception is thrown when a process finished with an error status
 */
public class ProcessErrorException extends RuntimeException {

    private final String processCommandline;
    private final int errorCode;
    private final String errorOutput;

    /**
     * Creates a new ProcessErrorException
     * @param errorCode The process exit code
     * @param errorOutput The process error output
     */
    public ProcessErrorException(String processCommandline, int errorCode, String errorOutput){
        super(buildErrorMessage(processCommandline, errorCode, errorOutput));
        this.processCommandline = processCommandline;
        this.errorCode = errorCode;
        this.errorOutput = errorOutput;
    }

    private static String buildErrorMessage(String processCommandline, int errorCode, String errorOutput){
        return "Process Error " + errorCode + "; Commandline: " + processCommandline + "\n Error Output:\n" + errorOutput;
    }


    public String getProcessCommandline() {
        return processCommandline;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorOutput() {
        return errorOutput;
    }
}
