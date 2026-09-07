package M6FGR.mapi.cls.logging;

import M6FGR.mapi.utils.code.CodeUtils;
import org.apache.logging.log4j.LogManager;
import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class LoggingManager implements Logger {

    private final org.apache.logging.log4j.Logger rawLogger;

    private LoggingManager(String name) {
        this.rawLogger = LogManager.getLogger(name);
    }

    private LoggingManager() {
        this(CodeUtils.getCallerClass().getSimpleName());
    }

    public static LoggingManager getLogger() {
        return new LoggingManager();
    }

    public static LoggingManager getLogger(String name) {
        return new LoggingManager(name);
    }

    // ==========================================
    // TRACE LEVEL
    // ==========================================

    @Override
    public void trace(String message, Object... params) {
        this.rawLogger.trace(message, params);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        this.rawLogger.trace(message, throwable);
    }

    @Override
    public void trace(Supplier<?> messageSupplier) {
        this.rawLogger.trace(messageSupplier::get);
    }

    @Override
    public void trace(Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.trace(messageSupplier::get, throwable);
    }

    @Override
    public void trace(String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.trace(message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // DEBUG LEVEL
    // ==========================================

    @Override
    public void debug(String message, Object... params) {
        this.rawLogger.debug(message, params);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        this.rawLogger.debug(message, throwable);
    }

    @Override
    public void debug(Supplier<?> messageSupplier) {
        this.rawLogger.debug(messageSupplier::get);
    }

    @Override
    public void debug(Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.debug(messageSupplier::get, throwable);
    }

    @Override
    public void debug(String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.debug(message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // INFO LEVEL
    // ==========================================

    @Override
    public void info(String message, Object... params) {
        this.rawLogger.info(message, params);
    }

    @Override
    public void info(String message, Throwable throwable) {
        this.rawLogger.info(message, throwable);
    }

    @Override
    public void info(Supplier<?> messageSupplier) {
        this.rawLogger.info(messageSupplier::get);
    }

    @Override
    public void info(Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.info(messageSupplier::get, throwable);
    }

    @Override
    public void info(String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.info(message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // WARN LEVEL
    // ==========================================

    @Override
    public void warn(String message, Object... params) {
        this.rawLogger.warn(message, params);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        this.rawLogger.warn(message, throwable);
    }

    @Override
    public void warn(Supplier<?> messageSupplier) {
        this.rawLogger.warn(messageSupplier::get);
    }

    @Override
    public void warn(Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.warn(messageSupplier::get, throwable);
    }

    @Override
    public void warn(String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.warn(message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // ERROR LEVEL
    // ==========================================

    @Override
    public void error(String message, Object... params) {
        this.rawLogger.error(message, params);
    }

    @Override
    public void error(String message, Throwable throwable) {
        this.rawLogger.error(message, throwable);
    }

    @Override
    public void error(Supplier<?> messageSupplier) {
        this.rawLogger.error(messageSupplier::get);
    }

    @Override
    public void error(Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.error(messageSupplier::get, throwable);
    }

    @Override
    public void error(String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.error(message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // FATAL LEVEL
    // ==========================================

    @Override
    public void fatal(String message, Object... params) {
        this.rawLogger.fatal(message, params);
    }

    @Override
    public void fatal(String message, Throwable throwable) {
        this.rawLogger.fatal(message, throwable);
    }

    @Override
    public void fatal(Supplier<?> messageSupplier) {
        this.rawLogger.fatal(messageSupplier::get);
    }

    @Override
    public void fatal(Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.fatal(messageSupplier::get, throwable);
    }

    @Override
    public void fatal(String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.fatal(message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // DYNAMIC LEVEL LOGGING
    // ==========================================

    @Override
    public void log(Level level, String message, Object... params) {
        this.rawLogger.log(toLog4jLevel(level), message, params);
    }

    @Override
    public void log(Level level, String message, Throwable throwable) {
        this.rawLogger.log(toLog4jLevel(level), message, throwable);
    }

    @Override
    public void log(Level level, Supplier<?> messageSupplier) {
        this.rawLogger.log(toLog4jLevel(level), messageSupplier::get);
    }

    @Override
    public void log(Level level, Supplier<?> messageSupplier, Throwable throwable) {
        this.rawLogger.log(toLog4jLevel(level), messageSupplier::get, throwable);
    }

    @Override
    public void log(Level level, String message, Supplier<?>... paramSuppliers) {
        this.rawLogger.log(toLog4jLevel(level), message, toLog4jSuppliers(paramSuppliers));
    }

    // ==========================================
    // UTILITY CHECKS
    // ==========================================

    @Override
    public boolean isTraceEnabled() {
        return this.rawLogger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return this.rawLogger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.rawLogger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return this.rawLogger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return this.rawLogger.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return this.rawLogger.isFatalEnabled();
    }

    @Override
    public boolean isEnabled(Level level) {
        return this.rawLogger.isEnabled(toLog4jLevel(level));
    }

    public org.apache.logging.log4j.Logger getRawLogger() {
        return this.rawLogger;
    }

    // ==========================================
    // HELPER CONVERTERS
    // ==========================================

    private static org.apache.logging.log4j.Level toLog4jLevel(Level level) {
        return level.getLog4jLevel();
    }

    private static org.apache.logging.log4j.util.Supplier<?>[] toLog4jSuppliers(Supplier<?>... paramSuppliers) {
        if (paramSuppliers == null) return null;
        return Arrays.stream(paramSuppliers)
                .map(s -> (org.apache.logging.log4j.util.Supplier<?>) s)
                .toArray(org.apache.logging.log4j.util.Supplier<?>[]::new);
    }
}