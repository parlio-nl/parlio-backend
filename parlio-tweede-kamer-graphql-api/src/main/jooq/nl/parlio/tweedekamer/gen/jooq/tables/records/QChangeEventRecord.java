/*
 * This file is generated by jOOQ.
 */
package nl.parlio.tweedekamer.gen.jooq.tables.records;


import java.time.OffsetDateTime;

import nl.parlio.tweedekamer.gen.jooq.tables.ChangeEventTable;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class QChangeEventRecord extends UpdatableRecordImpl<QChangeEventRecord> implements Record5<Long, String, OffsetDateTime, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.change_event.change_event_id</code>.
     */
    public QChangeEventRecord setChangeEventId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.change_event.change_event_id</code>.
     */
    public Long getChangeEventId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.change_event.operation_name</code>.
     */
    public QChangeEventRecord setOperationName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.change_event.operation_name</code>.
     */
    public String getOperationName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.change_event.time</code>.
     */
    public QChangeEventRecord setTime(OffsetDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.change_event.time</code>.
     */
    public OffsetDateTime getTime() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>public.change_event.model</code>.
     */
    public QChangeEventRecord setModel(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.change_event.model</code>.
     */
    public String getModel() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.change_event.ref</code>.
     */
    public QChangeEventRecord setRef(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.change_event.ref</code>.
     */
    public Long getRef() {
        return (Long) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, OffsetDateTime, String, Long> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, String, OffsetDateTime, String, Long> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return ChangeEventTable.CHANGE_EVENT.CHANGE_EVENT_ID;
    }

    @Override
    public Field<String> field2() {
        return ChangeEventTable.CHANGE_EVENT.OPERATION_NAME;
    }

    @Override
    public Field<OffsetDateTime> field3() {
        return ChangeEventTable.CHANGE_EVENT.TIME;
    }

    @Override
    public Field<String> field4() {
        return ChangeEventTable.CHANGE_EVENT.MODEL;
    }

    @Override
    public Field<Long> field5() {
        return ChangeEventTable.CHANGE_EVENT.REF;
    }

    @Override
    public Long component1() {
        return getChangeEventId();
    }

    @Override
    public String component2() {
        return getOperationName();
    }

    @Override
    public OffsetDateTime component3() {
        return getTime();
    }

    @Override
    public String component4() {
        return getModel();
    }

    @Override
    public Long component5() {
        return getRef();
    }

    @Override
    public Long value1() {
        return getChangeEventId();
    }

    @Override
    public String value2() {
        return getOperationName();
    }

    @Override
    public OffsetDateTime value3() {
        return getTime();
    }

    @Override
    public String value4() {
        return getModel();
    }

    @Override
    public Long value5() {
        return getRef();
    }

    @Override
    public QChangeEventRecord value1(Long value) {
        setChangeEventId(value);
        return this;
    }

    @Override
    public QChangeEventRecord value2(String value) {
        setOperationName(value);
        return this;
    }

    @Override
    public QChangeEventRecord value3(OffsetDateTime value) {
        setTime(value);
        return this;
    }

    @Override
    public QChangeEventRecord value4(String value) {
        setModel(value);
        return this;
    }

    @Override
    public QChangeEventRecord value5(Long value) {
        setRef(value);
        return this;
    }

    @Override
    public QChangeEventRecord values(Long value1, String value2, OffsetDateTime value3, String value4, Long value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached QChangeEventRecord
     */
    public QChangeEventRecord() {
        super(ChangeEventTable.CHANGE_EVENT);
    }

    /**
     * Create a detached, initialised QChangeEventRecord
     */
    public QChangeEventRecord(Long changeEventId, String operationName, OffsetDateTime time, String model, Long ref) {
        super(ChangeEventTable.CHANGE_EVENT);

        setChangeEventId(changeEventId);
        setOperationName(operationName);
        setTime(time);
        setModel(model);
        setRef(ref);
    }
}
