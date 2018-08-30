package io.sarvika.deepcopy.configuration;

import io.sarvika.deepcopy.exceptions.BadConfigurationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

    private static Configuration configuration = null;

    @SuppressWarnings("serial")
    private static final List<String> OPTIONS_ARGS =
            new ArrayList<String>() {
                {
                    add("-b");
                    add("--base"); // Base FROM directory
                    add("-d");
                    add("--dest"); // Base TO directory
                    add("-s");
                    add("--src"); // Source file (relative to base)
                    add("-f");
                    add("--file"); // File containing all source files (relative to base)
                }
            };

    @SuppressWarnings("serial")
    private static final List<Character> FLAGS_ARGS =
            new ArrayList<Character>() {
                {
                    add('X'); // For verbosity
                }
            };

    public static Configuration getConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException("The program has not been configured");
        }
        return configuration;
    }

    public static void configure(String[] args) {
        if (args.length == 0) {
            throw new BadConfigurationException();
        }

        if (configuration != null) {
            throw new IllegalStateException("The program has already been configured");
        }

        int i = 0, j;
        String arg;
        char flag;

        Map<String, String> options = new HashMap<>();
        List<Character> flags = new ArrayList<>();

        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if (OPTIONS_ARGS.contains(arg)) {
                if (i < args.length) {
                    options.put(arg, args[i++].trim());
                } else {
                    throw new BadConfigurationException(arg + " must have some value");
                }
            } else {
                for (j = 1; j < arg.length(); j++) {
                    flag = arg.charAt(j);
                    if (FLAGS_ARGS.contains(flag)) {
                        flags.add(flag);
                    } else {
                        throw new BadConfigurationException("Invalid option: " + flag);
                    }
                }
            }
        }

        configuration = new Configuration(flags, options);
    }
}
