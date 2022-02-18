/*
 * This file is generated by jOOQ.
 */
package nl.parlio.tweedekamer.gen.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class QChangeEventEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long   changeEventEntryId;
    private final Long   changeEventId;
    private final String key;
    private final String data;

    public QChangeEventEntry(QChangeEventEntry value) {
        this.changeEventEntryId = value.changeEventEntryId;
        this.changeEventId = value.changeEventId;
        this.key = value.key;
        this.data = value.data;
    }

    public QChangeEventEntry(
        Long   changeEventEntryId,
        Long   changeEventId,
        String key,
        String data
    ) {
        this.changeEventEntryId = changeEventEntryId;
        this.changeEventId = changeEventId;
        this.key = key;
        this.data = data;
    }

    /**
     * Getter for <code>public.change_event_entry.change_event_entry_id</code>.
     */
    public Long getChangeEventEntryId() {
        return this.changeEventEntryId;
    }

    /**
     * Getter for <code>public.change_event_entry.change_event_id</code>.
     */
    public Long getChangeEventId() {
        return this.changeEventId;
    }

    /**
     * Getter for <code>public.change_event_entry.key</code>.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Getter for <code>public.change_event_entry.data</code>.
     */
    public String getData() {
        return this.data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QChangeEventEntry (");

        sb.append(changeEventEntryId);
        sb.append(", ").append(changeEventId);
        sb.append(", ").append(key);
        sb.append(", ").append(data);

        sb.append(")");
        return sb.toString();
    }
}
