package memmanager;

public class TLBEntry {


    int memPageNumber = -1;

    int swapPageNumber = -1;

    public int getMemPageNumber() {
        return memPageNumber;
    }

    public void setMemPageNumber(int memPageNumber) {
        this.memPageNumber = memPageNumber;
    }

    public int getSwapPageNumber() {
        return swapPageNumber;
    }

    public void setSwapPageNumber(int swapPageNumber) {
        this.swapPageNumber = swapPageNumber;
    }
}
