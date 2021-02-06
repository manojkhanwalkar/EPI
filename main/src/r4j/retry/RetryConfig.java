package r4j.retry;

public final class RetryConfig {

    int waitTime;
    int numRetries;
    int backoff;


    public RetryConfig(int waitTime, int numRetries, int backoff) {
        this.waitTime = waitTime;
        this.numRetries = numRetries;
        this.backoff = backoff;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getNumRetries() {
        return numRetries;
    }

    public void setNumRetries(int numRetries) {
        this.numRetries = numRetries;
    }

    public int getBackoff() {
        return backoff;
    }

    public void setBackoff(int backoff) {
        this.backoff = backoff;
    }

    public static ConfigBuilder custom()
    {
        return new ConfigBuilder();
    }


    public static class ConfigBuilder
    {
        int waitTime;
        int numRetries;
        int backoff;


        public ConfigBuilder waitTime(int waitTime)
        {

            this.waitTime = waitTime;
            return this;
        }

        public ConfigBuilder numRetries(int numRetries)
        {
            this.numRetries = numRetries;
            return this;
        }

        public ConfigBuilder backoff(int backoff)
        {
            this.backoff=backoff;
            return this;
        }

        public RetryConfig build()
        {
            return new RetryConfig(waitTime, numRetries,backoff);
        }
    }
}
