package aero;



import org.apache.commons.pool2.BasePooledObjectFactory;
        import org.apache.commons.pool2.PooledObject;
        import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ReaderFactory  extends BasePooledObjectFactory<DataRecordReader> {

    @Override
    public DataRecordReader create() {
        return new DataRecordReader();
    }

    /**
     * Use the default PooledObject implementation.
     */
    @Override
    public PooledObject<DataRecordReader> wrap(DataRecordReader buffer) {
        return new DefaultPooledObject<DataRecordReader>(buffer);
    }

    /**
     * When an object is returned to the pool, clear the buffer.
     */
 /*   @Override
    public void passivateObject(PooledObject<DataRecordReader> pooledObject) {
       // pooledObject.getObject().setLength(0);
    }*/

    // for all other methods, the no-op implementation
    // in BasePooledObjectFactory will suffice
}
