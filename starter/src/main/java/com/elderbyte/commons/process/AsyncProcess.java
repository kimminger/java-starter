package com.elderbyte.commons.process;


import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents a native process and provides async access to the input and output streams of this process.
 * @param <T>
 */
public class AsyncProcess<T> {

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private final Process process;
    private final String commandLine;
    private final ExecutorService processIOThreadPool;

    private final StreamReader<T> standardReader;
    private final StreamReader<String> errorReader;

    /**
     * Starts a new process with the given commandline arguments
     * @param command the commandline args
     * @return The process
     * @throws IOException
     */
    public static AsyncProcess<String> start(
            final String[] command,
            ExecutorService processIOThreadPool) throws IOException {
       return start(command, new TextStreamReader(), processIOThreadPool);
    }

    /**
     * Starts a new process with the given commandline arguments
     * @param command the commandline args
     * @param standardOutReader standard output is copied to this stream
     * @return The process
     * @throws IOException
     */
    public static <T> AsyncProcess<T> start(
            final String[] command,
            StreamReader<T> standardOutReader,
            ExecutorService processIOThreadPool) throws IOException {
        return start(new ProcessBuilder(command), standardOutReader, processIOThreadPool);
    }


    /**
     * Starts a new process with the given commandline arguments
     * @param processBuilder the commandline args
     * @param standardOutReader standard output is copied to this stream
     * @return The process
     * @throws IOException
     */
    public static <T> AsyncProcess<T> start(
            ProcessBuilder processBuilder,
            StreamReader<T> standardOutReader,
            ExecutorService processIOThreadPool) throws IOException {
        return new AsyncProcess<>(
                processBuilder.start(),
                ShellParser.toCommandLine(processBuilder.command()),
                standardOutReader,
                new TextStreamReader(),
                processIOThreadPool);
    }

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new async process which async reads the std out/err into the given output streams.
     * @param process The process
     * @param commandLine The process commandline
     * @param standardReader The reader of the standard stream
     * @param errorReader The reader of the error stream
     * @param processIOThreadPool The thread pool in which the async process-io is processed
     */
    public AsyncProcess(Process process, String commandLine,
                        StreamReader<T> standardReader, StreamReader<String> errorReader,
                        ExecutorService processIOThreadPool){

        if(process == null) throw new ArgumentNullException("process");
        if(standardReader == null) throw new ArgumentNullException("standardReader");
        if(errorReader == null) throw new ArgumentNullException("errorReader");
        if(processIOThreadPool == null) throw new ArgumentNullException("processIOThreadPool");

        this.process = process;
        this.standardReader = standardReader;
        this.errorReader = errorReader;
        this.commandLine = commandLine;
        this.processIOThreadPool = processIOThreadPool;
        setupReaders();
    }


    /***************************************************************************
     *                                                                         *
     * Public methods                                                          *
     *                                                                         *
     **************************************************************************/

    /**
     * Writes all bytes from the given input-stream to this process.
     */
    public void writeToProcess(InputStream is) throws IOException {
        try(var output = getProcess().getOutputStream()){
            is.transferTo(output);
        }
    }

    /**
     * Gets the complete standard output of this process up to this point in time.
     * If the process is still running, a subsequent call might return additional data.
     */
    public T getStandardOutput(){
        return standardReader.getValue();
    }

    /**
     * Gets the complete error output of the process up to this point in time.
     * If the process is still running, a subsequent call might return more text.
     */
    public String getErrorOutputAsText(){
        return errorReader.getValue();
    }

    /**
     * Blocks until this process has completed. (exited)
     *
     * Throws an ProcessException if the process returned a
     *
     * @param timeout The max time this process can spend before a TimeoutException is thrown.
     * @param unit timeout unit
     * @throws ProcessErrorException Thrown when there was an error in this child process
     * @throws InterruptedException Thrown when the process was interrupted
     * @throws TimeoutException Thrown when the process took longer than the given timeout
     */
    public void await(long timeout, TimeUnit unit) throws ProcessErrorException, InterruptedException, TimeoutException {
        long startTime = System.nanoTime();
        long rem = unit.toNanos(timeout);

        do {
            if(process.isAlive()){
                // The process is still running
                if (rem > 0) {
                    Thread.sleep(
                        Math.min(TimeUnit.NANOSECONDS.toMillis(rem) + 1, 100));
                }
            }else{
                // The process is finished
                int exitCode = process.exitValue();

                if(exitCode == 0){
                    // By convention, a zero exit code means success
                    return;
                }else{
                    // The process completed with an error which we translate into an exception
                    throw new ProcessErrorException(commandLine, exitCode, getErrorOutputAsText());
                }
            }
            rem = unit.toNanos(timeout) - (System.nanoTime() - startTime);
        } while (rem > 0);

        throw new TimeoutException("The process took to long to complete.");
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public Process getProcess(){
        return process;
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/


    private void setupReaders(){
        processIOThreadPool.submit(
            () -> standardReader.read(process.getInputStream())
        );

        processIOThreadPool.submit(
            () -> errorReader.read(process.getErrorStream())
        );
    }
}
