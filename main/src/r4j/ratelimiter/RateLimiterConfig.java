package r4j.ratelimiter;

public final class RateLimiterConfig {

    int duration;
    int permits;
    int timeout;

    private RateLimiterConfig(int duration, int permits, int timeout) {

        this.duration = duration;
        this.permits = permits;
        this.timeout = timeout;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPermits() {
        return permits;
    }

    public void setPermits(int permits) {
        this.permits = permits;
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
        int duration;
        int permits;
        int timeout;

        public ConfigBuilder()
        {

        }

        public ConfigBuilder duration(int duration)
        {

            this.duration = duration;
            return this;
        }

        public ConfigBuilder permits(int permits)
        {
            this.permits = permits;
            return this;
        }

        public ConfigBuilder timeout(int timeout)
        {
            this.timeout=timeout;
            return this;
        }

        public RateLimiterConfig build()
        {
            return new RateLimiterConfig(duration, permits,timeout);
        }
    }
}
