package com.elderbyte.commons.process;


import com.elderbyte.commons.exceptions.ArgumentNullException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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

    private final Map<String, String> envVariables = new HashMap<>();
    private final List<String> arguments = new ArrayList<>();

    private ExecutorService processIOThreadPool = DEFAULT_PROCESS_IO_THREAD_POOL;
    private Path directory;
    private boolean redirectErrorToStdOut = false;



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
     * Process working directory
     * @param directory The process working directory
     */
    public ProcessTemplate workingDir(Path directory){

        if(directory == null) throw new ArgumentNullException("directory");

        this.directory = directory;
        return this;
    }

    /**
     * Set an environment variable for the process to use
     */
    public ProcessTemplate setEnv(String key, String value){

        if(key == null) throw new ArgumentNullException("key");

        this.envVariables.put(key, value);
        return this;
    }

    /**
     * Redirect the error out to std out.
     */
    public ProcessTemplate redirectErrorToStdOut(){
        redirectErrorToStdOut = true;
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
     * Starts a new process from this process-template with a custom std-out input-stream consumer
     * Useful if the process is not emitting text but other binary data.
     *
     * @param stdOutReader
     * @return The async running  process
     * @throws IOException Thrown when there was a problem starting the process.
     */
    public AsyncProcess<Void> start(Consumer<InputStream> stdOutReader) throws IOException {
        return start(new StreamReader<>() {
            @Override
            public void read(InputStream stream) {
                stdOutReader.accept(stream);
            }

            @Override
            public Void getValue() {
                return null;
            }
        });
    }

    /**
     * Starts a new process from this process-template with a custom std-out reader.
     * Useful if the process is not emitting text but other binary data.
     *
     * @param stdOutReader The std-out reader to use.
     * @param <T> The converted data type from std-out
     * @return The async running  process
     * @throws IOException Thrown when there was a problem starting the process.
     */
    public <T> AsyncProcess<T> start(StreamReader<T> stdOutReader) throws IOException {
        if(stdOutReader == null) throw new ArgumentNullException("stdOutReader");

        var processBuilder = new ProcessBuilder(buildCommand());
        // processBuilder.redirectErrorStream()

        if(directory != null) { processBuilder.directory(directory.toFile()); }

        processBuilder.redirectErrorStream(redirectErrorToStdOut);

        processBuilder.environment().putAll(envVariables);
        return AsyncProcess.start(processBuilder, stdOutReader, processIOThreadPool);
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
