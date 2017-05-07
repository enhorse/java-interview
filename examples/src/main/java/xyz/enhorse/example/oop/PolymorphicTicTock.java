package xyz.enhorse.example.oop;

/**
 * @author <a href="mailto:pavel13kalinin@gmail.com">Pavel Kalinin</a>
 *         21.10.2016
 */
public class PolymorphicTicTock {

    public static void main(String[] args) {
        final int times = 10;

        Commander commander = new Commander(TickTock.TICK);
        commander.run(times);
    }


    private enum TickTock {
        TICK("tick") {
            @Override
            TickTock opposite() {
                return TOCK;
            }
        },
        TOCK("tock") {
            @Override
            TickTock opposite() {
                return TICK;
            }
        };

        private String content;


        TickTock(final String content) {
            this.content = content;
        }


        void say() {
            System.out.println(content);
        }


        abstract TickTock opposite();
    }

    private static class Commander {

        private TickTock current;


        Commander(final TickTock current) {
            this.current = current;
        }


        void once() {
            current.say();
            current = current.opposite();
        }


        void run(final int times) {
            for (int i = 0; i < (times * 2); i++) {
                once();
            }
        }
    }
}
