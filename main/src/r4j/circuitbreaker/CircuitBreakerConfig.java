package r4j.circuitbreaker;

public final class CircuitBreakerConfig {

    int waitDuration;
    int percentageError;
    int numberOfEvents;

    public CircuitBreakerConfig(int waitDuration, int percentageError, int numberOfEvents) {
        this.waitDuration = waitDuration;
        this.percentageError = percentageError;
        this.numberOfEvents = numberOfEvents;
    }


    public int getWaitDuration() {
        return waitDuration;
    }

    public void setWaitDuration(int waitDuration) {
        this.waitDuration = waitDuration;
    }

    public int getPercentageError() {
        return percentageError;
    }

    public void setPercentageError(int percentageError) {
        this.percentageError = percentageError;
    }

    public int getNumberOfEvents() {
        return numberOfEvents;
    }

    public void setNumberOfEvents(int numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }

    public static ConfigBuilder custom()
    {
        return new ConfigBuilder();
    }


    public static class ConfigBuilder
    {
        int waitDuration;
        int percentageError;
        int numberOfEvents;

        public ConfigBuilder()
        {

        }

        public ConfigBuilder waitDuration(int waitDuration)
        {

            this.waitDuration = waitDuration;
            return this;
        }

        public ConfigBuilder percentageError(int percentageError)
        {
            this.percentageError = percentageError;
            return this;
        }

        public ConfigBuilder numberOfEvents(int numberOfEvents)
        {
            this.numberOfEvents=numberOfEvents;
            return this;
        }

        public CircuitBreakerConfig build()
        {
            return new CircuitBreakerConfig(waitDuration,percentageError,numberOfEvents);
        }
    }
}
