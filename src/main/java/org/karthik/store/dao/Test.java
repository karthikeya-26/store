package org.karthik.store.dao;

import com.adventnet.db.persistence.metadata.UniqueValueGeneration;
import com.adventnet.ds.query.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.IndexedDO;
import com.adventnet.persistence.Persistence;

import javax.transaction.TransactionManager;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;

public class Test {

    public static  void SelectDetails() {
        Table userDetailsTable = Table.getTable("UserDetails");
        Table skillsTable = new Table("Skills");
        TransactionManager transactionManager =DataAccess.getTransactionManager();
        SelectQuery selectQuery = new SelectQueryImpl(userDetailsTable);
        Column userDetailColumn = Column.getColumn("UserDetails","*");
        Column skillColumn = Column.getColumn("Skills","*");
        List<Column> columnList = new ArrayList<>();
        columnList.add(userDetailColumn);
        columnList.add(skillColumn);
        selectQuery.addSelectColumns(columnList);
        SortColumn sortColumn = new SortColumn(Column.getColumn("UserDetails","USER_ID"),false);
        selectQuery.addSortColumn(sortColumn);
        Join userAndSkillTableJoin = new Join(userDetailsTable,skillsTable, new String[]{"USER_ID"},new String[]{"USER_ID"},Join.INNER_JOIN);
        selectQuery.addJoin(userAndSkillTableJoin);
        GroupByClause groupByClause = new GroupByClause(Arrays.asList(new Column("UserDetails","USER_ID")));
        selectQuery.setGroupByClause(groupByClause);
        try (PrintWriter pw = new PrintWriter(new FileWriter("/home/karthi-pt7680/selectDetails.txt"))) {
            // Write the select query string
            pw.println("Generated Select Query:");
            pw.println(selectQuery);
            pw.println();

            // Write select columns
            pw.println("Select Columns:");
            pw.println(selectQuery.getSelectColumns());
            pw.println();

            // Log and write criteria
            pw.println("Criteria:");
            pw.println(selectQuery.getCriteria());
            pw.println();

            // Log and write CTE list
            pw.println("CTE List:");
            pw.println(selectQuery.getCteList());
            pw.println();

            // Log and write derived tables
            pw.println("Derived Tables:");
            pw.println(selectQuery.getDerivedTables());
            pw.println();

            // Log and write derived columns
            pw.println("Derived Columns:");
            pw.println(selectQuery.getDerivedColumns());
            pw.println();

            // Log and write group by clause
            pw.println("Group By Clause:");
            pw.println(selectQuery.getGroupByClause());
            pw.println();

            // Log and write index hints for userDetailsTable
            pw.println("Index Hint for UserDetailsTable:");
            pw.println(selectQuery.getIndexHint(userDetailsTable));
            pw.println();

            // Log and write index hints for skillsTable
            pw.println("Index Hint for SkillsTable:");
            pw.println(selectQuery.getIndexHint(skillsTable));
            pw.println();

            // Log and write index hint map
            pw.println("Index Hint Map:");
            pw.println(selectQuery.getIndexHintMap());
            pw.println();

            // Log and write joins
            pw.println("Joins:");
            pw.println(selectQuery.getJoins());
            pw.println();

            // Log and write lock status
            pw.println("Lock Status:");
            pw.println(selectQuery.getLockStatus());
            pw.println();

            // Log and write parallel workers
            pw.println("Parallel Workers:");
            pw.println(selectQuery.getParallelWorkers());
            pw.println();

            // Log and write parent
            pw.println("Parent:");
            pw.println(selectQuery.getParent());
            pw.println();

            // Log and write table list
            pw.println("Table List:");
            pw.println(selectQuery.getTableList());
            pw.println();

            // Log and write sort columns
            pw.println("Sort Columns:");
            pw.println(selectQuery.getSortColumns());
            pw.println();

            // Log and write range
            pw.println("Range:");
            pw.println(selectQuery.getRange());
            pw.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InsertQueryImpl insertQuery = new InsertQueryImpl("UserDetails",selectQuery);
        ReturningColumns returningColumns = new ReturningColumns();
        returningColumns.setColumns(Arrays.asList(userDetailColumn,skillColumn));
        DeleteQuery deleteQuery = new DeleteQueryImpl("UserDetails");
        try {

            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            persistence.delete(deleteQuery);
            DataObject dataObject = new IndexedDO();
            IndexedDO indexedDO = new IndexedDO();
            UniqueValueGeneration uniqueValueGeneration = new UniqueValueGeneration();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
