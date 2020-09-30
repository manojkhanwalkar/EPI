package rfs;

import java891011121314.JSONUtil;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class FileSystem {

     String directory = "/home/manoj/data/rfs/";

    static final String dataFile = "dataFile";

    static final String inodeFile = "inodeFile";

    ConcurrentMap<String,INode> inodeMap = new ConcurrentHashMap<>();

    RandomAccessFile dataFileReadWrite ;

    int fileNodeCounter = 0;   // to be set to last file node on recovering the nodes on restart.

    long nextWriteLocation=0;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    Lock readLock ; Lock writeLock;


    String fsName;


    public FileSystem(String fsName)
    {
        directory = directory+fsName+"/";

        this.fsName = fsName;

        writeLock = lock.writeLock();
        readLock = lock.readLock();
        try {
            dataFileReadWrite = new RandomAccessFile(directory+dataFile,"rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        recoverINodes();

        createDir("/");     // add a root if it does not exist .

    }

    private void recoverINodes()
    {

        //TODO - calculate data block location for next write .
        // TODO - calculate max INode for next writes
        Map<String, INode.SerializedINode> serInodeMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(directory+inodeFile)))
        {
            String line;
            while((line=reader.readLine())!=null)
            {
                INode.SerializedINode serializedINode = (INode.SerializedINode) JSONUtil.fromJSON(line, INode.SerializedINode.class);
                INode inode = new INode();
                inode.fqpn = serializedINode.fqpn;
                inode.dataBlockMetas = serializedINode.dataBlockMeta;
                inode.dirOrFile= serializedINode.dirOrFile;
                inodeMap.put(inode.fqpn,inode);


                serInodeMap.put(serializedINode.fqpn,serializedINode);
                if (NumberUtils.isCreatable(inode.fqpn))
                    fileNodeCounter = Math.max(fileNodeCounter,Integer.valueOf(inode.fqpn));

                if (inode.dataBlockMetas!=null) {

                    DataBlockMeta meta = inode.dataBlockMetas.get(inode.dataBlockMetas.size()-1);
                    nextWriteLocation = Math.max(nextWriteLocation, meta.dataBlockLocation+meta.lengthDataBlock);
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();}


        inodeMap.values().stream().forEach(inode->{

            INode.SerializedINode serializedINode = serInodeMap.get(inode.fqpn);

           // inode.next = inodeMap.get(serializedINode.nextNodeName);

            if (serializedINode.getNodeNames()!=null) {
                List<INode> nodes = serializedINode.getNodeNames().stream().map(name -> {
                    return inodeMap.get(name) ;
                }).collect(Collectors.toList());

                inode.setNodes(nodes);
            }
        });

    }



    public boolean createFile(String fqpn)
    {
        if (inodeMap.containsKey(fqpn))  // file already created.
        {
            return false;
        }
        // locate directory
        for (int i=fqpn.length()-1;i>=0;i--) {
            if (fqpn.charAt(i) == '/') {
                String dir = fqpn.substring(0, i + 1);
                //String file = fqpn.substring(i+1);
                if (inodeMap.containsKey(dir))
                {
                    INode inode = new INode();
                    inode.setDirOrFile(false);
                    inode.setFqpn(fqpn);
                    inodeMap.put(fqpn,inode);
                    inodeMap.get(dir).addNode(inode);
                    return true;
                }
            }
        }

        return false;

    }


    public boolean createDir(String dirName)
    {
        if (!dirName.startsWith("/"))
            return false;

        if (!dirName.endsWith("/"))
            return false;

        if (inodeMap.containsKey(dirName))
            return false;


        INode inode = new INode();
        inode.setDirOrFile(true);
        inode.setFqpn(dirName);

        if (dirName.length()==1)
        {
            inodeMap.put(dirName,inode);
            return true;
        }
        // search backwards to insert in the right inode as child
        for (int i=dirName.length()-1;i>=0;i--)
        {
            if (dirName.charAt(i)=='/')
            {
                String s = dirName.substring(0,i+1);
                if(inodeMap.containsKey(s))
                {
                    inodeMap.get(s).addNode(inode);
                    inodeMap.put(dirName,inode);
                    break;
                }
            }
        }

        return true;

    }

    public void printDirs()
    {
        readLock.lock();
        INode root = inodeMap.get("/");

        Queue<INode> dirs = new ArrayDeque<>();
        dirs.add(root);

        while(!dirs.isEmpty())
        {
            INode curr =  dirs.remove();
            System.out.println(curr.getFqpn());
            if (curr.getNodes()!=null)
            {
                dirs.addAll(curr.getNodes().stream().filter(node->node.isDir()).collect(Collectors.toList()));
            }
        }

        readLock.unlock();
    }

    public void printFiles()
    {
        readLock.lock();
        INode root = inodeMap.get("/");

        Queue<INode> dirs = new ArrayDeque<>();
        dirs.add(root);

        while(!dirs.isEmpty())
        {
            INode curr =  dirs.remove();
            if (curr.isFile()) {
                System.out.println(curr.getFqpn());
                if (curr.dataBlockMetas!=null)
                {
                    System.out.println("Number of data blocks is " + curr.dataBlockMetas.size());
                }

            }
            else {
                if (curr.getNodes() != null) {
                    dirs.addAll(curr.getNodes());
                }
            }
        }

        readLock.unlock();
    }



    private INode getFileNode()
    {
        INode node = new INode();
        node.setDirOrFile(false);
        node.setFqpn(String.valueOf(fileNodeCounter++));

        return node;

    }

    private boolean isValidFileName(String fileName)
    {
        INode start = inodeMap.get(fileName);
        if (start==null || start.isDir())
        {
            return false;
        }

        return true;
    }

    public enum WriteMode { OverWrite, Append} ;

   public boolean write(String fileName, List<String> lines, WriteMode mode )
    {

        try {

            writeLock.lock();

            if (!isValidFileName(fileName))
                return false;


            List<DataBlock> blocks = DataBlock.getBlocks(lines);
            INode curr = inodeMap.get(fileName);

            if (mode == WriteMode.OverWrite)
                curr.dataBlockMetas = null;


            for (DataBlock block : blocks) {
                // write data block to data file . TODO


                //   dataFileReadWrite.

                DataBlockMeta meta = new DataBlockMeta();
                meta.dataBlockLocation = nextWriteLocation;
                curr.addDataBlockMeta(meta);
                try {
                    dataFileReadWrite.seek(nextWriteLocation);
                    byte[] b = block.content.getBytes(StandardCharsets.US_ASCII);
                    dataFileReadWrite.write(b);
                    meta.lengthDataBlock = b.length;
                    nextWriteLocation = nextWriteLocation + b.length;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return true;

        } finally {
            writeLock.unlock();
        }


    }


    public static RFSFileReader getReader(String fileName, FileSystem fileSystem)
    {
        return new RFSFileReader(fileName,fileSystem);
    }

    public static RFSFileWriter getWriter(String fileName, FileSystem fileSystem)
    {
        return new RFSFileWriter(fileName,fileSystem);
    }

    public List<String> read(String fileName)
    {

        readLock.lock();
        List<String> contents = new ArrayList<>();

        try(RandomAccessFile reader = new RandomAccessFile(directory+dataFile,"r")) {

            // find file and get meta info
            // seek to each start and read upto length

            INode iNode = inodeMap.get(fileName);



            if (iNode != null && iNode.dataBlockMetas != null) {
                iNode.dataBlockMetas.stream().forEach(meta -> {

                    try {
                        reader.seek(meta.dataBlockLocation);
                        byte[] b = new byte[meta.lengthDataBlock];
                        reader.read(b);

                        String s = new String(b, StandardCharsets.US_ASCII);
                        contents.add(s);

                       // Thread.sleep(100);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }


        } catch (IOException ex) {ex.printStackTrace();}

        readLock.unlock();


        return contents;
    }

    private String getDirFromFileName(String fileName)
    {
        int index = 0;
        for (int i=fileName.length()-2;i>=0;i--)
        {
                if (fileName.charAt(i)=='/')
                {
                    index = i;
                    break;
                }
        }

        return fileName.substring(0,index+1);
    }

   public boolean deleteFile(String fileName)
    {
        writeLock.lock();
        String dir = null;
        INode node = null;
        try {
            INode fileNode = inodeMap.remove(fileName);
            if (fileNode != null) {
                dir = getDirFromFileName(fileName);
                 node = inodeMap.get(dir);
                node.nodes.remove(fileNode);
                return true;
            } else
                return false;

        } catch (Exception ex)
        {
            System.out.println(fileName + " " + dir + " " + node);
            throw ex;
        } finally {

            writeLock.unlock();

        }
    }




    public void deleteDirAndUnderlying(String dirName)
    {

        writeLock.lock();
        try {
            INode curr = inodeMap.get(dirName);
            if (curr != null) {
                if (curr.isFile()) {
                    deleteFile(curr.fqpn);
                    return;
                } else {
                    if (curr.getNodes() != null) {
                        List<INode> nodes = List.copyOf(curr.getNodes());
                        for (int i = 0; i < nodes.size(); i++) // delete files first
                        {
                            if (nodes.get(i).isFile())
                                deleteDirAndUnderlying(nodes.get(i).fqpn);
                        }

                        nodes = List.copyOf(curr.getNodes());
                        for (int i = 0; i < nodes.size(); i++) // then delete directories
                        {
                            deleteDirAndUnderlying(nodes.get(i).fqpn);
                        }
                    }
                }

                deleteFile(dirName);
            }

        } finally {

            writeLock.unlock();

        }
    }


    //@NotThreadSafe
    public void compress() throws Exception
    {


        String tmpDataFile = dataFile+".tmp";

        RandomAccessFile tmpdataFileReadWrite = new RandomAccessFile(directory+tmpDataFile,"rw");


        ConcurrentMap<String,INode> tmpInodeMap = new ConcurrentHashMap<>();

        inodeMap.entrySet().stream().forEach(entry->{

            try {
                INode node = entry.getValue();
                if (node.isDir())
                {
                    tmpInodeMap.put(entry.getKey(),entry.getValue());
                }
                else
                {
                    // copy data blocks from old data file to new data file
                    // adjust start location
                    if (node.dataBlockMetas!=null)
                    {
                        List<DataBlockMeta> dataBlockMetas = node.dataBlockMetas;
                        for (int i=0;i<dataBlockMetas.size();i++)
                        {
                            DataBlockMeta meta = dataBlockMetas.get(i);
                            dataFileReadWrite.seek(meta.dataBlockLocation);
                            byte[] b = new byte[meta.lengthDataBlock];
                            dataFileReadWrite.read(b);
                            meta.setDataBlockLocation(tmpdataFileReadWrite.getFilePointer());
                            tmpdataFileReadWrite.write(b);
                        }
                    }
                    tmpInodeMap.put(entry.getKey(), node);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        inodeMap = tmpInodeMap;

        nextWriteLocation = tmpdataFileReadWrite.getFilePointer();

        tmpdataFileReadWrite.close();

        // copy data file
        dataFileReadWrite.close();

        File file = new File(directory+dataFile);
        file.delete();

        File file1 = new File(directory+tmpDataFile);
        file1.renameTo(file);

        dataFileReadWrite = new RandomAccessFile(directory+dataFile,"rw");


    }




}
