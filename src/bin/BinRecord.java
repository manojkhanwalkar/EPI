package bin;

import java.util.Arrays;

public class BinRecord {

    String bin;
    String brand;
    String issuingOrg;
    String type;
    String category;
    String issuingCountryName;
    String issuingCountryCode2;
    String issuingCountryCode3;
    String issuingCountryNumber;
    String website;
    String phone;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIssuingOrg() {
        return issuingOrg;
    }

    public void setIssuingOrg(String issuingOrg) {
        this.issuingOrg = issuingOrg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIssuingCountryName() {
        return issuingCountryName;
    }

    public void setIssuingCountryName(String issuingCountryName) {
        this.issuingCountryName = issuingCountryName;
    }

    public String getIssuingCountryCode2() {
        return issuingCountryCode2;
    }

    public void setIssuingCountryCode2(String issuingCountryCode2) {
        this.issuingCountryCode2 = issuingCountryCode2;
    }

    public String getIssuingCountryCode3() {
        return issuingCountryCode3;
    }

    public void setIssuingCountryCode3(String issuingCountryCode3) {
        this.issuingCountryCode3 = issuingCountryCode3;
    }

    public String getIssuingCountryNumber() {
        return issuingCountryNumber;
    }

    public void setIssuingCountryNumber(String issuingCountryNumber) {
        this.issuingCountryNumber = issuingCountryNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static BinRecord create(String[] arr)
    {
        if (arr.length<11)
        {
          //  String[] fields = new String[11];
            arr = Arrays.copyOf(arr,11);
        }
        BinRecord binRecord = new BinRecord();
        binRecord.bin = arr[0];
        binRecord.brand = arr[1];
        binRecord.issuingOrg=arr[2];
        binRecord.type=arr[3];
        binRecord.category=arr[4];
        binRecord.issuingCountryName=arr[5];
        binRecord.issuingCountryCode2=arr[6];
        binRecord.issuingCountryCode3=arr[7];
        binRecord.issuingCountryNumber=arr[8];
        binRecord.website=arr[9];
        binRecord.phone=arr[10];

        return  binRecord;

    }

    @Override
    public String toString() {
        return "BinRecord{" +
                "bin='" + bin + '\'' +
                ", brand='" + brand + '\'' +
                ", issuingOrg='" + issuingOrg + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", issuingCountryName='" + issuingCountryName + '\'' +
                ", issuingCountryCode2='" + issuingCountryCode2 + '\'' +
                ", issuingCountryCode3='" + issuingCountryCode3 + '\'' +
                ", issuingCountryNumber='" + issuingCountryNumber + '\'' +
                ", website='" + website + '\'' +
                ", phone='" + phone + '\'' +
                '}' + '\n';
    }


    /*

    1. "407212" - BIN,



2. "VISA" - Card Brand,



3. "SUNTRUST BANK" - Issuing Organization,



4. "DEBIT" - Type of Card (DEBIT, CREDIT, or CHARGE CARD),



5. "PREPAID" - Category of Card,



6. "UNITED STATES" - issuing country ISO name,



7. "US" - issuing country ISO A2 code,



8. "USA" - issuing country ISO A3 code,



9. "840" - issuing country ISO number,



10. "HTTP://WWW.SUNTRUST.COM/" - issuing organization website,



11. "352-378-2125" - issuing organization phone number.


     */
}
