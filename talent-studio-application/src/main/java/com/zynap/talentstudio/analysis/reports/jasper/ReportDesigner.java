package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.ReportConstants;
import com.zynap.talentstudio.calculations.Expression;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.List;

/**
 * User: amark
 * Date: 23-Feb-2006
 * Time: 14:59:41
 */
public abstract class ReportDesigner {

    //public abstract JasperReportDesign getDesign(Report report) throws TalentStudioException;

    /**
     * Add link for header.
     *
     * @param column
     * @param headerText
     */
    protected abstract void createHeaderLinkIfRequired(Column column, JRDesignTextField headerText);

    /**
     * Add formula.
     *
     * @param column
     * @param design
     * @param style
     * @param x
     * @param y
     * @throws JRException
     */
    protected void addFormula(Column column, JasperReportDesign design, JRStyle style, int x, int y) throws JRException {
        StringBuffer formulaResult = new StringBuffer(" ");

        final List<Expression> expressions = column.getCalculation().getExpressions();
        for (Expression expression : expressions) {
            AnalysisParameter attribute = expression.getAnalysisParameter();

            if (expression.getLeftBracket() != null) formulaResult.append(" ").append(expression.getLeftBracket());
            if (expression.isValueOperand()) formulaResult.append(" ").append(expression.getValue()).append(" ");
            else if (expression.isMetricExpression()) {
                final String metricFieldName = getMetricFieldName(expression.getMetric().getId());

                formulaResult.append(" DataFormatter.valueToNumber($F{")
                        .append(metricFieldName)
                        .append("}) ");
                addField(metricFieldName, design.jasperDesign, Number.class.getName());

            } else if (expression.isAttributeOperand()) {
                formulaResult.append(" DataFormatter.valueToNumber($F{")
                        .append(expression.getAttributeExpression())
                        .append("}) ");
                createField(attribute, design.jasperDesign);

            } else if (expression.isRefValueOperand()) {
                formulaResult.append(" DataFormatter.valueToNumber($F{")
                        .append(expression.getRefValue())
                        .append("}) ");
                createField(attribute, design.jasperDesign);
            }
            if (expression.getRightBracket() != null) formulaResult.append(expression.getRightBracket()).append(" ");
            if (expression.getOperator() != null) formulaResult.append(expression.getOperator());
        }

        String formula = formulaResult.toString();
        formula = "DataFormatter.formatValue(new Double(" + formula + "),$P{" + ReportConstants.DECIMAL_PLACES_PARAM + "})";
        JRDesignTextField detailsText = new JRDesignTextField();
        JRDesignExpression expression = new JRDesignExpression();
        expression.setValueClass(String.class);
        expression.setText(formula);
        detailsText.setExpression(expression);
        detailsText.setStretchWithOverflow(true);
        detailsText.setPrintWhenDetailOverflows(true);
        applyStyleElements(detailsText, style, x, y, width, height);
        design.detail.addElement(detailsText);
    }

    private String getMetricFieldName(Object id) {
        return ReportConstants.METRIC_ATTR_PREFIX + (id != null ? id.toString() : IPopulationEngine.COUNT);
    }

    /**
     * Add group element to report definition.
     * <br/> Adds group plus blank field.
     *
     * @param column
     * @param design
     * @param style
     * @param x
     * @param y
     * @return A JRDesignElement, which is an instance of JRDesignTextField IF the column is not color displayable
     *         A JRDesignImage if the column is color displayable. The JRDesignElement is one that has been added for the group.
     *         If group already exists null is returned!
     * @throws JRException
     */
    protected JRDesignElement addGroup(Column column, JasperReportDesign design, JRStyle style, int x, int y) throws JRException {
        JRDesignElement element = null;
        final String attributeName = AnalysisAttributeHelper.getName(column.getAnalysisParameter());
        final JRGroup group = getGroupByName(attributeName, design.jasperDesign);
        if (group == null) {

            // create field for grouping column
            JRDesignField field = createField(column, design.jasperDesign);
            element = buildGroup(column, design, field, style, x, y);
        }

        return element;
    }

    protected JRDesignField createField(Column column, JasperDesign jasperDesign) throws JRException {

        AnalysisParameter attribute = column.getAnalysisParameter();
        return createField(attribute, jasperDesign);
    }

