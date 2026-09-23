package m6fgr.mapi.cls.numbers;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public abstract class AbstractSequence {

    protected static final VarHandle VALUE_HANDLER;
    protected volatile Number someNumber;
    protected final Number delta;

    protected AbstractSequence() {
        this(1, 0);
    }

    protected AbstractSequence(Number delta) {
        this(delta, 0);
    }

    protected AbstractSequence(Number delta, Number value) {
        this.delta = delta;
        this.someNumber = value;
    }

    static {
        try {
            VALUE_HANDLER = MethodHandles.lookup().findVarHandle(
                    AbstractSequence.class,
                    "someNumber",
                    Number.class
            );
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static class DoubleSequence extends AbstractSequence {
        private static final VarHandle VALUE_HANDLE;

        static {
            try {
                VALUE_HANDLE = MethodHandles.lookup().findVarHandle(
                        DoubleSequence.class,
                        "someNumber",
                        double.class
                );
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        private final double delta;
        private volatile double someNumber;

        private DoubleSequence() {
            this(1.0, 0.0);
        }

        public static DoubleSequence empty() {
            return new DoubleSequence();
        }

        public DoubleSequence(double delta) {
            this(delta, 0.0);
        }

        public DoubleSequence(double delta, double initialValue) {
            super(delta, initialValue);
            this.delta = delta;
            this.someNumber = initialValue;
        }

        public double addAndGet() {
            return (double) VALUE_HANDLE.getAndAdd(this, this.delta) + this.delta;
        }

        public double getAndAdd() {
            return (double) VALUE_HANDLE.getAndAdd(this, this.delta);
        }

        public double incrementAndGet() {
            return (double) VALUE_HANDLE.getAndAdd(this, 1.0) + 1.0;
        }

        public double getAndIncrement() {
            return (double) VALUE_HANDLE.getAndAdd(this, 1.0);
        }

        public double get() {
            return this.someNumber;
        }

        public void set(double newValue) {
            this.someNumber = newValue;
        }
    }

    public static class FloatSequence extends AbstractSequence {
        private static final VarHandle VALUE_HANDLE;

        static {
            try {
                VALUE_HANDLE = MethodHandles.lookup().findVarHandle(
                        FloatSequence.class,
                        "someNumber",
                        float.class
                );
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        private final float delta;
        private volatile float someNumber;

        private FloatSequence() {
            this(1.0f, 0.0f);
        }

        public static FloatSequence empty() {
            return new FloatSequence();
        }

        public FloatSequence(float delta) {
            this(delta, 0.0f);
        }

        public FloatSequence(float delta, float initialValue) {
            super(delta, initialValue);
            this.delta = delta;
            this.someNumber = initialValue;
        }

        public float addAndGet() {
            return (float) VALUE_HANDLE.getAndAdd(this, this.delta) + this.delta;
        }

        public float getAndAdd() {
            return (float) VALUE_HANDLE.getAndAdd(this, this.delta);
        }

        public float incrementAndGet() {
            return (float) VALUE_HANDLE.getAndAdd(this, 1.0f) + 1.0f;
        }

        public float getAndIncrement() {
            return (float) VALUE_HANDLE.getAndAdd(this, 1.0f);
        }

        public float get() {
            return this.someNumber;
        }

        public void set(float newValue) {
            this.someNumber = newValue;
        }
    }

    public static class IntegerSequence extends AbstractSequence {
        private static final VarHandle VALUE_HANDLE;

        static {
            try {
                VALUE_HANDLE = MethodHandles.lookup().findVarHandle(
                        IntegerSequence.class,
                        "someNumber",
                        int.class
                );
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        private final int delta;
        private volatile int someNumber;

        private IntegerSequence() {
            this(1, 0);
        }

        public static IntegerSequence empty() {
            return new IntegerSequence();
        }

        public IntegerSequence(int delta) {
            this(delta, 0);
        }

        public IntegerSequence(int delta, int initialValue) {
            super(delta, initialValue);
            this.delta = delta;
            this.someNumber = initialValue;
        }

        public int addAndGet() {
            return (int) VALUE_HANDLE.getAndAdd(this, this.delta) + this.delta;
        }

        public int addAndGet(int val) {
            return (int) VALUE_HANDLE.getAndAdd(this, val) + val;
        }

        public int getAndAdd() {
            return (int) VALUE_HANDLE.getAndAdd(this, this.delta);
        }

        public int incrementAndGet() {
            return (int) VALUE_HANDLE.getAndAdd(this, 1) + 1;
        }

        public int getAndIncrement() {
            return (int) VALUE_HANDLE.getAndAdd(this, 1);
        }

        public int get() {
            return this.someNumber;
        }

        public void set(int newValue) {
            this.someNumber = newValue;
        }
    }

    public static class ShortSequence extends AbstractSequence {
        private static final VarHandle VALUE_HANDLE;

        static {
            try {
                VALUE_HANDLE = MethodHandles.lookup().findVarHandle(
                        ShortSequence.class,
                        "someNumber",
                        short.class
                );
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        private final short delta;
        private volatile short someNumber;

        private ShortSequence() {
            this((short) 1, (short) 0);
        }

        public static ShortSequence empty() {
            return new ShortSequence();
        }

        public ShortSequence(short delta) {
            this(delta, (short) 0);
        }

        public ShortSequence(short delta, short initialValue) {
            super(delta, initialValue);
            this.delta = delta;
            this.someNumber = initialValue;
        }

        public short addAndGet() {
            return (short) (getAndAdd(this.delta) + this.delta);
        }

        public short getAndAdd(short delta) {
            short prev, next;
            do {
                prev = (short) VALUE_HANDLE.getVolatile(this);
                next = (short) (prev + delta);
            } while (!VALUE_HANDLE.compareAndSet(this, prev, next));
            return prev;
        }

        public short getAndAdd() {
            return getAndAdd(this.delta);
        }

        public short incrementAndGet() {
            return (short) (getAndAdd((short) 1) + 1);
        }

        public short getAndIncrement() {
            return getAndAdd((short) 1);
        }

        public short get() {
            return this.someNumber;
        }

        public void set(short newValue) {
            this.someNumber = newValue;
        }
    }

    public static class ByteSequence extends AbstractSequence {
        private static final VarHandle VALUE_HANDLE;

        static {
            try {
                VALUE_HANDLE = MethodHandles.lookup().findVarHandle(
                        ByteSequence.class,
                        "someNumber",
                        byte.class
                );
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        private final byte delta;
        private volatile byte someNumber;

        private ByteSequence() {
            this((byte) 1, (byte) 0);
        }

        public static ByteSequence empty() {
            return new ByteSequence();
        }

        public ByteSequence(byte delta) {
            this(delta, (byte) 0);
        }

        public ByteSequence(byte delta, byte initialValue) {
            super(delta, initialValue);
            this.delta = delta;
            this.someNumber = initialValue;
        }

        public byte addAndGet() {
            return (byte) (getAndAdd(this.delta) + this.delta);
        }

        public byte getAndAdd(byte delta) {
            byte prev, next;
            do {
                prev = (byte) VALUE_HANDLE.getVolatile(this);
                next = (byte) (prev + delta);
            } while (!VALUE_HANDLE.compareAndSet(this, prev, next));
            return prev;
        }

        public byte getAndAdd() {
            return getAndAdd(this.delta);
        }

        public byte incrementAndGet() {
            return (byte) (getAndAdd((byte) 1) + 1);
        }

        public byte getAndIncrement() {
            return getAndAdd((byte) 1);
        }

        public byte get() {
            return this.someNumber;
        }

        public void set(byte newValue) {
            this.someNumber = newValue;
        }
    }
}