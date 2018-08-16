package com.elderbyte.commons.process;


import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A process builder/factory from which a native process can be configured and started.
 */
public class ProcessTemplate {

    /**
     * Creates a new process template for the given command.
     * @param command The command (Must not be null)
     * @return A new process template.
     */
    public static ProcessTemplate command(String command){

        if(command == null) throw new ArgumentNullException("command");

        return new ProcessTemplate(command);
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    private static final ExecutorService DEFAULT_PROCESS_IO_THREAD_POOL = Executors.newCachedThreadPool();


    private final List<String> arguments = new ArrayList<>();
    private ExecutorService processIOThreadPool = DEFAULT_PROCESS_IO_THREAD_POOL;


    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    private ProcessTemplate(String command){
        arguments.add(command);
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Adds a command-line argument, that is a flag with a parameter value such as:
     *
     * -i /my/input/file.mp3
     *
     * The '-i' is the argument
     * The path after is the value.
     *
     * @param argument The argument to add
     * @param value The value for the argument
     */
    public ProcessTemplate argument(String argument, String value){

        if(argument == null) throw new ArgumentNullException("argument");
        if(value == null) throw new ArgumentNullException("value");

        arguments.add(argument);
        arguments.add(value);
        return this;
    }

    /**
     * Adds a single flag to the commandline without any argument, such as:
     *
     * '-quiet'
     *
     * @param flag The flag to add
     */
    public ProcessTemplate flag(String flag){

        if(flag == null) throw new ArgumentNullException("flag");

        arguments.add(flag);
        return this;
    }

    /**
     * Sets the thread pool which is used for the async IO of the process.
     * @param threadPool The thread pool to use.
     */
    public ProcessTemplate threadPool(ExecutorService threadPool){

        if(threadPool == null) throw new ArgumentNullException("threadPool");

        processIOThreadPool = threadPool;
        return this;
    }

    /**
     * Starts a new process from this process-template with a default std-out text reader.
     * @return Returns an async-process
     * @throws IOException Thrown when there was a problem starting the process.
     */
    public AsyncProcess<String> start() throws IOException {
        return start(new TextStreamReader());
    }

    /**
     * Starts a new process from this process-template with a custom std-out reader.
     * Usefully if the process is not emitting text but other binary data.
     *
     * @param stdOutReader The std-out reader to use.
     * @param <T> The converted data type from std-out
     * @return The async running  process
     * @throws IOException Thrown when there was a problem starting the process.
     */
    public <T> AsyncProcess<T> start(StreamReader<T> stdOutReader) throws IOException {
        if(stdOutReader == null) throw new ArgumentNullException("stdOutReader");
        return AsyncProcess.start(buildCommand(), stdOutReader, processIOThreadPool);
    }

    @Override
    public String toString(){
        return String.join(" ", arguments);
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/


    private String[] buildCommand(){
        return arguments.toArray(new String[arguments.size()]);
    }


}
