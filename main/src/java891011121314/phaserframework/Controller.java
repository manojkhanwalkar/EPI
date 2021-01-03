package java891011121314.phaserframework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {


    static class Action implements Runnable
    {

        Phazer phaser;
        Validator validator;
        PII pii;

        public Action(Phazer phaser, Validator validator, PII pii) {
            this.phaser = phaser;
            this.validator = validator;
            this.pii = pii;
            phaser.register();

        }

        public void run()
        {

            validator.validate(pii);

//            phaser.arriveAndAwaitAdvance();

            phaser.arriveAndDeregister();
        }
    }


    static class Action1 implements Runnable
    {

        CDLatch latch;
        Validator validator;
        PII pii;

        public Action1(CDLatch phaser, Validator validator, PII pii) {
            this.latch = phaser;
            this.validator = validator;
            this.pii = pii;


        }

        public void run()
        {

            validator.validate(pii);
            latch.countDown();


        }
    }


    static class Action2 implements Runnable
    {

        CBarrier latch;
        Validator validator;
        PII pii;

        public Action2(CBarrier phaser, Validator validator, PII pii) {
            this.latch = phaser;
            this.validator = validator;
            this.pii = pii;


        }

        public void run()
        {

            validator.validate(pii);
            latch.await();


        }
    }




    Validator inputValidator = new InputValidator();
    Validator nameValidator = new NameValidator();
    Validator emailValidator = new EmailValidator();
    Validator ssnValidator = new SSNValidator();
    Validator telValidator = new TelephoneValidator();

    ExecutorService service = Executors.newCachedThreadPool();

    public boolean isValid(PII pii)
    {
        // phase 1

        Phazer phaser = new Phazer();
        phaser.register();

        service.submit(new Action(phaser,inputValidator,pii));

        phaser.arriveAndAwaitAdvance();

        if (pii.status) {

            service.submit(new Action(phaser, nameValidator, pii));
            service.submit(new Action(phaser, emailValidator, pii));

        }

        phaser.arriveAndAwaitAdvance();

        if (pii.status) {

            service.submit(new Action(phaser, ssnValidator, pii));
            service.submit(new Action(phaser, telValidator, pii));

        }

        phaser.arriveAndAwaitAdvance();

        phaser.arriveAndDeregister();

        return pii.status;



    }


    public boolean isValid1(PII pii)
    {
        // phase 1

        CDLatch phaser = new CDLatch(1);


        service.submit(new Action1(phaser,inputValidator,pii));

        phaser.await();
        phaser = new CDLatch(2);

        if (pii.status) {

            service.submit(new Action1(phaser, nameValidator, pii));
            service.submit(new Action1(phaser, emailValidator, pii));

        }




        phaser.await();

        phaser = new CDLatch(2);


        if (pii.status) {

            service.submit(new Action1(phaser, ssnValidator, pii));
            service.submit(new Action1(phaser, telValidator, pii));

        }

        phaser.await();

        return pii.status;



    }


    public boolean isValid2(PII pii)
    {
        // phase 1

        CBarrier phaser = new CBarrier(2);


        service.submit(new Action2(phaser,inputValidator,pii));

        phaser.await();


        phaser = new CBarrier(2);

        if (pii.status) {

            service.submit(new Action2(phaser, nameValidator, pii));
            service.submit(new Action2(phaser, emailValidator, pii));

        }




        phaser.await();

        phaser = new CBarrier(2);


        if (pii.status) {

            service.submit(new Action2(phaser, ssnValidator, pii));
            service.submit(new Action2(phaser, telValidator, pii));

        }

        phaser.await();

        return pii.status;



    }

    public static void main(String[] args) {

        Controller controller = new Controller();

        PII pii = new PII();

        pii.email="manoj@test.com";
        pii.name="Manoj";
        pii.tel="01(732)888-0000";
        pii.ssn = "909-99-8888";

       // System.out.println(controller.isValid2(pii));

        System.out.println(controller.telValidator.validate(pii));


   /*     System.out.println(controller.isValid(pii));


        System.out.println(controller.isValid1(pii));*/


    }



}
