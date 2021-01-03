package ej;

import java.util.HashMap;
import java.util.Map;

public class BuilderPattern {

    static abstract class Identity {

        Map<String, String> attributes = new HashMap<>();

        Identity(Builder<?> builder)
        {
            attributes.putAll(builder.attributes);
        }

        static abstract class Builder<T extends Builder<T>> {
            Map<String, String> attributes = new HashMap<>();

            public T addAttribute(String name, String value)
            {
                attributes.put(name,value);
                return self();
            }
            abstract Identity build();

            protected abstract T self();


        }


    }


    static class PII extends Identity{

        static class Builder extends Identity.Builder<Builder>
        {

            public Builder()
            {

            }
            @Override
            protected Builder self() {
                return this;
            }

            @Override
            PII build() {
                return new PII(this);
            }
        }

        private PII(Builder builder)
        {
            super(builder);
        }
    }


    static class Device extends Identity{

        DeviceType deviceType;

        static class Builder extends Identity.Builder<Builder>
        {

            DeviceType deviceType;
            public Builder(DeviceType deviceType)
            {
                this.deviceType = deviceType;
            }
            @Override
            protected Builder self() {
                return this;
            }

            @Override
            Device build() {
                Device device= new Device(this);
                device.deviceType = deviceType;

                return device;
            }
        }

        private Device(Builder builder)
        {
            super(builder);
        }
    }


    static interface DeviceType
    {

    }

    static class Phone implements DeviceType
    {

    }

    static class Laptop implements DeviceType
    {

    }






    public static void main(String[] args) {

        {
            var builder = new PII.Builder();

            builder.addAttribute("Name", "AAA");

            var identity = builder.build();

            System.out.println(identity);
        }

        {
            var builder = new Device.Builder(new Laptop());

            builder.addAttribute("OS" , "Mac");

            var identity = builder.build();

            System.out.println(identity);
        }


    }
}
