/*
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.reports;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 09-Feb-2006 09:14:44
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.analysis.reports.TabularReport;
import com.zynap.talentstudio.arenas.MenuSection;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.web.analysis.reports.functions.FunctionWrapperBean;

import java.util.ArrayList;
import java.util.List;

public class TestTabularReportWrapperBean extends TestCase {

    public void testAssignColumnIndexes() throws Exception {

        Report report = new TabularReport("test", "", "Public");
        List<Column> columns = new ArrayList<Column>();
        int i = 0;
        columns.add(createColumn("title", i++));
        columns.add(createColumn("subjectPrimaryAssociations.subject.coreDetail.title", i++));
        columns.add(createColumn("organisationUnit.label", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.children.title", i++));

        columns.add(createColumn("subjectPrimaryAssociations.subject.coreDetail.name", i++));

        columns.add(createColumn("children.organisationUnit.label", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.children.organisationUnit.label", i++));
        columns.add(createColumn("parent.title", i++));
        columns.add(createColumn("children.title", i));
        report.setColumns(columns);
        TabularReportWrapperBean tabularReportWrapperBean = new TabularReportWrapperBean(report, new ArrayList<MenuSection>(), new ArrayList<MenuSection>(), "test.htm");
        List result = tabularReportWrapperBean.assignColumnIndexes();

        // assert core attributes
        assertEquals("title", ((Column) result.get(0)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.subject.coreDetail.title", ((Column) result.get(1)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.subject.coreDetail.name", ((Column) result.get(2)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.children.title", ((Column) result.get(3)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.children.organisationUnit.label", ((Column) result.get(4)).getAttributeName());
        assertEquals("organisationUnit.label", ((Column) result.get(5)).getAttributeName());
        assertEquals("children.organisationUnit.label", ((Column) result.get(6)).getAttributeName());
        assertEquals("children.title", ((Column) result.get(7)).getAttributeName());
        // sub of sub subjectAssociations
        assertEquals("parent.title", ((Column) result.get(8)).getAttributeName());

    }

    public void testAssignColumnIndexesWithGroups() throws Exception {
        Report report = new TabularReport("test", "", "Public");
        List<Column> columns = new ArrayList<Column>();
        int i = 0;
        columns.add(createColumn("coreDetail.firstName", i++));
        columns.add(createColumn("12345", i++, true));
        columns.add(createColumn("subjectPrimaryAssociations.position.title", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.4435", i++, true));
        columns.add(createColumn("coreDetail.title", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.children.title", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.organisationUnit.label", i++, true));
        columns.add(createColumn("subjectPrimaryAssociations.position.children.organisationUnit.label", i++));
        columns.add(createColumn("coreDetail.contactTelephone", i++));
        columns.add(createColumn("coreDetail.name", i));
        report.setColumns(columns);
        TabularReportWrapperBean tabularReportWrapperBean = new TabularReportWrapperBean(report, new ArrayList<MenuSection>(), new ArrayList<MenuSection>(), "test.htm");
        List result = tabularReportWrapperBean.assignColumnIndexes();

        // THE 3 GROUPED COLUMNS GO NEXT
        assertEquals("12345", ((Column) result.get(0)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.4435", ((Column) result.get(1)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.organisationUnit.label", ((Column) result.get(2)).getAttributeName());

        assertEquals("subjectPrimaryAssociations.position.title", ((Column) result.get(3)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.children.title", ((Column) result.get(4)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.children.organisationUnit.label", ((Column) result.get(5)).getAttributeName());

        assertEquals("coreDetail.firstName", ((Column) result.get(6)).getAttributeName());
        assertEquals("coreDetail.title", ((Column) result.get(7)).getAttributeName());

        assertEquals("coreDetail.contactTelephone", ((Column) result.get(8)).getAttributeName());
        assertEquals("coreDetail.name", ((Column) result.get(9)).getAttributeName());
    }

    public void testAssignColumnIndexesWithFormulae() throws Exception {
        Report report = new TabularReport("test", "", "Public");
        List<Column> columns = new ArrayList<Column>();
        int i = 0;
        columns.add(createColumn("coreDetail.firstName", i++));
        columns.add(createColumn("12345", i++, true));
        columns.add(createColumn("subjectPrimaryAssociations.position.title", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.4435", i++, true));
        columns.add(createColumn("coreDetail.title", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.children.title", i++));
        columns.add(createFormula("12345", "+", "12345", i++));
        columns.add(createColumn("subjectPrimaryAssociations.position.organisationUnit.label", i++, true));
        columns.add(createColumn("subjectPrimaryAssociations.position.children.organisationUnit.label", i++));
        columns.add(createColumn("coreDetail.contactTelephone", i++));
        columns.add(createColumn("coreDetail.name", i));
        report.setColumns(columns);
        TabularReportWrapperBean tabularReportWrapperBean = new TabularReportWrapperBean(report, new ArrayList<MenuSection>(), new ArrayList<MenuSection>(), "test.htm");
        List<Column> result = tabularReportWrapperBean.assignColumnIndexes();

        // THE 3 GROUPED COLUMNS GO first
        assertEquals("12345", (result.get(0)).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.4435", result.get(1).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.organisationUnit.label", result.get(2).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.title", result.get(3).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.children.title", result.get(4).getAttributeName());
        assertEquals("subjectPrimaryAssociations.position.children.organisationUnit.label", result.get(5).getAttributeName());

        assertEquals("coreDetail.firstName", result.get(6).getAttributeName());
        assertEquals("coreDetail.title", result.get(7).getAttributeName());
        assertEquals("12345 + 12345", result.get(8).getLabel());
        assertEquals("coreDetail.contactTelephone", result.get(9).getAttributeName());
        assertEquals("coreDetail.name", result.get(10).getAttributeName());

    }

    private Column createFormula(String attributeName, String function, String attrName2, int i) {
        FunctionWrapperBean bean = new FunctionWrapperBean();
        Expression exp = new Expression(new DynamicAttribute(Long.valueOf(attributeName)), function);
        Expression exp2 = new Expression(new DynamicAttribute(Long.valueOf(attrName2)), null);
        bean.addWrappedExpression(exp);
        bean.addWrappedExpression(exp2);
        Column column = new Column(attributeName + " " + function + " " + attrName2);
        column.setFormula(true);
        column.setCalculation(bean.getModifiedCalculation());
        column.setPosition(new Integer(i));
        return column;
    }

    private Column createColumn(String attributeName, int i) {
        final Column column = new Column("" + i, attributeName, "N/A");
        column.setPosition(new Integer(i));
        return column;
    }

    private Column createColumn(String attributeName, int i, boolean grouped) {
        final Column column = new Column("" + i, attributeName, "N/A");
        column.setPosition(new Integer(i));
        column.setGrouped(grouped);
        return column;
    }
}