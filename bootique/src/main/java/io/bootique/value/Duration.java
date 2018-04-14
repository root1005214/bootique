package io.bootique.value;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 0.26
 */
public class Duration implements Comparable<Duration> {

    private static final Pattern TOKENIZER = Pattern.compile("^([0-9]+)\\s*([a-z]+)$");

    private static final Map<String, TemporalUnit> UNIT_VOCABULARY;

    static {
        UNIT_VOCABULARY = new HashMap<>();
        UNIT_VOCABULARY.put("ms", ChronoUnit.MILLIS);
        UNIT_VOCABULARY.put("s", ChronoUnit.SECONDS);
        UNIT_VOCABULARY.put("sec", ChronoUnit.SECONDS);
        UNIT_VOCABULARY.put("second", ChronoUnit.SECONDS);
        UNIT_VOCABULARY.put("seconds", ChronoUnit.SECONDS);
        UNIT_VOCABULARY.put("min", ChronoUnit.MINUTES);
        UNIT_VOCABULARY.put("minute", ChronoUnit.MINUTES);
        UNIT_VOCABULARY.put("minutes", ChronoUnit.MINUTES);
        UNIT_VOCABULARY.put("hr", ChronoUnit.HOURS);
        UNIT_VOCABULARY.put("hour", ChronoUnit.HOURS);
        UNIT_VOCABULARY.put("hours", ChronoUnit.HOURS);
        UNIT_VOCABULARY.put("d", ChronoUnit.DAYS);
        UNIT_VOCABULARY.put("day", ChronoUnit.DAYS);
        UNIT_VOCABULARY.put("days", ChronoUnit.DAYS);
    }

    private java.time.Duration duration;
    private String stringDuration;

    /**
     * Creates a Duration instance from a String representation. The String has a numeric part, an optional space and
     * a unit part. E.g. "3ms", "5 sec", "5 s".
     *
     * @param value a String value representing duration.
     */
    public Duration(String value) {
        this.duration = parse(value);
        this.stringDuration = value;
    }

    static java.time.Duration parse(String value) {
        Objects.requireNonNull(value, "Null 'value' argument");

        if (value.length() == 0) {
            throw new IllegalArgumentException("Empty 'value' argument");
        }

        Matcher matcher = TOKENIZER.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid Duration format: " + value);
        }

        TemporalUnit unit = parseUnit(matcher.group(2));
        long amount = parseAmount(matcher.group(1));

        return java.time.Duration.of(amount, unit);
    }

    private static long parseAmount(String amount) {
        try {
            return Long.parseLong(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time amount: " + amount);
        }
    }

    private static TemporalUnit parseUnit(String unitString) {
        TemporalUnit unit = UNIT_VOCABULARY.get(unitString);
        if (unit == null) {
            throw new IllegalArgumentException("Invalid time unit: " + unitString);
        }

        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Duration) {
            return duration.equals(((Duration) obj).duration);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return duration.hashCode();
    }

    public java.time.Duration getDuration() {
        return duration;
    }

    @Override
    public int compareTo(Duration o) {
        return duration.compareTo(o.duration);
    }

    @Override
    public String toString() {
        return stringDuration;
    }
}