package m6fgr.mapi.cls.logging;

import java.util.function.Supplier;

public interface Logger {

    // ==========================================
    // TRACE LEVEL
    // ==========================================

    void trace(String message, Object... params);

    void trace(String message, Throwable throwable);

    void trace(Supplier<?> messageSupplier);

    void trace(Supplier<?> messageSupplier, Throwable throwable);

    void trace(String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // DEBUG LEVEL
    // ==========================================

    void debug(String message, Object... params);

    void debug(String message, Throwable throwable);

    void debug(Supplier<?> messageSupplier);

    void debug(Supplier<?> messageSupplier, Throwable throwable);

    void debug(String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // INFO LEVEL
    // ==========================================

    void info(String message, Object... params);

    void info(String message, Throwable throwable);

    void info(Supplier<?> messageSupplier);

    void info(Supplier<?> messageSupplier, Throwable throwable);

    void info(String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // WARN LEVEL
    // ==========================================

    void warn(String message, Object... params);

    void warn(String message, Throwable throwable);

    void warn(Supplier<?> messageSupplier);

    void warn(Supplier<?> messageSupplier, Throwable throwable);

    void warn(String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // ERROR LEVEL
    // ==========================================

    void error(String message, Object... params);

    void error(String message, Throwable throwable);

    void error(Supplier<?> messageSupplier);

    void error(Supplier<?> messageSupplier, Throwable throwable);

    void error(String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // FATAL LEVEL
    // ==========================================

    void fatal(String message, Object... params);

    void fatal(String message, Throwable throwable);

    void fatal(Supplier<?> messageSupplier);

    void fatal(Supplier<?> messageSupplier, Throwable throwable);

    void fatal(String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // DYNAMIC LEVEL LOGGING
    // ==========================================

    void log(Level level, String message, Object... params);

    void log(Level level, String message, Throwable throwable);

    void log(Level level, Supplier<?> messageSupplier);

    void log(Level level, Supplier<?> messageSupplier, Throwable throwable);

    void log(Level level, String message, Supplier<?>... paramSuppliers);

    // ==========================================
    // UTILITY CHECKS
    // ==========================================

    boolean isTraceEnabled();

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();

    boolean isFatalEnabled();

    boolean isEnabled(Level level);
}