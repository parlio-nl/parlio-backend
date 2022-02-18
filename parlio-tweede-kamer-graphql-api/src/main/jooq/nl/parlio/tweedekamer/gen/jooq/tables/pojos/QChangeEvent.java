/*
 * This file is generated by jOOQ.
 */
package nl.parlio.tweedekamer.gen.jooq.tables.pojos;


import java.io.Serializable;
import java.time.OffsetDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class QChangeEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long           changeEventId;
    private final String         operationName;
    private final OffsetDateTime time;
    private final String         model;
    private final Long           ref;

    public QChangeEvent(QChangeEvent value) {
        this.changeEventId = value.changeEventId;
        this.operationName = value.operationName;
        this.time = value.time;
        this.model = value.model;
        this.ref = value.ref;
    }

    public QChangeEvent(
        Long           changeEventId,
        String         operationName,
        OffsetDateTime time,
        String         model,
        Long           ref
    ) {
        this.changeEventId = changeEventId;
        this.operationName = operationName;
        this.time = time;
        this.model = model;
        this.ref = ref;
    }

    /**
     * Getter for <code>public.change_event.change_event_id</code>.
     */
    public Long getChangeEventId() {
        return this.changeEventId;
    }

    /**
     * Getter for <code>public.change_event.operation_name</code>.
     */
    public String getOperationName() {
        return this.operationName;
    }

    /**
     * Getter for <code>public.change_event.time</code>.
     */
    public OffsetDateTime getTime() {
        return this.time;
    }

    /**
     * Getter for <code>public.change_event.model</code>.
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Getter for <code>public.change_event.ref</code>.
     */
    public Long getRef() {
        return this.ref;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QChangeEvent (");

        sb.append(changeEventId);
        sb.append(", ").append(operationName);
        sb.append(", ").append(time);
        sb.append(", ").append(model);
        sb.append(", ").append(ref);

        sb.append(")");
        return sb.toString();
    }
}