    /**
     * Add header to report definition.
     *
     * @param headerStyle
     * @param x
     * @param headerY
     * @param column
     * @param pageHeader
     */
    protected final void addHeader(JRStyle headerStyle, int x, int headerY, Column column, JRDesignBand pageHeader) {

        JRDesignTextField headerText = new JRDesignTextField();
        headerText.setPrintWhenDetailOverflows(true);
        headerText.setStretchWithOverflow(true);

        JRDesignExpression textExpression = new JRDesignExpression();
        textExpression.setValueClass(String.class);
        final String escapedLabel = StringEscapeUtils.escapeJava(column.getLabel());
        textExpression.setText("\"" + escapedLabel + "\"");
        headerText.setExpression(textExpression);

        createHeaderLinkIfRequired(column, headerText);

        applyStyleElements(headerText, headerStyle, x, headerY, width, height);
        pageHeader.addElement(headerText);
    }

    protected final JRDesignImage createDetailImage(JRDesignField field, Integer position) {
        JRDesignImage jrDesignImage = new JRDesignImage(null);

        jrDesignImage.setExpression(createImageExpression(field, position));
        jrDesignImage.setLazy(true);
        jrDesignImage.setAnchorNameExpression(createExpression(field));

        return jrDesignImage;
    }

    protected final JRDesignTextField createTextField(JRDesignField field) {
        JRDesignTextField detailsText = new JRDesignTextField();
        detailsText.setExpression(createExpression(field));
        detailsText.setStretchWithOverflow(true);
        detailsText.setPrintWhenDetailOverflows(true);
        return detailsText;
    }

    protected final String urlParameter(Column column) {
        if (column.getAnalysisParameter().isQuestionnaireAttribute()) {
            return _QUESTIONNAIRE_URL_PARAM_NAME;
        }
        return column.getAnalysisParameter().isPersonLinkAttribute() ? _SUBJECT_URL_PARAM_NAME : _POSITION_URL_PARAM_NAME;
    }

    protected final void loadDesign(JasperReportDesign design, String templateFileName) throws TalentStudioException {
        InputStream resourceAsStream = ReportDesigner.class.getClassLoader().getResourceAsStream(templateFileName);
        try {
            // NOTE: this property has been set as the digester parser is set to validating without a schema, this throws an error in the OC4J parsers.
            JRProperties.setProperty(JRProperties.COMPILER_XML_VALIDATION, false);
            design.jasperDesign = JRXmlLoader.load(resourceAsStream);
        } catch (JRException e) {
            logger.error("unable to load template: " + templateFileName, e);
            throw new TalentStudioException(e);
        }
    }

    protected final JRDesignParameter createParameter(String name, String className) {
        JRDesignParameter field = new JRDesignParameter();
        field.setName(name);
        field.setValueClassName(className);
        return field;
    }

    protected final JRDesignExpression buildFormattingExpression(final String attributeName) {
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText("DataFormatter.formatValue($F{" + attributeName + "})");

        return expression;
    }

    protected final JRDesignElement buildGroup(Column column, JasperReportDesign design, JRDesignField field, JRStyle style, int x, int y) throws JRException {

        final String fieldName = field.getName();

        JRDesignElement element;

        // add new group
        JRDesignGroup newGroup = new JRDesignGroup();
        newGroup.setName(fieldName);

        // build expression to formatting grouping values and set on group
        JRDesignExpression expression = buildFormattingExpression(fieldName);
        expression.setValueClass(String.class);
        newGroup.setExpression(expression);

        // add group to design
        design.jasperDesign.addGroup(newGroup);

        // build corresponding element for field
        if (column.isColorDisplayable()) {
            JRDesignImage designImage = createDetailImage(field, column.getPosition());
            designImage.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
            designImage.setEvaluationGroup(newGroup);
            element = designImage;
        } else {
            JRDesignTextField text = createTextField(field);
            text.setEvaluationTime(JRExpression.EVALUATION_TIME_GROUP);
            text.setEvaluationGroup(newGroup);
            element = text;
        }
        applyStyleElements(element, style, x, 0, width, height);

        // create band and add element
        JRDesignBand band = new JRDesignBand();
        band.setHeight(20);
        band.addElement(element);

        // set band on group
        newGroup.setGroupHeader(band);

        // add blank element straight after group to get blank line
        JRDesignStaticText blank = createBlankTextField();
        applyStyleElements(blank, style, x, y, width, height);
        design.detail.addElement(blank);
        return element;
    }

