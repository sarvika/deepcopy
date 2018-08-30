package io.sarvika.deepcopy;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class FileCopier {

    private static final Logger logger = LogManager.getLogger("FileCopier");

    private File base;
    private File dest;
    private List<String> targets;

    public FileCopier(String base, String dest, List<String> targets) {

        this.base = new File(base);
        this.dest = new File(dest);

        this.targets = targets;
    }

    public void copy() throws Exception {

        long count = 1;
        for (String target : targets) {

            File tgtDir = new File(dest, target.substring(0, target.lastIndexOf('/')));
            File src = new File(base, target);
            File tgt = new File(dest, target);

            if (tgtDir.exists() && !tgtDir.isDirectory()) tgtDir.delete();

            tgtDir.mkdirs();

            logger.info(
                    "["
                            + count++
                            + " OF "
                            + targets.size()
                            + "] Copying "
                            + src.getAbsolutePath()
                            + " -> "
                            + tgt.getAbsolutePath());
            FileUtils.copyFile(src, tgt);
        }
    }
}
