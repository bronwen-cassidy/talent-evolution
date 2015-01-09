package com.zynap.talentstudio.organisation.attributes;

/**
 * User: amark
 * Date: 04-Oct-2005
 * Time: 15:26:49
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.calculations.DateCalculation;
import com.zynap.talentstudio.calculations.CalculationResult;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestDynamicAttribute extends AbstractHibernateTestCase {


    protected void setUp() throws Exception {
        super.setUp();
        this.dynamicAttributeService = (IDynamicAttributeService) getBean("dynamicAttrService");
    }

    public void testClone() throws Exception {

        final DynamicAttribute dynamicAttribute = new DynamicAttribute((long) 22, "testing 22", DynamicAttribute.DA_TYPE_STRUCT, "P", false, true, true);
        final LookupType refersToType = new LookupType("type label", "some description");
        refersToType.setActive(true);
        refersToType.setId((long) 44);
        refersToType.setType("P");
        refersToType.setTypeId("THIS_IS_A_TEST");
        List<LookupValue> lookupValues = new ArrayList<LookupValue>();
        final LookupValue value1 = new LookupValue("value1ID", "1 label", "desc1", refersToType);
        value1.setId((long) 1);
        lookupValues.add(value1);

        final LookupValue value2 = new LookupValue("value2ID", "2 label", "desc2", refersToType);
        value2.setId((long) 2);
        lookupValues.add(value2);

        refersToType.setLookupValues(lookupValues);
        dynamicAttribute.setRefersToType(refersToType);

        QuestionnaireDefinition definition = new QuestionnaireDefinition((long) 16, "some questionnaire def");
        dynamicAttribute.setQuestionnaireDefinition(definition);
        DynamicAttributeReference reference = new DynamicAttributeReference();
        reference.setParentDa(dynamicAttribute);
        reference.setReferenceDa(dynamicAttribute);
        dynamicAttribute.addChild(reference);

        final DynamicAttribute cloned = (DynamicAttribute) dynamicAttribute.clone();

        assertEquals(dynamicAttribute.getId(), cloned.getId());
        assertEquals(dynamicAttribute.getLabel(), cloned.getLabel());
        assertEquals(dynamicAttribute.getRefersToType(), cloned.getRefersToType());
        assertEquals(definition.getId(), dynamicAttribute.getQuestionnaireDefinitionId());

        DynamicAttributeReference child = cloned.getChildren().get(0);
        assertEquals(cloned.getQuestionnaireDefinitionId(), child.getParentDa().getQuestionnaireDefinitionId());
        assertEquals(cloned.getQuestionnaireDefinitionId(), child.getReferenceDa().getQuestionnaireDefinitionId());

        Iterator clonedIterator = cloned.getRefersToType().getLookupValues().iterator();
        for (Iterator iterator = lookupValues.iterator(); iterator.hasNext() && clonedIterator.hasNext();) {
            LookupValue lookupValue = (LookupValue) iterator.next();
            LookupValue clonedValue = (LookupValue) clonedIterator.next();
            assertEquals(lookupValue, clonedValue);
        }
    }

    public void testCalculatedField_Date() throws Exception {

        DynamicAttribute currentDate = getFunctionAttributes();

        Subject subject = new Subject();
        setDateOfBirth(subject, "1965-07-10");

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);

        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);
        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression1.setIndex(1);
        calculation.addExpression(expression1);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertTrue(42 <= Integer.parseInt(result.getValue()));

        // as months
        calculation.setFormat(DateCalculation.MONTHS);
        result = ageAttribute.getCalculation().execute(subject);
        assertTrue(504 <= Integer.parseInt(result.getValue()));
    }

    public void testCalculatedField_DateInverse() throws Exception {

        DynamicAttribute currentDate = getFunctionAttributes();

        Subject subject = new Subject();
        setDateOfBirth(subject, "1965-07-10");

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);

        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);


        Expression expression1 = new Expression();
        expression1.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression1.setIndex(0);
        expression1.setOperator(Expression.MINUS);
        calculation.addExpression(expression1);

        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setIndex(1);
        calculation.addExpression(expression0);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertNotNull(result.getValue());
    }

    public void testCalculatedField_DADate() throws Exception {


        DynamicAttribute currentDate = getFunctionAttributes();

        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        Subject subject = createSubject(startDate, (long) -3);

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Time In Service", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        ageAttribute.setCalculated(true);

        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setAttribute(startDate);
        expression1.setIndex(1);
        calculation.addExpression(expression1);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertTrue(41 <= Integer.parseInt(result.getValue()));
    }

    public void testCalculatedFieldAddingYearsToDate() throws Exception {
        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        Subject subject = createSubject(startDate, (long) -3);

        final DynamicAttribute timeInService = new DynamicAttribute((long) 22, "Time In Service", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        timeInService.setCalculated(true);

        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setValue("12");
        expression0.setOperator(Expression.PLUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setAttribute(startDate);
        expression1.setIndex(1);
        calculation.addExpression(expression1);

        timeInService.setCalculation(calculation);

        CalculationResult result = timeInService.getCalculation().execute(subject);
        assertEquals("10 Aug 1977", result.getValue());
    }

    public void testCalculatedFieldAddingMonthsToDate() throws Exception {

        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        Subject subject = createSubject(startDate, (long) -3);

        final DynamicAttribute timeInService = new DynamicAttribute((long) 22, "Time In Service", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        timeInService.setCalculated(true);

        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.MONTHS);

        Expression expression0 = new Expression();
        expression0.setValue("9");
        expression0.setOperator(Expression.PLUS);
        expression0.setFormat(DateCalculation.MONTHS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setAttribute(startDate);
        expression1.setIndex(1);
        calculation.addExpression(expression1);

        timeInService.setCalculation(calculation);

        CalculationResult result = timeInService.getCalculation().execute(subject);
        assertEquals("10 May 1966", result.getValue());
    }

    public void testCalculatedFieldAddingMonthsToDateReversed() throws Exception {

        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        Subject subject = createSubject(startDate, (long) -3);

        final DynamicAttribute timeInService = new DynamicAttribute((long) 22, "Time In Service", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        timeInService.setCalculated(true);

        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.MONTHS);

        Expression expression1 = new Expression();
        expression1.setAttribute(startDate);
        expression1.setIndex(0);
        expression1.setFormat(DateCalculation.MONTHS);
        expression1.setOperator(Expression.PLUS);
        calculation.addExpression(expression1);

        Expression expression0 = new Expression();
        expression0.setValue("9");
        expression0.setIndex(1);
        calculation.addExpression(expression0);
        timeInService.setCalculation(calculation);

        CalculationResult result = timeInService.getCalculation().execute(subject);
        assertEquals("10 May 1966", result.getValue());
    }

    public void testCalculatedField_CurrentDateDADateNumber() throws Exception {

        DynamicAttribute currentDate = getFunctionAttributes();
        Subject subject = new Subject();
        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        NodeExtendedAttribute extendedAttribute = new NodeExtendedAttribute("1965-06-10", startDate);
        extendedAttribute.setId((long) -3);
        subject.addNodeExtendedAttribute(extendedAttribute);

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Time In Service", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        ageAttribute.setCalculated(true);

        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setAttribute(startDate);
        expression1.setIndex(1);
        expression1.setOperator(Expression.MINUS);
        calculation.addExpression(expression1);

        Expression expression2 = new Expression();
        expression2.setValue("3");
        expression2.setIndex(2);
        calculation.addExpression(expression2);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        int actual = Integer.parseInt(result.getValue());
        int expected = 39;
        assertTrue(actual >= expected);
    }

    public void testCalculatedField_DOB_Null() throws Exception {

        DynamicAttribute currentDate = getFunctionAttributes();

        Subject subject = new Subject();

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);

        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);
        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression1.setIndex(1);
        calculation.addExpression(expression1);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertEquals("-", result.getValue());
    }

    public void testCalculatedField_DOB_NullAddNumber() throws Exception {

        DynamicAttribute currentDate = getFunctionAttributes();

        Subject subject = new Subject();

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);

        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);
        
        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression1.setIndex(1);
        expression1.setOperator(Expression.PLUS);
        calculation.addExpression(expression1);

        Expression expression2 = new Expression();
        expression2.setValue("12");
        expression2.setIndex(2);
        calculation.addExpression(expression2);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertEquals("-", result.getValue());
    }

    public void testCalculatedField_DOB_AddNumber() throws Exception {

        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        // startdate = 1965-08-10
        Subject subject = createSubject(startDate, (long) -3);
        setDateOfBirth(subject, "1932-08-10");
        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setAttribute(startDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression1.setIndex(1);
        expression1.setOperator(Expression.PLUS);
        expression1.setFormat(DateCalculation.YEARS);
        calculation.addExpression(expression1);

        Expression expression2 = new Expression();
        expression2.setValue("12");
        expression2.setIndex(2);
        calculation.addExpression(expression2);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertEquals("45", result.getValue());
        assertEquals(DynamicAttribute.DA_TYPE_TEXTFIELD, result.getType());
    }

    public void testCalculatedField_DOB_AddMonths() throws Exception {

        Subject subject = new Subject();
        setDateOfBirth(subject, "1932-08-10");
        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression0.setOperator(Expression.PLUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression2 = new Expression();
        expression2.setValue("15");
        expression2.setIndex(2);
        expression2.setFormat(DateCalculation.MONTHS);
        calculation.addExpression(expression2);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);
        assertEquals("10 Nov 1933", result.getValue());
        assertEquals(DynamicAttribute.DA_TYPE_DATE, result.getType());
    }

    public void testCalculatedField_DOB_AddYears_AddMonths() throws Exception {

        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        Subject subject = createSubject(startDate, (long) -3);
        setDateOfBirth(subject, "2000-07-10");

        DynamicAttribute currentDate = getFunctionAttributes();

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setAttribute(currentDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression01 = new Expression();
        expression01.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression01.setOperator(Expression.PLUS);
        expression01.setIndex(1);
        calculation.addExpression(expression01);

        Expression expression1 = new Expression();
        expression1.setValue("2");
        expression1.setIndex(2);
        expression1.setOperator(Expression.PLUS);
        expression1.setFormat(DateCalculation.YEARS);
        calculation.addExpression(expression1);

        Expression expression2 = new Expression();
        expression2.setValue("3");
        expression2.setIndex(3);
        expression2.setFormat(DateCalculation.MONTHS);
        calculation.addExpression(expression2);

        ageAttribute.setCalculation(calculation);

        CalculationResult result = ageAttribute.getCalculation().execute(subject);

        //assertEquals("9.3", result.getValue());
        assertNotNull(result.getValue());
        assertEquals(DynamicAttribute.DA_TYPE_TEXTFIELD, result.getType());
    }

    public void testCalculatedField_Nested() throws Exception {

        DynamicAttribute startDate = new DynamicAttribute((long) -312, "Start Date", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        // startdate = 1965-08-10
        Subject subject = createSubject(startDate, (long) -3);
        setDateOfBirth(subject, "1932-08-10");

        final DynamicAttribute ageAttribute = new DynamicAttribute((long) 22, "Age", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        ageAttribute.setCalculated(true);
        Calculation calculation = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);
        calculation.setFormat(DateCalculation.YEARS);

        Expression expression0 = new Expression();
        expression0.setAttribute(startDate);
        expression0.setOperator(Expression.MINUS);
        expression0.setIndex(0);
        calculation.addExpression(expression0);

        Expression expression1 = new Expression();
        expression1.setRefValue(AnalysisAttributeHelper.DOB_ATTR);
        expression1.setIndex(1);
        expression1.setOperator(Expression.PLUS);
        expression1.setFormat(DateCalculation.YEARS);
        calculation.addExpression(expression1);
        ageAttribute.setCalculation(calculation);

        DynamicAttribute nested = new DynamicAttribute((long) -313, "Nested Calculations", DynamicAttribute.DA_TYPE_DATE, "S", false, true, true);
        nested.setCalculated(true);
        Calculation calculation1 = new DateCalculation(DynamicAttribute.DA_TYPE_DATE);

        Expression expression00 = new Expression();
        expression00.setAttribute(ageAttribute);
        expression00.setOperator(Expression.MINUS);
        expression00.setIndex(0);
        calculation1.addExpression(expression00);

        Expression expression11 = new Expression();
        expression11.setValue("2");
        expression11.setIndex(1);
        calculation1.addExpression(expression11);

        nested.setCalculation(calculation1);

        CalculationResult result = nested.getCalculation().execute(subject);
        assertEquals("31", result.getValue());
        assertEquals(DynamicAttribute.DA_TYPE_TEXTFIELD, result.getType());
    }

    private Subject createSubject(DynamicAttribute startDate, Long id) {
        
        Subject subject = new Subject();
        NodeExtendedAttribute extendedAttribute = new NodeExtendedAttribute("1965-08-10", startDate);
        extendedAttribute.setId(id);
        subject.addNodeExtendedAttribute(extendedAttribute);
        return subject;
    }

    private void setDateOfBirth(Subject subject, String date) throws ParseException {
        Date date1 = getDateValue(date);
        subject.setDateOfBirth(date1);
    }

    private Date getDateValue(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(date);
    }

    private DynamicAttribute getFunctionAttributes() {
        Collection functionAttributes = dynamicAttributeService.getActiveAttributes(DynamicAttribute.NODE_TYPE_FUNCTION, false, new String[]{DynamicAttribute.DA_TYPE_DATE});
        return (DynamicAttribute) functionAttributes.iterator().next();
    }

    protected IDynamicAttributeService dynamicAttributeService;
}