    protected final void applyStyleElements(JRDesignElement element, JRStyle style, int x, int y, int width, int height) {
        element.setStyle(style);
        element.setX(x);
        element.setY(y);
        element.setWidth(width);
        element.setHeight(height);
    }

    protected final JRDesignField addField(String name, JasperDesign jasperDesign, String className) throws JRException {

        JRDesignField field = getFieldByName(name, jasperDesign);
        if (field == null) {
            field = new JRDesignField();
            field.setName(name);
            field.setValueClassName(className);
            jasperDesign.addField(field);
        }

        return field;
    }

    protected final JRGroup getGroupByName(String name, JasperDesign jasperDesign) {
        final JRGroup[] groups = jasperDesign.getGroups();
        for (int i = 0; i < groups.length; i++) {
            JRGroup group = groups[i];
            if (group.getName().equals(name)) {
                return group;
            }
        }

        return null;
    }

    protected final JRDesignField createField(AnalysisParameter attribute, JasperDesign jasperDesign) throws JRException {
        final String name = AnalysisAttributeHelper.getName(attribute);
        String className;

        if (attribute.isDynamicAttribute() || attribute.isQuestionnaireAttribute()) {
            className = Object.class.getName();
        } else if (attribute.isDerivedAttribute()) {
            className = Integer.class.getName();
        } else {
            className = String.class.getName();
        }

        return addField(name, jasperDesign, className);
    }

    private JRDesignField getFieldByName(String name, JasperDesign jasperDesign) {
        for (int i = 0; i < jasperDesign.getFields().length; i++) {
            JRDesignField field = (JRDesignField) jasperDesign.getFields()[i];
            if (field.getName().equals(name))
                return field;
        }

        return null;
    }

    private JRDesignExpression createExpression(JRDesignField field) {

        final String name = field.getName();

        JRDesignExpression expression = buildFormattingExpression(name);
        expression.setValueClass(String.class);

        return expression;
    }

    private JRDesignExpression createImageExpression(JRDesignField field, Integer position) {

        JRDesignExpression expression = new JRDesignExpression();
        expression.setValueClass(String.class);

        expression.setText("\"../images/report/tabular/\" + "
                + "DataFormatter.getColorCellValue((Column)$P{report}.getColumns().get(" + position.intValue() + "),$F{" + field.getName() + "})" + "+ \".gif\"");

        return expression;
    }

    private JRDesignStaticText createBlankTextField() {
        JRDesignStaticText detailsText = new JRDesignStaticText();
        detailsText.setText(" ");
        detailsText.setPrintWhenDetailOverflows(true);
        return detailsText;
    }

    /**
     * constants defining styles for the different sections.
     */
    public static final String HEADER_STYLE = "templateHeaderStyle";
    public static final String DETAIL_STYLE = "templateDetailStyle";
    public static final String GROUP_STYLE = "templateGroupStyle";

    /**
     * Constants for placeholders used so that Jasper Exporters can substitute URLs at display time.
     */
    public static final String _SUBJECT_URL_PARAM_NAME = "__subjectUrl__";
    public static final String _QUESTIONNAIRE_URL_PARAM_NAME = "__questionnaireUrl__";
    public static final String _POSITION_URL_PARAM_NAME = "__positionUrl__";
    public static final String _DRILL_DOWN_URL_PARAM_NAME = "_drilldownURL_";

    static int width = 150;
    final static int height = 15;

    protected static final Log logger = LogFactory.getLog(ReportDesigner.class);

    protected static final String TEMPLATE_PATH = "com/zynap/talentstudio/analysis/reports/jasper/";

    protected final static String NODE_ID_PARAM = "command.node.id";
    protected static final String FIELD_ID_PARAM = "id";

    protected static final String FIELD_QUE_WF_ID_PARAM = ReportConstants.WORKFLOW_ID_NAME;
    protected static final String FIELD_HASACCESS_PARAM = "hasAccess";
    protected static final String QUESTIONNAIRE_WF_ID_PARAM = "WORKFLOW_ID";

}
