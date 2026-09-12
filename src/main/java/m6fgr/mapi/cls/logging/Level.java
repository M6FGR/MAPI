package m6fgr.mapi.cls.logging;

public enum Level {
    FATAL(org.apache.logging.log4j.Level.FATAL),
    ERROR(org.apache.logging.log4j.Level.ERROR),
    WARN(org.apache.logging.log4j.Level.WARN),
    INFO(org.apache.logging.log4j.Level.INFO),
    DEBUG(org.apache.logging.log4j.Level.DEBUG),
    TRACE(org.apache.logging.log4j.Level.TRACE),
    OFF(org.apache.logging.log4j.Level.OFF),
    ALL(org.apache.logging.log4j.Level.ALL);

    private org.apache.logging.log4j.Level log4jLevel;

    Level(org.apache.logging.log4j.Level level) {
        this.log4jLevel = level;
    }

    public org.apache.logging.log4j.Level getLog4jLevel() {
        return this.log4jLevel;
    }


    public static Level of(String name) {
        if (name == null || name.isBlank()) {
            return INFO;
        }

        // Level.forName resolves an existing level or defines a new dynamic Log4j Level
        // Priority intLevel: 400 maps roughly to custom INFO/DEBUG threshold
        INFO.log4jLevel = org.apache.logging.log4j.Level.forName(name.trim().toUpperCase(), 400);
        return INFO;
    }
}