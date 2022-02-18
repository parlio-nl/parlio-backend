/*
 * This file is generated by jOOQ.
 */
package nl.parlio.tweedekamer.gen.jooq.tables;


import java.util.Arrays;
import java.util.List;

import nl.parlio.tweedekamer.gen.jooq.Keys;
import nl.parlio.tweedekamer.gen.jooq.PublicTable;
import nl.parlio.tweedekamer.gen.jooq.tables.records.QChangeEventEntryRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChangeEventEntryTable extends TableImpl<QChangeEventEntryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.change_event_entry</code>
     */
    public static final ChangeEventEntryTable CHANGE_EVENT_ENTRY = new ChangeEventEntryTable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QChangeEventEntryRecord> getRecordType() {
        return QChangeEventEntryRecord.class;
    }

    /**
     * The column <code>public.change_event_entry.change_event_entry_id</code>.
     */
    public final TableField<QChangeEventEntryRecord, Long> CHANGE_EVENT_ENTRY_ID = createField(DSL.name("change_event_entry_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.change_event_entry.change_event_id</code>.
     */
    public final TableField<QChangeEventEntryRecord, Long> CHANGE_EVENT_ID = createField(DSL.name("change_event_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.change_event_entry.key</code>.
     */
    public final TableField<QChangeEventEntryRecord, String> KEY = createField(DSL.name("key"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.change_event_entry.data</code>.
     */
    public final TableField<QChangeEventEntryRecord, String> DATA = createField(DSL.name("data"), SQLDataType.VARCHAR.nullable(false), this, "");

    private ChangeEventEntryTable(Name alias, Table<QChangeEventEntryRecord> aliased) {
        this(alias, aliased, null);
    }

    private ChangeEventEntryTable(Name alias, Table<QChangeEventEntryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.change_event_entry</code> table reference
     */
    public ChangeEventEntryTable(String alias) {
        this(DSL.name(alias), CHANGE_EVENT_ENTRY);
    }

    /**
     * Create an aliased <code>public.change_event_entry</code> table reference
     */
    public ChangeEventEntryTable(Name alias) {
        this(alias, CHANGE_EVENT_ENTRY);
    }

    /**
     * Create a <code>public.change_event_entry</code> table reference
     */
    public ChangeEventEntryTable() {
        this(DSL.name("change_event_entry"), null);
    }

    public <O extends Record> ChangeEventEntryTable(Table<O> child, ForeignKey<O, QChangeEventEntryRecord> key) {
        super(child, key, CHANGE_EVENT_ENTRY);
    }

    @Override
    public Schema getSchema() {
        return PublicTable.PUBLIC;
    }

    @Override
    public UniqueKey<QChangeEventEntryRecord> getPrimaryKey() {
        return Keys.CHANGE_EVENT_ENTRY_PKEY;
    }

    @Override
    public List<UniqueKey<QChangeEventEntryRecord>> getKeys() {
        return Arrays.<UniqueKey<QChangeEventEntryRecord>>asList(Keys.CHANGE_EVENT_ENTRY_PKEY);
    }

    @Override
    public List<ForeignKey<QChangeEventEntryRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QChangeEventEntryRecord, ?>>asList(Keys.CHANGE_EVENT_ENTRY__CHANGE_EVENT_ENTRY_CHANGE_EVENT_ID_FKEY);
    }

    private transient ChangeEventTable _changeEvent;

    public ChangeEventTable changeEvent() {
        if (_changeEvent == null)
            _changeEvent = new ChangeEventTable(this, Keys.CHANGE_EVENT_ENTRY__CHANGE_EVENT_ENTRY_CHANGE_EVENT_ID_FKEY);

        return _changeEvent;
    }

    @Override
    public ChangeEventEntryTable as(String alias) {
        return new ChangeEventEntryTable(DSL.name(alias), this);
    }

    @Override
    public ChangeEventEntryTable as(Name alias) {
        return new ChangeEventEntryTable(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ChangeEventEntryTable rename(String name) {
        return new ChangeEventEntryTable(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ChangeEventEntryTable rename(Name name) {
        return new ChangeEventEntryTable(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Long, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
