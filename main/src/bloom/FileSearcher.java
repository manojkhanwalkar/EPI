package bloom;

public class FileSearcher {


    public static void main(String[] args) {


        FileDB db = new FileDB();

        db.add("/home/manoj/data/words.txt");

        //System.out.println(db.contains("knnknjvhvhvhvhvhvhvknn"));

        System.out.println(db.search("knnknjvhvhvhvhvhvhvknn"));

    }

}
