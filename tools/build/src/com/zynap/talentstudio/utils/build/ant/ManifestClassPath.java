package com.zynap.talentstudio.utils.build.ant;

/**
 * Utility used to create a manifest class path.
 * It takes, as input, a reference to a path.  It converts this
 * into a space-separated list of file names.  The default
 * behavior is to simply strip off the directory portion of
 * each file entirely.
 *
 * <p>
 * The final result is assigned to the property.
 *
 * User: amark (taken from hivemind)
 * Date: 04-Nov-2005
 * Time: 12:19:57
 */

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

import java.io.File;

public class ManifestClassPath extends Task {
    private String _property;
    private Path _classpath;
    private File _directory;
    private String directoryPrefix;

    public Path createClasspath() {
        _classpath = new Path(getProject());

        return _classpath;
    }

    public String getProperty() {
        return _property;
    }

    public void setProperty(String string) {
        _property = string;
    }

    public String getDirectoryPrefix() {
        return directoryPrefix;
    }

    public void setDirectoryPrefix(String directoryPrefix) {
        this.directoryPrefix = directoryPrefix;
    }

    public void execute() {
        if (_classpath == null)
            throw new BuildException("You must specify a classpath to generate the manifest entry from");

        if (_property == null)
            throw new BuildException("You must specify a property to assign the manifest classpath to");

        StringBuffer buffer = new StringBuffer();

        String[] paths = _classpath.list();

        String stripPrefix = null;

        if (_directory != null)
            stripPrefix = _directory.getPath();

        // Will paths ever be null?

        boolean needSep = false;

        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];

            String value;
            if (stripPrefix != null) {
                if (!path.startsWith(stripPrefix))
                    continue;

                // Sometimes, people put the prefix directory in as a
                // classpath entry; we ignore it (otherwise
                // we get a IndexOutOfBoundsException

                if (path.length() == stripPrefix.length())
                    continue;

                if (needSep)
                    buffer.append(' ');

                // Strip off the directory and the seperator, leaving
                // just the relative path.

                value = filter(path.substring(stripPrefix.length() + 1));
                needSep = true;

            } else {
                if (needSep)
                    buffer.append(' ');

                File f = new File(path);
                value = f.getName();

                needSep = true;
            }

            if (directoryPrefix != null) {
                value = directoryPrefix + SEPARATOR + value;
            }

            buffer.append(value);
        }

        getProject().setProperty(_property, buffer.toString());
    }

    public File getDirectory() {
        return _directory;
    }

    /**
     * Sets a containing directory.  This has two effects:
     * <ul>
     * <li>Only files in the classpath that are contained by the directory are included.
     * <li>The directory path is stripped from each path, leaving a relative path
     * to the file.
     * </ul>
     */
    public void setDirectory(File file) {
        _directory = file;
    }

    /**
     * Classpath entries must use a forward slash, regardless of what the
     * local filesystem uses.
     */
    protected String filter(String value) {
        if (File.separatorChar == SEPARATOR)
            return value;

        return value.replace(File.separatorChar, SEPARATOR);
    }

    private static final char SEPARATOR = '/';
}

