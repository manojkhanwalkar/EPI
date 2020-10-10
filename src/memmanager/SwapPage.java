package memmanager;

public class SwapPage {

    char[] buf = new char[Page.size];

    final int pageNum;

    public SwapPage(int num)
    {
        this.pageNum = num;
    }

}
