package bin;

public class BinRecordUtil {

    public static enum CardType { Credit , Debit , Charge, Unknown};

    public static CardType getType(BinRecord binRecord)
    {
        String str = binRecord.getType();
        switch (str)
        {
            case "DEBIT" :
                return  CardType.Debit;
            case "CREDIT" :
                return CardType.Credit;
            case "CHARGE CARD" :
                return CardType.Charge;
            default :
                return CardType.Unknown;

        }
    }



}
