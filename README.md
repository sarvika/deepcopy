# Deepcopy

This utility can copy files from one directory to other while maintaining the base directory structure.

## Usage

Clone this program, and then build with:

```
mvn install
```

And then execute the `deepcopy.jar` file in the `bin` folder with these options:

`-b` or `--base`: The source directory from which to copy

`-d` or `--dest`: The destination directory

`-s` or `--src`: The source file name, relative to base directory. (*)

`-f` or `--file`: A file containing the name of files, one on each line, relative to the base.  