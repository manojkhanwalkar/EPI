package fs;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileSystemReplicator {

    public void replicate(String fs1, String fs2)
    {
        File file1 = new File(fs1);
        File file2 = new File(fs2);

        if (!file2.exists())
        {
            file2.mkdir();
        }

        // check and reconcile files under this directory.
        var file2Files = List.of(file2.listFiles());
        var file1Files = List.of(file1.listFiles());
        // copy files that dont exist in target.
       file1Files.stream().filter(f->f.isFile()).forEach(f->{

            if (!file2Files.contains(f))
            {
                copyFile(f,fs2+"/"+f.getName());
            }
        });



        // delete files from target that dont exist in source
        file2Files.stream().filter(f->f.isFile()).forEach(f->{

            if (!file1Files.contains(f))
            {
                f.delete();
            }
        });

        // check all underlying directories
        file1Files.stream().filter(File::isDirectory).forEach(f->{

            if (!file2Files.contains(f))
            {
                replicate(fs1+"/"+f.getName(),fs2+"/"+f.getName());
            }
        });

        file2Files.stream().filter(File::isDirectory).forEach(f->{

            if (!file1Files.contains(f))
            {
                try {
                    FileUtils.deleteDirectory(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }




    @SneakyThrows
    public void copyFile(File original,String copy)
    {

        File file2 = new File(copy);


        com.google.common.io.Files.copy(original, file2);

    }

    public static void main(String[] args) {

        FileSystemReplicator fsr = new FileSystemReplicator();

        fsr.replicate("/home/manoj/data","/home/manoj/replicatetest");
    }

}
