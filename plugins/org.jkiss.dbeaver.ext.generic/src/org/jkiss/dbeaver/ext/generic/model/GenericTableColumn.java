/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2023 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ext.generic.model;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.model.DBPEvaluationContext;
import org.jkiss.dbeaver.model.DBPNamedObject2;
import org.jkiss.dbeaver.model.DBPOrderedObject;
import org.jkiss.dbeaver.model.impl.DBPositiveNumberTransformer;
import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCColumnKeyType;
import org.jkiss.dbeaver.model.impl.jdbc.struct.JDBCTableColumn;
import org.jkiss.dbeaver.model.meta.Property;
import org.jkiss.dbeaver.model.meta.PropertyLength;
import org.jkiss.dbeaver.model.struct.rdb.DBSTableColumn;
import org.jkiss.utils.CommonUtils;

import java.util.Collection;

/**
 * Generic table column
 */
public class GenericTableColumn extends JDBCTableColumn<GenericTableBase> implements DBSTableColumn, DBPNamedObject2, JDBCColumnKeyType, DBPOrderedObject
{
    private int radix;
    private String remarks;
    private int sourceType;
    private long charLength;
    private boolean autoIncrement;

    public GenericTableColumn(GenericTableBase table)
    {
        super(table, false);
    }

    public GenericTableColumn(
        GenericTableBase table,
        String columnName,
        String typeName,
        int valueType,
        int sourceType,
        int ordinalPosition,
        long columnSize,
        long charLength,
        Integer scale,
        Integer precision,
        int radix,
        boolean notNull,
        String remarks,
        String defaultValue,
        boolean autoIncrement,
        boolean autoGenerated)
    {
        super(table,
            true,
            columnName,
            typeName,
            valueType,
            ordinalPosition,
            columnSize,
            scale,
            precision,
            notNull,
            autoGenerated || autoIncrement,
            defaultValue);
        this.sourceType = sourceType;
        this.charLength = charLength;
        this.remarks = remarks;
        this.radix = radix;
        this.autoIncrement = autoIncrement;
    }

    @NotNull
    @Override
    public GenericDataSource getDataSource()
    {
        return getTable().getDataSource();
    }

    public int getSourceType()
    {
        return sourceType;
    }

    public long getCharLength()
    {
        return charLength;
    }

    @Override
    @Property(viewable = true, editable = true, valueRenderer = DBPositiveNumberTransformer.class, order = 41)
    public Integer getScale()
    {
        return super.getScale();
    }

    @Override
    @Property(viewable = true, order = 51)
    public boolean isAutoGenerated()
    {
        return autoGenerated;
    }

    @Property(viewable = true, editable = true, order = 52)
    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public JDBCColumnKeyType getKeyType()
    {
        return this;
    }

    @Property(viewable = false, valueRenderer = DBPositiveNumberTransformer.class, order = 62)
    public int getRadix()
    {
        return radix;
    }

    public void setRadix(int radix)
    {
        this.radix = radix;
    }

    @Override
    @Property(viewable = false, order = 80)
    public boolean isInUniqueKey()
    {
        final Collection<GenericUniqueKey> uniqueKeysCache = getTable().getContainer().getConstraintKeysCache().getCachedObjects(getTable());
        if (!CommonUtils.isEmpty(uniqueKeysCache)) {
            for (GenericUniqueKey key : uniqueKeysCache) {
                if (key.hasColumn(this)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInReferenceKey()
    {
        return false;
    }

    @Nullable
    @Override
    @Property(viewable = true, editableExpr = "object.dataSource.metaModel.tableColumnCommentEditable", updatableExpr = "object.dataSource.metaModel.tableColumnCommentEditable", length = PropertyLength.MULTILINE, order = 100)
    public String getDescription()
    {
        return remarks;
    }

    public void setDescription(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString()
    {
        return getTable().getFullyQualifiedName(DBPEvaluationContext.UI) + "." + getName();
    }

}
