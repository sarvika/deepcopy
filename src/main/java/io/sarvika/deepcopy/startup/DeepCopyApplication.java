package io.sarvika.deepcopy.startup;

import io.sarvika.deepcopy.FileCopier;
import io.sarvika.deepcopy.configuration.Settings;
import io.sarvika.deepcopy.exceptions.BadConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeepCopyApplication {

    private static final Logger logger = LogManager.getLogger("DeepCopy");

    private String srcBaseDir;
    private List<String> fileNames;
    private String destBaseDir;

    public static void main(String[] args) {

        DeepCopyApplication application = new DeepCopyApplication();
        try {
            application.load(args);
        } catch (BadConfigurationException ex) {
            logger.info("Usage: ");
            logger.info(
                    "deepcopy -b path/to/base/dir < -s relative/path/to/base | -f "
                            + "listfile > -d path/to/dest/base");
            logger.error("Invalid configuration.", ex);
            System.exit(1);
        }

        logger.info("Starting DeepCopy");
        try {
            new FileCopier(application.srcBaseDir, application.destBaseDir, application.fileNames)
                    .copy();
        } catch (Exception ex) {
            logger.error("DeepCopy failed!", ex);
        }
        logger.info("DeepCopy finished");
    }

    private void load(String[] args) {
        Settings.configure(args);

        if (Settings.getConfiguration().isVerbose())
            Configurator.setAllLevels("io.sarvika", Level.DEBUG);

        srcBaseDir = Settings.getConfiguration().getOption("-b", "--base");
        destBaseDir = Settings.getConfiguration().getOption("-d", "--dest");

        logger.debug("Source base directory: " + srcBaseDir);
        logger.debug("Destination base directory: " + destBaseDir);

        if (StringUtils.isAllBlank(srcBaseDir, destBaseDir)) throw new BadConfigurationException();

        String sourceFileOption = Settings.getConfiguration().getOption("-f", "--file");
        String sourceFile = Settings.getConfiguration().getOption("-s", "--src");

        if (!StringUtils.isBlank(sourceFileOption)) {
            File srcFile = new File(sourceFileOption);
            logger.debug("Reading file list from: " + srcFile.getAbsolutePath());

            try {
                fileNames = Collections.unmodifiableList(FileUtils.readLines(srcFile, "UTF-8"));
                fileNames.forEach(f -> logger.debug("-- File: " + f));
            } catch (IOException e) {
                logger.error("Cannot read file: " + srcFile.getAbsolutePath());
                throw new BadConfigurationException(e);
            }
        } else {
            if (!StringUtils.isBlank(sourceFile)) {
                logger.debug("Will copy: " + sourceFile);
                fileNames = Collections.unmodifiableList(Arrays.asList(sourceFile));
            } else {
                throw new BadConfigurationException("Either -f or -s option must be specified.");
            }
        }
    }
}
