package rfs;

import java891011121314.JSONUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DFS {


    static final String dfsNodeFile = "/home/manoj/data/rfs/dfsNodeFile";

    List<FileSystem> fileSystems = new ArrayList<>();

    ReadWriteLock lock = new ReentrantReadWriteLock();

    Lock readLock ; Lock writeLock;

    ConcurrentMap<String,DFSNode> inodeMap = new ConcurrentHashMap<>();

    public void register(FileSystem fileSystem)
    {

        fileSystems.add(fileSystem);
    }


    //TODO - recover file systems first , then register them and then recover DFS metadata

    public DFS()
    {


        writeLock = lock.writeLock();
        readLock = lock.readLock();

        recoverINodes();

        createDir("/");     // add a root if it does not exist .

    }

    public boolean createDir(String dirName)
    {
        if (!dirName.startsWith("/"))
            return false;

        if (!dirName.endsWith("/"))
            return false;

        if (inodeMap.containsKey(dirName))
            return false;


        DFSNode inode = new DFSNode();
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

        // create dirs in the file systems
        fileSystems.forEach(fileSystem -> {

            fileSystem.createDir(dirName);
        });

        return true;

    }

    public void printDirs()
    {
        readLock.lock();
        DFSNode root = inodeMap.get("/");

        Queue<DFSNode> dirs = new ArrayDeque<>();
        dirs.add(root);

        while(!dirs.isEmpty())
        {
            DFSNode curr =  dirs.remove();
            System.out.println(curr.getFqpn());
            if (curr.getNodes()!=null)
            {
                dirs.addAll(curr.getNodes().stream().filter(node->node.isDir()).collect(Collectors.toList()));
            }
        }

        readLock.unlock();
    }


    int fileNodeCounter = 0;   // to be set to last file node on recovering the nodes on restart.




    private FileSystem resolve(String fileName)
    {
        return fileSystems.stream().filter(fs->fs.fsName.equals(fileName)).findAny().get();
    }



    private void recoverINodes()
    {

        //TODO - calculate data block location for next write .
        // TODO - calculate max INode for next writes
        Map<String, DFSNode.SerializedDFSNode> serInodeMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dfsNodeFile)))
        {
            String line;
            while((line=reader.readLine())!=null)
            {
                DFSNode.SerializedDFSNode serializedINode = (DFSNode.SerializedDFSNode) JSONUtil.fromJSON(line, DFSNode.SerializedDFSNode.class);
                DFSNode inode = new DFSNode();
                inode.fqpn = serializedINode.fqpn;
                inode.dataBlockMetas = serializedINode.dataBlockMeta;
                inode.dirOrFile= serializedINode.dirOrFile;
                inodeMap.put(inode.fqpn,inode);


                serInodeMap.put(serializedINode.fqpn,serializedINode);
                if (NumberUtils.isCreatable(inode.fqpn))
                    fileNodeCounter = Math.max(fileNodeCounter,Integer.valueOf(inode.fqpn));

          /*      if (inode.dataBlockMetas!=null) {

                    DFSDataBlockMeta meta = inode.dataBlockMetas.get(inode.dataBlockMetas.size()-1);

                    meta.fileSystem = resolve(meta.fileSystemName);

                }*/


            }
        } catch (Exception ex) {
            ex.printStackTrace();}


        inodeMap.values().stream().forEach(inode->{

            DFSNode.SerializedDFSNode serializedINode = serInodeMap.get(inode.fqpn);

           // inode.next = inodeMap.get(serializedINode.nextNodeName);

            if (serializedINode.getNodeNames()!=null) {
                List<DFSNode> nodes = serializedINode.getNodeNames().stream().map(name -> {
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
                    DFSNode inode = new DFSNode();
                    inode.setDirOrFile(false);
                    inode.setFqpn(fqpn);
                    inodeMap.put(fqpn,inode);
                    inodeMap.get(dir).addNode(inode);
                    fileSystems.stream().forEach(fileSystem -> {
                        fileSystem.createFile(fqpn);

                });

                    return true;
                }
            }
        }

        return false;

    }





    public void printFiles()
    {
        readLock.lock();
        DFSNode root = inodeMap.get("/");

        Queue<DFSNode> dirs = new ArrayDeque<>();
        dirs.add(root);

        while(!dirs.isEmpty())
        {
            DFSNode curr =  dirs.remove();
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
        DFSNode node = null;
        try {
            DFSNode fileNode = inodeMap.remove(fileName);
            if (fileNode != null) {
                dir = getDirFromFileName(fileName);
                node = inodeMap.get(dir);
                node.nodes.remove(fileNode);

                fileSystems.forEach(fileSystem -> {fileSystem.deleteFile(fileName); });
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
            DFSNode curr = inodeMap.get(dirName);
            if (curr != null) {
                if (curr.isFile()) {
                    deleteFile(curr.fqpn);
                    return;
                } else {
                    if (curr.getNodes() != null) {
                        List<DFSNode> nodes = List.copyOf(curr.getNodes());
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

            fileSystems.forEach(fs->fs.deleteDirAndUnderlying(dirName));

        } finally {

            writeLock.unlock();

        }
    }

    private DFSNode getFileNode()
    {
        DFSNode node = new DFSNode();
        node.setDirOrFile(false);
        node.setFqpn(String.valueOf(fileNodeCounter++));

        return node;

    }

    private boolean isValidFileName(String fileName)
    {
        DFSNode start = inodeMap.get(fileName);
        if (start==null || start.isDir())
        {
            return false;
        }

        return true;
    }


    public boolean write(String fileName, List<String> lines )
    {

        try {

            writeLock.lock();

            if (!isValidFileName(fileName))
                return false;


          //  List<DataBlock> blocks = DataBlock.getBlocks(lines);
            DFSNode curr = inodeMap.get(fileName);

            curr.dataBlockMetas = null;

         /*   if (mode == WriteMode.OverWrite)
                curr.dataBlockMetas = null;*/

         List<List<String>> linesByFileSystem = new ArrayList<>(fileSystems.size());

         int count = lines.size()/fileSystems.size();
         int index = 0;
         for (int i=0;i<fileSystems.size();i++)
         {
             List<String> currList = new ArrayList<>();
             for (int j=0;j<count;j++)
             {
                 currList.add(lines.get(index++));
             }

             linesByFileSystem.add(currList);
         }


         for (int i=0;i<fileSystems.size();i++)
         {
             DFSDataBlockMeta meta = new DFSDataBlockMeta();
             //meta.setBlockStart(count);
             //meta.setBlockEnd(linesByFileSystem.get(i).size());

             meta.setFileSystem(fileSystems.get(i));
             meta.setFileSystemName(fileSystems.get(i).fsName);

             curr.addDataBlockMeta(meta);

             fileSystems.get(i).write(fileName,linesByFileSystem.get(i), FileSystem.WriteMode.OverWrite);

         }


            return true;

        } finally {
            writeLock.unlock();
        }


    }







    public List<String> read(String fileName)
    {

        readLock.lock();
        List<String> contents = new ArrayList<>();


            DFSNode iNode = inodeMap.get(fileName);



            if (iNode != null && iNode.dataBlockMetas != null) {
                iNode.dataBlockMetas.stream().forEach(meta -> {

                    meta.fileSystem = resolve(meta.fileSystemName);


                    contents.addAll(meta.fileSystem.read(fileName));

             });
            }



          readLock.unlock();


        return contents;
    }







}
