package r4j.bulkhead;

public final class BulkheadConfig {

    int concurrent;
    int timeout;

    private BulkheadConfig(int concurrent, int timeout) {

        this.concurrent = concurrent;

        this.timeout = timeout;
    }


    public int getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static ConfigBuilder custom()
    {
        return new ConfigBuilder();
    }


    public static class ConfigBuilder
    {
        int concurrent;
        int timeout;

        public ConfigBuilder()
        {

        }

        public ConfigBuilder concurrent(int concurrent)
        {

            this.concurrent = concurrent;
            return this;
        }


        public ConfigBuilder timeout(int timeout)
        {
            this.timeout=timeout;
            return this;
        }

        public BulkheadConfig build()
        {
            return new BulkheadConfig(concurrent,timeout);
        }
    }
}
